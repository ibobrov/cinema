package ru.job4j.cinema.service;

import org.junit.jupiter.api.Test;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.repository.UserRepository;

import java.util.Optional;

import static java.util.Optional.empty;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * It checks the creation of unique users and their search.
 */
class SimpleUserServiceTest {
    private final UserRepository userRepository = mock(UserRepository.class);
    private final SimpleUserService userService = new SimpleUserService(userRepository);

    @Test
    public void whenSaveUserThenReturnSameUser() {
        var user = new User();
        when(userRepository.save(user)).thenReturn(Optional.of(user));
        assertThat(userService.save(user).get()).isEqualTo(user);
    }

    @Test
    public void whenSaveUserThenReturnEmpty() {
        var user1 = new User(-1, "", "mail@mail.test", "Password1");
        var user2 = new User(-1, "name2", "", "Password2");
        var user3 = new User(-1, "name1", "mail@mail.test", "");
        when(userRepository.save(any())).thenReturn(Optional.of(new User()));
        assertThat(userService.save(user1)).isEqualTo(empty());
        assertThat(userService.save(user2)).isEqualTo(empty());
        assertThat(userService.save(user3)).isEqualTo(empty());
    }

    @Test
    public void whenFindByMailAndPasswordThenReturnUser() {
        var user = new User();
        when(userRepository.findByEmailAndPassword(any(), any())).thenReturn(Optional.of(user));
        assertThat(userService.findByEmailAndPassword("", "").get()).isEqualTo(user);
    }

    @Test
    public void whenFindByMailAndPasswordThenReturnEmpty() {
        when(userRepository.findByEmailAndPassword(any(), any())).thenReturn(empty());
        assertThat(userService.findByEmailAndPassword("", "")).isEqualTo(empty());
    }
}