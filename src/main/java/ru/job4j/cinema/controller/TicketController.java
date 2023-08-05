package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.TicketService;

import javax.servlet.http.HttpSession;

import static java.util.Optional.empty;

@Controller
@RequestMapping("/tickets")
public class TicketController {
    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    /**
     * To be safe, we check that the user id matches the user id in the ticket.
     */
    @PostMapping("/buy")
    public String buyTicket(HttpSession session, Model model, @ModelAttribute Ticket ticket) {
        var user = (User) session.getAttribute("user");
        var optionalTicket = user.getId() == ticket.getUserId() ? ticketService.save(ticket)
                                                                : empty();
        if (optionalTicket.isEmpty()) {
            model.addAttribute("message", "Failed to place an order. "
                    + "Refresh the ticket purchase page, the seat may be full.");
            return "errors/error";
        }
        return "tickets/paid";
    }
}
