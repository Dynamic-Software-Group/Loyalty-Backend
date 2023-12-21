package dev.change.services.data.storage;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.*;

@Service
public class ReplicationService {
    private static JdbcTemplate jdbcTemplate = null;

    // @Autowired
    // public ReplicationService(JdbcTemplate jdbcTemplate) {
    //     ReplicationService.jdbcTemplate = jdbcTemplate;
    // }

    public static void insertData(String email, @NotNull JSONObject data) throws SQLException {
        String sql = "INSERT INTO data (email, data) VALUES (?, ?)";
        jdbcTemplate.update(
                con -> {
                    PreparedStatement ps = con.prepareStatement(sql);
                    ps.setString(1, email);
                    ps.setString(2, data.toString());
                    return ps;
                }
        );
    }

    public static @Unmodifiable @NotNull Object getData(String email) throws SQLException {
        String query = "SELECT data FROM data WHERE email = ?";
        return jdbcTemplate.query(
                con -> {
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, email);
                    return ps;
                },
                new BeanPropertyRowMapper<>(Object.class)
        );
    }

}
