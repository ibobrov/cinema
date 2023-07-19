package ru.job4j.cinema.service;

import ru.job4j.cinema.dto.DtoFilmSession;

import java.time.LocalDate;
import java.util.List;

public interface FilmSessionService {

    List<DtoFilmSession> getSessionsByDay(LocalDate date);
}
