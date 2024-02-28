package com.teknokote.cm.dto;

import com.teknokote.cm.core.model.EnumMovementType;
import com.teknokote.core.dto.ESSIdentifiedDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class MovementDto extends ESSIdentifiedDto<Long>
{
   private EnumMovementType type;
   private LocalDateTime dateTime;
   private BigDecimal amount;
   private Long accountId;

   @Builder
   public MovementDto(Long id, Long version, EnumMovementType type, LocalDateTime dateTime, BigDecimal amount, Long accountId)
   {
      super(id, version);
      this.type = type;
      this.dateTime = dateTime;
      this.amount = amount;
      this.accountId = accountId;
   }
}
