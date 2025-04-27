package pl.edu.pg.persistance.services;

import pl.edu.pg.persistance.models.Czlowiek;
import pl.edu.pg.persistance.models.Firma;
import pl.edu.pg.persistance.models.Hierarchia;
import pl.edu.pg.persistance.repository.Repository;

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
      case "1" -> new Repository<Czlowiek>().findAll(limit, Czlowiek.class).forEach(System.out::println);
      case "2" -> new Repository<Firma>().findAll(limit, Firma.class).forEach(System.out::println);
      case "3" -> new Repository<Hierarchia>().findAll(limit, Hierarchia.class).forEach(System.out::println);
      default -> System.out.println("Nieznany wybor");
    }
  }

}
