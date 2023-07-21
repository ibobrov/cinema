package ru.job4j.cinema.service;

import ru.job4j.cinema.dto.DtoFilmSession;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface FilmSessionService {

    Optional<DtoFilmSession> findById(int id);

    List<DtoFilmSession> getByDay(LocalDate date);

    List<DtoFilmSession> getByFilm(int id);
}
