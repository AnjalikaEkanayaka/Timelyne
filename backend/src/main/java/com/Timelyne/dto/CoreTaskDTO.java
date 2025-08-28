package com.Timelyne.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoreTaskDTO {

    private String title;

    private String description;

    private int hoursRequired;     
    private int minutesRequired;   //  Must be 0, 15, 30, 45

    private LocalDateTime deadline;

    private Long parentTaskId;     // optional: if it's a subtask
}
