package pl.edu.pg.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import pl.edu.pg.DatabaseTestingFixture;
import pl.edu.pg.persistance.models.Firma;
import pl.edu.pg.persistance.repository.FirmaRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(DatabaseTestingFixture.class)
public class FirmaRepositoryTest {

  private FirmaRepository firmaRepository;

  @BeforeEach
  public void createRepository() {
    firmaRepository = new FirmaRepository();
  }

  @Test
  void testFindByName_Exists() {
    var firma = firmaRepository.save(new Firma("Test Company"));
    var foundFirma = firmaRepository.findByName("Test Company");
    assertTrue(foundFirma.isPresent());
    assertEquals(firma, foundFirma.get());
  }

  @Test
  void testFindByName_DoesntExist() {
    var foundFirma = firmaRepository.findByName("Test Company");
    assertTrue(foundFirma.isEmpty());
  }
}
