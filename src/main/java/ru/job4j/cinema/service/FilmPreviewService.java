package ru.job4j.cinema.service;

import ru.job4j.cinema.dto.FilmPreview;

import java.util.List;
import java.util.Optional;

public interface FilmPreviewService {

    Optional<FilmPreview> findById(int id);

    List<FilmPreview> getAll();
}
