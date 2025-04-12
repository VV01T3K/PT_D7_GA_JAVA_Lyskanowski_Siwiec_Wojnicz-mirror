package pl.edu.pg.persistance;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class PersistenceManager {
  private static final EntityManagerFactory factory = Persistence.createEntityManagerFactory("lab5-persist");
  private static final EntityManager em = factory.createEntityManager();

  public static EntityManager getEntityManager() {
    return em;
  }
}
