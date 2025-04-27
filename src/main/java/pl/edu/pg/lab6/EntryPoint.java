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
            System.out.println("3. Znajdz firme");
            System.out.println("4. Znajdz osobe");
            System.out.println("5. Usun firme");
            System.out.println("6. Usun osobe");
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
                    System.out.println("Podaj nazwe firmy do wyszukania:");
                    String name = scanner.nextLine();
                    String result = firmController.find(name);
                    System.out.println(result);
                }
                case "4" -> {
                    System.out.println("Podaj imie i nazwisko osoby do wyszukania (oddzielone spacją):");
                    String[] nameParts = scanner.nextLine().split(" ");
                    if (nameParts.length != 2) {
                        System.out.println("Niepoprawne dane. Podaj imie i nazwisko oddzielone spacją.");
                        continue;
                    }
                    String result = personController.find(nameParts[0] + "_" + nameParts[1]);
                    System.out.println(result);
                }
                case "5" -> {
                    System.out.println("Podaj nazwe firmy do usuniecia:");
                    String name = scanner.nextLine();
                    mainService.deleteFirm(name);
                }
                case "6" -> {
                    System.out.println("Podaj imie i nazwisko osoby do usuniecia (oddzielone spacją):");
                    String[] nameParts = scanner.nextLine().split(" ");
                    if (nameParts.length != 2) {
                        System.out.println("Niepoprawne dane. Podaj imie i nazwisko oddzielone spacją.");
                        continue;
                    }
                    mainService.deletePerson(nameParts[0] + "_" + nameParts[1]);
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
