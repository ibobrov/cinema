package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.cinema.dto.DtoFilmSession;
import ru.job4j.cinema.dto.HallSchema;
import ru.job4j.cinema.model.Hall;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.service.FilmSessionService;
import ru.job4j.cinema.service.TicketService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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
        var todaySessions = sort(sessionService.findByDay(TODAY));
        var tomorrowSessions = sort(sessionService.findByDay(TOMORROW));
        model.addAttribute("todaySessions", todaySessions);
        model.addAttribute("tomorrowSessions", tomorrowSessions);
        return "sessions/list";
    }

    @GetMapping("/{id}")
    public String findById(Model model, @PathVariable int id) {
        var filmSessionDto = sessionService.findById(id);
        if (filmSessionDto.isEmpty()) {
            return "errors/error-404";
        }
        var hall = filmSessionDto.get().getHall();
        model.addAttribute("rows", IntStream.range(1, hall.getRowCount() + 1).toArray());
        model.addAttribute("places", IntStream.range(1, hall.getPlaceCount() + 1).toArray());
        model.addAttribute("filmSession", filmSessionDto.get());
        model.addAttribute("schema", createSchema(hall, filmSessionDto.get().getId()));
        return "sessions/one";
    }

    @PostMapping("/bye")
    public String bayTicket(Model model, @ModelAttribute Ticket ticket) {
        var optionalTicket = ticketService.save(ticket);
        if (optionalTicket.isEmpty()) {
            model.addAttribute("message", "Failed to place an order. "
                    + "Refresh the ticket purchase page, the seat may be full.");
            return "errors/error";
        }
        return "sessions/paid";
    }

    private HallSchema createSchema(Hall hall, int sessionId) {
        var rows = hall.getRowCount();
        var places = hall.getPlaceCount();
        var schema = new HallSchema((rows + "x" + places), rows, places);
        for (var ticket : ticketService.findBySession(sessionId)) {
            schema.setValue(ticket.getRowNumber(), ticket.getPlaceNumber(), true);
        }
        return schema;
    }

    private List<DtoFilmSession> sort(List<DtoFilmSession> list) {
        var copySessions = new ArrayList<>(list);
        copySessions.sort(Comparator.comparing(DtoFilmSession::getStartTime));
        return copySessions;
    }
}
