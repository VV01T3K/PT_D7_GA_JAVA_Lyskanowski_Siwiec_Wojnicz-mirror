package pl.edu.pg;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.datafaker.Faker;

public class TestRepo {
    // private static final int seed = new Random().nextInt();
    private static final int liczbaElementow = 100;
    private static final int levels = 3;
    private static final int maxConnections = 5;
    private static final int seed = 1234;
    private static final Random rand = new Random(seed);
    private static final Faker faker = new Faker(Locale.of("pl"), rand);

    public static Faker getFaker() {
        return faker;
    }

    private static Set<Czlowiek> heads = CzlowiekPodlegliFactory.chooseSet();

    public static Set<Czlowiek> getHeads() {
        return heads;
    }

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

    public static ArrayList<Czlowiek> generateRandomCzlowiekList(int count) {
        return Stream.generate(TestRepo::generateCzlowiek)
                .limit(count)
                .collect(Collectors.toCollection(() -> new ArrayList<>(count)));
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

    public static Set<Czlowiek> generateCzlowiekList(int count, int levels, int maxConnections) {
        ArrayList<Czlowiek> pool = generateRandomCzlowiekList(count);
        Set<Czlowiek> heads = CzlowiekPodlegliFactory.chooseSet();

        while (!pool.isEmpty()) {
            Czlowiek head = pool.removeFirst();
            heads.add(head);
            connectPeople(head, pool, levels - 1, maxConnections);
        }

        return heads;
    }

    public static void generateTestData() {
        heads = generateCzlowiekList(liczbaElementow, levels, maxConnections);
    }

    public static void saveJson() {
        String filePath = "src/main/people.json";

        try (Writer writer = new FileWriter(filePath)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(heads, writer);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    public static void loadJson() {
        String filePath = "src/main/people.json";

        try {
            Gson gson = new GsonBuilder().create();
            java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<Set<Czlowiek>>() {
            }.getType();
            heads = gson.fromJson(new FileReader(filePath), type);
        } catch (Exception e) {
            System.err.println("Error reading from file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        generateTestData();
        saveJson();
        getHeads().clear();
        loadJson();
        for (Czlowiek czlowiek : heads) {
            czlowiek.wypiszRekurencjnie(0);
        }
    }
}
