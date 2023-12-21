package dev.change.services.data.impl;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.change.services.data.DatabaseConfig;
import dev.change.services.data.Identifiable;
import dev.change.services.data.RedisRepository;
import org.jetbrains.annotations.NotNull;

import redis.clients.jedis.Jedis;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings("unchecked")
public class RedisRepositoryImpl<T extends Identifiable<ID>, ID> implements RedisRepository<T, ID> {
    // private final DatabaseConfig databaseConfig = new DatabaseConfig();
    // public final Jedis jedis = databaseConfig.jedis();
    public final Jedis jedis = null;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<ID, Class<? extends T>> idClassMap = new HashMap<>();

    @NotNull
    @Override
    public <S extends T> S save(@NotNull S entity) {
        String jsonStr = entity.toString();
        jedis.set(entity.getId().toString(), jsonStr);
        idClassMap.put((ID) (entity.getClass().getCanonicalName() + entity.getId().toString()), (Class<? extends T>) entity.getClass());
        return entity;
    }

    @NotNull
    @Override
    public <S extends T> Iterable<S> saveAll(@NotNull Iterable<S> entities) {
        for (S entity : entities) {
            save(entity);
        }
        return entities;
    }

    @NotNull
    @Override
    public Optional<T> findById(@NotNull ID id) {
        Class<? extends T> clazz = idClassMap.get(id);
        String jsonStr = jedis.get(id.toString());
        T entity = objectMapper.convertValue(jsonStr, clazz);
        entity.setId(id);
        return Optional.of(entity);
    }

    @Override
    public boolean existsById(@NotNull ID id) {
        return jedis.exists(id.toString());
    }

    /**
     * This method is deprecated because it is not possible to implement it with Redis.
     * @return null
     * @deprecated
     */
    @Deprecated
    @Override
    public @NotNull Iterable<T> findAll() {
        return Collections.emptyList();
    }

    @Override
    public Iterable<T> findAllById(@NotNull Iterable<ID> ids) {
        return null;
    }

    @Override
    public long count() {
        return jedis.dbSize();
    }

    @Override
    public void deleteById(@NotNull ID id) {
        jedis.del(id.toString());
    }

    @Override
    public void delete(@NotNull T entity) {
        deleteById(entity.getId());
    }

    @Override
    public void deleteAllById(@NotNull Iterable<? extends ID> ids) {
        for (ID id : ids) {
            deleteById(id);
        }
    }

    @Override
    public void deleteAll(@NotNull Iterable<? extends T> entities) {
        for (T entity : entities) {
            delete(entity);
        }
    }

    /**
     * DANGEROUS METHOD! DO NOT USE WITHOUT EXPLICIT REASONING!
     */
    @Override
    public void deleteAll() {
        jedis.flushDB();
        //TODO: before flushing, backup the entire dataset to mysql
    }

    @Override
    public void replicate() {
        //todo: replicate to the sql database
    }
}
