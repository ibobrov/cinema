package ru.job4j.cinema.controller;

import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.TicketService;

import javax.servlet.http.HttpSession;
import java.util.Optional;

import static java.util.Optional.empty;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TicketControllerTest {
    private final TicketService ticketService = mock(TicketService.class);
    private final TicketController controller = new TicketController(ticketService);
    private final Model model = new ConcurrentModel();
    private final HttpSession session = mock(HttpSession.class);

    @Test
    public void whenBayTicketThenReturnPaidPage() {
        var ticket = new Ticket();
        ticket.setUserId(1);
        var user = new User();
        user.setId(1);
        when(session.getAttribute(any())).thenReturn(user);
        when(ticketService.save(any())).thenReturn(Optional.of(ticket));
        assertThat(controller.buyTicket(session, model, ticket)).isEqualTo("tickets/paid");
    }

    @Test
    public void whenTicketUserIdNotEqualUserIdThenReturnErrorPage() {
        var ticket = new Ticket();
        ticket.setUserId(1);
        var user = new User();
        user.setId(2);
        when(session.getAttribute(any())).thenReturn(user);
        when(ticketService.save(any())).thenReturn(Optional.of(ticket));
        var msg = "Failed to place an order. Refresh the ticket purchase page, the seat may be full.";
        assertThat(controller.buyTicket(session, model, ticket)).isEqualTo("errors/error");
        assertThat(model.getAttribute("message")).isEqualTo(msg);
    }

    @Test
    public void whenBayTicketThenReturnErrorPage() {
        var ticket = new Ticket();
        ticket.setUserId(1);
        var user = new User();
        user.setId(1);
        when(session.getAttribute(any())).thenReturn(user);
        when(ticketService.save(any())).thenReturn(empty());
        var msg = "Failed to place an order. Refresh the ticket purchase page, the seat may be full.";
        assertThat(controller.buyTicket(session, model, ticket)).isEqualTo("errors/error");
        assertThat(model.getAttribute("message")).isEqualTo(msg);
    }
}
