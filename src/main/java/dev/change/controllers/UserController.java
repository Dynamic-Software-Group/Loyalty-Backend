package dev.change.controllers;

import dev.change.beans.User;
import dev.change.services.authentication.UserRepository;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody @NotNull AuthRequest request, @RequestHeader String api) {
        if (SecretHandler.notValid(api)) {
            return ResponseEntity.badRequest().body("Invalid API key");
        }
        String jwt = userRepository.authenticate(request.email, request.password);
        if (jwt != null) {
            return ResponseEntity.ok().body(jwt);
        }
        return ResponseEntity.badRequest().body("Invalid credentials");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @NotNull AuthRequest request, @RequestHeader String api) {
        if (SecretHandler.notValid(api)) {
            return ResponseEntity.badRequest().body("Invalid API key");
        }
        if (userRepository.existsByEmail(request.email)) {
            return ResponseEntity.badRequest().body("User already exists");
        }
        User user = new User();
        user.setEmail(request.email);
        user.setPassword(request.password);
        String jwt = userRepository.set(user);
        if (jwt != null) {
            return ResponseEntity.ok().body(jwt);
        }
        return ResponseEntity.badRequest().body("User already exists");
    }

    @GetMapping("/get")
    public ResponseEntity<?> getUser(@RequestParam String id, @RequestHeader String api) {
        if (SecretHandler.notValid(api)) {
            return ResponseEntity.badRequest().body(Optional.empty());
        }
        User user = userRepository.lookup(id);
        if (user != null) {
            return ResponseEntity.ok().body(Optional.of(user));
        }
        return ResponseEntity.badRequest().body("Doesn't exist");
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody User user, @RequestHeader String api) {
        if (SecretHandler.notValid(api)) {
            return ResponseEntity.badRequest().body("Invalid API key");
        }
        if (userRepository.existsById(user.getId())) {
            userRepository.set(user);
            return ResponseEntity.ok().body(user);
        }
        return ResponseEntity.badRequest().body("User doesn't exist");
    }

    @PostMapping("/points/add/{business_id}")
    public ResponseEntity<?> addPoints(@RequestBody String jwt, @PathVariable String business_id, @RequestHeader String api) {
        if (SecretHandler.notValid(api)) {
            return ResponseEntity.badRequest().body("Invalid API key");
        }
        if (userRepository.authenticated(jwt)) {
            User user = userRepository.lookup(userRepository.getId(jwt));
            user.addPoints(business_id, 1);
            userRepository.set(user);
            return ResponseEntity.ok().body("OK");
        }
        return ResponseEntity.badRequest().body("Invalid JWT");
    }

    @PostMapping("/points/add/{value}/{business_id}")
    public ResponseEntity<?> addPoints(@RequestBody String jwt, @PathVariable int value, @PathVariable String business_id, @RequestHeader String api) {
        if (SecretHandler.notValid(api)) {
            return ResponseEntity.badRequest().body("Invalid API key");
        }
        if (userRepository.authenticated(jwt)) {
            User user = userRepository.lookup(jwt);
            user.addPoints(business_id, value);
            userRepository.set(user);
            return ResponseEntity.ok().body("OK");
        }
        return ResponseEntity.badRequest().body("Invalid JWT");
    }

    @PostMapping("/points/set/{value}/{business_id}")
    public ResponseEntity<?> setPoints(@RequestBody String jwt, @PathVariable int value, @PathVariable String business_id, @RequestHeader String api) {
        if (SecretHandler.notValid(api)) {
            return ResponseEntity.badRequest().body("Invalid API key");
        }
        if (userRepository.authenticated(jwt)) {
            User user = userRepository.lookup(jwt);
            user.setPoints(business_id, value);
            userRepository.set(user);
            return ResponseEntity.ok().body("OK");
        }
        return ResponseEntity.badRequest().body("Invalid JWT");
    }

    @DeleteMapping("/points/reset/{business_id}")
    public ResponseEntity<?> resetPoints(@RequestBody String jwt, @PathVariable String business_id, @RequestHeader String api) {
        if (SecretHandler.notValid(api)) {
            return ResponseEntity.badRequest().body("Invalid API key");
        }
        if (userRepository.authenticated(jwt)) {
            User user = userRepository.lookup(jwt);
            user.setPoints(business_id, 0);
            userRepository.set(user);
            return ResponseEntity.ok().body("OK");
        }
        return ResponseEntity.badRequest().body("Invalid JWT");
    }

    @DeleteMapping("/points/remove/{value}/{business_id}")
    public ResponseEntity<?> removePoints(@RequestBody String jwt, @PathVariable int value, @PathVariable String business_id, @RequestHeader String api) {
        if (SecretHandler.notValid(api)) {
            return ResponseEntity.badRequest().body("Invalid API key");
        }
        if (userRepository.authenticated(jwt)) {
            User user = userRepository.lookup(jwt);
            user.removePoints(business_id, value);
            userRepository.set(user);
            return ResponseEntity.ok().body("OK");
        }
        return ResponseEntity.badRequest().body("Invalid JWT");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestBody String jwt, @RequestHeader String api) {
        if (SecretHandler.notValid(api)) {
            return ResponseEntity.badRequest().body("Invalid API key");
        }
        if (userRepository.authenticated(jwt)) {
            userRepository.delete(jwt);
            return ResponseEntity.ok().body("OK");
        }
        return ResponseEntity.badRequest().body("Invalid JWT");
    }

    @AllArgsConstructor
    public static class AuthRequest {
        public String email;
        public String password;
    }
}
