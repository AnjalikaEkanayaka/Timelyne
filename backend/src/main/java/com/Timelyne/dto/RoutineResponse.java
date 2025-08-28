package com.Timelyne.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class RoutineResponse {

    private Long id;
    private String title;
    private String description;
    private Integer minutesPerDay;
    private Boolean active;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

}
