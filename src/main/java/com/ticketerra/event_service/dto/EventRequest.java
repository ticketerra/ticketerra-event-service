package com.ticketerra.event_service.dto;

import com.ticketerra.event_service.model.Artist;
import com.ticketerra.event_service.model.Location;
import com.ticketerra.event_service.model.TicketSale;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventRequest {
    private String date;
    private String title;
    private String description;
    private String posterPicture;
    private List<String> media;
    private Location location;
    private List<Artist> artistList;
    private List<TicketSale> ticketSaleList;
}
