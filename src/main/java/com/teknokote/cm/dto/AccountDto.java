package com.teknokote.cm.dto;

import com.teknokote.core.dto.ESSIdentifiedDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountDto extends ESSIdentifiedDto<Long>
{
   private String code;
   private String label;
   private String description;
   private Long customerId;

   @Builder
   public AccountDto(Long id, Long version, String code, String label, String description, Long customerId)
   {
      super(id, version);
      this.code = code;
      this.label = label;
      this.description = description;
      this.customerId = customerId;
   }
}
