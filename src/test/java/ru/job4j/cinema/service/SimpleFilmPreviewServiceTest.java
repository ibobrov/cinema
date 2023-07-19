package ru.job4j.cinema.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.model.Film;
import ru.job4j.cinema.model.Genre;
import ru.job4j.cinema.repository.FilmRepository;
import ru.job4j.cinema.repository.GenreRepository;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SimpleFilmPreviewServiceTest {
    private static FilmRepository filmRepo;
    private static GenreRepository genreRepo;
    private static SimpleFilmPreviewService previewService;

    @BeforeEach
    public void mockedRepo() {
        filmRepo = mock(FilmRepository.class);
        genreRepo = mock(GenreRepository.class);
        previewService = new SimpleFilmPreviewService(filmRepo, genreRepo);
    }

    @Test
    public void whenFindByOneThenGetOne() {
        var film = new Film();
        film.setId(3);
        film.setGenreId(2);
        when(filmRepo.findById(3)).thenReturn(Optional.of(film));
        when(genreRepo.findById(2)).thenReturn(Optional.of(new Genre(3, "test")));
        var preview = previewService.findById(3).get();
        assertThat(preview.getFilmId()).isEqualTo(3);
        assertThat(preview.getGenre()).isEqualTo("test");
    }

    @Test
    public void whenFindByNonExistIndexThenGetEmpty() {
        when(filmRepo.findById(3)).thenReturn(empty());
        var preview = previewService.findById(3);
        assertThat(preview).isEqualTo(empty());
    }

    @Test
    public void whenFindByByOneButNonExistGenreThenGetCorrected() {
        var film = new Film();
        film.setId(3);
        film.setGenreId(2);
        when(filmRepo.findById(3)).thenReturn(Optional.of(film));
        when(genreRepo.findById(2)).thenReturn(empty());
        var preview = previewService.findById(3).get();
        assertThat(preview.getFilmId()).isEqualTo(3);
        assertThat(preview.getGenre()).isEqualTo("");
    }

    @Test
    public void whenGetAllThenNotExistAnyAndGetEmptyList() {
        when(filmRepo.findById(1)).thenReturn(empty());
        var all = filmRepo.getAll();
        assertThat(all.size()).isEqualTo(0);
    }

    @Test
    public void whenGetAllThenGetCorrectedList() {
        var film1 = new Film();
        film1.setId(1);
        film1.setGenreId(1);
        var film2 = new Film();
        film2.setId(2);
        film2.setGenreId(2);
        when(filmRepo.getAll()).thenReturn(List.of(film1, film2));
        when(genreRepo.getAll()).thenReturn(List.of(new Genre(1, "first"), new Genre(2, "second")));
        var all = previewService.getAll();
        assertThat(all.size()).isEqualTo(2);
        assertThat(all.get(0).getFilmId()).isEqualTo(1);
        assertThat(all.get(0).getGenre()).isEqualTo("first");
        assertThat(all.get(1).getFilmId()).isEqualTo(2);
        assertThat(all.get(1).getGenre()).isEqualTo("second");
    }
}