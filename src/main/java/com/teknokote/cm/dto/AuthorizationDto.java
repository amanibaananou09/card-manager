package com.teknokote.cm.dto;

import com.teknokote.core.dto.ESSIdentifiedDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class AuthorizationDto extends ESSIdentifiedDto<Long>{
    private String reference;
    private LocalDateTime dateTime;
    private BigDecimal amount;
    private BigDecimal quantity;
    private Long cardId;
    @Builder
    public AuthorizationDto(Long id,Long version,String reference,LocalDateTime dateTime,BigDecimal amount,BigDecimal quantity,Long cardId)
    {
        super(id,version);
        this.reference = reference;
        this.dateTime = dateTime;
        this.amount = amount;
        this.quantity = quantity;
        this.cardId = cardId;
    }
}
