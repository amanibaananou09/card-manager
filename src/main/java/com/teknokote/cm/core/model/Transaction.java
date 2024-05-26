package com.teknokote.cm.core.model;

import com.teknokote.core.model.ESSEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "cm_transaction")
public class Transaction extends ESSEntity<Long, User>
{
   @Serial
   private static final long serialVersionUID = -8894883898791331704L;
   private LocalDateTime dateTime;
   private BigDecimal amount;
   private BigDecimal quantity;
   @ManyToOne(fetch = FetchType.LAZY)
   private Card card;
   @Column(name = "card_id", insertable = false, updatable = false)
   private Long cardId;
   @ManyToOne
   private Authorization authorization;
   @Column(name = "authorization_id", insertable = false, updatable = false)
   private Long authorizationId;
   @ManyToOne
   private Product product;
   @Column(name = "product_id", insertable = false, updatable = false)
   private Long productId;
   private BigDecimal availableBalance;
}
