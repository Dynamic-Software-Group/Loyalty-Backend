package dev.change.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.change.services.authentication.JwtService;
import dev.change.services.authentication.UserRepository;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    UserController userController;

    @Mock
    UserRepository userRepository;

    @Autowired
    ObjectMapper mapper;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    String jwt;

    @Test
    void register() {
        String uri = "/users/register";
        JSONObject request = new JSONObject();
        request.put("email", "mock@gmail.com");
        request.put("password", "mock");
        try {
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(uri)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(request.toString())
                    .header("api", "test"))
                    .andReturn();
            jwt = result.getResponse().getContentAsString();
            Assertions.assertEquals(200, result.getResponse().getStatus());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void logout() {
        String uri = "/users/logout";
        try {
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(uri)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jwt)
                    .header("api", "test"))
                    .andReturn();
            Assertions.assertEquals(200, result.getResponse().getStatus());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void authenticate() {
        String uri = "/users/authenticate";
        JSONObject request = new JSONObject();
        request.put("email", "mock@gmail.com");
        request.put("password", "mock");
        try {
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(uri)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(request.toString())
                    .header("api", "test"))
                    .andReturn();
            jwt = result.getResponse().getContentAsString();
            Assertions.assertEquals(200, result.getResponse().getStatus());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void update() {
        String uri = "/users/update";
        JSONObject request = new JSONObject();
        request.put("email", "updated@gmail.com");
        request.put("password", "updated");
        try {
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(uri)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(request.toString())
                    .header("api", "test"))
                    .andReturn();
            Assertions.assertEquals(200, result.getResponse().getStatus());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void get() {
        String uri = "/users/get";
        try {
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(uri)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("jwt", jwt))
                    .andReturn();
            Assertions.assertEquals(200, result.getResponse().getStatus());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
