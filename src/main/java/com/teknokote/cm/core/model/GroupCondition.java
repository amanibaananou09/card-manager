package com.teknokote.cm.core.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Embeddable
@Getter
@Setter
public class GroupCondition {
    @Column(name = "allowed_days")
    private String allowedDays;
    @Column(name = "allowed_sale_points")
    private String allowedSalePoints;
    @Column(name = "allowed_city")
    private String allowedCity;
    @Column(name = "allowed_product")
    private String allowedProduct;
    @Column(name = "start_activity_time")
    private LocalTime startActivityTime;
    @Column(name = "end_activity_time")
    private LocalTime endActivityTime;
}
