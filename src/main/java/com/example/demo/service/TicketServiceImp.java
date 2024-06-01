package com.example.demo.service;

import com.example.demo.model.Seat;
import com.example.demo.model.Ticket;
import com.example.demo.model.Users;
import com.example.demo.repository.SeatRepository;
import com.example.demo.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TicketServiceImp implements TicketService{
    private final TicketRepository ticketRepository;

    private final SeatRepository seatRepository;

    private final UserService userService;

    public Ticket purchaseTicket(Ticket ticket){
        try {

            Optional<List<Seat>> availableSeats = Optional.ofNullable(seatRepository.findBySectionAndIsOccupied(ticket.getSeat().getSection(), false));

//            if (availableSeats.isEmpty()) {
//                String otherSection = ticket.getSeat().getSection().equals("A") ? "B" : "A";
//                availableSeats = Optional.ofNullable(seatRepository.findBySectionAndIsOccupied(otherSection, false));
//            }
            if (availableSeats.isPresent()){
                Seat seat = availableSeats.get().getFirst();
                seat.setIsOccupied(true);
                ticket.setSeat(seat);
                seatRepository.save(seat);
                Users users = userService.createUser(ticket.getUsers());
                ticket.setUsers(users);
                ticketRepository.save(ticket);
                log.info("Successfully created ticket with id = {}", ticket.getId());
                return ticket;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
        return null;
    }

    @Override
    public Optional<Ticket> getTicketById(Long id) {
        try {
            Optional<Ticket> ticket = ticketRepository.findById(id);

            if (ticket.isPresent()){
                log.info("Ticket with id = {} retrieved", id);
                return ticketRepository.findById(id);
            } else {
                log.error("No ticket with id = {} found", id);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Ticket> getUsersBySection(String section) {
        return ticketRepository.findAll()
                .stream()
                .filter(ticket -> ticket.getSeat().getSection().equals(section))
                .toList();
    }

    @Override
    public Boolean deleteTicket(Long id) {
        try {
            Optional<Ticket> ticket = ticketRepository.findById(id);

            if (ticket.isPresent()){
                Seat seat = ticket.get().getSeat();
                seat.setIsOccupied(false);
                seatRepository.save(seat);
                userService.deleteUser(id);
                ticketRepository.deleteById(id);
                log.info("Successfully deleted ticket with id = {}", id);
                return true;
            } else{
                log.error("Unable to deleted ticket with id = {}", id);
                return false;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Ticket> modifySeat(Long ticketId, String newSection, String newSeatNumber) {
        try {
            Optional<Ticket> optionalTicket = ticketRepository.findById(ticketId);
            if (optionalTicket.isPresent()){
                Ticket ticket1 = optionalTicket.get();
                Seat seat = ticket1.getSeat();
                seat.setIsOccupied(false);
                seatRepository.save(seat);

                List<Seat> availableSeat = seatRepository
                        .findBySectionAndIsOccupied(newSection,
                                false);
                Optional<Seat> newSeat = availableSeat.stream()
                        .filter(theSeat -> theSeat.getSeatNumber().equals(newSeatNumber))
                        .findFirst();
                if (newSeat.isPresent()){
                    newSeat.get().setIsOccupied(true);
                    ticket1.setSeat(newSeat.get());
                    seatRepository.save(newSeat.get());
                    ticketRepository.save(ticket1);
                } else {
                    throw new RuntimeException("Requested seat not available.");
                }
            } else {
                throw new RuntimeException("Ticket not found.");
            }
            return ticketRepository.findById(ticketId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
