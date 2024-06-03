package com.teknokote.cm.dto;

import lombok.Data;

import java.util.List;
@Data
public class TransactionFilterDto {
    private List<Long> cardIds;
    private List<Long> salePointIds;
    private List<Long> productIds;
    private List<String> city;
    private PeriodFilterDto period;
}
