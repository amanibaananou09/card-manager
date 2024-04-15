package com.teknokote.cm.dto;

import com.teknokote.cm.core.model.EnumOriginModule;
import com.teknokote.cm.core.model.SalePoint;
import com.teknokote.cm.core.model.User;
import com.teknokote.core.dto.ESSActivatableDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
public class SupplierDto extends ESSActivatableDto<Long>{
    private EnumOriginModule origin;
    private String reference;
    private String name;
    private String address;
    private String city;
    private String phone;
    private Set<SalePointDto> salePoints;
    private Set<UserDto> users;
    @Builder
    public SupplierDto(Long id, Long version, boolean actif, LocalDateTime dateStatusChange, EnumOriginModule origin, String reference, String name, String address, String phone,String city,Set<SalePointDto> salePoints,Set<UserDto> users)
    {
        super(id,version,actif,dateStatusChange);
        this.origin = origin;
        this.reference = reference;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.city=city;
        this.salePoints=salePoints;
        this.users=users;
    }

}
