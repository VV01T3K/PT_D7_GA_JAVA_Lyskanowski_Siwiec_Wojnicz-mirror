package pl.edu.pg.persistance.terminal;

import pl.edu.pg.persistance.builders.CzlowiekBuilder;
import pl.edu.pg.persistance.builders.FirmaBuilder;
import pl.edu.pg.persistance.builders.HierarchiaBuilder;
import pl.edu.pg.persistance.builders.IBuilder;
import pl.edu.pg.persistance.models.IModel;
import pl.edu.pg.persistance.repository.CzlowiekRepository;
import pl.edu.pg.persistance.repository.FirmaRepository;
import pl.edu.pg.persistance.repository.HierarchiaRepository;
import pl.edu.pg.persistance.repository.Repository;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class CreateService {
  private final Scanner scanner;

  public CreateService(Scanner scanner) {
    this.scanner = scanner;
  }

  private <T extends IModel> T createInteractively(IBuilder<T> builder, Repository<T> repository) {
    try {
      builder.buildInteractive(scanner);
      T entity = builder.build();
      repository.save(entity);
      return entity;
    } catch (NoSuchElementException ex) {
      System.out.println("Nie znaleziono podanego elementu.");
      return null;
    }
  }


  public void create() {
    System.out.println("Co chcesz dodac?");
    System.out.println("1. Czlowiek");
    System.out.println("2. Firma");
    System.out.println("3. Hierarchia");
    String choice = scanner.nextLine();
    switch (choice) {
      case "1" -> createInteractively(new CzlowiekBuilder(), new CzlowiekRepository());
      case "2" -> createInteractively(new FirmaBuilder(), new FirmaRepository());
      case "3" -> createInteractively(new HierarchiaBuilder(), new HierarchiaRepository());
      default -> System.out.println("Nieznany wybor");
    }
  }

}
