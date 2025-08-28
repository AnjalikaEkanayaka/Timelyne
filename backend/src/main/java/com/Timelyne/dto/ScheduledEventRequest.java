package com.Timelyne.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ScheduledEventRequest {
    
    @NotBlank(message = "title is required")
    @Size(max = 120, message = "title must be at most 120 characters")
    private String title;

    @Size(max = 10_000, message = "description is too long")
    private String description;

    @NotNull(message = "startTime is required")
    private LocalDateTime startTime;

    @NotNull(message = "endTime is required")
    private LocalDateTime endTime;



}
