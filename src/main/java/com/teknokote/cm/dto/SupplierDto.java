package com.teknokote.cm.dto;

import com.teknokote.cm.core.model.EnumOriginModule;
import com.teknokote.core.dto.ESSActivatableDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SupplierDto extends ESSActivatableDto<Long>{
    private EnumOriginModule origin;
    private String reference;
    private String name;
    private String address;
    private String phone;
    @Builder
    public SupplierDto(Long id, Long version, boolean actif, LocalDateTime dateStatusChange, EnumOriginModule origin, String reference, String name, String address, String phone)
    {
        super(id,version,actif,dateStatusChange);
        this.origin = origin;
        this.reference = reference;
        this.name = name;
        this.address = address;
        this.phone = phone;
    }
}
