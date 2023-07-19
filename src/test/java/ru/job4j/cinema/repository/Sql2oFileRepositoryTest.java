package ru.job4j.cinema.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.File;

import java.io.IOException;
import java.util.Properties;

import static java.util.Optional.empty;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class Sql2oFileRepositoryTest {
    private static Sql2oFileRepository fileRepo;

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
        fileRepo = new Sql2oFileRepository(sql2o);
    }

    @Test
    public void whenFindByReturnFile() {
        var expectedFile = new File(1, "wonder_woman_2017.jpg", "src/main/resources/files/wonder_woman_2017.jpg");
        var actualFile = fileRepo.findById(1);
        assertThat(actualFile.get()).usingRecursiveComparison().isEqualTo(expectedFile);
    }

    @Test
    public void whenFindByReturnEmpty() {
        assertThat(fileRepo.findById(-1)).isEqualTo(empty());
    }
}