package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FilmController {

    @GetMapping("films")
    public String getFilms() {
        return "films/list";
    }
}
