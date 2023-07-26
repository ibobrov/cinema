package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.cinema.dto.DtoFilmSession;
import ru.job4j.cinema.service.FilmPreviewService;
import ru.job4j.cinema.service.FilmSessionService;

import java.util.ArrayList;
import java.util.Comparator;

@Controller
@RequestMapping("/films")
public class FilmController {
    private final FilmPreviewService filmPreviewService;
    private final FilmSessionService sessionService;

    public FilmController(FilmPreviewService filmPreviewService, FilmSessionService sessionService) {
        this.filmPreviewService = filmPreviewService;
        this.sessionService = sessionService;
    }

    @GetMapping("/list")
    public String getAll(Model model) {
        model.addAttribute("cards", filmPreviewService.getAll());
        return "films/list";
    }

    @GetMapping("/{id}")
    public String findById(Model model, @PathVariable int id) {
        var preview = filmPreviewService.findById(id);
        if (preview.isEmpty()) {
            return "errors/error-404";
        }
        model.addAttribute("film", preview.get());
        var sessions = sessionService.findByFilm(id);
        var sessionCopies = new ArrayList<>(sessions);
        sessionCopies.sort(Comparator.comparing(DtoFilmSession::getStartTime));
        model.addAttribute("filmSessions", sessions);
        return "films/one";
    }
}
