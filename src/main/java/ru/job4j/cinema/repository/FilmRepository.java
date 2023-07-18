package ru.job4j.cinema.repository;

import ru.job4j.cinema.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmRepository {

    Film save(Film film);

    boolean delete(int id);

    Optional<Film> findById(int id);

    List<Film> getAll();
}
