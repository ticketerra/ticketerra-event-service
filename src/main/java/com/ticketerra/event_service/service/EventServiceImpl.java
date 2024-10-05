package com.ticketerra.event_service.service;

import com.ticketerra.event_service.dto.EventRequest;
import com.ticketerra.event_service.dto.EventResponse;
import com.ticketerra.event_service.model.Event;
import com.ticketerra.event_service.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService{
    private final EventRepository eventRepository;
    @Override
    public EventResponse createEvent(EventRequest eventRequest) {
        try {
            // Create a new Event entity from the request
            Event eventEntity = new Event();
            BeanUtils.copyProperties(eventRequest, eventEntity);

            // Save the event and copy to the response
            EventResponse eventResponse = new EventResponse();
            BeanUtils.copyProperties(eventRepository.save(eventEntity), eventResponse);

            return eventResponse;
        } catch (DuplicateKeyException e) {
            // Handle the unique constraint violation (e.g., duplicate title)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    public List<EventResponse> getEvents() {
        return eventRepository.findAllByIsDeletedFalse().stream().map(event -> {
            EventResponse eventResponse = new EventResponse();
            BeanUtils.copyProperties(event, eventResponse);
            return eventResponse;
        }).collect(Collectors.toList());
    }

    @Override
    public EventResponse getEvent(String id) {
        return eventRepository.findByIdAndIsDeletedFalse(id).map(event -> {
            EventResponse eventResponse = new EventResponse();
            BeanUtils.copyProperties(event, eventResponse);
            return eventResponse;
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
    }

    @Override
    public EventResponse updateEvent(String id, EventRequest eventRequest) {
        return eventRepository.findByIdAndIsDeletedFalse(id).map(event -> {

            try {
                BeanUtils.copyProperties(eventRequest, event);
                EventResponse eventResponse = new EventResponse();
                BeanUtils.copyProperties(eventRepository.save(event), eventResponse);
                return eventResponse;
            } catch (DuplicateKeyException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
            }
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
    }

    @Override
    public void deleteEvent(String id) {
        eventRepository.findByIdAndIsDeletedFalse(id).map(event -> {
            event.setDeleted(true);
            EventResponse eventResponse = new EventResponse();
            BeanUtils.copyProperties(eventRepository.save(event), eventResponse);
            return eventResponse;
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
    }

}
