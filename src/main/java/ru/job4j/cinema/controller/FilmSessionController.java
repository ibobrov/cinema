package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.cinema.dto.DtoFilmSession;
import ru.job4j.cinema.service.FilmSessionService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

@Controller
@RequestMapping("/session")
public class FilmSessionController {
    private final FilmSessionService sessionService;
    private static final LocalDate TODAY = LocalDate.of(2023, 7, 18);
    private static final LocalDate TOMORROW = LocalDate.of(2023, 7, 19);

    public FilmSessionController(FilmSessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping("/list")
    public String getAll(Model model) {
        model.addAttribute("timeFormatter", DateTimeFormatter.ofPattern("HH:m"));
        var today = sessionService.getSessionsByDay(TODAY);
        today.sort(Comparator.comparing(DtoFilmSession::getStartTime));
        model.addAttribute("todaySessions", today);
        var tomorrow = sessionService.getSessionsByDay(TOMORROW);
        tomorrow.sort(Comparator.comparing(DtoFilmSession::getStartTime));
        model.addAttribute("tomorrowSessions", tomorrow);
        return "sessions/list";
    }

    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable int id) {
        //TODO
        return "sessions/one";
    }
}
