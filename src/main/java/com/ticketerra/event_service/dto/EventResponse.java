package com.ticketerra.event_service.dto;

import com.ticketerra.event_service.model.Artist;
import com.ticketerra.event_service.model.Location;
import com.ticketerra.event_service.model.TicketSale;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class EventResponse {
    private String id;
    private String date;
    private String title;
    private String description;
    private String posterPicture;
    private List<String> media;
    private Location location;
    private List<Artist> artistList;
    private List<TicketSale> ticketSaleList;
    private boolean isDeleted;
}
