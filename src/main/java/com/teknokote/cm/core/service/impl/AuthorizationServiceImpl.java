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
        String generatedReference = generateReference();
        if (supplierDto != null) {
            SalePointDto salePoint = findSalePoint(supplierDto, authorizationRequest.getSalePointName());
            if (salePoint != null) {
                CardDto cardDto = cardService.findByTag(authorizationRequest.getTag());
                if (cardDto != null && cardDto.getExpirationDate().isAfter(LocalDate.now())) {
                    CardGroupDto cardGroupDto = cardGroupService.checkedFindById(cardDto.getCardGroupId());
                    BigDecimal dailyCardLimit = calculateDailyCardLimit(cardDto);
                    if (cardGroupDto != null) {
                        String condition = cardGroupDto.getCondition();
                        boolean isAuthorized = evaluateCondition(condition, authorizationRequest.getProductName(),
                                authorizationRequest.getSalePointName(), LocalDate.now().getDayOfWeek().toString(), salePoint.getCity());
                        if (isAuthorized) {
                            CeilingDto ceilingDto = cardGroupDto.getCeilings().stream().findFirst().get();
                            return authorizeIfAuthorized(cardDto, ceilingDto, generatedReference, dailyCardLimit, authorizationRequest);
                        } else {
                            return createAuthorizationDto(generatedReference, EnumAuthorizationStatus.REFUSED, cardDto.getId(), dailyCardLimit, authorizationRequest);
                        }
                    }
                }
                return createAuthorizationDto(generatedReference, EnumAuthorizationStatus.REFUSED, null, BigDecimal.ZERO, authorizationRequest);
            }
            return createAuthorizationDto(generatedReference, EnumAuthorizationStatus.REFUSED, null, BigDecimal.ZERO, authorizationRequest);
        }
        return createAuthorizationDto(generatedReference, EnumAuthorizationStatus.REFUSED, null, BigDecimal.ZERO, authorizationRequest);
    }

    private AuthorizationDto createAuthorizationDto(String reference, EnumAuthorizationStatus status, Long cardId, BigDecimal ceilingValue, AuthorizationRequest authorizationRequest) {
        return create(AuthorizationDto.builder()
                .status(status)
                .reference(reference)
                .cardId(cardId)
                .dateTime(LocalDateTime.now())
                .quantity(ceilingValue)
                .ptsId(authorizationRequest.getPtsId())
                .pump(authorizationRequest.getPump())
                .build());
    }

    private String generateReference() {
        String lastReference = null;
        int nextNumber = 1;
        if (dao.findLastAuthorization() != null) {
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
        boolean negate = value.contains("not");
        boolean result;
        switch (property) {
            case "allowedDays":
                result = value.contains(day);
                break;
            case "allowedSalePoints":
                result = value.contains(salePointName);
                break;
            case "allowedCity":
                result = value.contains(city);
                break;
            case "allowedProduct":
                result = value.contains(productName);
                break;
            case "timeSlot":
                result = evaluateTimeSlot(value);
                break;
            default:
                throw new IllegalArgumentException("Unrecognized property: " + property);
        }
        if (negate) {
            // If 'not' is present, negate the result
            result = !result;
        }
        return result;
    }

    private boolean evaluateTimeSlot(String value) {
        String[] timeSlots = value.split("or");
        for (String slot : timeSlots) {
            String[] times = slot.split("to");
            if (times.length != 2) {
                throw new IllegalArgumentException("Malformed time slot: " + slot);
            }
            LocalTime startTime = LocalTime.parse(times[0].trim());
            LocalTime endTime = LocalTime.parse(times[1].trim());
            // Check if the current time is within the time slot
            LocalTime currentTime = LocalTime.now();
            if (currentTime.isAfter(startTime) && currentTime.isBefore(endTime)) {
                return true; // Return true if current time is within any time slot
            }
        }
        return false; // Return false if current time is not within any time slot
    }

    private BigDecimal calculateDailyCardLimit(CardDto cardDto) {
        if (cardDto != null) {
            CardGroupDto cardGroupDto = cardGroupService.findById(cardDto.getCardGroupId()).orElse(null);
            if (cardGroupDto != null) {
                return cardGroupDto.getCeilings().stream()
                        .findFirst()
                        .map(ceilingDto -> ceilingDto.getDailyLimitValue()
                        )
                        .orElse(BigDecimal.ZERO);
            }
        }
        return null;
    }

    private AuthorizationDto authorizeIfAuthorized(CardDto cardDto, CeilingDto ceilingDto, String generatedReference, BigDecimal dailyCardLimit, AuthorizationRequest authorizationRequest) {
        Optional<TransactionDto> lastTransaction = transactionService.findLastTransactionByCardIdAndMonth(cardDto.getId(), LocalDateTime.now().getMonthValue());
        if (lastTransaction.isPresent()) {
            BigDecimal availableBalance = lastTransaction.get().getAvailableBalance();
            if (availableBalance.compareTo(BigDecimal.ZERO) > 0) {
                return authorizeBasedOnAvailableBalance(cardDto, ceilingDto, generatedReference, dailyCardLimit, authorizationRequest);
            } else {
                return createAuthorizationDto(generatedReference, EnumAuthorizationStatus.REFUSED, cardDto.getId(), BigDecimal.ZERO, authorizationRequest);
            }
        } else {
            return authorizeBasedOnLastTransaction(cardDto, generatedReference, dailyCardLimit, authorizationRequest);
        }
    }

    private AuthorizationDto authorizeBasedOnAvailableBalance(CardDto cardDto, CeilingDto ceilingDto, String generatedReference, BigDecimal dailyCardLimit, AuthorizationRequest authorizationRequest) {
        List<TransactionDto> dailyTransaction = transactionService.findTodayTransaction(cardDto.getId());
        if (!dailyTransaction.isEmpty()) {
            BigDecimal totalDailyAmount = calculateTotalDailyAmount(ceilingDto.getCeilingType(), dailyTransaction);
            if (totalDailyAmount.compareTo(dailyCardLimit) < 0) {
                cardDto.setStatus(EnumCardStatus.IN_USE);
                cardService.update(cardDto);
                return createAuthorizationDto(generatedReference, EnumAuthorizationStatus.GRANTED, cardDto.getId(), dailyCardLimit.subtract(totalDailyAmount), authorizationRequest);
            } else {
                return createAuthorizationDto(generatedReference, EnumAuthorizationStatus.REFUSED, cardDto.getId(), BigDecimal.ZERO, authorizationRequest);
            }
        } else {
            cardDto.setStatus(EnumCardStatus.IN_USE);
            cardService.update(cardDto);
            return createAuthorizationDto(generatedReference, EnumAuthorizationStatus.GRANTED, cardDto.getId(), dailyCardLimit, authorizationRequest);
        }
    }

    private AuthorizationDto authorizeBasedOnLastTransaction(CardDto cardDto, String generatedReference, BigDecimal dailyCardLimit, AuthorizationRequest authorizationRequest) {
        cardDto.setStatus(EnumCardStatus.IN_USE);
        cardService.update(cardDto);
        return createAuthorizationDto(generatedReference, EnumAuthorizationStatus.GRANTED, cardDto.getId(), dailyCardLimit, authorizationRequest);
    }

    private BigDecimal calculateTotalDailyAmount(EnumCeilingType ceilingType, List<TransactionDto> dailyTransaction) {
        if (ceilingType.equals(EnumCeilingType.AMOUNT)) {
            return dailyTransaction.stream().map(TransactionDto::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        } else {
            return dailyTransaction.stream().map(TransactionDto::getQuantity).reduce(BigDecimal.ZERO, BigDecimal::add);
        }
    }

    private SalePointDto findSalePoint(SupplierDto supplierDto, String salePointName) {
        return supplierDto != null ? supplierDto.getSalePoints().stream()
                .filter(salePointDto -> salePointDto.getName().equals(salePointName))
                .findFirst().orElse(null) : null;
    }

    @Override
    public AuthorizationDto findByPtsIdAndPump(String ptsId, Long pump) {
        return getDao().findAuthorizationByPtsIdAndPump(ptsId, pump);
    }
}

