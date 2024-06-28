package com.teknokote.cm.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class TransactionChart {
    private String fuelGrade;
    private String cardIdentifier;
    private BigDecimal sum;

    public TransactionChart(String fuelGrade, String cardIdentifier, BigDecimal sum) {
        this.cardIdentifier = cardIdentifier;
        this.fuelGrade=fuelGrade;
        this.sum = sum;
    }
}
