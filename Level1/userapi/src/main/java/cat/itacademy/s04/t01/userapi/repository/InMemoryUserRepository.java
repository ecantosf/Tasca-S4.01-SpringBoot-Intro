package cat.itacademy.s04.t01.userapi.repository;

import cat.itacademy.s04.t01.userapi.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserRepository implements UserRepository {

    private final List<User> users = new ArrayList<>();

    @Override
    public User save(User user) {
        users.add(user);
        return user;
    }

    @Override
    public List<User> findAll() {
        // Còpia defensiva per evitar modificacions externes
        return new ArrayList<>(users);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<User> findByNameContainingIgnoreCase(String name) {
        if (name == null || name.trim().isEmpty()) {
            return findAll();
        }
        return users.stream()
                .filter(user -> user.getName().toLowerCase()
                        .contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByEmail(String email) {
        return users.stream()
                .anyMatch(user -> user.getEmail().equalsIgnoreCase(email));
    }

    @Override
    public void clear() {
        users.clear();
    }
}