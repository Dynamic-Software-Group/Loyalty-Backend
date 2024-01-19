package dev.change.services.authentication;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.BeanUtil;
import dev.change.beans.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Set;

@Service
public class RedisUserService implements UserRepository {
    private final PasswordEncoder passwordEncoder;
    private static RedisTemplate<String, String> redisTemplate = null;
    private final ObjectMapper mapper;

    public RedisUserService(RedisTemplate<String, String> redisTemplate, ObjectMapper mapper) {
        RedisUserService.redisTemplate = redisTemplate;
        this.mapper = mapper;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public String set(@NotNull User user) {
        String userJson;
        try {
            String jwt = user.getJwt();
            if (!(jwt == null)) {
                if (getId(user.getJwt()) != null) {
                    String id = getId(user.getJwt());
                    User oldUser = getUser(id);
                    User newUser = new User();

                    if (oldUser == null) {
                        return null;
                    }

                    BeanUtils.copyProperties(oldUser, newUser);
                    BeanUtils.copyProperties(user, newUser);
                    user = newUser;
                    user.setId(id);
                }
            }
            userJson = mapper.writeValueAsString(user);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        String id = user.getId();
        redisTemplate.opsForValue().set("users:" + id, userJson);
        return authenticate(user.getEmail(), user.getPassword());
    }

    @Override
    public String authenticate(String email, String password) {
        System.out.println("Authenticating");
        System.out.println(email + " " + password);
        try {
            User user = getUserByEmail(email);
            if (user == null) {
                return null;
            }
            Set<String> keys = redisTemplate.keys("authenticated:*");
            assert keys != null;
            for (String key : keys) {
                String id = redisTemplate.opsForValue().get(key);
                if (Objects.equals(id, user.getId())) {
                    return key.split(":")[1];
                }
            }
            String id = user.getId();
            String jwt = JwtService.generateJwt(id);
            if (Objects.equals(password, user.getPassword())) {
                redisTemplate.opsForValue().set("authenticated:" + jwt, id);
                return jwt;
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private @Nullable User getUserByEmail(String email) {
        Set<String> keys = redisTemplate.keys("users:*");
        assert keys != null;
        for (String key : keys) {
            String id = key.split(":")[1];
            User user;
            try {
                user = getUser(id);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            if (user != null && user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
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
            redisTemplate.delete("authenticated:" + jwt);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        try {
            User user = getUserByEmail(email);
            return user != null;
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

    private @Nullable User getUser(String id) {
        Set<String> keys = redisTemplate.keys("users:*");
        assert keys != null;
        String json = redisTemplate.opsForValue().get("users:" + id);
        if (json != null) {
            try {
                return mapper.readValue(json, User.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println(id);
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
        return Boolean.TRUE.equals(redisTemplate.hasKey("authenticated:" + jwt));
    }

    @Override
    public boolean delete(String jwt) {
        try {
            Set<String> keys = redisTemplate.keys("users:*");
            assert keys != null;
            for (String key : keys) {
                String userJson = redisTemplate.opsForValue().get(key);
                JsonNode jsonNode = mapper.readTree(userJson);
                if (jsonNode.get("jwt").asText().equals(jwt)) {
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

    public static void logoutAll() {
        Set<String> keys = redisTemplate.keys("authenticated:*");
        assert keys != null;
        for (String key : keys) {
            redisTemplate.delete(key);
        }
    }
}
