package com.teknokote.cm.dto;

import com.teknokote.core.dto.ESSActivatableDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
public class CustomerDto extends ESSActivatableDto<Long> {
    private String identifier;
    private String address;
    private String phone;
    private String email;
    private String city;
    private String companyName;
    private Set<AccountDto> accounts;
    private Long supplierId;
    private String supplierName;
    private Set<UserDto> users;


    @Builder
    public CustomerDto(Long id, Long version, boolean actif, LocalDateTime dateStatusChange, String city, String identifier, String address, String phone, String email, String companyName, Long supplierId, String supplierName, Set<AccountDto> accounts, Set<UserDto> users) {
        super(id, version, actif, dateStatusChange);
        this.identifier = identifier;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.city = city;
        this.companyName = companyName;
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.accounts = accounts;
        this.users = users;

    }
}
