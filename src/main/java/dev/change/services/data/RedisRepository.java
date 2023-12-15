package dev.change.services.data;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface RedisRepository<T, ID> extends CrudRepository<T, ID> {
    <S extends T> @NotNull S save(@NotNull S entity);
    <S extends T> @NotNull Iterable<S> saveAll(@NotNull Iterable<S> entities);
    @NotNull Optional<T> findById(@NotNull ID id);
    boolean existsById(@NotNull ID id);
    @NotNull Iterable<T> findAllById(@NotNull Iterable<ID> ids);
    long count();
    void deleteById(@NotNull ID id);
    void delete(@NotNull T entity);
    void deleteAllById(@NotNull Iterable<? extends ID> ids);
    void deleteAll(@NotNull Iterable<? extends T> entities);
    void deleteAll();
    void replicate();
}
