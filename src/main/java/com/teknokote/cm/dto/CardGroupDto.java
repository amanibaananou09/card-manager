package com.teknokote.cm.dto;

import com.teknokote.core.dto.ESSActivatableDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CardGroupDto extends ESSActivatableDto<Long>{
    private String name;
    private String condition;
    private Long customerId;
    @Builder
    public CardGroupDto(Long id, Long version, boolean actif, LocalDateTime dateStatusChange, String name, String condition,Long customerId)
    {
        super(id,version,actif,dateStatusChange);
        this.name = name;
        this.condition = condition;
        this.customerId=customerId;
    }
}
