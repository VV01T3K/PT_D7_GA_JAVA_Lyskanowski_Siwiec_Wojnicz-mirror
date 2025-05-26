package pl.edu.pg.persistance.services;

import pl.edu.pg.persistance.models.IModel;
import pl.edu.pg.persistance.repository.Repository;

import java.util.List;
import java.util.Optional;

public abstract class CrudService<T extends IModel> {
  protected final Repository<T> repository;

  public CrudService(Repository<T> repository) {
    this.repository = repository;
  }

  public T save(T entity) {
    repository.save(entity);
    return entity;
  }

  public Optional<T> findById(Integer id) {
    return repository.findById(id);
  }

  public List<T> findAll() {
    return repository.findAll();
  }

  public void delete(Integer id) {
    Optional<T> entity = repository.findById(id);
    if (entity.isPresent()) {
      repository.delete(entity.get());
    } else {
      throw new IllegalArgumentException("Entity not found");
    }
  }

  public void update(T entity) {
    Optional<T> existingEntity = repository.findById(entity.getId());
    if (existingEntity.isPresent()) {
      repository.update(entity);
    } else {
      throw new IllegalArgumentException("Entity not found");
    }
  }
}
