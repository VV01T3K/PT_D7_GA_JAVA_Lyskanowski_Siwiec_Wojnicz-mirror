package pl.edu.pg.persistance.terminal;

import pl.edu.pg.persistance.repository.CzlowiekRepository;
import pl.edu.pg.persistance.repository.FirmaRepository;
import pl.edu.pg.persistance.repository.HierarchiaRepository;

import java.util.Scanner;

public class SelectService {
  private final Scanner scanner;

  public SelectService(Scanner scanner) {
    this.scanner = scanner;
  }

  public void select() {
    System.out.println("Co chcesz wyswietlic?");
    System.out.println("1. Czlowiek");
    System.out.println("2. Firma");
    System.out.println("3. Hierarchia");
    String choice = scanner.nextLine();
    System.out.println("Ile? (0 - wszystkie)");
    int limit = Integer.parseInt(scanner.nextLine());
    switch (choice) {
      case "1" -> new CzlowiekRepository().findAll(limit).forEach(System.out::println);
      case "2" -> new FirmaRepository().findAll(limit).forEach(System.out::println);
      case "3" -> new HierarchiaRepository().findAll(limit).forEach(System.out::println);
      default -> System.out.println("Nieznany wybor");
    }
  }

}
