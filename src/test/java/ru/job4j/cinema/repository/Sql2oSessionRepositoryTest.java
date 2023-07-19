package ru.job4j.cinema.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.FilmSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Properties;

import static java.util.Optional.empty;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class Sql2oSessionRepositoryTest {
    private static Sql2oSessionRepository sessionRepo;

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
    public void whenFindByReturnGenre() {
        var expected = new FilmSession(13, 3, 1,
                LocalDateTime.of(2023, 7, 19, 10, 0),
                LocalDateTime.of(2023, 7, 19, 11, 55),
                5);
        var actual = sessionRepo.findById(13).get();
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void whenFindByReturnEmpty() {
        assertThat(sessionRepo.findById(-1)).isEqualTo(empty());
    }

    @Test
    public void whenGetAll() {
        assertThat(sessionRepo.getAll().size()).isEqualTo(24);
        assertThat(sessionRepo.findById(7).get().getFilmId()).isEqualTo(3);
        assertThat(sessionRepo.findById(19).get().getFilmId()).isEqualTo(5);
    }
}