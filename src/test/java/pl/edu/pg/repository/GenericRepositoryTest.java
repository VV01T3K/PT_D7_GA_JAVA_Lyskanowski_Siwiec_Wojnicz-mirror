package pl.edu.pg.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import pl.edu.pg.DatabaseTestingFixture;
import pl.edu.pg.persistance.models.Firma;
import pl.edu.pg.persistance.repository.FirmaRepository;
import pl.edu.pg.persistance.repository.Repository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(DatabaseTestingFixture.class)
public class GenericRepositoryTest {

  private Repository<Firma> repository;

  @BeforeEach
  public void createRepository() {
    repository = new FirmaRepository();
  }

  @Test
  void deleteById_Success() {
    Firma firm = repository.save(new Firma("TestCompany"));
    repository.delete(firm);
    Optional<Firma> foundFirm = repository.findById(1);
    assertFalse(foundFirm.isPresent());
  }

  @Test
  void deleteById_DoesntExist() {
    Firma firm = new Firma("TestCompany");
    assertThrows(IllegalArgumentException.class, () -> repository.delete(firm));
  }

  @Test
  void testSave_AlreadyExists() {
    Firma firm1 = repository.save(new Firma("TestCompany"));
    assertThrows(IllegalArgumentException.class, () -> repository.save(new Firma("TestCompany", firm1.getId())));
  }

  @Test
  void testFindById_Exists() {
    Firma firm = repository.save(new Firma("TestCompany"));
    Optional<Firma> foundFirm = repository.findById(1);
    assertTrue(foundFirm.isPresent());
    assertEquals(firm, foundFirm.get());
  }

  @Test
  void testFindById_DoestExist() {
    Optional<Firma> foundFirm = repository.findById(1);
    assertFalse(foundFirm.isPresent());
  }

}
