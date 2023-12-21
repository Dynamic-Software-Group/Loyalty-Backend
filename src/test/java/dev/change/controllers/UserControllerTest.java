package dev.change.controllers;

import dev.change.beans.User;
import dev.change.services.authentication.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.when;

class UserControllerTest {
    @InjectMocks
    UserController userController;

    @Mock
    UserRepository userRepository;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser() {
        String email = "test";
        String password = "test";

        User expected = new User();
        expected.setEmail(email);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);
        expected.setPassword(hashedPassword);

        when(userRepository.register(email, password)).thenReturn(expected);

        UserController.AuthRequest request = new UserController.AuthRequest(email, password);
        ResponseEntity<?> response = userController.register(request, "test");

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode()); // Checks if the status code is 200
        Assertions.assertEquals(expected, response.getBody()); // Checks if the body is the expected user

        ResponseEntity<?> invalidApi = userController.register(request, "invalid");

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, invalidApi.getStatusCode()); // Checks if the status code is 400
        Assertions.assertEquals("Invalid API key", invalidApi.getBody()); // Checks if the body is the expected error message
    }

}
