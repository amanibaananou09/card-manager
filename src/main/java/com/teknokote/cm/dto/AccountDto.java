package com.teknokote.cm.dto;

import com.teknokote.core.dto.ESSActivatableDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AccountDto extends ESSActivatableDto<Long>{
    private String code;
    private String label;
    private String description;
    private Long customerId;
    @Builder
    public AccountDto(Long id, Long version, boolean actif, LocalDateTime dateStatusChange, String code, String label, String description,Long customerId)
    {
        super(id,version,actif,dateStatusChange);
        this.code = code;
        this.label = label;
        this.description = description;
        this.customerId = customerId;
    }
}
