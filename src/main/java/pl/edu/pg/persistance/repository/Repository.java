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

  public void save(T entity) {
    var em = PersistenceManager.getEntityManager();
    var transaction = em.getTransaction();
    transaction.begin();
    if (entity.getId() != null) {
      em.merge(entity);
    } else {
      em.persist(entity);
    }
    transaction.commit();
  }

  public void delete(T entity) {
    var em = PersistenceManager.getEntityManager();
    var transaction = em.getTransaction();
    transaction.begin();
    if (entity.getId() != null) {
      em.remove(em.contains(entity) ? entity : em.merge(entity));
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

  public Optional<T> findById(Integer id) {
    var em = PersistenceManager.getEntityManager();
    return Optional.ofNullable(em.find(entityClass, id));
  }

  public void deleteById(Integer id) {
    var em = PersistenceManager.getEntityManager();
    var transaction = em.getTransaction();
    transaction.begin();
    T entity = em.find(entityClass, id);
    if (entity != null) {
      em.remove(entity);
    }
    transaction.commit();
  }
}
