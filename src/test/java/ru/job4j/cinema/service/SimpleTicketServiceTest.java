package ru.job4j.cinema.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.model.FilmSession;
import ru.job4j.cinema.model.Hall;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.repository.HallRepository;
import ru.job4j.cinema.repository.TicketRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * The base behavior is checked and an empty result is returned.
 * It is checked that tickets are created in the halls where there are these places,
 * and that there will not be two tickets for one place.
 */
class SimpleTicketServiceTest {
    private static SimpleTicketService ticketService;
    private static TicketRepository ticketRepo;
    private static HallRepository hallRepo;
    private static final Ticket TICKET = new Ticket(1, 1, 1, 1, 1);
    private static final FilmSession SESSION = new FilmSession(1, 1, 1, LocalDateTime.now(), LocalDateTime.now(), 5);
    private static final Hall HALL = new Hall(1, "", 5, 5, "");

    @BeforeEach
    public void initServices() {
        ticketRepo = mock(TicketRepository.class);
        hallRepo = mock(HallRepository.class);
        ticketService = new SimpleTicketService(ticketRepo, hallRepo);
    }

    @Test
    public void whenSaveTicketThenReturnSameTicket() {
        when(hallRepo.findBySession(1)).thenReturn(Optional.of(HALL));
        when(ticketRepo.save(any())).thenReturn(Optional.of(TICKET));
        assertThat(ticketService.save(TICKET).get()).isEqualTo(TICKET);
    }

    @Test
    public void whenSaveTicketWithIncorrectHallIdThenReturnEmpty() {
        when(hallRepo.findBySession(1)).thenReturn(empty());
        when(ticketRepo.save(any())).thenReturn(Optional.of(TICKET));
        assertThat(ticketService.save(TICKET)).isEqualTo(empty());
    }

    @Test
    public void whenSaveTicketWithIncorrectTicketIdThenReturnEmpty() {
        when(hallRepo.findBySession(1)).thenReturn(Optional.of(HALL));
        when(ticketRepo.save(any())).thenReturn(empty());
        assertThat(ticketService.save(TICKET)).isEqualTo(empty());
    }

    @Test
    public void whenSaveTicketWithIncorrectRowInTheTicketThenReturnEmpty() {
        var ticketIncRow = new Ticket(1, 1, 10, 1, 1);
        when(hallRepo.findBySession(1)).thenReturn(Optional.of(HALL));
        when(ticketRepo.save(any())).thenReturn(Optional.of(TICKET));
        assertThat(ticketService.save(ticketIncRow)).isEqualTo(empty());
    }

    @Test
    public void whenSaveTicketWithIncorrectSeatInTheTicketThenReturnEmpty() {
        var ticketIncSeat = new Ticket(1, 1, 1, 10, 1);
        when(hallRepo.findBySession(1)).thenReturn(Optional.of(HALL));
        when(ticketRepo.save(any())).thenReturn(Optional.of(TICKET));
        assertThat(ticketService.save(ticketIncSeat)).isEqualTo(empty());
    }

    @Test
    public void whenFindBySessionThenReturnTwoTickets() {
        var expected = List.of(TICKET);
        when(ticketRepo.findBySession(1)).thenReturn(expected);
        assertThat(ticketService.findBySession(1)).isEqualTo(expected);
    }
}