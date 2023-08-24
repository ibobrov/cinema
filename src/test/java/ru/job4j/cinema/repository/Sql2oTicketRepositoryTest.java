package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sql2o.Sql2o;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;

import java.util.List;

import static java.util.Optional.empty;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * In the tests, checks are made to return the result for such samples as id, session id.
 * The case with an empty output and a specific result is checked.
 * It also checks to save instances with the same seat.
 */
class Sql2oTicketRepositoryTest {
    private static final Sql2o SQL_2_O = ConfigLouder.getSql2o();
    private static final Sql2oTicketRepository TICKET_REPO = new Sql2oTicketRepository(SQL_2_O);
    private static final Sql2oUserRepository USER_REPO = new Sql2oUserRepository(SQL_2_O);
    private static final User USER = new User(-1,
            "Test Test", "mail@mail.test", "Pass");

    @BeforeAll
    public static void initRepository() {
        USER_REPO.save(USER);
    }

    @AfterEach
    public void cleanRepo() {
        for (var ticket : TICKET_REPO.getAll()) {
            TICKET_REPO.delete(ticket.getId());
        }
    }

    @AfterAll
    public static void cleanUsers() {
        USER_REPO.delete(USER.getId());
    }

    @Test
    public void whenSaveTwoTicketWhenFindByIdEach() {
        var expected1 = new Ticket(-1, 1, 1, 1, USER.getId());
        var expected2 = new Ticket(-1, 2, 2, 2, USER.getId());
        TICKET_REPO.save(expected1);
        TICKET_REPO.save(expected2);
        var actual1 = TICKET_REPO.findById(expected1.getId()).get();
        var actual2 = TICKET_REPO.findById(expected2.getId()).get();
        assertThat(actual1).usingRecursiveComparison().isEqualTo(expected1);
        assertThat(actual2).usingRecursiveComparison().isEqualTo(expected2);
    }

    @Test
    public void whenSaveTwoTicketOnOneSeatWhenFindByFirst() {
        var expected1 = new Ticket(-1, 1, 1, 1, USER.getId());
        var expected2 = new Ticket(-1, 1, 1, 1, USER.getId());
        TICKET_REPO.save(expected1);
        TICKET_REPO.save(expected2);
        var actual1 = TICKET_REPO.findById(expected1.getId()).get();
        var actual2 = TICKET_REPO.findById(expected2.getId());
        assertThat(actual1).usingRecursiveComparison().isEqualTo(expected1);
        assertThat(actual2).isEqualTo(empty());
    }

    @Test
    public void whenFindByIdNegativeNumberThenReturnEmpty() {
        assertThat(TICKET_REPO.findById(-1)).isEqualTo(empty());
    }

    @Test
    public void whenFindBySessionOneOrTwoThenCheckThatResultIsCorrected() {
        var ticket1 = new Ticket(-1, 1, 1, 1, USER.getId());
        var ticket2 = new Ticket(-1, 1, 1, 2, USER.getId());
        var ticket3 = new Ticket(-1, 2, 2, 3, USER.getId());
        var ticket4 = new Ticket(-1, 2, 2, 4, USER.getId());
        TICKET_REPO.save(ticket1);
        TICKET_REPO.save(ticket2);
        TICKET_REPO.save(ticket3);
        TICKET_REPO.save(ticket4);
        assertThat(TICKET_REPO.findBySession(1))
                .usingRecursiveComparison()
                .isEqualTo(List.of(ticket1, ticket2));
        assertThat(TICKET_REPO.findBySession(2))
                .usingRecursiveComparison()
                .isEqualTo(List.of(ticket3, ticket4));
    }

    @Test
    public void whenTryDeleteTwiceThenSecondTryWillFalse() {
        var ticket = new Ticket(-1, 1, 1, 1, USER.getId());
        TICKET_REPO.save(ticket);
        assertThat(TICKET_REPO.delete(ticket.getId())).isTrue();
        var all = TICKET_REPO.getAll();
        assertThat(all.size()).isEqualTo(0);
        assertThat(TICKET_REPO.delete(ticket.getId())).isFalse();
    }
}