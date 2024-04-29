package com.teknokote.cm.dto;

import com.teknokote.core.dto.ESSActivatableDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserDto extends ESSActivatableDto<Long>
{
   private String username;
   private String firstName;
   private String lastName;
   private String phone;
   private String email;
   private LocalDateTime lastConnectionDate;
   private String reference;

   @Builder
   public UserDto(Long id, Long version,Boolean actif,LocalDateTime dateStatusChange, String username,String firstName,String lastName,String email,String phone, LocalDateTime lastConnectionDate,String reference)
   {
      super(id, version,actif,dateStatusChange);
      this.username = username;
      this.firstName=firstName;
      this.lastName=lastName;
      this.email=email;
      this.phone=phone;
      this.lastConnectionDate=lastConnectionDate;
      this.reference=reference;
   }
}
