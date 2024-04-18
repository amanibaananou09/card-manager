package com.teknokote.cm.dto;

import com.teknokote.core.dto.ESSIdentifiedDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDto extends ESSIdentifiedDto<Long>
{
   private String code;
   private String name;
   private Long supplierId;
   private String reference;

   @Builder
   public ProductDto(Long id, Long version, String code, String name, Long supplierId)
   {
      super(id, version);
      this.code = code;
      this.name = name;
      this.supplierId= supplierId;
   }
}
