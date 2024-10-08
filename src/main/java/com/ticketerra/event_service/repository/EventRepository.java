package com.ticketerra.event_service.repository;

import com.ticketerra.event_service.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventRepository extends MongoRepository<Event, String> {
    Optional<Event> findByIdAndIsDeletedFalse(String id);

    Page<Event> findAllByIsDeletedFalse(Pageable pageable);
    @Query("{ $text: { $search: ?0 }, isDeleted: false }")
    Page<Event> searchByTextAndIsDeletedFalse(String text, Pageable pageable);
}
