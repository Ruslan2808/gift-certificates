package ru.clevertec.ecl.integration.repository;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import ru.clevertec.ecl.entity.User;
import ru.clevertec.ecl.integration.BaseIntegrationTest;
import ru.clevertec.ecl.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class UserRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void checkFindByUsernameShouldReturnOptionalUser() {
        Long expectedId = 1L;
        String username = "ivan";

        Optional<User> actualUser = userRepository.findByUsername(username);

        assertThat(actualUser).isPresent();
        assertThat(actualUser.get().getId()).isEqualTo(expectedId);
    }

    @Test
    void checkFindByUsernameShouldReturnEmptyOptionalUser() {
        String username = "max";

        Optional<User> actualUser = userRepository.findByUsername(username);

        assertThat(actualUser).isNotPresent();
    }

    @Test
    void checkFindByEmailShouldReturnOptionalUser() {
        Long expectedId = 2L;
        String email = "petrov@mail.ru";

        Optional<User> actualUser = userRepository.findByEmail(email);

        assertThat(actualUser).isPresent();
        assertThat(actualUser.get().getId()).isEqualTo(expectedId);
    }

    @Test
    void checkFindByEmailShouldReturnEmptyOptionalUser() {
        String email = "max@mail.ru";

        Optional<User> actualUser = userRepository.findByEmail(email);

        assertThat(actualUser).isNotPresent();
    }
}
