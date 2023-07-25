package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import static java.util.Optional.empty;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * In the tests, checks are made to return the result for such samples as id, session id.
 * The case with an empty output and a specific result is checked.
 * It also checks to save instances with the same seat.
 */
class Sql2oTicketRepositoryTest {
    private static Sql2oTicketRepository ticketRepo;
    private static Sql2oUserRepository userRepo;
    private static final User USER = new User(-1,
            "Test Test", "mail@mail.test", "Pass");

    @BeforeAll
    public static void initRepository() throws IOException {
        var properties = new Properties();
        try (var inputStream = Sql2oHallRepositoryTest.class.getClassLoader()
                .getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        var configuration = new DatasourceConfiguration();
        var datasource = configuration.connectionPool(
                properties.getProperty("datasource.url"),
                properties.getProperty("datasource.username"),
                properties.getProperty("datasource.password")
        );
        var sql2o = configuration.databaseClient(datasource);
        ticketRepo = new Sql2oTicketRepository(sql2o);
        userRepo = new Sql2oUserRepository(sql2o);
        userRepo.save(USER);
    }

    @AfterEach
    public void cleanRepo() {
        for (var ticket : ticketRepo.getAll()) {
            ticketRepo.delete(ticket.getId());
        }
    }

    @AfterAll
    public static void cleanUsers() {
        userRepo.delete(USER.getId());
    }

    @Test
    public void whenSaveTwoTicketWhenFindByIdEach() {
        var expected1 = new Ticket(-1, 1, 1, 1, USER.getId());
        var expected2 = new Ticket(-1, 2, 2, 2, USER.getId());
        ticketRepo.save(expected1);
        ticketRepo.save(expected2);
        var actual1 = ticketRepo.findById(expected1.getId()).get();
        var actual2 = ticketRepo.findById(expected2.getId()).get();
        assertThat(actual1).usingRecursiveComparison().isEqualTo(expected1);
        assertThat(actual2).usingRecursiveComparison().isEqualTo(expected2);
    }

    @Test
    public void whenSaveTwoTicketOnOneSeatWhenFindByFirst() {
        var expected1 = new Ticket(-1, 1, 1, 1, USER.getId());
        var expected2 = new Ticket(-1, 1, 1, 1, USER.getId());
        ticketRepo.save(expected1);
        ticketRepo.save(expected2);
        var actual1 = ticketRepo.findById(expected1.getId()).get();
        var actual2 = ticketRepo.findById(expected2.getId());
        assertThat(actual1).usingRecursiveComparison().isEqualTo(expected1);
        assertThat(actual2).isEqualTo(empty());
    }

    @Test
    public void whenFindByIdNegativeNumberThenReturnEmpty() {
        assertThat(ticketRepo.findById(-1)).isEqualTo(empty());
    }

    @Test
    public void whenFindBySessionOneOrTwoThenCheckThatResultIsCorrected() {
        var ticket1 = new Ticket(-1, 1, 1, 1, USER.getId());
        var ticket2 = new Ticket(-1, 1, 1, 2, USER.getId());
        var ticket3 = new Ticket(-1, 2, 2, 3, USER.getId());
        var ticket4 = new Ticket(-1, 2, 2, 4, USER.getId());
        ticketRepo.save(ticket1);
        ticketRepo.save(ticket2);
        ticketRepo.save(ticket3);
        ticketRepo.save(ticket4);
        assertThat(ticketRepo.findBySession(1))
                .usingRecursiveComparison()
                .isEqualTo(List.of(ticket1, ticket2));
        assertThat(ticketRepo.findBySession(2))
                .usingRecursiveComparison()
                .isEqualTo(List.of(ticket3, ticket4));
    }

    @Test
    public void whenTryDeleteTwiceThenSecondTryWillFalse() {
        var ticket = new Ticket(-1, 1, 1, 1, USER.getId());
        ticketRepo.save(ticket);
        assertThat(ticketRepo.delete(ticket.getId())).isTrue();
        var all = ticketRepo.getAll();
        assertThat(all.size()).isEqualTo(0);
        assertThat(ticketRepo.delete(ticket.getId())).isFalse();
    }
}