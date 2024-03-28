package com.teknokote.cm.dto;

import com.teknokote.cm.core.model.EnumActivityType;
import com.teknokote.core.dto.ESSIdentifiedDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserHistoryDto extends ESSIdentifiedDto<Long>{
    private Long userId;
    private EnumActivityType activityType;
    private String requestURI;
    private String requestHandler;
    private String ipAddress;
    private LocalDateTime activityDate;
    @Builder
    public UserHistoryDto(Long id,Long version,Long userId,EnumActivityType activityType,String requestURI,String requestHandler,String ipAddress,LocalDateTime activityDate)
    {
        super(id,version);
        this.userId = userId;
        this.activityType = activityType;
        this.requestURI = requestURI;
        this.requestHandler = requestHandler;
        this.ipAddress = ipAddress;
        this.activityDate = activityDate;
    }
}
