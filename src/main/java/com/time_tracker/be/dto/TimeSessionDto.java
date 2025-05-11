package com.time_tracker.be.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.ZonedDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TimeSessionDto {
    private Long idUser;
    private Long idTimeSession;
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;
    private String description;

    // Getter otomatis untuk durasi
    public Duration getDuration() {
        if (startTime != null && endTime != null) {
            return Duration.between(startTime, endTime);
        }
        return null;
    }
}
