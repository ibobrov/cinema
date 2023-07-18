package ru.job4j.cinema.repository;

import org.springframework.stereotype.Repository;
import org.sql2o.Connection;
import org.sql2o.Query;
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
        try (Connection connection = sql2o.open()) {
            var sql = """
                      SELECT films.id, films.name, films.description, films.year, genres.name AS genre,
                             films.minimal_age, films.duration_in_minutes, films.file_id
                      FROM films
                      JOIN genres ON genres.id = films.genre_id
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
        try (Connection connection = sql2o.open()) {
            String str = """
                    SELECT films.id, films.name, films.description, films.year, genres.name AS genre,
                             films.minimal_age, films.duration_in_minutes, films.file_id
                    FROM films
                    JOIN genres ON genres.id = films.genre_id
                    """;
            Query query = connection.createQuery(str);
            return query.setColumnMappings(Film.COLUMN_MAPPING)
                        .executeAndFetch(Film.class);
        }
    }
}