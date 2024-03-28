package com.teknokote.cm.dto;

import com.teknokote.core.dto.ESSIdentifiedDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CeilingDto extends ESSIdentifiedDto<Long>{
    private String condition;
    @Builder
    public CeilingDto(Long id,Long version,String condition)
    {
        super(id,version);
        this.condition = condition;
    }
}
