package com.Timelyne.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiError {
    private LocalDateTime timastamp;        // when error happened
    private int status;                     // HTTP status code
    private String error;                   // title
    private String message;                 // huma-friendly message
    private String path;                    // request path that caused the error
}
