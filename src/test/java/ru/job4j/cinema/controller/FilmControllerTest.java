package ru.job4j.cinema.controller;

import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import ru.job4j.cinema.dto.FilmPreview;
import ru.job4j.cinema.service.FilmPreviewService;
import ru.job4j.cinema.service.FilmSessionService;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * It is checked that pages with attributes in the model are returned on
 * request through all, id. If not found return error page with 404 status.
 */
class FilmControllerTest {
    private final FilmPreviewService filmPreviewService = mock(FilmPreviewService.class);
    private final FilmSessionService sessionService = mock(FilmSessionService.class);
    private final FilmController filmController = new FilmController(filmPreviewService, sessionService);
    private final Model model = new ConcurrentModel();
    private final FilmPreview preview = new FilmPreview();

    @Test
    public void whenGetAllThenReturnPageFilmList() {
        when(filmPreviewService.getAll()).thenReturn(List.of());
        var view = filmController.getAll(model);
        assertThat(model.getAttribute("cards")).isEqualTo(List.of());
        assertThat(view).isEqualTo("films/list");
    }

    @Test
    public void whenGetByIdThenReturnPageWithFilm() {
        when(filmPreviewService.findById(1)).thenReturn(Optional.of(preview));
        when(sessionService.findByFilm(1)).thenReturn(List.of());
        var view = filmController.findById(model, 1);
        assertThat(model.getAttribute("film")).isEqualTo(preview);
        assertThat(model.getAttribute("filmSessions")).isEqualTo(List.of());
        assertThat(view).isEqualTo("films/one");
    }

    @Test
    public void whenGetByIdThenReturnPageWith404() {
        when(filmPreviewService.findById(1)).thenReturn(empty());
        var view = filmController.findById(model, 1);
        assertThat(view).isEqualTo("errors/error-404");
    }
}