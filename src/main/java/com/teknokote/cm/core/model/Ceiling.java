package com.teknokote.cm.core.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

@Getter
@Setter
@Entity
@DiscriminatorValue(value = EnumCounterType.CounterType.CEILING)
public class Ceiling extends Counter
{
   @Serial
   private static final long serialVersionUID = 2460709192312470325L;
   private String condition;
   @Enumerated(EnumType.STRING)
   private EnumCeilingType ceilingType;
   @Enumerated(EnumType.STRING)
   private EnumCeilingLimitType limitType;

}
