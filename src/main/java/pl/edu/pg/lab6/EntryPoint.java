package pl.edu.pg.lab6;

import pl.edu.pg.lab6.controller.FirmController;
import pl.edu.pg.lab6.controller.PersonController;
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
        MainService mainService = new MainService(firmController, personController);
        boolean running = true;
        while (running) {
            System.out.println("Polecenia do wyboru:");
            System.out.println("1. Wyswietl wszystko");
            System.out.println("2. Wstaw testowe dane");
            System.out.println("3. Wyswietl wszystkie osoby");
            System.out.println("4. Wyswietl wszystkie firmy");
            System.out.println("5. Usun osobe o id");
            System.out.println("6. Usun firme o name");
            System.out.println("9. Wyjdz");
            String command = scanner.nextLine();
            switch (command) {
                case "1" -> {
                    System.out.println("Wyswietlono wszystkie dane:");
                    mainService.displayAll();
                }
                case "2" -> {
                    System.out.println("Wstawiono testowe dane:");
                    mainService.insertTestData();
                }
                case "3" -> {
                    System.out.println("Wyswietlono wszystkie osoby:");
                    mainService.displayAllPersons();
                }
                case "4" -> {
                    System.out.println("Wyswietlono wszystkie firmy:");
                    mainService.displayAllFirms();
                }
                case "5" -> {
                    System.out.println("Podaj id osoby do usuniecia:");
                    String id = scanner.nextLine();
                    mainService.deletePerson(id);
                }
                case "6" -> {
                    System.out.println("Podaj name firmy do usuniecia:");
                    String id = scanner.nextLine();
                    mainService.deleteFirm(id);
                }

                case "9" -> {
                    running = false;
                }
                default -> {
                    System.out.println("Nieznana komenda");
                }
            }
        }
        scanner.close();
    }
}
