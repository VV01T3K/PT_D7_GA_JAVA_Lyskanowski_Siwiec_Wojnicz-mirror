package pl.edu.pg.lab6;

import pl.edu.pg.lab6.controller.FirmController;
import pl.edu.pg.lab6.controller.PersonController;
import pl.edu.pg.lab6.entity.Firm;

public class MainService {
    private final FirmController firmController;
    private final PersonController personController;

    public MainService(FirmController firmController, PersonController personController) {
        this.firmController = firmController;
        this.personController = personController;
    }

    public void displayAll() {
        firmController.findAll();
        personController.findAll();
    }

    public void insertTestData() {
        DataGenerator.generateSampleData(firmController, personController, 5, 5);
        System.out.println("Testowe dane wstawione.");
    }

    public void saveFirm(String name, String department) {
        firmController.save(name, department);
    }

    public void savePerson(String name, String surname, int age, Firm firm) {
        personController.save(name, surname, age, firm);
    }
}
