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
    public Hall save(Hall hall) {
        try (var connection = sql2o.open()) {
            var sql = """
                      INSERT INTO halls(name, row_count, place_count, description)
                      VALUES (:name, :rowCount, :placeCount, :description);
                      """;
            var query = connection.createQuery(sql, true)
                    .addParameter("name", hall.getName())
                    .addParameter("rowCount", hall.getRowCount())
                    .addParameter("placeCount", hall.getPlaceCount())
                    .addParameter("description", hall.getDescription());
            int generatedId = query.executeUpdate().getKey(Integer.class);
            hall.setId(generatedId);
            return hall;
        }
    }

    @Override
    public boolean delete(int id) {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("DELETE FROM halls WHERE id = :id;");
            query.addParameter("id", id);
            var affectedRow = query.executeUpdate().getResult();
            return affectedRow > 0;
        }
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
