package com.Timelyne.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity                             // tells JPA this class maps to a table
@Table(name = "scheduledEvents")    // table name
@Data
@AllArgsConstructor
@NoArgsConstructor

public class ScheduledEvents {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                // primary key (auto-increment)

    @Column(nullable = false, length = 120)
    private String title;  // "Meeting"

    @Column(columnDefinition = "text")
    private String description; // "Discuss project updates"

    @Column(nullable = false)
    private LocalDateTime startTime; // date+time

    @Column(nullable = false)
    private LocalDateTime endTime;


}
