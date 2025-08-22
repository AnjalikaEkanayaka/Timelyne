package com.Timelyne.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class RoutineRequest {
    private String title;          
    private String description;    
    private Integer minutesPerDay; 
    private Boolean active;       
}
