package pl.edu.pg;

import net.datafaker.Faker;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestRepoGenerator {
  private final String[] wyksztalcenie = {
          "Podstawowe",
          "Zawodowe",
          "Srednie",
          "Wyzsze",
          "Doktoranckie",
          "Magisterskie"
  };
  private final Random rand = new Random();
  private final Faker faker = new Faker(Locale.of("pl"), rand);

  public Czlowiek generateCzlowiek() {
    String imie = faker.name().firstName();
    String nazwisko = faker.name().lastName();
    int wiek = faker.number().numberBetween(18, 80);
    Plec plec = imie.endsWith("a") ? Plec.KOBIETA : Plec.MEZCZYZNA;
    MartialStatus stanCywilny = MartialStatus.fromString(faker.demographic().maritalStatus());
    String wyksztalcenie = this.wyksztalcenie[(int) (Math.random() * this.wyksztalcenie.length)];
    String pozycjaZawodowa = faker.job().position();
    String numerTelefonu = faker.phoneNumber().cellPhoneInternational();
    return new Czlowiek(imie, nazwisko, wiek, plec, stanCywilny, wyksztalcenie, pozycjaZawodowa, numerTelefonu);
  }

  public ArrayList<Czlowiek> generateRandomCzlowiekList(int count) {
    return Stream.generate(
                    this::generateCzlowiek)
            .parallel()
            .limit(count)
            .collect(Collectors.toCollection(() -> new ArrayList<>(count)));
  }

  private void connectPeopleRecursively(
          Czlowiek czlowiek, ArrayList<Czlowiek> pool,
          int maxDepth,
          int maxConnections) {

    if (maxDepth == 0)
      return;
    int podlegliCount = faker.number().numberBetween(1, maxConnections);
    for (int i = 0; i < podlegliCount; i++) {
      if (pool.isEmpty())
        return;
      Czlowiek podlegly = pool.removeLast();
      connectPeopleRecursively(podlegly, pool, maxDepth - 1, maxConnections);
      czlowiek.dodajPodleglego(podlegly);
    }
  }

  public Set<Czlowiek> generateTestData(int count) {
    ArrayList<Czlowiek> pool = generateRandomCzlowiekList(count);
    Set<Czlowiek> heads = CzlowiekContainerFactory.chooseSet();

    while (!pool.isEmpty()) {
      Czlowiek head = pool.removeLast();
      heads.add(head);
      int levels = 5;
      int maxConnections = 20;
      connectPeopleRecursively(head, pool, levels - 1, maxConnections);
    }

    return heads;
  }

}
