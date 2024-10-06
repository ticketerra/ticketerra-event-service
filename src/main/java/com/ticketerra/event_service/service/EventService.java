package com.ticketerra.event_service.service;

import com.ticketerra.event_service.dto.EventRequest;
import com.ticketerra.event_service.dto.EventResponse;
import com.ticketerra.event_service.dto.PaginatedResponse;
import org.springframework.stereotype.Service;


@Service
public interface EventService {
    EventResponse createEvent(EventRequest eventRequest);
    PaginatedResponse<EventResponse> getEvents(Integer offset, Integer limit);
    EventResponse getEvent(String id);
    EventResponse updateEvent(String id, EventRequest eventRequest);

    void deleteEvent(String id);
}
