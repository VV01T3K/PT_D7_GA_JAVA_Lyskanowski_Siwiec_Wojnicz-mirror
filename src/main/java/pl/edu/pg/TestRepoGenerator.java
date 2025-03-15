package pl.edu.pg;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.datafaker.Faker;

public class TestRepoGenerator {
    // private final int seed = new Random().nextInt(); // random
    private final int seed = 2137; // deterministic
    private final int levels = 3;
    private final int maxConnections = 5;
    private final Random rand = new Random(seed);
    private final Faker faker = new Faker(Locale.of("pl"), rand);

    public Faker getFaker() {
        return faker;
    }

    public Random getRand() {
        return rand;
    }

    public Czlowiek generateCzlowiek() {
        String imie = faker.name().firstName();
        String nazwisko = faker.name().lastName();
        int wiek = faker.number().numberBetween(18, 80);
        Plec plec = imie.endsWith("a") ? Plec.KOBIETA : Plec.MEZCZYZNA;
        MartialStatus stanCywilny = MartialStatus.fromString(faker.demographic().maritalStatus());
        String wyksztalcenie = faker.educator().course();
        String pozycjaZawodowa = faker.job().position();
        String numerTelefonu = faker.phoneNumber().cellPhoneInternational();
        return new Czlowiek(imie, nazwisko, wiek, plec, stanCywilny, wyksztalcenie, pozycjaZawodowa, numerTelefonu);
    }

    public Czlowiek generateCzlowiekWithoutDetails() {
        String imie = faker.name().firstName();
        String nazwisko = faker.name().lastName();
        int wiek = faker.number().numberBetween(18, 80);
        Plec plec = imie.endsWith("a") ? Plec.KOBIETA : Plec.MEZCZYZNA;
        return new Czlowiek(imie, nazwisko, wiek, plec);
    }

    public Stream<Czlowiek> generateRandomCzlowiekStream(int count) {
        return Stream.generate(
                this::generateCzlowiekWithoutDetails)
                .limit(count);
    }

    public ArrayList<Czlowiek> generateRandomCzlowiekList(int count) {
        return Stream.generate(
                this::generateCzlowiekWithoutDetails)
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
            Czlowiek podlegly = pool.removeFirst();
            connectPeopleRecursively(podlegly, pool, maxDepth - 1, maxConnections);
            czlowiek.dodajPodleglego(podlegly);
        }
    }

    public Set<Czlowiek> generateTestData(int count) {
        ArrayList<Czlowiek> pool = generateRandomCzlowiekList(count);
        Set<Czlowiek> heads = CzlowiekContainerFactory.chooseSet();

        while (!pool.isEmpty()) {
            Czlowiek head = pool.removeFirst();
            heads.add(head);
            connectPeopleRecursively(head, pool, levels - 1, maxConnections);
        }

        return heads;
    }

}
