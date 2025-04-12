package pl.edu.pg.persistance;

import jakarta.persistence.NoResultException;
import pl.edu.pg.persistance.builders.CzlowiekBuilder;
import pl.edu.pg.persistance.builders.FirmaBuilder;
import pl.edu.pg.persistance.builders.HierarchiaBuilder;
import pl.edu.pg.persistance.builders.IBuilder;
import pl.edu.pg.persistance.models.Czlowiek;
import pl.edu.pg.persistance.models.Firma;
import pl.edu.pg.persistance.models.Hierarchia;
import pl.edu.pg.persistance.models.IModel;
import pl.edu.pg.persistance.repository.CzlowiekRepository;
import pl.edu.pg.persistance.repository.FirmaRepository;
import pl.edu.pg.persistance.repository.HierarchiaRepository;
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

  private void select() {
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

  private void create() {
    System.out.println("Co chcesz dodac?");
    System.out.println("1. Czlowiek");
    System.out.println("2. Firma");
    System.out.println("3. Hierarchia");
    String choice = scanner.nextLine();
    switch (choice) {
      case "1" -> createInteractively(new CzlowiekBuilder());
      case "2" -> createInteractively(new FirmaBuilder());
      case "3" -> createInteractively(new HierarchiaBuilder());
      default -> System.out.println("Nieznany wybor");
    }
  }

  public void delete() {
    System.out.println("Co chcesz usunac?");
    System.out.println("1. Czlowiek");
    System.out.println("2. Firma");
    System.out.println("3. Hierarchia");
    String choice = scanner.nextLine();
    switch (choice) {
      case "1" -> {
        System.out.println("Podaj imie i nazwisko czlowieka do usuniecia:");
        String fullName = scanner.nextLine();
        System.out.println("Podaj nazwe firmy:");
        String nazwaFirmy = scanner.nextLine();
        CzlowiekRepository czlowiekRepository = new CzlowiekRepository();
        Czlowiek czlowiek = czlowiekRepository.findByFullNameAndFirma(fullName, nazwaFirmy);
        if (czlowiek != null) {
          czlowiekRepository.delete(czlowiek);
        } else {
          System.out.println("Nie znaleziono czlowieka o podanym imieniu i nazwisku w tej firmie.");
        }
      }
      case "2" -> {
        System.out.println("Podaj nazwe firmy do usuniecia:");
        String nazwaFirmy = scanner.nextLine();
        FirmaRepository firmaRepository = new FirmaRepository();
        var firma = firmaRepository.findByName(nazwaFirmy);
        if (firma.isPresent()) {
          firmaRepository.delete(firma.get());
        } else {
          System.out.println("Nie znaleziono firmy o podanej nazwie.");
        }
      }
      case "3" -> {
        System.out.println("Podaj imie i nazwisko podwladnego do usuniecia:");
        String fullName = scanner.nextLine();
        System.out.println("Podaj nazwe firmy:");
        Czlowiek czlowiek = new CzlowiekRepository().findByFullNameAndFirma(fullName, scanner.nextLine());
        if (czlowiek == null) {
          System.out.println("Nie znaleziono czlowieka o podanym imieniu i nazwisku w tej firmie.");
          return;
        }
        HierarchiaRepository hierarchiaRepository = new HierarchiaRepository();
        try {
          var hierarchia = hierarchiaRepository.fidnByPodwladny(czlowiek);
          hierarchia.forEach(hierarchiaRepository::delete);
        } catch (NoResultException e) {
          System.out.println("Nie znaleziono hierarchii dla podanego czlowieka.");
        }
      }
      default -> System.out.println("Nieznany wybor");
    }
  }

  private void start() {
    while (true) {
      System.out.println("Polecenia do wyboru:");
      System.out.println("1. Dodaj");
      System.out.println("2. Usun");
      System.out.println("3. Wyswietl");
      System.out.println("9. Wyjdz");
      String command = scanner.nextLine();
      switch (command) {
        case "1" -> create();
        case "2" -> delete();
        case "3" -> select();
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
