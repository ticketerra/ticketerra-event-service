package com.ticketerra.event_service.controller;

import com.ticketerra.event_service.dto.EventRequest;
import com.ticketerra.event_service.dto.EventResponse;
import com.ticketerra.event_service.dto.PaginatedResponse;
import com.ticketerra.event_service.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;


@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @PostMapping
    public ResponseEntity<EventResponse> createEvent(@RequestBody EventRequest eventRequest) {
        // Call your service to create the event
        EventResponse createdEvent = eventService.createEvent(eventRequest);

        // Create the URI of the newly created event
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdEvent.getId()) // Assuming EventResponse has a getId() method
                .toUri();

        // Return 201 Created with the location and the created event in the body
        return ResponseEntity.created(location).body(createdEvent);
    }

    @GetMapping
    public ResponseEntity<PaginatedResponse<EventResponse>> getEvents(
            @RequestParam(defaultValue = "-1") Integer offset,
            @RequestParam(defaultValue = "-1") Integer limit
    ){
        return ResponseEntity.ok(eventService.getEvents(offset, limit));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> getEvent(@PathVariable String id){
        return ResponseEntity.ok(eventService.getEvent(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventResponse> updateEvent(@PathVariable String id,
                                                     @RequestBody EventRequest eventRequest){
        return ResponseEntity.ok(eventService.updateEvent(id, eventRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable String id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
}
