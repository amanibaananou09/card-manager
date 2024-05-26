package com.teknokote.cm.core.service.impl;

import com.teknokote.cm.core.dao.AuthorizationDao;
import com.teknokote.cm.core.model.*;
import com.teknokote.cm.core.service.*;
import com.teknokote.cm.dto.*;
import com.teknokote.core.service.ESSValidator;
import com.teknokote.core.service.GenericCheckedService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@Getter
public class AuthorizationServiceImpl extends GenericCheckedService<Long, AuthorizationDto> implements AuthorizationService {
   @Autowired
   private ESSValidator<AuthorizationDto> validator;
   @Autowired
   private AuthorizationDao dao;
   @Autowired
   private CardService cardService;
   @Autowired
   private CardGroupService cardGroupService;
   @Autowired
   private SalePointService salePointService;
   @Autowired
   private SupplierService supplierService;
   @Autowired
   private TransactionService transactionService;
   private int lastReferenceNumber = 0;
   @Override
   public AuthorizationDto createAuthorization(AuthorizationRequest authorizationRequest) {
      SupplierDto supplierDto = supplierService.findByReference(authorizationRequest.getReference());
      SalePointDto salePoint = findSalePoint(supplierDto, authorizationRequest.getSalePointName());
      CardDto cardDto = cardService.findByTag(authorizationRequest.getTag());
      CardGroupDto cardGroupDto = cardGroupService.checkedFindById(cardDto.getCardGroupId());
      BigDecimal dailyCardLimit = calculateDailyCardLimit(cardDto);
      if (cardGroupDto != null) {
            String condition = cardGroupDto.getCondition();
            boolean isAuthorized = evaluateCondition(condition, authorizationRequest.getProductName(),
                    authorizationRequest.getSalePointName(), LocalDate.now().getDayOfWeek().toString(), salePoint.getCity());
            String generatedReference = generateReference();
            if (isAuthorized) {
               return authorizeIfAuthorized(cardDto, generatedReference, dailyCardLimit);
            } else {
               return createAuthorizationDto(generatedReference, EnumAuthorizationStatus.REFUSED, cardDto.getId(), dailyCardLimit);
            }
      }
      return null;
   }
   private AuthorizationDto createAuthorizationDto(String reference, EnumAuthorizationStatus status, Long cardId,BigDecimal ceilingValue) {
      return create(AuthorizationDto.builder()
              .status(status)
              .reference(reference)
              .cardId(cardId)
              .dateTime(LocalDateTime.now())
              .quantity(ceilingValue)
              .build());
   }
   private String generateReference() {
      String lastReference =null;
      int nextNumber = 1;
      if (dao.findLastAuthorization()!=null){
          lastReference = dao.findLastAuthorization().getReference();
          nextNumber = Integer.parseInt(lastReference.substring(1)) + 1;
      }
      return "A" + nextNumber;
   }
   private boolean evaluateCondition(String condition, String productName, String salePointName, String day, String city) {
      // Split the condition into individual parts based on logical operators
      String[] orParts = condition.split("(?i)\\bOR\\b");
      // Evaluate each part separately
      for (String orPart : orParts) {
         boolean partResult = evaluateOrPart(orPart.trim(), productName, salePointName, day, city);
         if (partResult) {
            // If any OR part evaluates to true, the whole condition is true
            return true;
         }
      }
      // If none of the OR parts evaluate to true, the whole condition is false
      return false;
   }

   private boolean evaluateOrPart(String orPart, String productName, String salePointName, String day, String city) {
      // Split the OR part into individual parts based on "AND"
      String[] andParts = orPart.split("(?i)\\bAND\\b");
      // Evaluate each part separately
      for (String andPart : andParts) {
         boolean partResult = evaluateAndPart(andPart.trim(), productName, salePointName, day, city);
         if (!partResult) {
            // If any AND part evaluates to false, the whole AND expression is false
            return false;
         }
      }
      // If all AND parts evaluate to true, the whole OR expression is true
      return true;
   }

   private boolean evaluateAndPart(String andPart, String productName, String salePointName, String day, String city) {
      // Split the AND part into property and value
      String[] parts = andPart.split("==");
      if (parts.length != 2) {
         throw new IllegalArgumentException("Malformed expression: " + andPart);
      }
      String property = parts[0].trim();
      String value = parts[1].trim().replaceAll("'", "");

      switch (property) {
         case "allowedDays":
            return value.contains(day);
         case "allowedSalePoints":
            return value.contains(salePointName);
         case "allowedCity":
            return value.contains(city);
         case "allowedProduct":
            return value.contains(productName);
         case "timeSlot":
            String[] times = value.split("to");
            if (times.length != 2) {
               throw new IllegalArgumentException("Malformed time slot: " + value);
            }
            LocalTime startTime = LocalTime.parse(times[0].trim());
            LocalTime endTime = LocalTime.parse(times[1].trim());
            // Check if the current time is within the time slot
            LocalTime currentTime = LocalTime.now();
            return !currentTime.isBefore(startTime) && !currentTime.isAfter(endTime);
         default:
            throw new IllegalArgumentException("Unrecognized property: " + property);
      }
   }

