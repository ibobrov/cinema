package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.cinema.dto.DtoFilmSession;
import ru.job4j.cinema.service.FilmPreviewService;
import ru.job4j.cinema.service.FilmSessionService;

import java.util.Comparator;

@Controller
@RequestMapping("/films")
public class FilmController {
    private final FilmPreviewService filmPreviewService;
    private final FilmSessionService sessionRepo;

    public FilmController(FilmPreviewService filmPreviewService, FilmSessionService sessionRepo) {
        this.filmPreviewService = filmPreviewService;
        this.sessionRepo = sessionRepo;
    }

    @GetMapping("/list")
    public String getAll(Model model) {
        model.addAttribute("cards", filmPreviewService.getAll());
        return "films/list";
    }

    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable int id) {
        model.addAttribute("film", filmPreviewService.findById(id).get());
        var sessions = sessionRepo.getByFilm(id);
        sessions.sort(Comparator.comparing(DtoFilmSession::getStartTime));
        model.addAttribute("filmSessions", sessions);
        return "films/one";
    }
}
