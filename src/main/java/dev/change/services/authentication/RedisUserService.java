package dev.change.services.authentication;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.change.beans.User;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;

@Service
public class RedisUserService implements UserRepository {
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper mapper;

    public RedisUserService(RedisTemplate<String, String> redisTemplate, ObjectMapper mapper) {
        this.redisTemplate = redisTemplate;
        this.mapper = mapper;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public String set(User user) {
        String userJson = null;
        try {
            userJson = mapper.writeValueAsString(user);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        redisTemplate.opsForValue().set("users:" + user.getId(), userJson);
        return authenticate(user.getEmail(), user.getPassword());
    }

    @Override
    public String authenticate(String email, String password) {
        try {
            String id = Objects.requireNonNull(getUser(email)).getId();
            String jwt = JwtService.generateJwt(id);
            if (passwordEncoder.matches(password, Objects.requireNonNull(getUser(email)).getPassword())) {
                redisTemplate.opsForValue().set("authenticated:" + id, jwt);
                return jwt;
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getId(String jwt) {
        try {
            User user = getUser(JWT.decode(jwt).getClaim("id").asString());
            if (user != null) {
                return user.getId();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public boolean hasAuthority(String jwt, String authority) {
        try {
            User user = getUser(JWT.decode(jwt).getClaim("id").asString());
            if (user != null) {
                return user.hasAuthority(authority);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    public boolean logout(String jwt) {
        try {
            User user = getUser(JWT.decode(jwt).getClaim("id").asString());
            if (user != null) {
                redisTemplate.delete("authenticated:" + user.getId());
            }
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        try {
            return getUser(email) != null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean existsById(String id) {
        try {
            return getUser(id) != null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private @Nullable User getUser(String id) throws Exception {
        Set<String> keys = redisTemplate.keys("users:*");
        assert keys != null;
        for (String key : keys) {
            String userJson = redisTemplate.opsForValue().get(key);
            JsonNode jsonNode = mapper.readTree(userJson);
            if (jsonNode.get("id").asText().equals(id)) {
                return mapper.readValue(userJson, User.class);
            }
        }
        return null;
    }

    @Override
    public User lookup(String id) {
        try {
            return getUser(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean authenticated(String jwt) {
        try {
            User user = getUser(JWT.decode(jwt).getClaim("id").asString());
            if (user != null) {
                return Objects.equals(redisTemplate.opsForValue().get("authenticated:" + user.getId()), jwt);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    public boolean delete(String jwt) {
        try {
            Set<String> keys = redisTemplate.keys("users:*");
            assert keys != null;
            for (String key : keys) {
                String userJson = redisTemplate.opsForValue().get(key);
                JsonNode jsonNode = mapper.readTree(userJson);
                if (jsonNode.get("id").asText().equals(jwt)) {
                    redisTemplate.delete(key);
                    logout(jwt);
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
