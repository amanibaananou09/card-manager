package com.teknokote.cm.core.model;

import com.teknokote.core.model.ESSEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@MappedSuperclass
public abstract class Counter  extends ESSEntity<Long, User> implements Serializable
{
   @Serial
   private static final long serialVersionUID = -8693534709306863581L;
   @Enumerated(EnumType.STRING)
   private EnumCounterType counterType;
   private String name;
   private BigDecimal value;
   private BigDecimal dailyLimitValue;
}
