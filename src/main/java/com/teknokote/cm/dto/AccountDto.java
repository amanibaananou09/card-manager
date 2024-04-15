package com.teknokote.cm.dto;

import com.teknokote.cm.core.model.EnumAccountStatus;
import com.teknokote.core.dto.ESSActivatableDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
public class AccountDto extends ESSActivatableDto<Long> {
    private String bankName;
    private String code;
    private String label;
    private String description;
    private EnumAccountStatus status;
    private Long customerId;
    private Set<MovementDto> movements;

    @Builder
    public AccountDto(Long id, Long version, boolean actif, LocalDateTime dateStatusChange, String code, String label, String description, Long customerId, String bankName, EnumAccountStatus status, Set<MovementDto> movements) {
        super(id, version, actif, dateStatusChange);
        this.bankName = bankName;
        this.code = code;
        this.label = label;
        this.description = description;
        this.status = status;
        this.customerId = customerId;
        this.movements = movements;
    }
}
