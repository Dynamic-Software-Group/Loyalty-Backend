package dev.change.services.authentication.impl;

import dev.change.beans.User;
import dev.change.services.authentication.UserRepository;
import dev.change.services.data.impl.RedisRepositoryImpl;

import java.util.Set;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserRepositoryImpl extends RedisRepositoryImpl<User, String> implements UserRepository {
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public User findByEmail(String email) {
        Set<String> users = jedis.keys("users:*");
        for (String user : users) {
            User u = findById(user).isPresent() ? findById(user).get() : null;
            assert u != null;
            if (u.getUsername().equals(email)) {
                return u;
            }
        }
        return null;
    }

    @Override
    public boolean existsByEmail(String email) {
        return findByEmail(email) != null;
    }

    @Override
    public boolean checkPassword(String email, String password) {
        User user = findByEmail(email);
        return user != null && passwordEncoder.matches(password, user.getPassword());
    }

    @Override
    public User register(String email, String password) {
        if (existsByEmail(email)) {
            return null;
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        save(user);
        return user;
    }

    @Override
    public User login(String email, String password) {
        if (!existsByEmail(email)) {
            return null;
        }
        if (!checkPassword(email, password)) {
            return null;
        }
        return findByEmail(email);
    }
}
