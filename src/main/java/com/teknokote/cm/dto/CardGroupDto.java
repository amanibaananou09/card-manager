package com.teknokote.cm.dto;

import com.teknokote.core.dto.ESSActivatableDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class CardGroupDto extends ESSActivatableDto<Long> {
    private String name;
    private String condition;
    private Long customerId;
    private Set<CeilingDto> ceilings;
    private Set<BonusDto> bonuses;
    private GroupConditionDto groupCondition;
    @Builder
    public CardGroupDto(Long id, Long version, boolean actif, LocalDateTime dateStatusChange, String name, String condition,Long customerId,GroupConditionDto groupCondition,Set<CeilingDto> ceilings,Set<BonusDto> bonuses)
    {
        super(id,version,actif,dateStatusChange);
        this.name = name;
        this.condition = condition;
        this.customerId=customerId;
        this.groupCondition=groupCondition;
        this.ceilings=ceilings;
        this.bonuses=bonuses;
    }
    public void createConditionFromGroupCondition() {
        if (groupCondition != null) {
            condition = groupCondition.generateLogicalExpression();
        } else {
            condition = "";
        }
    }

    public List<TimeSlotDto> createListTimeSlotsFromString() {
        if (groupCondition != null) {
            return groupCondition.createListTimeSlotsFromString(groupCondition.getTimeSlot());
        }
        return null;
    }
    public List<String> createListOperatorFromString() {
        if (groupCondition != null) {
            return groupCondition.createListOperatorFromString(groupCondition.getLogicalOperators());
        }
        return null;
    }
}
