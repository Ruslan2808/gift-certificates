package ru.clevertec.ecl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.clevertec.ecl.entity.User;

import java.util.Optional;

/**
 * Interface for performing operations with {@link User} in the database
 *
 * @author Ruslan Kantsevitch
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Returns a user of type {@link Optional<User>} by username or empty optional tag
     * if not found in the database
     *
     * @param username the user username
     * @return the user of type {@link Optional<User>} with given username
     */
    Optional<User> findByUsername(String username);

    /**
     * Returns a user of type {@link Optional<User>} by email or empty optional tag
     * if not found in the database
     *
     * @param email the user email
     * @return the user of type {@link Optional<User>} with given email
     */
    Optional<User> findByEmail(String email);
}
