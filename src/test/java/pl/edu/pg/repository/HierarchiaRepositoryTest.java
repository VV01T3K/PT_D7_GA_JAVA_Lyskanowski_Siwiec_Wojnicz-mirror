package pl.edu.pg.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import pl.edu.pg.DatabaseTestingFixture;
import pl.edu.pg.persistance.models.Czlowiek;
import pl.edu.pg.persistance.models.Firma;
import pl.edu.pg.persistance.models.Hierarchia;
import pl.edu.pg.persistance.repository.CzlowiekRepository;
import pl.edu.pg.persistance.repository.FirmaRepository;
import pl.edu.pg.persistance.repository.HierarchiaRepository;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(DatabaseTestingFixture.class)
public class HierarchiaRepositoryTest {
  private HierarchiaRepository hierarchiaRepository;
  private CzlowiekRepository czlowiekRepository;
  private FirmaRepository firmaRepository;

  @BeforeEach
  public void createRepository() {
    hierarchiaRepository = new HierarchiaRepository();
    czlowiekRepository = new CzlowiekRepository();
    firmaRepository = new FirmaRepository();

  }

  @Test
  void testFindByPodwladni_ExistsMany() {
    var firma = firmaRepository.save(new Firma("TestCompany"));
    var podwladny = czlowiekRepository.save(new Czlowiek("Jan", "Kowalski", "123456789", null, null, null, null, firma));
    var przelozony = czlowiekRepository.save(new Czlowiek("Adam", "Nowak", "987654321", null, null, null, null, firma));
    var przelozony2 = czlowiekRepository.save(new Czlowiek("Adam", "Nowak", "987654321", null, null, null, null, firma));
    var hierarchia = hierarchiaRepository.save(new Hierarchia(przelozony, podwladny));
    var hierarchia2 = hierarchiaRepository.save(new Hierarchia(przelozony2, podwladny));
    var foundHierarchia = hierarchiaRepository.fidnByPodwladny(podwladny);
    assertFalse(foundHierarchia.isEmpty());
    assertEquals(2, foundHierarchia.size());
    assertEquals(hierarchia, foundHierarchia.get(0));
    assertEquals(hierarchia2, foundHierarchia.get(1));
  }

  @Test
  void testFindByPodwladni_ExistsOne() {
    var firma = firmaRepository.save(new Firma("TestCompany"));
    var podwladny = czlowiekRepository.save(new Czlowiek("Jan", "Kowalski", "123456789", null, null, null, null, firma));
    var przelozony = czlowiekRepository.save(new Czlowiek("Adam", "Nowak", "987654321", null, null, null, null, firma));
    var hierarchia = hierarchiaRepository.save(new Hierarchia(przelozony, podwladny));
    var foundHierarchia = hierarchiaRepository.fidnByPodwladny(podwladny);
    assertFalse(foundHierarchia.isEmpty());
    assertEquals(1, foundHierarchia.size());
    assertEquals(hierarchia, foundHierarchia.getFirst());
  }

  @Test
  void testFindByPodwladni_DoesntHavePodwladny() {
    var czlowiek = czlowiekRepository.save(new Czlowiek("Jan", "Kowalski", "123456789", null, null, null, null, null));
    var foundHierarchia = hierarchiaRepository.fidnByPodwladny(czlowiek);
    assertTrue(foundHierarchia.isEmpty());
  }
}
