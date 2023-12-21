package dev.change.services.authentication.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.change.beans.User;
import dev.change.controllers.SecretHandler;
import dev.change.services.authentication.UserRepository;
import dev.change.services.data.impl.RedisRepositoryImpl;

import java.lang.reflect.Field;
import java.util.*;

import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserRepositoryImpl extends RedisRepositoryImpl<User, String> implements UserRepository {
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public User findByEmail(String email) {
        Set<String> users = jedis.keys("users:*"); // * is email
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
        user.setJwt(encodeJwt(user));
        user.setId(SecretHandler.genId());
        save(user);
        jedis.set("authenticated:" + user.getId(), user.getJwt());
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
        User user = findByEmail(email);
        user.setJwt(encodeJwt(user));
        save(user);
        jedis.set("authenticated:" + user.getId(), user.getJwt());
        return user;
    }

    @Override
    public User decodeJwt(String jwt) {
        DecodedJWT decodedJWT = JWT.decode(jwt);
        return decodedJWT.getClaim("user").as(User.class);
    }

    private String encodeJwt(User user) {
        ObjectMapper mapper = new ObjectMapper();
        Algorithm algorithm = Algorithm.HMAC256(System.getenv("JWT_SECRET"));
        String userJson;
        try {
            userJson = mapper.writeValueAsString(user);
        } catch (Exception e) {
            return null;
        }

        Date expiresAt = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24); // 24 hours

        return JWT.create()
            .withSubject(user.getEmail())
            .withExpiresAt(expiresAt)
            .withClaim("user", userJson)
            .sign(algorithm);
    }

    @Override
    public boolean hasAuthority(String jwt, String authority) {
        DecodedJWT decodedJWT = JWT.decode(jwt);
        List<String> authorities = decodedJWT.getClaim("authorities").asList(String.class);
        return authorities.contains(authority);
    }

    @Override
    public boolean verifyJwt(String jwt) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(System.getenv("JWT_SECRET"));
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(jwt);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    @Override
    public void logout(String jwt) {
        DecodedJWT decodedJWT = JWT.decode(jwt);
        User user = decodedJWT.getClaim("user").as(User.class);
        user.setJwt(null);
        save(user);
        Set<String> authenticated = jedis.keys("authenticated:*");
        for (String key : authenticated) {
            if (key.contains(decodedJWT.toString())) {
                jedis.del(key);
            }
        }
    }

    @Override
    public User update(@NotNull User user) {
        String userJson = jedis.get("users:" + user.getId());
        ObjectMapper mapper = new ObjectMapper();
        User currentUser;
        try {
            currentUser = mapper.readValue(userJson, User.class);
        } catch (Exception e) {
            return null;
        }

        Field[] fields = user.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                if (field.get(user) != null) {
                    field.set(currentUser, field.get(user));
                }
            } catch (Exception e) {
                return null;
            }
        }

        save(currentUser);
        return user;
    }

    @Override
    public boolean authenticated(String email, String password) {
        Set<String> users = jedis.keys("authenticated:*"); // * is id
        for (String user : users) {
            String jwt = jedis.get(user);
            User u = decodeJwt(jwt);
            if (u.getEmail().equals(email) && checkPassword(email, password) && verifyJwt(jwt)) {
                return true;
            }
        }
        return false;
    }
}
