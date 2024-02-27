package com.teknokote.cm.dto;

import com.teknokote.core.dto.ESSIdentifiedDto;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto extends ESSIdentifiedDto<Long>{
    private String username;
    private String tag;
    private Long customerId;
    @Builder
    public UserDto(Long id,Long version,String username,String tag,Long customerId)
    {
        super(id,version);
        this.username = username;
        this.tag = tag;
        this.customerId = customerId;
    }
}
