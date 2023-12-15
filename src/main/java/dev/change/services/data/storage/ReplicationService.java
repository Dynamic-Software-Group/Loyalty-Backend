package dev.change.services.data.storage;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.json.JSONObject;

import java.sql.*;

public class ReplicationService {
    private static Connection conn;

    public static void init() throws ClassNotFoundException, SQLException {
        final String url = "jdbc://185.240.134.120:3306/main";
        final String username = "admin";
        final String password = ""; // TODO: set password from discord

        Class.forName("com.mysql.jdbc.Driver");
        conn = DriverManager.getConnection(url, username, password);

        conn.createStatement().execute("CREATE TABLE IF NOT EXISTS data (email TEXT PRIMARY KEY, data JSON)");
    }

    public static void insertData(String email, @NotNull JSONObject data) throws SQLException {
        final PreparedStatement statement = conn.prepareStatement("INSERT INTO data (email, data) VALUES (?, ?)");
        statement.setString(1, email);
        statement.setString(2, data.toString());

        statement.execute();
    }

    public static @Nullable @Unmodifiable Object getData(String email) throws SQLException {
        ResultSet resultSet = conn.createStatement().executeQuery("SELECT data FROM data WHERE email = '" + email + "'");
        if (resultSet.next()) {
            return resultSet.getString("data");
        } else {
            return null;
        }
    }

}
