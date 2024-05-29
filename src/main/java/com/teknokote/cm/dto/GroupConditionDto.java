package com.teknokote.cm.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class GroupConditionDto {
    private String allowedDays;
    private String allowedSalePoints;
    private String allowedCity;
    private String allowedProduct;
    private String timeSlot;
    private List<TimeSlotDto> timeSlots;
    private String logicalOperators;
    private List<String> operators;

    @Builder
    public GroupConditionDto(String allowedDays, String allowedSalePoints, String allowedCity, String allowedProduct, String logicalOperators, String timeSlot, List<TimeSlotDto> timeSlots,List<String> operators) {
        this.allowedDays = allowedDays;
        this.allowedSalePoints = allowedSalePoints;
        this.allowedCity = allowedCity;
        this.allowedProduct = allowedProduct;
        this.timeSlots = timeSlots;
        this.operators=operators;
        if (operators!=null && !operators.isEmpty()){
            this.logicalOperators=createLogicalOperatorsString(operators);
        }else {
            this.logicalOperators=logicalOperators;
        }
        // Construct timeSlot string from timeSlots list
        if (timeSlots != null && !timeSlots.isEmpty()) {
            StringBuilder timeSlotBuilder = new StringBuilder();
            for (int i = 0; i < timeSlots.size(); i++) {
                TimeSlotDto timeSlotDto = timeSlots.get(i);
                timeSlotBuilder.append(timeSlotDto.getStartActivityTime()).append(" to ")
                        .append(timeSlotDto.getEndActivityTime());
                if (i < timeSlots.size() - 1) {
                    timeSlotBuilder.append(" or ");
                }
            }
            this.timeSlot = timeSlotBuilder.toString();
        } else {
            this.timeSlot = timeSlot;
        }
    }

    public String generateLogicalExpression() {
        StringBuilder logicalExpression = new StringBuilder();

        if (logicalOperators == null || logicalOperators.isEmpty()) {
            return logicalExpression.toString();
        }

        String[] operators = logicalOperators.split(",");

        // Append each parameter with its condition and logical operator
        if (allowedDays != null && !allowedDays.isEmpty()) {
            logicalExpression.append("allowedDays == '").append(allowedDays).append("'");
        }

        for (int i = 0; i < operators.length; i++) {
            String operator = operators[i].trim();
            if (!operator.isEmpty()) {
                logicalExpression.append(" ").append(operator).append(" ");
            }

            switch (i) {
                case 0:
                    if (allowedSalePoints != null && !allowedSalePoints.isEmpty()) {
                        logicalExpression.append("allowedSalePoints == '").append(allowedSalePoints).append("'");
                    }
                    break;
                case 1:
                    if (allowedCity != null && !allowedCity.isEmpty()) {
                        logicalExpression.append("allowedCity == '").append(allowedCity).append("'");
                    }
                    break;
                case 2:
                    if (allowedProduct != null && !allowedProduct.isEmpty()) {
                        logicalExpression.append("allowedProduct == '").append(allowedProduct).append("'");
                    }
                    break;
                case 3:
                    if (timeSlot != null && !timeSlot.isEmpty()) {
                        logicalExpression.append("timeSlot == '").append(timeSlot).append("'");
                    }
                    break;
            }
        }
        return logicalExpression.toString();
    }

    public List<TimeSlotDto> createListTimeSlotsFromString(String timeSlotString) {
        List<TimeSlotDto> timeSlotList = new ArrayList<>();
        String[] timeSlotRanges = timeSlotString.split(" or ");
        for (String range : timeSlotRanges) {
            String[] times = range.split(" to ");
            if (times.length == 2) {
                LocalTime startActivity = LocalTime.parse(times[0]);
                LocalTime endActivity = LocalTime.parse(times[1]);
                timeSlotList.add(TimeSlotDto.builder()
                        .startActivityTime(startActivity)
                        .endActivityTime(endActivity)
                        .build());
            }
        }
        return timeSlotList;
    }

    public List<String> createListOperatorFromString(String logicalOperators) {
        List<String> operatorList = new ArrayList<>();
        if (logicalOperators != null && !logicalOperators.isEmpty()) {
            String[] operators = logicalOperators.split(",");
            for (String operator : operators) {
                String trimmedOperator = operator.trim();
                if (!trimmedOperator.isEmpty()) {
                    operatorList.add(trimmedOperator);
                }
            }
        }
        return operatorList;
    }
    private String createLogicalOperatorsString(List<String> operatorList) {
        if (operatorList == null || operatorList.isEmpty()) {
            return null;
        }
        return String.join(", ", operatorList);
    }

}
