package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Hall;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.repository.HallRepository;
import ru.job4j.cinema.repository.TicketRepository;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;

@Service
public class SimpleTicketService implements TicketService {
    private final TicketRepository ticketRepo;
    private final HallRepository hallRepo;

    public SimpleTicketService(TicketRepository ticketRepo, HallRepository hallRepo) {
        this.ticketRepo = ticketRepo;
        this.hallRepo = hallRepo;
    }

    /**
     * For security purposes, the data from the request is validated against the data from the repository.
     * Do not think that the data from the http request will be correct.
     */
    @Override
    public Optional<Ticket> save(Ticket ticket) {
        Optional<Ticket> rsl = empty();
        var hall = hallRepo.findBySession(ticket.getSessionId());
        if (hall.isPresent() && validatePlace(ticket, hall.get())) {
            rsl = ticketRepo.save(ticket);
        }
        return rsl;
    }

    @Override
    public List<Ticket> findBySession(int sessionId) {
        return ticketRepo.findBySession(sessionId);
    }

    private boolean validatePlace(Ticket ticket, Hall hall) {
        var rowIsValid = ticket.getRowNumber() > 0
                && ticket.getRowNumber() <= hall.getRowCount();
        var placeIsValid = ticket.getPlaceNumber() > 0
                && ticket.getPlaceNumber() <= hall.getPlaceCount();
        return rowIsValid && placeIsValid;
    }
}
