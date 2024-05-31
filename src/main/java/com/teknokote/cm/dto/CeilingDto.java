package com.teknokote.cm.dto;

import com.teknokote.cm.core.model.EnumCeilingLimitType;
import com.teknokote.cm.core.model.EnumCeilingType;
import com.teknokote.cm.core.model.EnumCounterType;
import com.teknokote.core.dto.ESSIdentifiedDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CeilingDto extends ESSIdentifiedDto<Long>{
    private EnumCounterType counterType;
    private String name;
    private BigDecimal value;
    private BigDecimal dailyLimitValue;
    private String condition;
    private EnumCeilingType ceilingType;
    private EnumCeilingLimitType limitType;
    @Builder
    public CeilingDto(Long id, Long version, EnumCounterType counterType, String name, BigDecimal value, BigDecimal dailyLimitValue, String condition, EnumCeilingType ceilingType, EnumCeilingLimitType limitType) {
        super(id, version);
        this.counterType = counterType;
        this.name = name;
        this.value = value;
        this.dailyLimitValue = dailyLimitValue;
        this.condition = condition;
        this.ceilingType = ceilingType;
        this.limitType = limitType;
    }
}
