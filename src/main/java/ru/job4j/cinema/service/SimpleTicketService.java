package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.repository.HallRepository;
import ru.job4j.cinema.repository.SessionRepository;
import ru.job4j.cinema.repository.TicketRepository;

import java.util.Optional;

@Service
public class SimpleTicketService implements TicketService {
    private final TicketRepository ticketRepo;
    private final HallRepository hallRepo;
    private final SessionRepository sessionRepo;

    public SimpleTicketService(TicketRepository ticketRepo, HallRepository hallRepo, SessionRepository sessionRepo) {
        this.ticketRepo = ticketRepo;
        this.hallRepo = hallRepo;
        this.sessionRepo = sessionRepo;
    }

    @Override
    public Optional<Ticket> save(Ticket ticket) {
        var session = sessionRepo.findById(ticket.getSessionId());
        if (session.isPresent()) {
            var hall = hallRepo.findById(session.get().getHallId());
            if (hall.isPresent()) {
                var rowIsValid = ticket.getRowNumber() > 0
                        && ticket.getRowNumber() <= hall.get().getRowCount();
                var placeIsValid = ticket.getPlaceNumber() > 0
                        && ticket.getPlaceNumber() <= hall.get().getPlaceCount();
                if (rowIsValid && placeIsValid) {
                    return ticketRepo.save(ticket);
                }
            }
        }
        return Optional.empty();
    }
}
