package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.cinema.dto.FilmCard;
import ru.job4j.cinema.service.FilmService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping()
    public String getAll(Model model) {
        List<FilmCard> cards = new ArrayList<>();
        for (var film : filmService.findAll()) {
            cards.add(new FilmCard(film.getId(), film.getName(), film.getMinimalAge(),
                    film.getDurationInMinutes(), film.getGenre(), film.getFileId()));
        }
        model.addAttribute("cards", cards);
        return "films/list";
    }
}
