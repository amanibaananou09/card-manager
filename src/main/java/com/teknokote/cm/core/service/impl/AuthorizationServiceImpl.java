package com.teknokote.cm.core.service.impl;

import com.teknokote.cm.core.dao.AuthorizationDao;
import com.teknokote.cm.core.model.EnumAuthorizationStatus;
import com.teknokote.cm.core.model.EnumCardStatus;
import com.teknokote.cm.core.model.EnumCounterType;
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
   private int lastReferenceNumber = 0;
   @Override
   public AuthorizationDto createAuthorization(AuthorizationRequest authorizationRequest) {
      SupplierDto supplierDto = supplierService.findByReference(authorizationRequest.getReference());
      if (supplierDto != null) {
         SalePointDto salePoint = supplierDto.getSalePoints().stream()
                 .filter(salePointDto -> salePointDto.getName().equals(authorizationRequest.getSalePointName()))
                 .findFirst().orElse(null);
         if (salePoint != null) {
            CardDto cardDto = cardService.findByTag(authorizationRequest.getTag());
            if (cardDto != null) {
               CardGroupDto cardGroupDto = cardGroupService.findById(cardDto.getCardGroupId()).orElse(null);
               BigDecimal cardValue= cardGroupDto.getCounters().stream().filter(counterDto -> counterDto.getCounterType().equals(EnumCounterType.CEILING)).findFirst().get().getValue();
               if (cardGroupDto != null) {
                  String condition = cardGroupDto.getCondition();
                  boolean isAuthorized = evaluateCondition(condition, authorizationRequest.getProductName(), authorizationRequest.getSalePointName(), LocalDate.now().getDayOfWeek().toString(), salePoint.getCity());
                  String generatedReference = generateReference();
                  if (isAuthorized) {
                     AuthorizationDto authorizationDto=createAuthorizationDto(generatedReference, EnumAuthorizationStatus.GRANTED, cardDto.getId(),cardValue);
                     cardDto.setStatus(EnumCardStatus.IN_USE);
                     cardService.update(cardDto);
                     return authorizationDto;
                  } else {
                     return createAuthorizationDto(generatedReference, EnumAuthorizationStatus.REFUSED, cardDto.getId(),cardValue);
                  }
               }
            }
         }
      }
      return null;
   }
   private AuthorizationDto createAuthorizationDto(String reference, EnumAuthorizationStatus status, Long cardId,BigDecimal cardValue) {
      return create(AuthorizationDto.builder()
              .status(status)
              .reference(reference)
              .cardId(cardId)
              .dateTime(LocalDateTime.now())
              .quantity(cardValue)
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
}

