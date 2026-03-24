package cat.itacademy.s04.t01.userapi.controller;

import cat.itacademy.s04.t01.userapi.model.User;
import cat.itacademy.s04.t01.userapi.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.clear();
    }

    private String extractIdFromResponse(String responseJson) throws Exception {
        return objectMapper.readTree(responseJson).get("id").asText();
    }

    @Test
    void getUsers_returnsEmptyListInitially() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void createUser_returnsUserWithId() throws Exception {
        User newUser = new User();
        newUser.setName("Test User");
        newUser.setEmail("test@example.com");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void getUserById_returnsCorrectUser() throws Exception {
        User newUser = new User();
        newUser.setName("Ada Lovelace");
        newUser.setEmail("ada@example.com");

        MvcResult createResult = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isOk())
                .andReturn();

        String createdUserJson = createResult.getResponse().getContentAsString();
        String userId = extractIdFromResponse(createdUserJson);

        mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.name").value("Ada Lovelace"))
                .andExpect(jsonPath("$.email").value("ada@example.com"));
    }

    @Test
    void getUsers_withNameParam_returnsFilteredUsers() throws Exception {
        User ada = new User();
        ada.setName("Ada Lovelace");
        ada.setEmail("ada@example.com");

        User alan = new User();
        alan.setName("Alan Turing");
        alan.setEmail("alan@example.com");

        User grace = new User();
        grace.setName("Grace Hopper");
        grace.setEmail("grace@example.com");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ada)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(alan)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(grace)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/users").param("name", "la"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].name", hasItems("Ada Lovelace", "Alan Turing")));

        mockMvc.perform(get("/users").param("name", "ada"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Ada Lovelace"));

        mockMvc.perform(get("/users").param("name", "xyz"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void createUser_shouldReturn409WhenEmailAlreadyExists() throws Exception {
        User firstUser = new User();
        firstUser.setName("George Harrison");
        firstUser.setEmail("george@mail.com");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firstUser)))
                .andExpect(status().isOk());

        User duplicateUser = new User();
        duplicateUser.setName("Ringo Starr");
        duplicateUser.setEmail("george@mail.com");  // MATEIX email que el primer

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicateUser)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.message").value("Email already exists: george@mail.com"));
    }
}
