package com.example.demo.service;

import com.example.demo.model.Ticket;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface TicketService {
    Ticket purchaseTicket(Ticket ticket);

    Optional<Ticket> getTicketById(Long id);

    List<Ticket> getUsersBySection(String section);

    Boolean deleteTicket(Long id);

    Optional<Ticket> modifySeat(Long ticketId, String newSection, String newSeatNumber);
}
