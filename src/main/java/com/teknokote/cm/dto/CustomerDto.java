package com.teknokote.cm.dto;

import com.teknokote.core.dto.ESSActivatableDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CustomerDto extends ESSActivatableDto<Long>{
    private String name;
    private Long supplierId;
    @Builder
    public CustomerDto(Long id, Long version, boolean actif, LocalDateTime dateStatusChange, String name,Long supplierId)
    {
        super(id,version,actif,dateStatusChange);
        this.name = name;
        this.supplierId = supplierId;
    }
}
