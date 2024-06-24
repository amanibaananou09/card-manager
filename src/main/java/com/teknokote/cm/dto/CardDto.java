package com.teknokote.cm.dto;

import com.teknokote.cm.core.model.EnumCardStatus;
import com.teknokote.cm.core.model.EnumCardType;
import com.teknokote.core.dto.ESSActivatableDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class CardDto extends ESSActivatableDto<Long> {
    private String tag;
    private EnumCardType type;
    private String companyName;
    private String holder;
    private String cardId;
    private String codePin;
    private String holderPassword;
    private LocalDate expirationDate;
    private EnumCardStatus status;
    private Long cardGroupId;
    private Long accountId;
    private String cardGroupName;


    @Builder
    public CardDto(Long id, Long version, boolean actif, LocalDateTime dateStatusChange, String tag, EnumCardType type, EnumCardStatus status, Long cardGroupId, Long accountId, String companyName, String holder, String cardId, String codePin, String holderPassword, LocalDate expirationDate, String cardGroupName) {
        super(id, version, actif, dateStatusChange);
        this.tag = tag;
        this.type = type;
        this.status = status;
        this.cardGroupId = cardGroupId;
        this.accountId = accountId;
        this.companyName = companyName;
        this.holder = holder;
        this.cardId = cardId;
        this.codePin = codePin;
        this.holderPassword = holderPassword;
        this.expirationDate = expirationDate;
        this.cardGroupName = cardGroupName;
    }
}
