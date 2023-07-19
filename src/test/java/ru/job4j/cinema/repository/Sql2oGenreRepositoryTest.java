package ru.job4j.cinema.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.Genre;

import java.io.IOException;
import java.util.Properties;

import static java.util.Optional.empty;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class Sql2oGenreRepositoryTest {
    private static Sql2oGenreRepository genreRepo;

    @BeforeAll
    public static void initRepository() throws IOException {
        var properties = new Properties();
        try (var inputStream = Sql2oHallRepositoryTest.class.getClassLoader().
                getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        var configuration = new DatasourceConfiguration();
        var datasource = configuration.connectionPool(
                properties.getProperty("datasource.url"),
                properties.getProperty("datasource.username"),
                properties.getProperty("datasource.password")
        );
        var sql2o = configuration.databaseClient(datasource);
        genreRepo = new Sql2oGenreRepository(sql2o);
    }

    @Test
    public void whenFindByReturnGenre() {
        var expected = new Genre(2, "Adventure");
        var actual = genreRepo.findById(2).get();
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void whenFindByReturnEmpty() {
        assertThat(genreRepo.findById(-1)).isEqualTo(empty());
    }

    @Test
    public void whenGetAll() {
        assertThat(genreRepo.getAll().size()).isEqualTo(5);
        assertThat(genreRepo.findById(1).get().getName()).isEqualTo("Thriller");
        assertThat(genreRepo.findById(3).get().getName()).isEqualTo("Science fiction");
    }
}