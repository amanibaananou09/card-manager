package com.teknokote.cm.dto;

import com.teknokote.core.dto.ESSIdentifiedDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class TransactionDto extends ESSIdentifiedDto<Long>
{
   private LocalDateTime dateTime;
   private BigDecimal amount;
   private BigDecimal quantity;
   private Long cardId;
   private Long authorizationId;
   private Long productId;

   @Builder
   public TransactionDto(Long id, Long version, LocalDateTime dateTime, BigDecimal amount, BigDecimal quantity, Long cardId, Long authorizationId, Long productId)
   {
      super(id, version);
      this.dateTime = dateTime;
      this.amount = amount;
      this.quantity = quantity;
      this.cardId = cardId;
      this.authorizationId = authorizationId;
      this.productId = productId;
   }
}
