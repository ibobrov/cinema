package ru.job4j.cinema.repository;

import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;
import ru.job4j.cinema.model.FilmSession;

import java.util.List;
import java.util.Optional;

@Repository
public class Sql2oFilmSessionRepository implements FilmSessionRepository {
    private final Sql2o sql2o;

    public Sql2oFilmSessionRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public FilmSession save(FilmSession filmSession) {
        try (var connection = sql2o.open()) {
            var sql = """
                      INSERT INTO film_sessions(film_id, halls_id, start_time, end_time, price)
                      VALUES (:filmId, :hallsId, :startTime, :endTime, :price);
                      """;
            var query = connection.createQuery(sql, true)
                    .addParameter("filmId", filmSession.getFilmId())
                    .addParameter("hallsId", filmSession.getHallId())
                    .addParameter("startTime", filmSession.getStartTime())
                    .addParameter("endTime", filmSession.getEndTime())
                    .addParameter("price", filmSession.getPrice());
            int generatedId = query.executeUpdate().getKey(Integer.class);
            filmSession.setId(generatedId);
            return filmSession;
        }
    }

    @Override
    public boolean delete(int id) {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("DELETE FROM film_sessions WHERE id = :id");
            query.addParameter("id", id);
            var affectedRow = query.executeUpdate().getResult();
            return affectedRow > 0;
        }
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
