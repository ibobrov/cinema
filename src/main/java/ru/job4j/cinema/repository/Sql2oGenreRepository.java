package ru.job4j.cinema.repository;

import org.springframework.stereotype.Service;
import org.sql2o.Sql2o;
import ru.job4j.cinema.model.Genre;

import java.util.List;
import java.util.Optional;

@Service
public class Sql2oGenreRepository implements GenreRepository {
    private final Sql2o sql2o;

    public Sql2oGenreRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Optional<Genre> findById(int id) {
        try (var connection = sql2o.open()) {
            var sql = """
                      SELECT id, name
                      FROM genres
                      WHERE id = :id;
                      """;
            var query = connection.createQuery(sql);
            query.addParameter("id", id);
            var genre = query.executeAndFetchFirst(Genre.class);
            return Optional.ofNullable(genre);
        }
    }

    @Override
    public List<Genre> getAll() {
        try (var connection = sql2o.open()) {
            var sql = """
                      SELECT id, name
                      FROM genres;
                      """;
            var query = connection.createQuery(sql);
            return query.executeAndFetch(Genre.class);
        }
    }
}
