package cat.itacademy.s04.t01.userapi.controller;

import cat.itacademy.s04.t01.userapi.model.User;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    private List<User> users = new ArrayList<>();

    @GetMapping
    public List<User> getUsers(@RequestParam(required = false) String name) {
        if (name == null || name.trim().isEmpty()) {
            return users;
        }
        return users.stream()
                .filter(user -> user.getName().toLowerCase()
                        .contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        UUID id = UUID.randomUUID();
        user.setId(id);
        users.add(user);
        return user;
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable String id) {
        UUID uuid = UUID.fromString(id);
        return users.stream()
                .filter(user -> user.getId().equals(uuid))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    void clearUsers() {
        users.clear();
    }
}
