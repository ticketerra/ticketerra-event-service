package com.ticketerra.event_service.service;

import com.ticketerra.event_service.dto.EventRequest;
import com.ticketerra.event_service.dto.EventResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EventService {
    EventResponse createEvent(EventRequest eventRequest);
    List<EventResponse> getEvents();
    EventResponse getEvent(String id);
    EventResponse updateEvent(String id, EventRequest eventRequest);

    void deleteEvent(String id);
}
