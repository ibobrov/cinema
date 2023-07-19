package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.FilmPreview;
import ru.job4j.cinema.model.Film;
import ru.job4j.cinema.model.Genre;
import ru.job4j.cinema.repository.FilmRepository;
import ru.job4j.cinema.repository.GenreRepository;

import java.util.*;

@Service
public class SimpleFilmPreviewService implements FilmPreviewService {
    private final FilmRepository filmRepository;
    private final GenreRepository genreRepository;

    public SimpleFilmPreviewService(FilmRepository filmRepository, GenreRepository genreRepository) {
        this.filmRepository = filmRepository;
        this.genreRepository = genreRepository;
    }

    @Override
    public Optional<FilmPreview> findById(int id) {
        Optional<Film> film = filmRepository.findById(id);
        return film.map(value -> toPreview(value, getGenre(value.getGenreId())));
    }

    @Override
    public List<FilmPreview> getAll() {
        List<FilmPreview> rsl = new ArrayList<>();
        Map<Integer, String> genres = getAllGenre();
        for (var film : filmRepository.getAll()) {
            rsl.add(toPreview(film, genres.get(film.getGenreId())));
        }
        return rsl;
    }

    private static FilmPreview toPreview(Film film, String genre) {
        return new FilmPreview(film.getId(), film.getName(), film.getMinimalAge(),
                        film.getDurationInMinutes(), genre, film.getFileId());
    }

    private String getGenre(int id) {
        Optional<Genre> genre = genreRepository.findById(id);
        return genre.isPresent() ? genre.get().getName() : "";
    }

    private Map<Integer, String> getAllGenre() {
        HashMap<Integer, String> rsl = new HashMap<>();
        for (Genre genre : genreRepository.getAll()) {
            rsl.put(genre.getId(), genre.getName());
        }
        return rsl;
    }
}
