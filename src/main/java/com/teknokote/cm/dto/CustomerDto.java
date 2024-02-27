package com.teknokote.cm.dto;

import com.teknokote.core.dto.ESSIdentifiedDto;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDto extends ESSIdentifiedDto<Long>{
    private String name;
    private Long parentId;
    @Builder
    public CustomerDto(Long id,Long version,String name,Long parentId)
    {
        super(id,version);
        this.name = name;
        this.parentId = parentId;
    }
}
