package com.teknokote.cm.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class GroupConditionDto {
    private String allowedDays;
    private String allowedSalePoints;
    private String allowedCity;
    private String allowedProduct;
    private LocalTime startActivityTime;
    private LocalTime endActivityTime;
    @Builder
    public GroupConditionDto(String allowedDays, String allowedSalePoints,String allowedCity,String allowedProduct, LocalTime startActivityTime,LocalTime endActivityTime) {
        this.allowedDays = allowedDays;
        this.allowedSalePoints = allowedSalePoints;
        this.allowedCity=allowedCity;
        this.allowedProduct=allowedProduct;
        this.startActivityTime=startActivityTime;
        this.endActivityTime=endActivityTime;
    }
}
