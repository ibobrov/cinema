package ru.job4j.cinema.repository;

import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;
import ru.job4j.cinema.model.Film;

import java.util.List;
import java.util.Optional;

@Repository
public class Sql2oFilmRepository implements FilmRepository {
    private final Sql2o sql2o;

    public Sql2oFilmRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Optional<Film> findById(int id) {
        try (var connection = sql2o.open()) {
            var sql = """
                      SELECT id, name, description, "year", genre_id,
                             minimal_age, duration_in_minutes, file_id
                      FROM films
                      WHERE films.id = :id;
                      """;
            var query = connection.createQuery(sql);
            query.addParameter("id", id);
            var film = query.setColumnMappings(Film.COLUMN_MAPPING)
                            .executeAndFetchFirst(Film.class);
            return Optional.ofNullable(film);
        }
    }

    @Override
    public List<Film> getAll() {
        try (var connection = sql2o.open()) {
            var sql = """
                    SELECT id, name, description, "year", genre_id, minimal_age, 
                           duration_in_minutes, file_id
                    FROM films
                    """;
            var query = connection.createQuery(sql);
            return query.setColumnMappings(Film.COLUMN_MAPPING)
                        .executeAndFetch(Film.class);
        }
    }
}
