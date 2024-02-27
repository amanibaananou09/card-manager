package com.teknokote.cm.dto;

import com.teknokote.cm.core.model.Account;
import com.teknokote.cm.core.model.EnumCardStatus;
import com.teknokote.cm.core.model.EnumCardType;
import com.teknokote.core.dto.ESSIdentifiedDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CardDto extends ESSIdentifiedDto<Long>{
    private String code;
    private EnumCardType type;
    private EnumCardStatus status;
    private BigDecimal ceiling;
    private Long accountId;
    @Builder
    public CardDto(Long id,Long version,String code,EnumCardType type,EnumCardStatus status,BigDecimal ceiling,Account account,Long accountId)
    {
        super(id,version);
        this.code = code;
        this.type = type;
        this.status = status;
        this.ceiling = ceiling;
        this.accountId = accountId;
    }
}
