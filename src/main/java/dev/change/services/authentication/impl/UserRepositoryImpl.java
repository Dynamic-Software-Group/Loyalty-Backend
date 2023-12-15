package dev.change.services.authentication.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import dev.change.beans.User;
import dev.change.services.authentication.UserRepository;
import dev.change.services.data.impl.RedisRepositoryImpl;

import java.util.List;
import java.util.Map;
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

    @Override
    public Map<String, Object> decodeJwt(String jwt) {
        DecodedJWT decodedJWT = JWT.decode(jwt);
        String email = decodedJWT.getSubject();
        List<String> authorities = decodedJWT.getClaim("authorities").asList(String.class);
        return Map.of("email", email, "authorities", authorities);
    }

    @Override
    public boolean hasAuthority(String jwt, String authority) {
        DecodedJWT decodedJWT = JWT.decode(jwt);
        List<String> authorities = decodedJWT.getClaim("authorities").asList(String.class);
        return authorities.contains(authority);
    }

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
}
