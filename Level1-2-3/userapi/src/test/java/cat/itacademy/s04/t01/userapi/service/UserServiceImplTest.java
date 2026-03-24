package cat.itacademy.s04.t01.userapi.service;

import cat.itacademy.s04.t01.userapi.model.User;
import cat.itacademy.s04.t01.userapi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User createTestUser(UUID id, String name, String email) {
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        return user;
    }

    @Test
    void createUser_shouldThrowExceptionWhenEmailAlreadyExists() {
        String existingEmail = "john@mail.com";
        String name = "John Lennon";

        when(userRepository.existsByEmail(existingEmail)).thenReturn(true);

        assertThatThrownBy(() -> userService.createUser(name, existingEmail))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Email already exists");

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void createUser_shouldCreateUserWhenEmailIsUnique() {
        String email = "paul@mail.com";
        String name = "Paul McCartney";
        UUID generatedId = UUID.randomUUID();

        when(userRepository.existsByEmail(email)).thenReturn(false);

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User userToSave = invocation.getArgument(0);
            userToSave.setId(generatedId);
            return userToSave;
        });

        User result = userService.createUser(name, email);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(name);
        assertThat(result.getEmail()).isEqualTo(email);
        assertThat(result.getId()).isNotNull();

        verify(userRepository).existsByEmail(email);

        verify(userRepository).save(any(User.class));
    }

    @Test
    void getAllUsers_shouldReturnAllUsers() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        User user1 = createTestUser(id1, "John Lennon", "john@mail.com");
        User user2 = createTestUser(id2, "Paul MacCartney", "paul@mail.com");
        List<User> expectedUsers = List.of(user1, user2);

        when(userRepository.findAll()).thenReturn(expectedUsers);

        List<User> result = userService.getAllUsers();

        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(user1, user2);
        verify(userRepository).findAll();
    }

    @Test
    void getUserById_shouldReturnUserWhenExists() {
        UUID id = UUID.randomUUID();
        User expectedUser = createTestUser(id, "John Lennon", "john@mail.com");

        when(userRepository.findById(id)).thenReturn(Optional.of(expectedUser));

        User result = userService.getUserById(id);

        assertThat(result).isEqualTo(expectedUser);
        verify(userRepository).findById(id);
    }

    @Test
    void getUserById_shouldThrowExceptionWhenUserNotFound() {
        UUID id = UUID.randomUUID();

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(id))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("User not found with id: " + id);

        verify(userRepository).findById(id);
    }

    @Test
    void searchUsersByName_shouldReturnFilteredUsers() {
        String searchTerm = "john";
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        User user1 = createTestUser(id1, "John Lennon", "john@mail.com");
        User user2 = createTestUser(id2, "Johnny Cash", "johnny@test.com");
        List<User> expectedUsers = List.of(user1, user2);

        when(userRepository.findByNameContainingIgnoreCase(searchTerm))
                .thenReturn(expectedUsers);

        List<User> result = userService.searchUsersByName(searchTerm);

        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(user1, user2);
        verify(userRepository).findByNameContainingIgnoreCase(searchTerm);
    }

    @Test
    void searchUsersByName_shouldReturnAllUsersWhenNameIsNull() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        User user1 = createTestUser(id1, "Paul MacCartney", "paul@mail.com");
        User user2 = createTestUser(id2, "Janis Joplin", "janis@test.com");
        List<User> expectedUsers = List.of(user1, user2);

        when(userRepository.findByNameContainingIgnoreCase(null))
                .thenReturn(expectedUsers);

        List<User> result = userService.searchUsersByName(null);

        assertThat(result).hasSize(2);
        verify(userRepository).findByNameContainingIgnoreCase(null);
    }
}
