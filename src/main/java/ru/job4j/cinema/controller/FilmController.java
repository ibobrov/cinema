package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.cinema.service.FilmPreviewService;

@Controller
@RequestMapping("/films")
public class FilmController {
    private final FilmPreviewService filmPreviewService;

    public FilmController(FilmPreviewService filmPreviewService) {
        this.filmPreviewService = filmPreviewService;
    }

    @GetMapping("/list")
    public String getAll(Model model) {
        model.addAttribute("cards", filmPreviewService.findAll());
        return "films/list";
    }
}
