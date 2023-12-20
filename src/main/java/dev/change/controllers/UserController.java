package dev.change.controllers;

import dev.change.beans.User;
import dev.change.services.authentication.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody String email, @RequestBody String password) {
        boolean authenticated = userRepository.authenticated(email, password);
        if (authenticated) {
            return ResponseEntity.ok().body("Authenticated");
        }
        User user = userRepository.login(email, password);
        if (user != null) {
            return ResponseEntity.ok().body(user);
        }
        return ResponseEntity.badRequest().body("Invalid credentials");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody String email, @RequestBody String password) {
        if (userRepository.authenticated(email, password)) {
            return ResponseEntity.badRequest().body("User already authenticated");
        }
        if (userRepository.existsByEmail(email)) {
            return ResponseEntity.badRequest().body("User already exists");
        }
        User user = userRepository.register(email, password);
        if (user != null) {
            return ResponseEntity.ok().body(user);
        }
        return ResponseEntity.badRequest().body("User already exists");
    }
}
