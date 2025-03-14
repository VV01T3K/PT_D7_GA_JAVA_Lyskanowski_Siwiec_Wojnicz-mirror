package pl.edu.pg;

import java.util.Locale;
import java.util.Random;
import java.util.stream.Stream;

import net.datafaker.Faker;

public class TestRepo {
    private static final int seed = 123;
    public static final Faker faker = new Faker(Locale.of("pl"), new Random(seed));

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

    public static void main(String[] args) {
        generateCzlowiekStream(10).forEach(System.out::println);
    }

}
