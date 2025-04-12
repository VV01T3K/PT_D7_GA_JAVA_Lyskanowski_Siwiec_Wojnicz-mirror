package pl.edu.pg.persistance;

import pl.edu.pg.persistance.builders.CzlowiekBuilder;
import pl.edu.pg.persistance.builders.FirmaBuilder;
import pl.edu.pg.persistance.builders.IBuilder;
import pl.edu.pg.persistance.models.IModel;
import pl.edu.pg.persistance.repository.Repository;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class Terminal {

  private final Scanner scanner = new Scanner(System.in);

  public static void main(String[] args) {
    Terminal terminal = new Terminal();
    terminal.start();
  }

  private <T extends IModel> T createInteractively(IBuilder<T> builder) {
    try {
      builder.buildInteractive(scanner);
      T entity = builder.build();
      Repository<T> repository = new Repository<>();
      repository.save(entity);
      return entity;
    } catch (NoSuchElementException ex) {
      System.out.println("Nie znaleziono podanego elementu.");
      return null;
    }
  }

  private void start() {
    while (true) {
      System.out.println("Polecenia do wyboru:");
      System.out.println("1. Dodaj nowego czlowieka");
      System.out.println("2. Dodaj nowa firme");
      System.out.println("3. Dodaj podwladnego");
      System.out.println("5. Usuń czlowieka");
      System.out.println("6. Usuń firme");
      String command = scanner.nextLine();
      switch (command) {
        case "1" -> createInteractively(new CzlowiekBuilder());
        case "2" -> createInteractively(new FirmaBuilder());
      }
    }
  }

}
