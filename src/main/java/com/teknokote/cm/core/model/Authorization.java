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
@Table(name = "cm_authorization",uniqueConstraints = @UniqueConstraint(columnNames = {"reference"}))
public class Authorization extends ESSEntity<Long, User>
{
   @Serial
   private static final long serialVersionUID = -4849769892990810892L;
   private String reference;
   private LocalDateTime dateTime;
   private BigDecimal amount;
   private BigDecimal quantity;
   @Enumerated(EnumType.STRING)
   EnumAuthorizationStatus status;
   @ManyToOne(fetch = FetchType.LAZY)
   private Card card;
   @Column(name = "card_id", insertable = false, updatable = false)
   private Long cardId;
}
