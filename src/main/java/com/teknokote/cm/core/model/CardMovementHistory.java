package com.teknokote.cm.core.model;

import com.teknokote.core.model.SimpleESSEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@Entity
@Table(name = "cm_card_movement_history")
public class CardMovementHistory extends SimpleESSEntity<Long> {

    @ManyToOne(fetch = FetchType.LAZY)
    private Card card;
    @Column(name = "card_id", insertable = false, updatable = false)
    private Long cardId;
    @ManyToOne(fetch = FetchType.LAZY)
    private Authorization authorization;
    @Column(name = "authorization_id", insertable = false, updatable = false)
    private Long authorizationId;
    @Enumerated(EnumType.STRING)
    private EnumCardStatus oldStatus;
    @Enumerated(EnumType.STRING)
    private EnumCardStatus newStatus;
    private Long ctrTransactionRef;
    private LocalDateTime dateTime;
}
