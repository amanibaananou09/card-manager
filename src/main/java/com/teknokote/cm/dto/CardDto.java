package com.teknokote.cm.dto;

import com.teknokote.cm.core.model.EnumCardStatus;
import com.teknokote.cm.core.model.EnumCardType;
import com.teknokote.core.dto.ESSActivatableDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CardDto extends ESSActivatableDto<Long>{
    private String tag;
    private EnumCardType type;
    private EnumCardStatus status;
    private Long cardGroupId;
    private Long accountId;
    @Builder
    public CardDto(Long id, Long version, boolean actif, LocalDateTime dateStatusChange, String tag, EnumCardType type, EnumCardStatus status,  Long cardGroupId, Long accountId)
    {
        super(id,version,actif,dateStatusChange);
        this.tag = tag;
        this.type = type;
        this.status = status;
        this.cardGroupId = cardGroupId;
        this.accountId=accountId;
    }
}
