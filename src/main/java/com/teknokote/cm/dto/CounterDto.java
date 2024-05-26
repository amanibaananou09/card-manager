package com.teknokote.cm.dto;

import com.teknokote.cm.core.model.EnumCounterType;
import com.teknokote.core.dto.ESSIdentifiedDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CounterDto extends ESSIdentifiedDto<Long> {
    private EnumCounterType counterType;
    private String name;
    private BigDecimal value;
    private BigDecimal dailyLimitValue;

    @Builder
    public CounterDto(Long id,Long version,EnumCounterType counterType, String name, BigDecimal value,BigDecimal dailyLimitValue) {
        super(id,version);
        this.counterType = counterType;
        this.name = name;
        this.value = value;
        this.dailyLimitValue=dailyLimitValue;
    }
}
