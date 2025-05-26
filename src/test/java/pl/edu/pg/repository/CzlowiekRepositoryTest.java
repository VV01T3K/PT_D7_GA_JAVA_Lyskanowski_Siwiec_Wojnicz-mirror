package pl.edu.pg.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import pl.edu.pg.DatabaseTestingFixture;
import pl.edu.pg.persistance.models.Czlowiek;
import pl.edu.pg.persistance.models.Firma;
import pl.edu.pg.persistance.repository.CzlowiekRepository;
import pl.edu.pg.persistance.repository.FirmaRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith(DatabaseTestingFixture.class)
public class CzlowiekRepositoryTest {
  private CzlowiekRepository repository;
  private FirmaRepository firmaRepository;

  @BeforeEach
  public void createRepository() {
    repository = new CzlowiekRepository();
    firmaRepository = new FirmaRepository();
  }

  @Test
  void testFindByFullName_ExistsWithoutFirma() {
    var czlowiek = repository.save(new Czlowiek("Jan", "Kowalski", "123456789", null, null, null, null, null));
    var foundCzlowiek = repository.findByFullNameAndFirma("Jan Kowalski", "");
    assertTrue(foundCzlowiek.isPresent());
    assertEquals(czlowiek, foundCzlowiek.get());
  }

  @Test
  void testFindByFullName_ExistsWithFirma() {
    var firma = firmaRepository.save(new Firma("TestCompany"));
    var czlowiek = repository.save(new Czlowiek("Jan", "Kowalski", "123456789", null, null, null, null, firma));
    var foundCzlowiek = repository.findByFullNameAndFirma("Jan Kowalski", "TestCompany");
    assertTrue(foundCzlowiek.isPresent());
    assertEquals(czlowiek, foundCzlowiek.get());
  }

  @Test
  void testFindByFullName_ExistsWrongFirma() {
    var firma = firmaRepository.save(new Firma("TestCompany"));
    repository.save(new Czlowiek("Jan", "Kowalski", "123456789", null, null, null, null, firma));
    var foundCzlowiek = repository.findByFullNameAndFirma("Jan Kowalski", "WrongCompany");
    assertTrue(foundCzlowiek.isEmpty());
  }

  @Test
  void testFindByFullName_DoesntExist() {
    var foundCzlowiek = repository.findByFullNameAndFirma("Jan Kowalski", null);
    assertTrue(foundCzlowiek.isEmpty());
  }

}
