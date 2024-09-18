package com.teknokote.cm.dto;

import com.teknokote.core.dto.ESSIdentifiedDto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalePointDto extends ESSIdentifiedDto<Long> {
    @NotEmpty
    private String identifier;
    private String name;
    private String city;
    private String area;
    private Long supplierId;
    private String countryName;
    private String reference;
    private String phone;
    private boolean status;
    private CountryDto country;
    @NotNull
    private Long countryId;

    @Builder
    public SalePointDto(Long id, Long version, String name, String city, String area, Long supplierId, boolean status, Long countryId, CountryDto country, String reference, String phone, String countryName, String identifier) {
        super(id, version);
        this.identifier = identifier;
        this.name = name;
        this.city = city;
        this.area = area;
        this.supplierId = supplierId;
        this.countryId = countryId;
        this.country = country;
        this.reference = reference;
        this.phone = phone;
        this.status = status;
        this.countryName = countryName;
    }
}
