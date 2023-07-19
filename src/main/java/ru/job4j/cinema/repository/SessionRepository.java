package ru.job4j.cinema.repository;

import ru.job4j.cinema.model.FilmSession;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SessionRepository {

    Optional<FilmSession> findById(int id);

    List<FilmSession> findByDay(LocalDate date);

    List<FilmSession> findByFilm(int id);

    List<FilmSession> getAll();
}
