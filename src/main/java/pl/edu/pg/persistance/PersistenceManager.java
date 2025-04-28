package pl.edu.pg.persistance;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class PersistenceManager {
  private static Instance instance;

  public static EntityManager getEntityManager() {
    if (instance == null) {
      instance = new Instance();
    }
    return instance.em;
  }

  private static class Instance {
    private final EntityManagerFactory factory = Persistence.createEntityManagerFactory("lab5-persist");
    private final EntityManager em = factory.createEntityManager();
  }

}
