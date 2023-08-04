package ru.job4j.cinema.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.model.Film;
import ru.job4j.cinema.model.FilmSession;
import ru.job4j.cinema.model.Hall;
import ru.job4j.cinema.repository.FilmRepository;
import ru.job4j.cinema.repository.HallRepository;
import ru.job4j.cinema.repository.SessionRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * The base behavior is checked and an empty result is returned.
 * It is checked that one dto is collected from several repositories for certain id.
 */
class SimpleFilmSessionServiceTest {
    private static SimpleFilmSessionService sessionService;
    private static SessionRepository sessionRepo;
    private static FilmRepository filmRepo;
    private static HallRepository hallRepo;
    private static final FilmSession SESSION = new FilmSession(-1, 1, 1,
            LocalDateTime.now(), LocalDateTime.now(), 10);
    private static final Film FILM = new Film();
    private static final Hall HALL = new Hall();

    @BeforeEach
    public void initServices() {
        sessionRepo = mock(SessionRepository.class);
        filmRepo = mock(FilmRepository.class);
        hallRepo = mock(HallRepository.class);
        sessionService = new SimpleFilmSessionService(sessionRepo, filmRepo, hallRepo);
    }

    @Test
    public void whenFindByIdThenReturnDto() {
        when(sessionRepo.findById(1)).thenReturn(Optional.of(SESSION));
        when(filmRepo.findById(1)).thenReturn(Optional.of(FILM));
        when(hallRepo.findById(1)).thenReturn(Optional.of(HALL));
        var actualDto = sessionService.findById(1).get();
        assertThat(actualDto.getId()).isEqualTo(-1);
        assertThat(actualDto.getHall()).isEqualTo(HALL);
        assertThat(actualDto.getFilm()).isEqualTo(FILM);
    }

    @Test
    public void whenFindByIdThenReturnEmpty() {
        when(sessionRepo.findById(1)).thenReturn(empty());
        assertThat(sessionService.findById(1)).isEqualTo(empty());
    }

    @Test
    public void whenFindByDayWhenThenReturnList() {
        when(sessionRepo.findByDay(any())).thenReturn(List.of(SESSION));
        when(filmRepo.findById(1)).thenReturn(Optional.of(FILM));
        when(hallRepo.findById(1)).thenReturn(Optional.of(HALL));
        var listActualDto = sessionService.findByDay(LocalDateTime.now().toLocalDate());
        assertThat(listActualDto.size()).isEqualTo(1);
        assertThat(listActualDto.get(0).getFilm()).isEqualTo(FILM);
        assertThat(listActualDto.get(0).getHall()).isEqualTo(HALL);
    }

    @Test
    public void whenFindByFilmWhenThenReturnList() {
        when(sessionRepo.findByFilm(1)).thenReturn(List.of(SESSION));
        when(filmRepo.findById(1)).thenReturn(Optional.of(FILM));
        when(hallRepo.findById(1)).thenReturn(Optional.of(HALL));
        var listActualDto = sessionService.findByFilm(1);
        assertThat(listActualDto.size()).isEqualTo(1);
        assertThat(listActualDto.get(0).getFilm()).isEqualTo(FILM);
        assertThat(listActualDto.get(0).getHall()).isEqualTo(HALL);
    }

    @Test
    public void whenFindByIdTrowIllegalStateThenCatchIt() {
        var thrown = catchThrowable(() -> {
            when(sessionRepo.findByFilm(1)).thenReturn(List.of(SESSION));
            when(filmRepo.findById(1)).thenReturn(empty());
            when(hallRepo.findById(1)).thenReturn(Optional.of(HALL));
            var listActualDto = sessionService.findByFilm(1);
        });
        assertThat(thrown).isInstanceOf(IllegalStateException.class);
        var msg = "No such object by id. FilmSession id = -1, Film id = 1, Hall id = 1.";
        assertThat(thrown.getMessage()).isEqualTo(msg);
    }
}