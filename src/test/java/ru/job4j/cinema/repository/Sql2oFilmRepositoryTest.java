package ru.job4j.cinema.repository;

import org.junit.jupiter.api.Test;
import ru.job4j.cinema.model.Film;

import static java.util.Optional.empty;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * In tests, checks are made to return a result for such selections as id, all.
 * A case with an empty output and a specific result is checked.
 */
class Sql2oFilmRepositoryTest {
    private final Sql2oFilmRepository filmRepo = new Sql2oFilmRepository(ConfigLouder.getSql2o());

    @Test
    public void whenFindByIdReturnSameFilm() {
        var expectedFilm = new Film(1, "Wonder Women",
                "When a pilot crashes and tells of conflict in the outside world, Diana, an Amazonian"
                        + " warrior in training, leaves home to fight a war, discovering her full powers and true"
                        + " destiny.", 2017, 2, 6, 141, 1);
        var actualFilm = filmRepo.findById(1);
        assertThat(actualFilm.get()).usingRecursiveComparison().isEqualTo(expectedFilm);
    }

    @Test
    public void whenFindByIdReturnEmpty() {
        assertThat(filmRepo.findById(-1)).isEqualTo(empty());
    }

    @Test
    public void whenGetAll() {
        assertThat(filmRepo.getAll().size()).isEqualTo(6);
        assertThat(filmRepo.findById(3).get().getName()).isEqualTo("Annihilation");
        assertThat(filmRepo.findById(6).get().getName()).isEqualTo("Jaws");
    }
}