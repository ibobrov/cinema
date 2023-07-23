package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.cinema.dto.DtoFilmSession;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.service.FilmSessionService;
import ru.job4j.cinema.service.TicketService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/sessions")
public class FilmSessionController {
    private final FilmSessionService sessionService;
    private final TicketService ticketService;
    private static final LocalDate TODAY = LocalDate.of(2023, 7, 18);
    private static final LocalDate TOMORROW = LocalDate.of(2023, 7, 19);

    public FilmSessionController(FilmSessionService sessionService, TicketService ticketService) {
        this.sessionService = sessionService;
        this.ticketService = ticketService;
    }

    @GetMapping("/list")
    public String getAll(Model model) {
        model.addAttribute("timeFormatter", DateTimeFormatter.ofPattern("HH:m"));
        var today = sessionService.getByDay(TODAY);
        today.sort(Comparator.comparing(DtoFilmSession::getStartTime));
        model.addAttribute("todaySessions", today);
        var tomorrow = sessionService.getByDay(TOMORROW);
        tomorrow.sort(Comparator.comparing(DtoFilmSession::getStartTime));
        model.addAttribute("tomorrowSessions", tomorrow);
        return "sessions/list";
    }

    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable int id) {
        var dto = sessionService.findById(id).get();
        model.addAttribute("rows", IntStream.range(1, dto.getHall().getRowCount() + 1).toArray());
        model.addAttribute("places", IntStream.range(1, dto.getHall().getPlaceCount() + 1).toArray());
        model.addAttribute("filmSession", dto);
        return "sessions/one";
    }

    @PostMapping("/bye")
    public String bayTicket(@ModelAttribute Ticket ticket, Model model) {
        var optionalTicket = ticketService.save(ticket);
        if (optionalTicket.isEmpty()) {
            //TODO
        }
        return "sessions/paid";
    }
}
