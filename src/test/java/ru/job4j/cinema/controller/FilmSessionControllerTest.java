package ru.job4j.cinema.controller;

import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import ru.job4j.cinema.dto.DtoFilmSession;
import ru.job4j.cinema.dto.HallSchema;
import ru.job4j.cinema.model.Hall;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.service.FilmSessionService;
import ru.job4j.cinema.service.TicketService;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * It is checked that sessions are returned, with a successful purchase,
 * the corresponding page is returned, with a negative result,
 * an error page is returned.
 */
class FilmSessionControllerTest {
    private final FilmSessionService sessionService = mock(FilmSessionService.class);
    private final TicketService ticketService = mock(TicketService.class);
    private final FilmSessionController controller = new FilmSessionController(sessionService, ticketService);
    private final Model model = new ConcurrentModel();
    private final DtoFilmSession dto = new DtoFilmSession(1,
            null, new Hall(1, "", 3, 2, ""),
            null, null, 5);

    @Test
    public void whenGetAllThenReturnListPageAndAddToModelTwoAttributes() {
        when(sessionService.findByDay(any())).thenReturn(List.of());
        assertThat(controller.getAll(model)).isEqualTo("sessions/list");
        assertThat(model.getAttribute("todaySessions")).isEqualTo(List.of());
        assertThat(model.getAttribute("tomorrowSessions")).isEqualTo(List.of());
    }

    @Test
    public void whenFindByIdThenReturnPageAndAddAttributes() {
        when(sessionService.findById(1)).thenReturn(Optional.of(dto));
        when(ticketService.findBySession(1)).thenReturn(List.of());
        assertThat(controller.findById(model, 1)).isEqualTo("sessions/one");
        assertThat(model.getAttribute("rows")).isEqualTo(new int[] {1, 2, 3});
        assertThat(model.getAttribute("places")).isEqualTo(new int[] {1, 2});
        assertThat(model.getAttribute("filmSession")).isEqualTo(dto);
        assertThat(model.getAttribute("schema")).isEqualTo(new HallSchema(("3x2"), 3, 2));
    }

    @Test
    public void whenFindByIdThenReturn404ErrorPageWith() {
        when(sessionService.findById(1)).thenReturn(empty());
        assertThat(controller.findById(model, 1)).isEqualTo("errors/error-404");
    }

    @Test
    public void whenBayTicketThenReturnPaidPage() {
        var ticket = new Ticket();
        when(ticketService.save(any())).thenReturn(Optional.of(ticket));
        assertThat(controller.bayTicket(model, ticket)).isEqualTo("sessions/paid");
    }

    @Test
    public void whenBayTicketThenReturnErrorPage() {
        when(ticketService.save(any())).thenReturn(empty());
        var msg = "Failed to place an order. Refresh the ticket purchase page, the seat may be full.";
        assertThat(controller.bayTicket(model, new Ticket())).isEqualTo("errors/error");
        assertThat(model.getAttribute("message")).isEqualTo(msg);
    }
}