   private BigDecimal calculateDailyCardLimit(CardDto cardDto) {
      if (cardDto != null) {
         EnumCardType cardType = cardDto.getType();
         CardGroupDto cardGroupDto = cardGroupService.findById(cardDto.getCardGroupId()).orElse(null);
         if (cardGroupDto != null) {
            return cardGroupDto.getCounters().stream()
                    .filter(counterDto -> counterDto.getCounterType().equals(EnumCounterType.CEILING))
                    .findFirst()
                    .map(counterDto -> {
                       if (cardType == EnumCardType.AMOUNT) {
                          return counterDto.getDailyLimitValue().multiply(BigDecimal.valueOf(1000));
                       } else {
                          return counterDto.getDailyLimitValue();
                       }
                    })
                    .orElse(BigDecimal.ZERO);
         }
      }
      return null;
   }
   private AuthorizationDto authorizeIfAuthorized(CardDto cardDto, String generatedReference, BigDecimal dailyCardLimit) {
      Optional<TransactionDto> lastTransaction = transactionService.findLastTransactionByCardIdAndMonth(cardDto.getId(), LocalDateTime.now().getMonthValue());
      if (lastTransaction.isPresent()) {
         BigDecimal availableBalance = lastTransaction.get().getAvailableBalance();
         if (availableBalance.compareTo(BigDecimal.ZERO) > 0) {
            return authorizeBasedOnAvailableBalance(cardDto, generatedReference, dailyCardLimit);
         } else {
            return createAuthorizationDto(generatedReference, EnumAuthorizationStatus.REFUSED, cardDto.getId(), BigDecimal.ZERO);
         }
      } else {
         return authorizeBasedOnLastTransaction(cardDto, generatedReference, dailyCardLimit);
      }
   }

   private AuthorizationDto authorizeBasedOnAvailableBalance(CardDto cardDto, String generatedReference, BigDecimal dailyCardLimit) {
      List<TransactionDto> dailyTransaction = transactionService.findTodayTransaction(cardDto.getId(), LocalDateTime.now());
      if (!dailyTransaction.isEmpty()) {
         BigDecimal totalDailyAmount = calculateTotalDailyAmount(cardDto.getType(),dailyTransaction);
         if (totalDailyAmount.compareTo(dailyCardLimit) < 0) {
            cardDto.setStatus(EnumCardStatus.IN_USE);
            cardService.update(cardDto);
            return createAuthorizationDto(generatedReference, EnumAuthorizationStatus.GRANTED, cardDto.getId(), dailyCardLimit.subtract(totalDailyAmount));
         } else {
            return createAuthorizationDto(generatedReference, EnumAuthorizationStatus.REFUSED, cardDto.getId(), BigDecimal.ZERO);
         }
      } else {
         cardDto.setStatus(EnumCardStatus.IN_USE);
         cardService.update(cardDto);
         return createAuthorizationDto(generatedReference, EnumAuthorizationStatus.GRANTED, cardDto.getId(), dailyCardLimit);
      }
   }

   private AuthorizationDto authorizeBasedOnLastTransaction(CardDto cardDto, String generatedReference, BigDecimal dailyCardLimit) {
      cardDto.setStatus(EnumCardStatus.IN_USE);
      cardService.update(cardDto);
      return createAuthorizationDto(generatedReference, EnumAuthorizationStatus.GRANTED, cardDto.getId(), dailyCardLimit);
   }

   private BigDecimal calculateTotalDailyAmount(EnumCardType cardType,List<TransactionDto> dailyTransaction) {
      if (cardType.equals(EnumCardType.AMOUNT)){
         return dailyTransaction.stream().map(TransactionDto::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
      }else{
         return dailyTransaction.stream().map(TransactionDto::getQuantity).reduce(BigDecimal.ZERO, BigDecimal::add);
      }
   }
   private SalePointDto findSalePoint(SupplierDto supplierDto, String salePointName) {
      return supplierDto != null ? supplierDto.getSalePoints().stream()
              .filter(salePointDto -> salePointDto.getName().equals(salePointName))
              .findFirst().orElse(null) : null;
   }

}

