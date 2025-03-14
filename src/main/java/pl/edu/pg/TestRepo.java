package pl.edu.pg;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.sqrt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.datafaker.Faker;

public class TestRepo {
    private static final int seed = new Random().nextInt();
    public static final Faker faker = new Faker(Locale.of("pl"), new Random(seed));

    public static ArrayList<Czlowiek> heads = new ArrayList<Czlowiek>();

    public static Czlowiek generateCzlowiek() {
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

    public static Czlowiek generateCzlowiekWithoutDetails() {
        String imie = faker.name().firstName();
        String nazwisko = faker.name().lastName();
        int wiek = faker.number().numberBetween(18, 80);
        Plec plec = imie.endsWith("a") ? Plec.KOBIETA : Plec.MEZCZYZNA;
        return new Czlowiek(imie, nazwisko, wiek, plec);
    }

    public static Stream<Czlowiek> generateCzlowiekStream(int count) {
        return Stream.generate(TestRepo::generateCzlowiek).limit(count);
    }

    private static void connectPeople(Czlowiek czlowiek, ArrayList<Czlowiek> pool, int maxDepth, int maxConnections) {
        if (maxDepth == 0)
            return;
        int podlegliCount = faker.number().numberBetween(1, maxConnections);
        for (int i = 0; i < podlegliCount; i++) {
            if (pool.isEmpty())
                return;
            Czlowiek podlegly = pool.removeFirst();
            connectPeople(podlegly, pool, maxDepth - 1, maxConnections);
            czlowiek.dodajPodleglego(podlegly);
        }
    }

    public static ArrayList<Czlowiek> generateCzlowiekList(int count, int levels, int maxConnections) {
        ArrayList<Czlowiek> ludzie = generateCzlowiekStream(count).collect(Collectors.toCollection(ArrayList::new));
        ArrayList<Czlowiek> pool = new ArrayList<Czlowiek>(ludzie);

        while (!pool.isEmpty()) {
            Czlowiek head = pool.removeFirst();
            heads.add(head);
            connectPeople(head, pool, levels - 1, maxConnections);
        }

        return ludzie;
    }

    public static void main(String[] args) {
        ArrayList<Czlowiek> ludzie = generateCzlowiekList(40, 3, 5);
        for (Czlowiek czlowiek : heads) {
            czlowiek.wypiszRekurencjnie(0);
        }
    }

}
