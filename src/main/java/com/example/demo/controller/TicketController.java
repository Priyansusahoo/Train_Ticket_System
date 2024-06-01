package com.example.demo.controller;

import com.example.demo.model.Ticket;
import com.example.demo.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ticket")
@RequiredArgsConstructor
public class TicketController {
    private final TicketService ticketService;

    @PostMapping("/purchaseTicket")
    public ResponseEntity<?> purchaseTicket(@RequestBody Ticket ticket){
        Optional<Ticket> optionalTicket = Optional.ofNullable(ticketService.purchaseTicket(ticket));
        if (optionalTicket.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Optional.of("No available seats in section " + ticket.getSeat().getSection()));
        } else {
            return ResponseEntity.ok().body(optionalTicket);
        }
    }

    @GetMapping("/getTicketById/{id}")
    public ResponseEntity<?> getTicketById(@PathVariable Long id){
        Optional<Ticket> optionalTicket = ticketService.getTicketById(id);
        if (optionalTicket.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Optional.of("No ticket found with the specified id: " + id));
        } else {
            return ResponseEntity.ok().body(optionalTicket);
        }
    }

    @GetMapping("/getUsersBySection/{section}")
    public ResponseEntity<Optional<List<?>>> getUsersBySection(@PathVariable String section){
        List<Ticket> ticketList = ticketService.getUsersBySection(section);
        if (ticketList.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Optional.of(Collections.singletonList("No Users found with the specified section: " + section)));
        }
        return ResponseEntity.ok().body(Optional.of(ticketList));
    }

    @DeleteMapping("/deleteTicket/{id}")
    public ResponseEntity<?> deleteTicket(@PathVariable Long id){
        Boolean bool = ticketService.deleteTicket(id);
        if (bool){
            return ResponseEntity.ok().body(Boolean.TRUE);
        } else {
            return ResponseEntity.ok().body(Boolean.FALSE);
        }
    }

    @PutMapping("/modifySeat/{ticketId}/{newSection}/{newSeatNumber}")
    public Optional<Ticket> modifySeat(@PathVariable Long ticketId,
                                        @PathVariable String newSection,
                                        @PathVariable String newSeatNumber){
        return ticketService.modifySeat(ticketId, newSection, newSeatNumber);
    }
}
