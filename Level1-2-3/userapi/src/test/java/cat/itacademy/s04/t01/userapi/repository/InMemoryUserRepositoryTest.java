package cat.itacademy.s04.t01.userapi.repository;

import cat.itacademy.s04.t01.userapi.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryUserRepositoryTest {

    private InMemoryUserRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryUserRepository();
    }

    private User createUser(String name, String email) {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName(name);
        user.setEmail(email);
        return user;
    }

    @Test
    void save_shouldAddUserToList() {
        User user = createUser("John Doe", "john@test.com");

        User savedUser = repository.save(user);

        assertEquals(user, savedUser);
        assertEquals(1, repository.findAll().size());
        assertTrue(repository.findAll().contains(user));
    }

    @Test
    void findAll_shouldReturnAllUsers() {
        User user1 = createUser("John Doe", "john@test.com");
        User user2 = createUser("Jane Doe", "jane@test.com");
        repository.save(user1);
        repository.save(user2);

        List<User> users = repository.findAll();

        assertEquals(2, users.size());
        assertTrue(users.contains(user1));
        assertTrue(users.contains(user2));
    }

    @Test
    void findAll_shouldReturnDefensiveCopy() {
        User user = createUser("John Doe", "john@test.com");
        repository.save(user);

        List<User> users = repository.findAll();
        users.clear();

        assertEquals(1, repository.findAll().size());
    }

    @Test
    void findById_shouldReturnUserWhenExists() {
        UUID id = UUID.randomUUID();
        User user = new User();
        user.setId(id);
        user.setName("John Doe");
        user.setEmail("john@test.com");
        repository.save(user);

        Optional<User> found = repository.findById(id);

        assertTrue(found.isPresent());
        assertEquals(user, found.get());
    }

    @Test
    void findById_shouldReturnEmptyWhenNotExists() {
        Optional<User> found = repository.findById(UUID.randomUUID());

        assertFalse(found.isPresent());
    }

    @Test
    void findByNameContainingIgnoreCase_shouldReturnFilteredUsers() {
        repository.save(createUser("John Doe", "john@test.com"));
        repository.save(createUser("Jane Smith", "jane@test.com"));
        repository.save(createUser("Johnny Cash", "johnny@test.com"));

        List<User> results = repository.findByNameContainingIgnoreCase("john");

        assertEquals(2, results.size());
        assertTrue(results.stream().allMatch(u ->
                u.getName().toLowerCase().contains("john")));
    }

    @Test
    void findByNameContainingIgnoreCase_shouldReturnAllWhenNameIsNull() {
        repository.save(createUser("John Doe", "john@test.com"));
        repository.save(createUser("Jane Smith", "jane@test.com"));

        List<User> results = repository.findByNameContainingIgnoreCase(null);

        assertEquals(2, results.size());
    }

    @Test
    void findByNameContainingIgnoreCase_shouldReturnAllWhenNameIsEmpty() {
        repository.save(createUser("John Doe", "john@test.com"));
        repository.save(createUser("Jane Smith", "jane@test.com"));

        List<User> results = repository.findByNameContainingIgnoreCase("");

        assertEquals(2, results.size());
    }

    @Test
    void findByNameContainingIgnoreCase_shouldReturnEmptyWhenNoMatches() {
        repository.save(createUser("John Doe", "john@test.com"));

        List<User> results = repository.findByNameContainingIgnoreCase("xyz");

        assertEquals(0, results.size());
    }

    @Test
    void existsByEmail_shouldReturnTrueWhenEmailExists() {
        repository.save(createUser("John Doe", "john@test.com"));

        assertTrue(repository.existsByEmail("john@test.com"));
    }

    @Test
    void existsByEmail_shouldBeCaseInsensitive() {
        repository.save(createUser("John Doe", "John@Test.com"));

        assertTrue(repository.existsByEmail("john@test.com"));
        assertTrue(repository.existsByEmail("JOHN@TEST.COM"));
    }

    @Test
    void existsByEmail_shouldReturnFalseWhenEmailNotExists() {
        assertFalse(repository.existsByEmail("notfound@test.com"));
    }

    @Test
    void clear_shouldRemoveAllUsers() {
        repository.save(createUser("John Doe", "john@test.com"));
        repository.save(createUser("Jane Doe", "jane@test.com"));

        repository.clear();

        assertEquals(0, repository.findAll().size());
    }
}
