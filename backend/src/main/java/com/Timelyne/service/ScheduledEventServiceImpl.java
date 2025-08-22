package com.Timelyne.service;

import com.Timelyne.dto.ScheduledEventRequest;
import com.Timelyne.dto.ScheduledEventResponse;
import com.Timelyne.model.ScheduledEvents;
import com.Timelyne.repository.ScheduledEventRepo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor

public class ScheduledEventServiceImpl implements ScheduledEventService {

    private final ScheduledEventRepo repo; // injected by Spring via constructor

    @Override
    public ScheduledEventResponse create(ScheduledEventRequest req) {
        validateTimes(req.getStartTime(), req.getEndTime());
        ensureNoOverlap(req.getStartTime(), req.getEndTime(), null);

        ScheduledEvents entity = toEntity(req);
        ScheduledEvents saved  = repo.save(entity);

        // (later) call PlannerService to recalc schedule
        return toResponse(saved);
    }

    @Override
    public ScheduledEventResponse update(Long id, ScheduledEventRequest req) {
        validateTimes(req.getStartTime(), req.getEndTime());
        ensureNoOverlap(req.getStartTime(), req.getEndTime(), id);

        ScheduledEvents entity = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        copyFromRequest(req, entity);        // mutate existing entity
        ScheduledEvents saved = repo.save(entity);
        return toResponse(saved);
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
        // (later) planner.recalculateForUser(...)
    }

    @Override
    @Transactional(readOnly = true)
    public ScheduledEventResponse getById(Long id) {
        ScheduledEvents e = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));
        return toResponse(e);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScheduledEventResponse> listAll() {
        return repo.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScheduledEventResponse> listByRange(LocalDateTime from, LocalDateTime to){
        if (!to.isAfter(from)) {
        throw new IllegalArgumentException("'to' must be after 'from'");
        }
        // Return events that overlap the requested window (so partial-day events still show)
        var overlapping = repo.findByStartTimeLessThanAndEndTimeGreaterThan(to, from);
        // sort by startTime for nicer UI
        return overlapping.stream()
                .sorted((a, b) -> a.getStartTime().compareTo(b.getStartTime()))
                .map(this::toResponse)
                .toList();
    }

    // ---------- helpers (business rules + mapping) ----------

    private void validateTimes(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("startTime and endTime are required");
        }
        if (!end.isAfter(start)) {
            throw new IllegalArgumentException("endTime must be after startTime");
        }
        if (start.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot schedule event in the past");
        }
    }

    private void ensureNoOverlap(LocalDateTime start, LocalDateTime end, Long selfId) {
        // Find events that collide with [start, end) — half-open interval (touching allowed)
        var overlaps = repo.findByStartTimeLessThanAndEndTimeGreaterThan(end, start);
        boolean conflict = overlaps.stream()
                .anyMatch(e -> selfId == null || !e.getId().equals(selfId));
        if (conflict) {
            throw new IllegalStateException("Event time overlaps with an existing event");
        }
    }

    // --- mapping: DTO <-> Entity (kept private to keep controller clean) ---

    private ScheduledEvents toEntity(ScheduledEventRequest req) {
        ScheduledEvents e = new ScheduledEvents();
        copyFromRequest(req, e);
        return e;
    }

    private void copyFromRequest(ScheduledEventRequest req, ScheduledEvents e) {
        e.setTitle(req.getTitle());
        e.setDescription(req.getDescription());
        e.setStartTime(req.getStartTime());
        e.setEndTime(req.getEndTime());
    }

    private ScheduledEventResponse toResponse(ScheduledEvents e) {
        return new ScheduledEventResponse(
                e.getId(), e.getTitle(), e.getDescription(),
                e.getStartTime(), e.getEndTime()
        );
    }
}
