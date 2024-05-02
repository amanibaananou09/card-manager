package com.teknokote.cm.dto;

import com.teknokote.cm.core.model.Counter;
import com.teknokote.cm.core.model.GroupCondition;
import com.teknokote.core.dto.ESSActivatableDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
public class CardGroupDto extends ESSActivatableDto<Long>{
    private String name;
    private String condition;
    private Long customerId;
    private GroupConditionDto groupCondition;
    private Set<CounterDto> counters;
    @Builder
    public CardGroupDto(Long id, Long version, boolean actif, LocalDateTime dateStatusChange, String name, String condition,Long customerId,GroupConditionDto groupCondition,Set<CounterDto> counters)
    {
        super(id,version,actif,dateStatusChange);
        this.name = name;
        this.condition = condition;
        this.customerId=customerId;
        this.groupCondition=groupCondition;
        this.counters=counters;
    }
}
