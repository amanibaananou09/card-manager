package com.teknokote.cm.core.model;

import com.teknokote.core.model.ActivatableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class Card extends ActivatableEntity<Long, User>
{
   @Serial
   private static final long serialVersionUID = -1049193271529044915L;
   private String code;
   @Enumerated(EnumType.STRING)
   private EnumCardType type;
   @Enumerated(EnumType.STRING)
   private EnumCardStatus status;
   private BigDecimal ceiling;
   @ManyToOne(fetch = FetchType.LAZY)
   private Account account;
   @Column(name = "account_id", insertable = false, updatable = false)
   private Long accountId;
   @OneToMany(mappedBy = "card")
   private Set<Authorization> authorizations = new HashSet<>();
   @OneToMany(mappedBy = "card")
   private Set<Transaction> transactions = new HashSet<>();
}
