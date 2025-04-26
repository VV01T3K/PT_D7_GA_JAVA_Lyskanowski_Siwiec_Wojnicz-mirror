package pl.edu.pg.lab6.repo;

import java.util.List;
import java.util.Optional;

public interface IRepo <T, ID> {
    void save(T entity) throws IllegalArgumentException;
    Optional<T> findById(ID id) throws IllegalArgumentException;
    List<T> findAll() throws IllegalArgumentException;
    void deleteById(ID id) throws IllegalArgumentException;
}
