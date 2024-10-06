package com.ticketerra.event_service;

import com.ticketerra.event_service.dto.EventRequest;
import com.ticketerra.event_service.dto.EventResponse;
import com.ticketerra.event_service.dto.PaginatedResponse;
import com.ticketerra.event_service.model.Event;
import com.ticketerra.event_service.model.Location;
import com.ticketerra.event_service.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class EventServiceApplicationTests {
	@Container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:8.0.0");

	@Autowired
	private RestTemplateBuilder restTemplateBuilder;

	@Autowired
	private EventRepository eventRepository;

	private RestTemplate restTemplate;

	@LocalServerPort
	private int port;

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
	}

	@BeforeEach
	void setUp() {
		this.restTemplate = restTemplateBuilder
				.setConnectTimeout(Duration.ofSeconds(5))
				.setReadTimeout(Duration.ofSeconds(5))
				.build();

		eventRepository.deleteAll();
	}

	@Test
	void shouldCreateEvent() {
		// Create a new event request
		EventRequest eventRequest = EventRequest.builder()
				.title("Olivia Rodrigo Live Concert")
				.date("2024-11-16T20:00:00Z")
				.description("An amazing live concert")
				.location(new Location("Madison Square Garden", "New York", "USA"))
				.build();

		String url = "http://localhost:" + port + "/api/events";

		// Send a POST request to create the event
		ResponseEntity<EventResponse> response = restTemplate.postForEntity(url, eventRequest, EventResponse.class);

		// Check if the response status is CREATED (201)
		assertEquals(HttpStatus.CREATED, response.getStatusCode());

		// Assert that the response body is not null
		assertNotNull(response.getBody());
		assertEquals("Olivia Rodrigo Live Concert", response.getBody().getTitle());
	}

	@Test
	void shouldGetEvents() {
		// Setup: Create and save a test event
		Event eventEntity = new Event();
		eventEntity.setTitle("Test Event");
		eventEntity.setDate("2024-11-16T20:00:00Z");
		eventEntity.setDescription("A test event for integration testing");
		eventEntity.setLocation(new Location("Test Venue", "Test City", "Test Country"));
		eventRepository.save(eventEntity);

		String url = "http://localhost:" + port + "/api/events";

		// Use ParameterizedTypeReference to handle generic types like PaginatedResponse
		ParameterizedTypeReference<PaginatedResponse<EventResponse>> responseType =
				new ParameterizedTypeReference<>() {};

		// Send a GET request to retrieve the events
		ResponseEntity<PaginatedResponse<EventResponse>> response =
				restTemplate.exchange(url, HttpMethod.GET, null, responseType);

		// Check if the response status is OK (200)
		assertEquals(HttpStatus.OK, response.getStatusCode());

		// Get the PaginatedResponse body
		PaginatedResponse<EventResponse> paginatedResponse = response.getBody();

		// Assert that the response body is not null
		assertNotNull(paginatedResponse);

		// Assert that the data in PaginatedResponse contains at least one event
		List<EventResponse> events = paginatedResponse.getData();
		assertNotNull(events);
		assertFalse(events.isEmpty());  // Ensure there is at least one event
	}

	@Test
	void shouldGetEvent() {
		// Setup: Create and save a test event
		Event eventEntity = new Event();
		eventEntity.setTitle("Test Event");
		eventEntity.setDate("2024-11-16T20:00:00Z");
		eventEntity.setDescription("A test event for integration testing");
		eventEntity.setLocation(new Location("Test Venue", "Test City", "Test Country"));
		Event savedEvent = eventRepository.save(eventEntity);

		String url = "http://localhost:" + port + "/api/events/" + savedEvent.getId();

		// Send a GET request to retrieve the event
		ResponseEntity<EventResponse> response = restTemplate.getForEntity(url, EventResponse.class);

		// Check if the response status is OK (200)
		assertEquals(HttpStatus.OK, response.getStatusCode());

		// Assert that the response body is not null and matches the saved event
		EventResponse eventResponse = response.getBody();
		assertNotNull(eventResponse);
		assertEquals(savedEvent.getTitle(), eventResponse.getTitle());
		assertEquals(savedEvent.getDescription(), eventResponse.getDescription());
	}

	@Test
	void shouldUpdateEvent() {
		// Setup: Create and save a test event
		Event eventEntity = new Event();
		eventEntity.setTitle("Test Event");
		eventEntity.setDate("2024-11-16T20:00:00Z");
		eventEntity.setDescription("A test event for integration testing");
		eventEntity.setLocation(new Location("Test Venue", "Test City", "Test Country"));
		Event savedEvent = eventRepository.save(eventEntity);

		// Prepare an update request
		EventRequest updateRequest = EventRequest.builder()
				.title("Updated Event Title")
				.date("2024-12-01T20:00:00Z")
				.description("Updated event description")
				.location(new Location("Updated Venue", "Updated City", "Updated Country"))
				.build();

		String url = "http://localhost:" + port + "/api/events/" + savedEvent.getId();

		// Send a PUT request to update the event
		ResponseEntity<EventResponse> response = restTemplate.exchange(url, HttpMethod.PUT, new ResponseEntity<>(updateRequest, HttpStatus.OK), EventResponse.class);

		// Check if the response status is OK (200)
		assertEquals(HttpStatus.OK, response.getStatusCode());

		// Assert that the response body is not null
		EventResponse updatedEventResponse = response.getBody();
		assertNotNull(updatedEventResponse);
		assertEquals("Updated Event Title", updatedEventResponse.getTitle());
	}

	@Test
	void shouldDeleteEvent() {
		// Setup: Create and save a test event
		Event eventEntity = new Event();
		eventEntity.setTitle("Test Event");
		eventEntity.setDate("2024-11-16T20:00:00Z");
		eventEntity.setDescription("A test event for integration testing");
		eventEntity.setLocation(new Location("Test Venue", "Test City", "Test Country"));
		Event savedEvent = eventRepository.save(eventEntity);

		String url = "http://localhost:" + port + "/api/events/" + savedEvent.getId();

		// Send a DELETE request to delete the event
		ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);

		// Check if the response status is NO_CONTENT (204)
		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

		// Verify the event has been deleted
		Optional<Event> deletedEvent = eventRepository.findByIdAndIsDeletedFalse(savedEvent.getId());
		assertFalse(deletedEvent.isPresent());
	}
}