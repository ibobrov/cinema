package ru.job4j.cinema.repository;

import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;
import ru.job4j.cinema.model.FilmSession;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public class Sql2oSessionRepository implements SessionRepository {
    private final Sql2o sql2o;

    public Sql2oSessionRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Optional<FilmSession> findById(int id) {
        try (var connection = sql2o.open()) {
            var sql = """
                      SELECT id, film_id, halls_id, start_time, end_time, price
                      FROM film_sessions
                      WHERE id = :id;
                      """;
            var query = connection.createQuery(sql);
            query.addParameter("id", id);
            var filmSession = query.setColumnMappings(FilmSession.COLUMN_MAPPING)
                    .executeAndFetchFirst(FilmSession.class);
            return Optional.ofNullable(filmSession);
        }
    }

    @Override
    public List<FilmSession> findByDay(LocalDate date) {
        try (var connection = sql2o.open()) {
            var sql = """
                      SELECT id, film_id, halls_id, start_time, end_time, price
                      FROM film_sessions
                      WHERE DATE(start_time) = DATE(:date);
                      """;
            var query = connection.createQuery(sql);
            query.addParameter("date", LocalDateTime.of(date, LocalTime.now()));
            return query.setColumnMappings(FilmSession.COLUMN_MAPPING)
                    .executeAndFetch(FilmSession.class);
        }
    }

    @Override
    public List<FilmSession> findByFilm(int id) {
        try (var connection = sql2o.open()) {
            var sql = """
                      SELECT id, film_id, halls_id, start_time, end_time, price
                      FROM film_sessions
                      WHERE film_id = :id;
                      """;
            var query = connection.createQuery(sql);
            query.addParameter("id", id);
            return query.setColumnMappings(FilmSession.COLUMN_MAPPING)
                        .executeAndFetch(FilmSession.class);
        }
    }

    @Override
    public List<FilmSession> getAll() {
        try (var connection = sql2o.open()) {
            var sql = """
                      SELECT id, film_id, halls_id, start_time, end_time, price
                      FROM film_sessions
                      """;
            var query = connection.createQuery(sql);
            return query.setColumnMappings(FilmSession.COLUMN_MAPPING)
                    .executeAndFetch(FilmSession.class);
        }
    }
}
