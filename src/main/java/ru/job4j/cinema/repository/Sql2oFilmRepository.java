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
    public Film save(Film film) {
        try (var connection = sql2o.open()) {
            var sql = """
                      INSERT INTO films(name, description, year, genre_id, minimal_age, duration_in_minutes, file_id)
                      VALUES (:name, :description, :year, :genre_id, :minimal_age, :duration_in_minutes, :file_id);
                      """;
            var query = connection.createQuery(sql, true)
                    .addParameter("name", film.getName())
                    .addParameter("description", film.getDescription())
                    .addParameter("year", film.getYear())
                    .addParameter("genre_id", film.getGenreId())
                    .addParameter("minimal_age", film.getMinimalAge())
                    .addParameter("duration_in_minutes", film.getDurationInMinutes())
                    .addParameter("file_id", film.getFileId());
            int generatedId = query.executeUpdate().getKey(Integer.class);
            film.setId(generatedId);
            return film;
        }
    }

    @Override
    public boolean delete(int id) {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("DELETE FROM films WHERE id = :id;");
            query.addParameter("id", id);
            var affectedRow = query.executeUpdate().getResult();
            return affectedRow > 0;
        }
    }

    @Override
    public Optional<Film> findById(int id) {
        try (var connection = sql2o.open()) {
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
        try (var connection = sql2o.open()) {
            var sql = """
                    SELECT id, name, description, year, genre_id, minimal_age, 
                           duration_in_minutes, file_id
                    FROM films
                    """;
            var query = connection.createQuery(sql);
            return query.setColumnMappings(Film.COLUMN_MAPPING)
                        .executeAndFetch(Film.class);
        }
    }
}
