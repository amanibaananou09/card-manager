package com.teknokote.cm.core.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

@Getter
@Setter
@Entity
@DiscriminatorValue(value = EnumCounterType.CounterType.BONUS)
public class Bonus extends Counter
{
   @Serial
   private static final long serialVersionUID = 4350361557543032426L;

}
