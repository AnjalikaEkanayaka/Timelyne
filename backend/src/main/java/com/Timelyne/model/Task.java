package com.Timelyne.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "core_tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private int hoursRequired;     //  NEW: Only whole hours
    private int minutesRequired;   //  NEW: Must be 0, 15, 30, or 45

    private int minutesDone;

    private LocalDateTime deadline;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Task parentTask;

    public int getTotalMinutesRequired() {
        return (hoursRequired * 60) + minutesRequired;
    }

    public int getProgress() {
        int totalMinutes = getTotalMinutesRequired();
        if (totalMinutes == 0) return 0;
        return (int) ((minutesDone * 100.0) / totalMinutes);
    }
}
