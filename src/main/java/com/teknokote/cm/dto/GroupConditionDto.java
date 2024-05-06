package com.teknokote.cm.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
public class GroupConditionDto {
    private String allowedDays;
    private String allowedSalePoints;
    private String allowedCity;
    private String allowedProduct;
    private LocalTime startActivityTime;
    private LocalTime endActivityTime;
    private List<String> logicalOperators;
    @Builder
    public GroupConditionDto(String allowedDays, String allowedSalePoints,String allowedCity,String allowedProduct, LocalTime startActivityTime,LocalTime endActivityTime,List<String> logicalOperators) {
        this.allowedDays = allowedDays;
        this.allowedSalePoints = allowedSalePoints;
        this.allowedCity=allowedCity;
        this.allowedProduct=allowedProduct;
        this.startActivityTime=startActivityTime;
        this.endActivityTime=endActivityTime;
        this.logicalOperators=logicalOperators;
    }
    public String generateLogicalExpression() {
        StringBuilder logicalExpression = new StringBuilder();

        if (logicalOperators == null || logicalOperators.isEmpty()) {
            return logicalExpression.toString();
        }
        // Append each parameter with its condition and logical operator
        if (allowedDays != null && !allowedDays.isEmpty()) {
            logicalExpression.append("allowedDays : '").append(allowedDays).append("'");
        }
        for (int i = 0; i < logicalOperators.size(); i++) {
            String operator = logicalOperators.get(i);
            if (operator != null && !operator.isEmpty()) {
                logicalExpression.append(" ").append(operator).append(" ");
            }
            switch (i) {
                case 0:
                    if (allowedSalePoints != null && !allowedSalePoints.isEmpty()) {
                        logicalExpression.append("allowedSalePoints : '").append(allowedSalePoints).append("'");
                    }
                    break;
                case 1:
                    if (allowedCity != null && !allowedCity.isEmpty()) {
                        logicalExpression.append("allowedCity : '").append(allowedCity).append("'");
                    }
                    break;
                case 2:
                    if (allowedProduct != null && !allowedProduct.isEmpty()) {
                        logicalExpression.append("allowedProduct : '").append(allowedProduct).append("'");
                    }
                    break;
                case 3:
                    if (startActivityTime != null) {
                        logicalExpression.append("startActivityTime : '").append(startActivityTime).append("'");
                    }
                    break;
                case 4:
                    if (endActivityTime != null) {
                        logicalExpression.append("endActivityTime : '").append(endActivityTime).append("'");
                    }
                    break;
            }
        }
        return logicalExpression.toString();
    }
}
