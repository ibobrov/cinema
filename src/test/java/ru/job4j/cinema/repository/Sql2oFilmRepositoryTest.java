package ru.job4j.cinema.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.Film;

import java.io.IOException;
import java.util.Properties;

import static java.util.Optional.empty;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class Sql2oFilmRepositoryTest {
    private static Sql2oFilmRepository filmRepo;

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
        filmRepo = new Sql2oFilmRepository(sql2o);
    }

    @Test
    public void whenFindByReturnFilm() {
        var expectedFilm = new Film(1, "Wonder Women",
                "When a pilot crashes and tells of conflict in the outside world, Diana, an Amazonian"
                        + " warrior in training, leaves home to fight a war, discovering her full powers and true"
                        + " destiny.", 2017, 2, 6, 141, 1);
        var actualFilm = filmRepo.findById(1);
        assertThat(actualFilm.get()).usingRecursiveComparison().isEqualTo(expectedFilm);
    }

    @Test
    public void whenFindByReturnEmpty() {
        assertThat(filmRepo.findById(-1)).isEqualTo(empty());
    }

    @Test
    public void whenGetAll() {
        assertThat(filmRepo.getAll().size()).isEqualTo(6);
        assertThat(filmRepo.findById(3).get().getName()).isEqualTo("Annihilation");
        assertThat(filmRepo.findById(6).get().getName()).isEqualTo("Jaws");
    }
}