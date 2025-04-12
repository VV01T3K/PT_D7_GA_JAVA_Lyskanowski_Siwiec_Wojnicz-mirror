package pl.edu.pg.persistance.repository;

import pl.edu.pg.persistance.PersistenceManager;
import pl.edu.pg.persistance.models.IModel;

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
}
