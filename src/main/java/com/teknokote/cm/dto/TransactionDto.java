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
   private String productName;
   private Long productId;
   private String reference;
   private BigDecimal availableBalance;
   private Long salePointId;
   private String salePointName;
   private String city;

   @Builder
   public TransactionDto(Long id, Long version, LocalDateTime dateTime, BigDecimal amount, BigDecimal quantity, Long cardId, Long authorizationId, Long productId,String productName,String reference,BigDecimal availableBalance,Long salePointId,String salePointName,String city)
   {
      super(id, version);
      this.dateTime = dateTime;
      this.amount = amount;
      this.quantity = quantity;
      this.cardId = cardId;
      this.authorizationId = authorizationId;
      this.productName=productName;
      this.reference=reference;
      this.productId = productId;
      this.availableBalance=availableBalance;
      this.salePointId=salePointId;
      this.salePointName=salePointName;
      this.city=city;
   }

   public TransactionDto(Long id, Long version) {
      super(id, version);
   }
}
