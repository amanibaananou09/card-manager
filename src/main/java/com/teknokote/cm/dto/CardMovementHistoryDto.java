package com.teknokote.cm.dto;

import com.teknokote.cm.core.model.EnumCardStatus;
import com.teknokote.core.dto.ESSIdentifiedDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class CardMovementHistoryDto extends ESSIdentifiedDto<Long> {

    private CardDto card;
    private Long cardId;
    private AuthorizationDto authorization;
    private Long authorizationId;
    private EnumCardStatus oldStatus;
    private EnumCardStatus newStatus;
    private Long ctrTransactionRef;
    private LocalDateTime dateTime;
    private String description;

    @Builder
    public CardMovementHistoryDto(Long id, Long version, CardDto card, Long cardId, AuthorizationDto authorization, Long authorizationId, EnumCardStatus oldStatus, EnumCardStatus newStatus, Long ctrTransactionRef, LocalDateTime dateTime,String description) {
        super(id, version);
        this.card = card;
        this.cardId = cardId;
        this.authorization = authorization;
        this.authorizationId = authorizationId;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
        this.ctrTransactionRef = ctrTransactionRef;
        this.dateTime = dateTime;
    }
}
