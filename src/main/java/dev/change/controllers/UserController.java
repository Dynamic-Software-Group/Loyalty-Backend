package dev.change.controllers;

import dev.change.beans.User;
import dev.change.services.authentication.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
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
    public ResponseEntity<?> authenticate(@RequestBody @NotNull AuthRequest request) {
        User user = userRepository.login(request.email, request.password);
        if (user != null) {
            return ResponseEntity.ok().body(user);
        }
        return ResponseEntity.badRequest().body("Invalid credentials");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @NotNull AuthRequest request) {
        if (userRepository.existsByEmail(request.email)) {
            return ResponseEntity.badRequest().body("User already exists");
        }
        User user = userRepository.register(request.email, request.password);
        if (user != null) {
            return ResponseEntity.ok().body(user);
        }
        return ResponseEntity.badRequest().body("User already exists");
    }

    @AllArgsConstructor
    public static class AuthRequest {
        public String email;
        public String password;
    }
}
