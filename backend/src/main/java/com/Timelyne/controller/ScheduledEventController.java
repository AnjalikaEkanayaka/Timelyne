package com.Timelyne.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.Timelyne.dto.ScheduledEventRequest;
import com.Timelyne.dto.ScheduledEventResponse;
import com.Timelyne.service.ScheduledEventService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/scheduled-events")
@RequiredArgsConstructor

public class ScheduledEventController {

    // DI: Spring injects the service bean via Lombok-generated constructor
    private final ScheduledEventService service;

    @PostMapping
    public ScheduledEventResponse create(@Valid @RequestBody ScheduledEventRequest req){
        return service.create(req);
    }

    @PutMapping("/{id}")
    public ScheduledEventResponse update(@PathVariable Long id, @Valid @RequestBody ScheduledEventRequest req){
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        service.delete(id);
    }

    //Read One
    @GetMapping("/{id}")
    public ScheduledEventResponse get(@PathVariable Long id){
        return service.getById(id);
    }

    //Read All
    @GetMapping
    public List<ScheduledEventResponse> listAll(){
        return service.listAll();
    }

    // from-to
    @GetMapping(params = {"from", "to"})
    public List<ScheduledEventResponse> listByRange(
        @RequestParam LocalDateTime from,
        @RequestParam LocalDateTime to
    ){
        return service.listByRange(from, to);
    }
}