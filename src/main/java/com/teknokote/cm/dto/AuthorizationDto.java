package com.teknokote.cm.dto;

import com.teknokote.cm.core.model.EnumAuthorizationStatus;
import com.teknokote.cm.core.model.EnumCardType;
import com.teknokote.core.dto.ESSIdentifiedDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class AuthorizationDto extends ESSIdentifiedDto<Long>
{
   EnumAuthorizationStatus status;
   private String reference;
   private LocalDateTime dateTime;
   private BigDecimal amount;
   private BigDecimal quantity;
   private Long cardId;
   private EnumCardType cardType;
   private String ptsId;
   private Long pump;

   @Builder
   public AuthorizationDto(Long id, Long version, String reference, LocalDateTime dateTime, BigDecimal amount, BigDecimal quantity, Long cardId,EnumAuthorizationStatus status,EnumCardType cardType,String ptsId,Long pump)
   {
      super(id, version);
      this.status=status;
      this.reference = reference;
      this.dateTime = dateTime;
      this.amount = amount;
      this.quantity = quantity;
      this.cardId = cardId;
      this.cardType=cardType;
      this.ptsId=ptsId;
      this.pump=pump;
   }
}
