package ru.job4j.cinema.repository;

import org.junit.jupiter.api.Test;
import ru.job4j.cinema.model.Genre;

import static java.util.Optional.empty;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * In tests, checks are made to return a result for such selections as id, all.
 * A case with an empty output and a specific result is checked.
 */
class Sql2oGenreRepositoryTest {
    private final Sql2oGenreRepository genreRepo = new Sql2oGenreRepository(ConfigLouder.getSql2o());

    @Test
    public void whenFindByIdReturnSameGenre() {
        var expected = new Genre(2, "Adventure");
        var actual = genreRepo.findById(2).get();
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void whenFindByIdReturnEmpty() {
        assertThat(genreRepo.findById(-1)).isEqualTo(empty());
    }

    @Test
    public void whenGetAll() {
        assertThat(genreRepo.getAll().size()).isEqualTo(5);
        assertThat(genreRepo.findById(1).get().getName()).isEqualTo("Thriller");
        assertThat(genreRepo.findById(3).get().getName()).isEqualTo("Science fiction");
    }
}