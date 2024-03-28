package com.teknokote.cm.core.model;

import com.teknokote.core.model.ActivatableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

@Getter
@Setter
@Entity
@Table(name = "cm_card")
public class Card extends ActivatableEntity<Long, User>
{
   @Serial
   private static final long serialVersionUID = -1049193271529044915L;
   private String tag;
   @Enumerated(EnumType.STRING)
   private EnumCardType type;
   @Enumerated(EnumType.STRING)
   private EnumCardStatus status;
   @ManyToOne(fetch = FetchType.LAZY)
   private CardGroup cardGroup;
   @Column(name = "card_group_id", insertable = false, updatable = false)
   private Long cardGroupId;
   @ManyToOne(fetch = FetchType.LAZY)
   private Account account;
   @Column(name = "account_id", insertable = false, updatable = false)
   private Long accountId;
}
