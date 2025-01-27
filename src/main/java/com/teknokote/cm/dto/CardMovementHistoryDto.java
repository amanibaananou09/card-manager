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
    private Long transactionId;
    private LocalDateTime dateTime;
    private String description;

    @Builder
    public CardMovementHistoryDto(Long id, Long version, CardDto card, Long cardId, AuthorizationDto authorization, Long authorizationId, EnumCardStatus oldStatus, EnumCardStatus newStatus, Long transactionId, LocalDateTime dateTime,String description) {
        super(id, version);
        this.card = card;
        this.cardId = cardId;
        this.authorization = authorization;
        this.authorizationId = authorizationId;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
        this.transactionId = transactionId;
        this.dateTime = dateTime;
        this.description = generateDescription(oldStatus,newStatus);
    }
    private String generateDescription(EnumCardStatus oldStatus, EnumCardStatus newStatus) {

        switch (newStatus) {
            case FREE:
                return "La carte est libre et disponible à l'utilisation, statut précédent : " + oldStatus;
            case BLOCKED:
                return "La carte a été bloquée pour des raisons de sécurité ou des problèmes signalés";
            case IN_USE:
                return "La carte est en cours d'utilisation";
            case AUTHORIZED:
                return "La carte a été autorisée";
            case CONFIRMATION:
                return "Confirmation de l'autorisation pour la transaction";
            default:
                return "Le statut de la carte a changé de " + oldStatus + " à " + newStatus;
        }
    }
}
