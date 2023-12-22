package dev.change.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import dev.change.services.authentication.JwtService;
import dev.change.services.authentication.UserRepository;
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

import java.io.IOException;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    UserController userController;

    @Mock
    UserRepository userRepository;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUserFlow() throws Exception {
        String id  = "test";
        String email = "test";
        String password = "test";

        String expectedJwt = JwtService.generateJwt(id);
        String jsonPayload = "{\"email\":\"" + email + "\",\"password\":\"" + password + "\"}";

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload)
                .header("api", "test"))
                .andReturn();

        String response = result.getResponse().getContentAsString();
        System.out.println(response);
        assert(response.equals(expectedJwt));
    }

}
