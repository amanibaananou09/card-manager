package com.teknokote.cm.dto;

import com.teknokote.core.dto.ESSIdentifiedDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BonusDto extends ESSIdentifiedDto<Long>{
    @Builder
    public BonusDto(Long id,Long version)
    {
        super(id,version);
    }
}
