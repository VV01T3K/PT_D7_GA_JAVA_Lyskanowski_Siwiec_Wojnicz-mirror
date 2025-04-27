package pl.edu.pg.lab6;

import pl.edu.pg.lab6.controller.FirmController;
import pl.edu.pg.lab6.controller.PersonController;
import pl.edu.pg.lab6.entity.Firm;
import pl.edu.pg.lab6.entity.Person;
import pl.edu.pg.lab6.repo.FirmRepo;
import pl.edu.pg.lab6.repo.PersonRepo;

import java.util.Scanner;


public class EntryPoint {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        FirmRepo firmRepo = new FirmRepo();
        PersonRepo personRepo = new PersonRepo();
        FirmController firmController = new FirmController(firmRepo);
        PersonController personController = new PersonController(personRepo);
        boolean running = true;
        while(running)
        {
            System.out.println("Polecenia do wyboru:");
            System.out.println("1. Wyswietl");
            System.out.println("2. Wstaw testowe dane");
            System.out.println("9. Wyjdz");
            String command = scanner.nextLine();
            switch (command) {
                case "1" -> {
                    firmController.findAll();
                    personController.findAll();
                }
                case "2" -> {
                    DataGenerator.generateSampleData(firmController, personController, 5, 5);
                    System.out.println("Testowe dane wstawione.");
                }
                case "9" -> {
                    running = false;
                }
                default -> {
                    System.out.println("Nieznana komenda");
                }
            }
        }
    }
}
