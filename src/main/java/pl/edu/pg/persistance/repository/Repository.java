package pl.edu.pg.persistance.repository;

import pl.edu.pg.persistance.PersistenceManager;
import pl.edu.pg.persistance.models.IModel;

import java.util.List;

public class Repository<T extends IModel> {

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

  public List<T> findAll(Class<T> entityClass) {
    return findAll(-1, entityClass);
  }

  public List<T> findAll(int limit, Class<T> entityClass) {
    var em = PersistenceManager.getEntityManager();
    var query = em.createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e", entityClass);
    if (limit > 0) {
      query = query.setMaxResults(limit);
    }
    return query.getResultList();
  }
}
