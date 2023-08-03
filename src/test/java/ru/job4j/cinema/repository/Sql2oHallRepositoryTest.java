package ru.job4j.cinema.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.Hall;

import java.io.IOException;
import java.util.Properties;

import static java.util.Optional.empty;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * In tests, checks are made to return the result for such samples as id, all.
 * A case with an empty output and a specific result is checked.
 */
class Sql2oHallRepositoryTest {
    private static Sql2oHallRepository hallRepo;

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
        hallRepo = new Sql2oHallRepository(sql2o);
    }

    @Test
    public void whenFindByIdReturnSameHall() {
        var expected = new Hall(2, "Hall 2", 10, 20,
                "Large room with seats made of soft and comfortable upholstery.");
        var actual = hallRepo.findById(2).get();
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void whenFindByIdReturnEmpty() {
        assertThat(hallRepo.findById(-1)).isEqualTo(empty());
    }

    @Test
    public void whenGetAllThenCheckSizeRslList() {
        assertThat(hallRepo.getAll().size()).isEqualTo(3);
        assertThat(hallRepo.findById(1).get().getName()).isEqualTo("Hall 1");
        assertThat(hallRepo.findById(3).get().getName()).isEqualTo("Vip room");
    }
}