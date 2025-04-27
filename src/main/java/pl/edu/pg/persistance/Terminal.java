package pl.edu.pg.persistance;

import pl.edu.pg.persistance.terminal.*;

import java.util.Scanner;

public class Terminal {

  private final Scanner scanner = new Scanner(System.in);

  public static void main(String[] args) {
    Terminal terminal = new Terminal();
    terminal.start();
  }

  private void start() {
    SelectService selectService = new SelectService(scanner);
    CreateService createService = new CreateService(scanner);
    DeleteService deleteService = new DeleteService(scanner);
    QueryService queryService = new QueryService(scanner);
    SeedService seedService = new SeedService();
    while (true) {
      System.out.println("Polecenia do wyboru:");
      System.out.println("1. Dodaj");
      System.out.println("2. Usun");
      System.out.println("3. Wyswietl");
      System.out.println("4. Zapytania");
      System.out.println("5. Wstaw testowe dane");
      System.out.println("9. Wyjdz");
      String command = scanner.nextLine();
      switch (command) {
        case "1" -> createService.create();
        case "2" -> deleteService.delete();
        case "3" -> selectService.select();
        case "4" -> queryService.query();
        case "5" -> seedService.seed();
        case "9" -> {
          return;
        }
        default -> {
          System.out.println("Nieznana komenda");
          System.out.println("Wybierz ponownie");
        }

      }
    }
  }

}
