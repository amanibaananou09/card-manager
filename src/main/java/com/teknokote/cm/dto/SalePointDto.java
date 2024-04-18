package com.teknokote.cm.dto;

import com.teknokote.core.dto.ESSIdentifiedDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalePointDto extends ESSIdentifiedDto<Long>{
    private String name;
    private String city;
    private String area;
    private Long supplierId;
    private Long countryId;
    private String reference;
    @Builder
    public SalePointDto(Long id, Long version,String name,String city,String area,Long supplierId,Long countryId,String reference)
    {
        super(id,version);
        this.name = name;
        this.city = city;
        this.area = area;
        this.supplierId = supplierId;
        this.countryId=countryId;
        this.reference=reference;
    }
}
