package ru.job4j.cinema.repository;

import ru.job4j.cinema.model.FilmSession;

import java.util.List;
import java.util.Optional;

public interface FilmSessionRepository {

    FilmSession save(FilmSession filmSession);

    boolean delete(int id);

    Optional<FilmSession> findById(int id);

    List<FilmSession> getAll();
}
