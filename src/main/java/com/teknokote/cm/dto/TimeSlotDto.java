package com.teknokote.cm.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class TimeSlotDto {
    private LocalTime startActivityTime;
    private LocalTime endActivityTime;
    @Builder
    public TimeSlotDto(LocalTime startActivityTime, LocalTime endActivityTime) {
        this.startActivityTime = startActivityTime;
        this.endActivityTime = endActivityTime;
    }
}
