package com.teknokote.cm.core.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
    @Column(name = "time_slot")
    private String timeSlot;
    private String logicalOperators;
}
