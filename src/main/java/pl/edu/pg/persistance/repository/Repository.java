package pl.edu.pg.persistance.repository;

import pl.edu.pg.persistance.PersistenceManager;
import pl.edu.pg.persistance.models.IModel;

import java.util.List;
import java.util.Optional;

public class Repository<T extends IModel> {
  private final Class<T> entityClass;

  Repository(Class<T> entityClass) {
    this.entityClass = entityClass;
  }

  public T save(T entity) throws IllegalArgumentException {
    var em = PersistenceManager.getEntityManager();
    var transaction = em.getTransaction();
    transaction.begin();
    if (entity.getId() == null) {
      em.persist(entity);
    } else if (em.contains(entity) || this.findById(entity.getId()).isPresent()) {
      throw new IllegalArgumentException("Entity exists");
    } else {
      entity = em.merge(entity);
    }
    transaction.commit();
    return entity;
  }

  public void delete(T entity) throws IllegalArgumentException {
    var em = PersistenceManager.getEntityManager();
    var transaction = em.getTransaction();
    transaction.begin();
    if (em.contains(entity)) {
      em.remove(entity);
    } else {
      throw new IllegalArgumentException("Entity does not exist");
    }
    transaction.commit();
  }

  public List<T> findAll() {
    return findAll(-1);
  }

  public List<T> findAll(int limit) {
    var em = PersistenceManager.getEntityManager();
    var query = em.createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e");
    if (limit > 0) {
      query = query.setMaxResults(limit);
    }
    return query.getResultList();
  }

  public Optional<T> findById(Object id) {
    var em = PersistenceManager.getEntityManager();
    return Optional.ofNullable(em.find(entityClass, id));
  }

  public void update(T entity) throws IllegalArgumentException {
    var em = PersistenceManager.getEntityManager();
    var transaction = em.getTransaction();
    transaction.begin();
    if (em.contains(entity)) {
      em.merge(entity);
    } else {
      throw new IllegalArgumentException("Entity does not exist");
    }
    transaction.commit();
  }
}
