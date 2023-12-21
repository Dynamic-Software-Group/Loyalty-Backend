package dev.change.services.data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import redis.clients.jedis.JedisPool;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {
    private final String redisHost = "localhost";

    private final int redisPort = 6379;

    private final String redisPassword = "test"; //TODO: Update

    private final String sqlHost = "localhost";

    private final int sqlPort = 3306;

    private final String sqlUsername = "root";

    private final String sqlPassword = "test"; //TODO: Update

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://" + sqlHost + ":" + sqlPort + "/change");
        dataSource.setUsername(sqlUsername);
        dataSource.setPassword(sqlPassword);
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }

    @Bean
    public JedisPool jedisPool() {
        return new JedisPool(redisHost, redisPort);
    }
}
