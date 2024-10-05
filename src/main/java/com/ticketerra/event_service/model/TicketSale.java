package com.ticketerra.event_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketSale {
    private String phase;
    private String startDate; // ISO 8601 format
    private String endDate;   // ISO 8601 format
    private List<TicketCategory> ticketCategories;
}
