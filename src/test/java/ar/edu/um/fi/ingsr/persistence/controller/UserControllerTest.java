package ar.edu.um.fi.ingsr.persistence.controller;

import ar.edu.um.fi.ingsr.persistence.domain.User;
import ar.edu.um.fi.ingsr.persistence.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;  // cambio: import nuevo
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean                                                             // cambio: @MockBean → @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateUserWithName() throws Exception {
        Map<String, String> payload = new HashMap<>();
        payload.put("email", "test@example.com");
        payload.put("name", "John Doe");

        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("test@example.com");
        mockUser.setName("John Doe");

        when(userService.save(any(User.class))).thenReturn(mockUser);

        mockMvc.perform(post("/api/v1/db/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    public void testCreateUserValidationError() throws Exception {
        Map<String, String> payload = new HashMap<>();
        payload.put("email", "invalid-email");

        mockMvc.perform(post("/api/v1/db/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetUserByIdFound() throws Exception {
        User mockUser = new User();
        mockUser.setId(10L);
        mockUser.setEmail("user10@example.com");
        mockUser.setName("Alice");

        when(userService.findById(10L)).thenReturn(mockUser);

        mockMvc.perform(get("/api/v1/db/users/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$.email").value("user10@example.com"));
    }
}