package pl.edu.pg.persistance.builders;

import pl.edu.pg.persistance.models.Czlowiek;
import pl.edu.pg.persistance.models.Hierarchia;
import pl.edu.pg.persistance.repository.CzlowiekRepository;

import java.util.Scanner;

public class HierarchiaBuilder implements IBuilder<Hierarchia> {
  Czlowiek przelozony;
  Czlowiek podwladny;

  @Override
  public IBuilder<Hierarchia> buildInteractive(Scanner scanner) {
    System.out.println("Podaj imie i nazwisko przelozonego:");
    String nazwaPrzelozonego = scanner.nextLine();
    System.out.println("Podaj imie i nazwisko podwladnego:");
    String nazwaPodwladnego = scanner.nextLine();
    System.out.println("Podaj nazwe firmy w ktorej pracuja:");
    String nazwaFirmy = scanner.nextLine();
    CzlowiekRepository czlowiekRepository = new CzlowiekRepository();
    przelozony = czlowiekRepository.findByFullNameAndFirma(nazwaPrzelozonego, nazwaFirmy);
    podwladny = czlowiekRepository.findByFullNameAndFirma(nazwaPodwladnego, nazwaFirmy);
    return this;
  }

  @Override
  public Hierarchia build() {
    return new Hierarchia(przelozony, podwladny);
  }
}
