package com.Timelyne.service;

import com.Timelyne.dto.RoutineRequest;
import com.Timelyne.dto.RoutineResponse;
import com.Timelyne.model.Routine;
import com.Timelyne.repository.RoutineRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;           //  add
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoutineService {

    private static final Logger log = LoggerFactory.getLogger(RoutineService.class);  //  add

    private static final int DAILY_BASE_CAPACITY_MINUTES = 16 * 60;

    private final RoutineRepository routineRepository;

    public RoutineService(RoutineRepository routineRepository) {
        this.routineRepository = routineRepository;
    }

    public RoutineResponse create(String userEmail, RoutineRequest req) {
        validateRequest(req);

        boolean willBeActive = req.getActive() == null ? true : req.getActive();
        int newMinutes = req.getMinutesPerDay() == null ? 0 : req.getMinutesPerDay();

        if (willBeActive) {
            int currentActiveTotal = routineRepository.sumActiveMinutesByUser(userEmail);
            int newTotal = currentActiveTotal + newMinutes;
            if (newTotal > DAILY_BASE_CAPACITY_MINUTES) {
                // log so you see it in VS Code terminal
                log.warn("Capacity exceeded for user={} current={} new={} limit={}",
                        userEmail, currentActiveTotal, newMinutes, DAILY_BASE_CAPACITY_MINUTES);

                //  throw with clear reason (ControllerAdvice will echo it to Postman)
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Not enough daily capacity to add this routine. Reduce minutes or deactivate others."
                );
            }
        }

        Routine r = new Routine();
        r.setUserEmail(userEmail);
        r.setTitle(req.getTitle());
        r.setDescription(req.getDescription());
        r.setMinutesPerDay(newMinutes);
        r.setActive(req.getActive() == null ? true : req.getActive());

        r = routineRepository.save(r);
        return toResponse(r);
    }

    public List<RoutineResponse> listMine(String userEmail) {
        return routineRepository.findByUserEmailOrderByTitleAsc(userEmail)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public RoutineResponse update(String userEmail, Long id, RoutineRequest req) {
        validateRequest(req);

        Routine existing = routineRepository.findByIdAndUserEmail(id, userEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Routine not found"));

        boolean willBeActive = req.getActive() == null ? existing.getActive() : req.getActive();
        int newMinutes = req.getMinutesPerDay();

        int currentActiveTotal = routineRepository.sumActiveMinutesByUser(userEmail);
        int oldActiveMinutes = existing.getActive() ? existing.getMinutesPerDay() : 0;
        int newActiveMinutes = willBeActive ? newMinutes : 0;
        int recomputedTotal = currentActiveTotal - oldActiveMinutes + newActiveMinutes;

        if (recomputedTotal > DAILY_BASE_CAPACITY_MINUTES) {
            log.warn("Capacity exceeded on update for user={} oldActive={} newActive={} recomputed={} limit={}",
                    userEmail, oldActiveMinutes, newActiveMinutes, recomputedTotal, DAILY_BASE_CAPACITY_MINUTES);

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Not enough daily capacity to update this routine with the requested minutes."
            );
        }

        existing.setTitle(req.getTitle());
        existing.setDescription(req.getDescription());
        existing.setMinutesPerDay(newMinutes);
        existing.setActive(willBeActive);

        existing = routineRepository.save(existing);
        return toResponse(existing);
    }

    public void delete(String userEmail, Long id) {
        Routine existing = routineRepository.findByIdAndUserEmail(id, userEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Routine not found"));
        routineRepository.delete(existing);
    }

    private void validateRequest(RoutineRequest req) {
        if (req.getTitle() == null || req.getTitle().isBlank()) {
            log.warn("Validation failed: title missing"); //  logs to terminal
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title is required.");
        }
        if (req.getMinutesPerDay() == null || req.getMinutesPerDay() <= 0) {
            log.warn("Validation failed: minutesPerDay invalid value={}", req.getMinutesPerDay());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "minutesPerDay must be a positive number.");
        }
        if (req.getMinutesPerDay() % 15 != 0) {
            log.warn("Validation failed: minutesPerDay not multiple of 15 value={}", req.getMinutesPerDay());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "minutesPerDay must be a multiple of 15.");
        }
    }

    private RoutineResponse toResponse(Routine r) {
        RoutineResponse dto = new RoutineResponse();
        dto.setId(r.getId());
        dto.setTitle(r.getTitle());
        dto.setDescription(r.getDescription());
        dto.setMinutesPerDay(r.getMinutesPerDay());
        dto.setActive(r.getActive());

        dto.setCreateAt(r.getCreateAt());
        dto.setUpdateAt(r.getUpdateAt());

        return dto;
    }
}
