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
@Table(name = "cm_movement")
public class Movement extends ESSEntity<Long, User>
{
   @Serial
   private static final long serialVersionUID = -1268120451180082255L;
   @Enumerated(EnumType.STRING)
   private EnumMovementType type;
   private LocalDateTime dateTime;
   private BigDecimal amount;
   @ManyToOne(fetch = FetchType.LAZY)
   private Account account;
   @Column(name = "account_id", insertable = false, updatable = false)
   private Long accountId;
}
