package cat.itacademy.s04.t01.userapi.service;

import cat.itacademy.s04.t01.userapi.model.User;

import java.util.List;
import java.util.UUID;

/**
 * Service layer interface defining business use cases for user management.
 * This layer contains business logic and orchestrates data access.
 */
public interface UserService {

    /**
     * Creates a new user with a generated UUID.
     * @param name the user's name
     * @param email the user's email (must be unique)
     * @return the created user with generated ID
     * @throws EmailAlreadyExistsException if email is already registered
     */
    User createUser(String name, String email);

    /**
     * Retrieves all users.
     * @return list of all users
     */
    List<User> getAllUsers();

    /**
     * Retrieves a user by their unique identifier.
     * @param id the user's UUID
     * @return the found user
     * @throws UserNotFoundException if no user exists with the given ID
     */
    User getUserById(UUID id);

    /**
     * Searches users by name (case-insensitive, partial match).
     * @param name the search term
     * @return list of users whose name contains the search term
     */
    List<User> searchUsersByName(String name);
}