package com.teknokote.cm.dto;

import com.teknokote.core.dto.ESSIdentifiedDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserDto extends ESSIdentifiedDto<Long>
{
   private String username;
   private String tag;
   private Long customerId;
   private LocalDateTime lastConnectionDate;

   @Builder
   public UserDto(Long id, Long version, String username, String tag, Long customerId,LocalDateTime lastConnectionDate)
   {
      super(id, version);
      this.username = username;
      this.tag = tag;
      this.customerId = customerId;
      this.lastConnectionDate=lastConnectionDate;
   }
}
