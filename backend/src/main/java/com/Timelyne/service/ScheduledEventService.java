package com.Timelyne.service;

import java.time.LocalDateTime;
import java.util.List;

import com.Timelyne.dto.ScheduledEventRequest;
import com.Timelyne.dto.ScheduledEventResponse;

public interface ScheduledEventService {
    ScheduledEventResponse create(ScheduledEventRequest reg);

    ScheduledEventResponse update(Long id, ScheduledEventRequest reg);

    void delete(Long id);

    ScheduledEventResponse getById(Long id);

    List<ScheduledEventResponse> listAll();

    List<ScheduledEventResponse> listByRange(LocalDateTime from, LocalDateTime to);

}
