package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/session")
public class FilmSessionController {

    @GetMapping("/list")
    public String getAll() {
        return "sessions/list";
    }

    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable int id) {
        //TODO
        return "sessions/one";
    }
}
