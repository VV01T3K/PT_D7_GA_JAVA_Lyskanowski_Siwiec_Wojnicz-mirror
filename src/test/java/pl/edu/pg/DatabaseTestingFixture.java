package pl.edu.pg;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.MockedStatic;
import pl.edu.pg.persistance.PersistenceManager;

import static org.mockito.Mockito.mockStatic;

public class DatabaseTestingFixture implements BeforeEachCallback, AfterEachCallback {
  private static EntityManager em;
  private static MockedStatic<PersistenceManager> mock;


  @Override
  public void afterEach(ExtensionContext context) throws Exception {
    if (em != null) {
      em.clear();
      em.close();
    }
    if (mock != null) {
      mock.close();
    }
  }

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    em = new TestEntityManagerFactory().createEntityManager();
    mock = mockStatic(PersistenceManager.class);
    mock.when(PersistenceManager::getEntityManager).thenReturn(em);
  }


  private static class TestEntityManagerFactory {

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("test-pu");

    public EntityManager createEntityManager() {
      return emf.createEntityManager();
    }
  }

}
