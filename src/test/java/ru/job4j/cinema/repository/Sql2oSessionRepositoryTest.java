package ru.job4j.cinema.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.FilmSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Properties;

import static java.util.Optional.empty;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * In the tests, checks are made to return the result for such samples as id, day, movie.
 * The case with an empty output and a specific result is checked.
 */
class Sql2oSessionRepositoryTest {
    private static Sql2oSessionRepository sessionRepo;
    private final static FilmSession FILM_SESSION_4 = new FilmSession(4);
    private final static FilmSession FILM_SESSION_20 = new FilmSession(20);

    @BeforeAll
    public static void initRepository() throws IOException {
        var properties = new Properties();
        try (var inputStream = Sql2oHallRepositoryTest.class.getClassLoader()
                .getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        var configuration = new DatasourceConfiguration();
        var datasource = configuration.connectionPool(
                properties.getProperty("datasource.url"),
                properties.getProperty("datasource.username"),
                properties.getProperty("datasource.password")
        );
        var sql2o = configuration.databaseClient(datasource);
        sessionRepo = new Sql2oSessionRepository(sql2o);
    }

    @Test
    public void whenFindByIdReturnSameFilmSession() {
        var expected = new FilmSession(13, 3, 1,
                LocalDateTime.of(2023, 7, 19, 10, 0),
                LocalDateTime.of(2023, 7, 19, 11, 55),
                5);
        var actual = sessionRepo.findById(13).get();
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void whenFindByIdNegativeNumberThenReturnEmpty() {
        assertThat(sessionRepo.findById(-1)).isEqualTo(empty());
    }

    @Test
    public void whenFindByFilmReturnFilmSessionsList() {
        var expected = List.of(FILM_SESSION_4, FILM_SESSION_20);
        var listByFilm = sessionRepo.findByFilm(2);
        assertThat(listByFilm.size()).isEqualTo(2);
        assertThat(expected).isEqualTo(expected);
    }

    @Test
    public void whenFindByFilmWithNegativeIndexReturnEmptyFilmSessionsList() {
        var listByFilm = sessionRepo.findByFilm(-1);
        assertThat(listByFilm.size()).isEqualTo(0);
    }

    @Test
    public void whenGetAllThenCheckSizeAndIdsAnySessionsFromRepository() {
        assertThat(sessionRepo.getAll().size()).isEqualTo(24);
        assertThat(sessionRepo.findById(7).get().getFilmId()).isEqualTo(3);
        assertThat(sessionRepo.findById(19).get().getFilmId()).isEqualTo(5);
    }
}