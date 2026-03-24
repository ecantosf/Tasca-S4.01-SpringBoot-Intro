package cat.itacademy.s04.t01.userapi.service;

import cat.itacademy.s04.t01.userapi.model.User;
import cat.itacademy.s04.t01.userapi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
}
