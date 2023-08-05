package ru.job4j.cinema.repository;

import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;
import ru.job4j.cinema.model.Hall;

import java.util.List;
import java.util.Optional;

@Repository
public class Sql2oHallRepository implements HallRepository {
    private final Sql2o sql2o;

    public Sql2oHallRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Optional<Hall> findById(int id) {
        try (var connection = sql2o.open()) {
            var sql = """
                      SELECT id, name, row_count, place_count, description
                      FROM halls
                      WHERE id = :id;
                      """;
            var query = connection.createQuery(sql);
            query.addParameter("id", id);
            var hall = query.setColumnMappings(Hall.COLUMN_MAPPING)
                    .executeAndFetchFirst(Hall.class);
            return Optional.ofNullable(hall);
        }
    }

    public Optional<Hall> findBySession(int id) {
        try (var connection = sql2o.open()) {
            var sql = """
                      SELECT h.*
                      FROM film_sessions AS s
                          INNER JOIN halls AS h
                              ON s.halls_id = h.id
                      WHERE s.id = :id;
                      """;
            var query = connection.createQuery(sql);
            query.addParameter("id", id);
            var hall = query.setColumnMappings(Hall.COLUMN_MAPPING)
                    .executeAndFetchFirst(Hall.class);
            return Optional.ofNullable(hall);
        }
    }

    @Override
    public List<Hall> getAll() {
        try (var connection = sql2o.open()) {
            var sql = """
                      SELECT id, name, row_count, place_count, description
                      FROM halls;
                      """;
            var query = connection.createQuery(sql);
            return query.setColumnMappings(Hall.COLUMN_MAPPING)
                    .executeAndFetch(Hall.class);
        }
    }
}
