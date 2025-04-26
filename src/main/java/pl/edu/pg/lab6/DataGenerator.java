package pl.edu.pg.lab6;

import net.datafaker.Faker;
import pl.edu.pg.lab6.entity.Firm;
import pl.edu.pg.lab6.entity.Person;

import java.util.Random;

public class DataGenerator {
    private static final Faker faker = new Faker();
    private static final Random random = new Random();

    public static Firm generateFirm() {
        String companyName = faker.company().name();
        String department = faker.company().industry();
        return new Firm(companyName, department);
    }
    public static Person generatePerson(Firm firm) {
        String name = faker.name().firstName();
        String surname = faker.name().lastName();
        int age = random.nextInt(100);
        return new Person(name, surname, age, firm);
    }

}
