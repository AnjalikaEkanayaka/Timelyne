package com.Timelyne.repository;

import com.Timelyne.model.Routine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoutineRepository extends JpaRepository<Routine, Long> {

    // 1) Get all my routines
    List<Routine> findByUserEmailOrderByTitleAsc(String userEmail);

    // 2) Load a single routine, but ONLY if it belongs to this user 
    Optional<Routine> findByIdAndUserEmail(Long id, String userEmail);

    // 3) Sum of all ACTIVE routine minutes for this user 
    @Query("select coalesce(sum(r.minutesPerDay), 0) from Routine r where r.userEmail = :userEmail and r.active = true")
    Integer sumActiveMinutesByUser(@Param("userEmail") String userEmail);
}
