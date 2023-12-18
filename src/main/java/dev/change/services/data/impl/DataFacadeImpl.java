package dev.change.services.data.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.change.services.data.storage.DataFacade;
import dev.change.services.data.storage.PreparedQuery;
import dev.change.services.internal.events.ConfigCat;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

public class DataFacadeImpl<T> implements DataFacade<T> {
    private boolean redisEnabled = ConfigCat.getClient().getValue(Boolean.class, "redisEnabled", true);
    private final ObjectMapper mapper = new ObjectMapper();
    private final Class<T> clazz;
    private final Connection conn;

    {
        try {
            conn = DriverManager.getConnection("url", "username", System.getenv("MYSQL_PASSWORD"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private final JedisCluster redis = new JedisCluster(new HostAndPort("localhost", 6379)); //TODO: Change the redis url

    public DataFacadeImpl(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T getData(PreparedQuery query) {
        try {
            return mapper.readValue(jsonData(query), clazz);
        } catch (Exception e) {
            return null;
        }
    }

    public void checkDatabases() {
        boolean redisEnabled = pingRedis();
        boolean mySQLEnabled = pingMySQL();
        boolean configCatEnabled = ConfigCat.getClient().getValue(Boolean.class, "configCatEnabled", true);

        if (!mySQLEnabled && !redisEnabled) {
            System.err.println("Both redis and mysql are down. Exiting..."); //TODO: Implement logging facade
            System.exit(1);
        }

        this.redisEnabled = redisEnabled && configCatEnabled;
    }
    private boolean pingRedis() {
        try (Jedis jedis = new Jedis("localhost")) { //TODO: Change the redis url
            return jedis.ping().equals("PONG");
        }
    }

    private boolean pingMySQL() {
        String url = "jdbc:mysql://localhost:3306/"; //TODO: Change the mysql url
        String username = "username"; //TODO: Change
        String password = System.getenv("MYSQL_PASSWORD"); //TODO: Set

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            return conn.isValid(2);
        } catch (Exception e) {
            return false;
        }
    }

    @Contract(pure = true)
    private @Nullable String jsonData(PreparedQuery preQuery) {
        String query = redisEnabled ? preQuery.toRedis() : preQuery.toSql();
        if (redisEnabled) {
            return redis.get(query);
        } else {
            Statement stmt;
            try {
                stmt = conn.createStatement();
                stmt.executeQuery(query);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }
}   
