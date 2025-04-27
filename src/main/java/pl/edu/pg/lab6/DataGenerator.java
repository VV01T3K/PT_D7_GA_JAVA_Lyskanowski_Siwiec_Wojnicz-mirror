package pl.edu.pg.lab6;

import net.datafaker.Faker;
import pl.edu.pg.lab6.controller.FirmController;
import pl.edu.pg.lab6.controller.PersonController;
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
    public static void generateSampleData(FirmController firmController, PersonController personController,
                                           int numberOfFirms,int numberOfPersons) {
        for (int i = 0; i < numberOfFirms; i++) {
            Firm firm = generateFirm();
            firmController.save(firm.getName(), firm.getDepartment());
            for (int j = 0; j < numberOfPersons; j++) {
                Person person = generatePerson(firm);
                personController.save(person.getName(), person.getSurname(), person.getAge(), firm);
            }
        }
    }
}
