package com.Timelyne.repository;

import com.Timelyne.model.ScheduledEvents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScheduledEventRepo extends JpaRepository<ScheduledEvents, Long> {

    // Custom query to check for overlapping events
    List<ScheduledEvents> findByStartTimeLessThanAndEndTimeGreaterThan(
            LocalDateTime end, LocalDateTime start
    );
    
}
