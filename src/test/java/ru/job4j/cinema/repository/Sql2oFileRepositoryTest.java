package ru.job4j.cinema.repository;

import org.junit.jupiter.api.Test;
import ru.job4j.cinema.model.File;

import static java.util.Optional.empty;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * In tests, checks are made to return a result for such selections as id.
 * A case with an empty output and a specific result is checked.
 */
class Sql2oFileRepositoryTest {
    private final Sql2oFileRepository fileRepo = new Sql2oFileRepository(ConfigLouder.getSql2o());

    @Test
    public void whenFindByIdReturnSameFile() {
        var expectedFile = new File(1, "wonder_woman_2017.jpg", "src/main/resources/files/wonder_woman_2017.jpg");
        var actualFile = fileRepo.findById(1);
        assertThat(actualFile.get()).usingRecursiveComparison().isEqualTo(expectedFile);
    }

    @Test
    public void whenFindByIdReturnEmpty() {
        assertThat(fileRepo.findById(-1)).isEqualTo(empty());
    }
}