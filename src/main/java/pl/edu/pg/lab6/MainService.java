package pl.edu.pg.lab6;

import pl.edu.pg.lab6.controller.ControllerResponses;
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

    public void deleteFirm(String name) {
        String res1 = firmController.delete(name);
        String res2 = personController.deleteByFirm(name);
        if (res1.equals(ControllerResponses.DONE.toString())) {
            System.out.println("Usunięto firmę.");
        } else {
            System.out.println("Nie znaleziono firmy.");
        }
        if(res2.equals(ControllerResponses.DONE.toString())) {
            System.out.println("Usunięto pracowników.");
        } else {
            System.out.println("Nie znaleziono pracowników do usunięcia.");
        }
    }
    public void deletePerson(String id) {
        String res1 = personController.delete(id);
        String res2 = firmController.deleteFromEmployees(id);
        if (res1.equals(ControllerResponses.DONE.toString())) {
            System.out.println("Usunięto osobę.");
        } else {
            System.out.println("Nie znaleziono osoby do usunięcia.");
        }
        if(res2.equals(ControllerResponses.DONE.toString())) {
            System.out.println("Usunięto osobę z firmy.");
        } else {
            System.out.println("Nie znaleziono osoby do usunięcia z pracowników.");
        }
    }
}
