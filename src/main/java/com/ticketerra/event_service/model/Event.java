package com.ticketerra.event_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "event")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Event {
    @Id
    private String id;
    private String date;
    @Indexed(unique = true)
    @TextIndexed
    private String title;
    @TextIndexed
    private String description;
    private String posterPicture;
    private List<String> media;
    private Location location;
    private List<Artist> artistList;
    private List<TicketSale> ticketSaleList;
    private boolean isDeleted;
}
