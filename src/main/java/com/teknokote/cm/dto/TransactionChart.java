package com.teknokote.cm.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class TransactionChart {
    private String date;
    private Long cardId;
    private BigDecimal sum;

    public TransactionChart(String date, Long cardId, BigDecimal sum) {
        this.date = date;
        this.cardId = cardId;
        this.sum = sum;
    }
}
