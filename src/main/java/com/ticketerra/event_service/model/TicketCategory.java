package com.ticketerra.event_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketCategory {
    private String category;
    private double price;
    private int available;
    private int sold;
}
