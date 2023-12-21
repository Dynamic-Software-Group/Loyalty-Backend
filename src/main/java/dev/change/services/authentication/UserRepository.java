package dev.change.services.authentication;

import dev.change.beans.User;

import dev.change.services.data.RedisRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface UserRepository extends RedisRepository<User, String> {
    User findByEmail(String email);
    boolean existsByEmail(String email);
    boolean checkPassword(String email, String password);
    User register(String email, String password);
    User login(String email, String password);
    boolean authenticated(String email, String password);
    boolean verifyJwt(String jwt);
    User decodeJwt(String jwt);
    boolean hasAuthority(String jwt, String authority);
    void logout(String jwt);
    User update(User user);
}
