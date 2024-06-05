package com.teknokote.cm.core.model;

import com.teknokote.core.model.ESSEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "cm_counter")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "counter_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Counter extends ESSEntity<Long, User>
{
   @Serial
   private static final long serialVersionUID = -8693534709306863581L;
   @Enumerated(EnumType.STRING)
   @Column(insertable=false, updatable=false,name = "counter_type")
   private EnumCounterType counterType;
   private String name;
   private BigDecimal value;
   private BigDecimal dailyLimitValue;
}
