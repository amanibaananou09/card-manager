package com.teknokote.cm.dto;

import com.teknokote.cm.core.model.EnumCeilingLimitType;
import com.teknokote.cm.core.model.EnumCeilingType;
import com.teknokote.core.dto.ESSIdentifiedDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CeilingDto extends ESSIdentifiedDto<Long>{
    private String condition;
    private EnumCeilingType ceilingType;
    private EnumCeilingLimitType limitType;
    @Builder
    public CeilingDto(Long id, Long version,EnumCeilingType ceilingType, EnumCeilingLimitType limitType, String condition)
    {
        super(id, version);
        this.condition = condition;
        this.ceilingType=ceilingType;
        this.limitType=limitType;
    }
}
