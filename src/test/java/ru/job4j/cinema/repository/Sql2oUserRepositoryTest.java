package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.model.User;

import static java.util.Optional.empty;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * In the tests, checks are made to return the result for such samples as id, email & password.
 * The case with an empty output and a specific result is checked.
 * It also checks to save instances with the same mail.
 */
class Sql2oUserRepositoryTest {
    private final Sql2oUserRepository userRepo = new Sql2oUserRepository(ConfigLouder.getSql2o());

    @AfterEach
    public void cleanRepo() {
        for (var user : userRepo.getAll()) {
            userRepo.delete(user.getId());
        }
    }

    @Test
    public void whenSaveTwoUserWhenFindByIdEach() {
        var expected1 = new User(-1, "name1", "mail@mail.test", "Password");
        var expected2 = new User(-1, "name2", "test@mail.test", "Password");
        userRepo.save(expected1);
        userRepo.save(expected2);
        var actual1 = userRepo.findById(expected1.getId()).get();
        var actual2 = userRepo.findById(expected2.getId()).get();
        assertThat(expected1).usingRecursiveComparison().isEqualTo(actual1);
        assertThat(expected2).usingRecursiveComparison().isEqualTo(actual2);
    }

    @Test
    public void whenSaveTwoUserWithEqualMailWhenFindByIdEach() {
        var expected1 = new User(-1, "name1", "mail@mail.test", "Password");
        var expected2 = new User(-1, "name2", "mail@mail.test", "Password");
        userRepo.save(expected1);
        userRepo.save(expected2);
        var actual1 = userRepo.findById(expected1.getId()).get();
        var actual2 = userRepo.findById(expected2.getId());
        assertThat(actual1).usingRecursiveComparison().isEqualTo(expected1);
        assertThat(actual2).isEqualTo(empty());
    }

    @Test
    public void whenFindByIdWithNegativeNumberThenReturnEmpty() {
        assertThat(userRepo.findById(-1)).isEqualTo(empty());
    }

    @Test
    public void whenFindByEmailAndPasswordThenReturnFoundOne() {
        var user1 = new User(-1, "name1", "mail@mail.test", "Password1");
        var user2 = new User(-1, "name2", "test@mail.test", "Password2");
        userRepo.save(user1);
        userRepo.save(user2);
        assertThat(userRepo.findByEmailAndPassword("mail@mail.test", "Password1").get())
                .usingRecursiveComparison().isEqualTo(user1);
        assertThat(userRepo.findByEmailAndPassword("test@mail.test", "Password2").get())
                .usingRecursiveComparison().isEqualTo(user2);
    }

    @Test
    public void whenFindByEmailAndPasswordThenReturnEmpty() {
        assertThat(userRepo.findByEmailAndPassword("test", "pass")).isEqualTo(empty());
    }

    @Test
    public void whenTryDeleteTwiceThenSecondTryWillFalse() {
        var user = new User(-1, "name1", "mail@mail.test", "Password");
        userRepo.save(user);
        assertThat(userRepo.delete(user.getId())).isTrue();
        var all = userRepo.getAll();
        assertThat(all.size()).isEqualTo(0);
        assertThat(userRepo.delete(user.getId())).isFalse();
    }
}