package com.teknokote.cm.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class DailyTransactionChart {
    private String date;
    private String fuelGrade;
    private String cardIdentifier;
    private BigDecimal sum;

    public DailyTransactionChart(String date,String fuelGrade, String cardIdentifier, BigDecimal sum) {
        this.date = date;
        this.cardIdentifier = cardIdentifier;
        this.fuelGrade=fuelGrade;
        this.sum = sum;
    }
}