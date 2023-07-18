package ru.job4j.cinema.repository;

import ru.job4j.cinema.model.Hall;

import java.util.List;
import java.util.Optional;

public interface HallRepository {

    Hall save(Hall hall);

    boolean delete(int id);

    Optional<Hall> findById(int id);

    List<Hall> getAll();
}
