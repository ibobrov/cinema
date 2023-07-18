package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.Hall;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import static java.util.Optional.empty;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class Sql2oHallRepositoryTest {
    private static Sql2oHallRepository sql2oHallRepository;

    @BeforeAll
    public static void initRepository() throws IOException {
        var properties = new Properties();
        try (var inputStream = Sql2oHallRepositoryTest.class.getClassLoader().getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        var configuration = new DatasourceConfiguration();
        var datasource = configuration.connectionPool(
                properties.getProperty("datasource.url"),
                properties.getProperty("datasource.username"),
                properties.getProperty("datasource.password")
        );
        var sql2o = configuration.databaseClient(datasource);
        sql2oHallRepository = new Sql2oHallRepository(sql2o);
    }

    @AfterEach
    public void clearRepository() {
        var list = sql2oHallRepository.getAll();
        for (var hall : list) {
            sql2oHallRepository.delete(hall.getId());
        }
    }

    @Test
    public void whenAddHall() {
        var hall = new Hall(0, "name", 4, 5, "desc");
        var actualHall = sql2oHallRepository.save(hall);
        assertThat(actualHall).usingRecursiveComparison().isEqualTo(hall);
    }

    @Test
    public void whenAddHallThenGetSame() {
        var hall = new Hall(0, "name", 4, 5, "desc");
        sql2oHallRepository.save(hall);
        var actualHall = sql2oHallRepository.findById(hall.getId()).get();
        assertThat(actualHall).usingRecursiveComparison().isEqualTo(hall);
    }

    @Test
    public void whenAddAnyHallThenGetAll() {
        var hall1 = new Hall(0, "name1", 4, 5, "desc");
        var hall2 = new Hall(0, "name2", 4, 5, "desc");
        var hall3 = new Hall(0, "name3", 4, 5, "desc");
        sql2oHallRepository.save(hall1);
        sql2oHallRepository.save(hall2);
        sql2oHallRepository.save(hall3);
        assertThat(sql2oHallRepository.getAll().get(0)).usingRecursiveComparison().isEqualTo(hall1);
        assertThat(sql2oHallRepository.getAll()).isEqualTo(List.of(hall1, hall2, hall3));
    }

    @Test
    public void whenRepositoryEmptyAfterAddOneThenFindByIdWithAnotherId() {
        var actualHall = sql2oHallRepository.findById(1);
        assertThat(sql2oHallRepository.getAll().size()).isEqualTo(0);
        assertThat(actualHall).isEqualTo(empty());
    }

    @Test
    public void whenDeleteOneHallThenHaveNotOnlyDeleted() {
        var hall1 = new Hall(0, "name1", 4, 5, "desc");
        var hall2 = new Hall(0, "name2", 4, 5, "desc");
        var hall3 = new Hall(0, "name3", 4, 5, "desc");
        sql2oHallRepository.save(hall1);
        sql2oHallRepository.save(hall2);
        sql2oHallRepository.save(hall3);
        var isDeleted = sql2oHallRepository.delete(hall2.getId());
        var isDeletedAgain = sql2oHallRepository.delete(hall2.getId());
        assertThat(isDeleted).isTrue();
        assertThat(isDeletedAgain).isFalse();
        assertThat(sql2oHallRepository.getAll()).isEqualTo(List.of(hall1, hall3));
    }
}