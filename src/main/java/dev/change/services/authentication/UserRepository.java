package dev.change.services.authentication;

import dev.change.beans.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository {
    String set(User user); // returns jwt
    String authenticate(String email, String password); // returns jwt
    String getId(String jwt); // returns id
    boolean hasAuthority(String jwt, String authority);
    boolean logout(String jwt);
    boolean existsByEmail(String email);
    boolean existsById(String id);
    User lookup(String id);
    boolean authenticated(String jwt);
    boolean delete(String jwt);
}
