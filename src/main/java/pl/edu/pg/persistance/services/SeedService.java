package pl.edu.pg.persistance.services;

import pl.edu.pg.Czlowiek;
import pl.edu.pg.Plec;
import pl.edu.pg.TestRepoGenerator;
import pl.edu.pg.persistance.models.Firma;
import pl.edu.pg.persistance.models.Hierarchia;
import pl.edu.pg.persistance.models.Pracownik;
import pl.edu.pg.persistance.repository.CzlowiekRepository;
import pl.edu.pg.persistance.repository.FirmaRepository;
import pl.edu.pg.persistance.repository.HierarchiaRepository;

public class SeedService {
  private final static String[] dostepneFirmy = {"Januszex", "OutPost", "MediaNoob", "TransPol"};
  private final TestRepoGenerator generator = new TestRepoGenerator();
  private final CzlowiekRepository czlowiekRepository = new CzlowiekRepository();
  private final FirmaRepository firmaRepository = new FirmaRepository();
  private final HierarchiaRepository hierarchiaRepository = new HierarchiaRepository();

  public Pracownik createCzlowiekInDatabase(Czlowiek czlowiek, Firma firma) {
    if (firma == null) {
      String randomFirma = dostepneFirmy[(int) (Math.random() * dostepneFirmy.length)];
      var foundFirma = firmaRepository.findByName(randomFirma);
      firma = foundFirma.orElseGet(() -> new Firma(randomFirma));
    }
    Pracownik pracownikModel = new Pracownik(
            czlowiek.getImie(),
            czlowiek.getNazwisko(),
            czlowiek.getNumerTelefonu(),
            czlowiek.getPlec() == Plec.MEZCZYZNA,
            czlowiek.getStanCywilny().toString(),
            czlowiek.getWyksztalcenie(),
            czlowiek.getPozycjaZawodowa(),
            firma
    );
    czlowiekRepository.save(pracownikModel);

    for (var podwladny : czlowiek.getPodlegli()) {
      var model = createCzlowiekInDatabase(podwladny, firma);
      Hierarchia hierarchiaModel = new Hierarchia(pracownikModel, model);
      hierarchiaRepository.save(hierarchiaModel);
    }
    return pracownikModel;
  }

  public void seed() {
    for (int i = 0; i < 4; i++) {
      var ludzie = generator.generateTestData(10);
      for (Czlowiek czlowiek : ludzie) {
        createCzlowiekInDatabase(czlowiek, null);
      }
    }
  }
}
