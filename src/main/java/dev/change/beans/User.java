package dev.change.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.change.services.data.Identifiable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class User implements Identifiable<String> {
    private String id;
    private String username;
    private String password;
    private String email;
    private List<String> authorities;
    private String jwt;

    @Override
    @JsonIgnore
    public String getId() {
        return id;
    }
}
