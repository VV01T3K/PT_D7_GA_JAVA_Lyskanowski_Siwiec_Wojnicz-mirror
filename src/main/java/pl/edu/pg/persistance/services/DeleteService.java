package pl.edu.pg.persistance.services;

import jakarta.persistence.NoResultException;
import pl.edu.pg.persistance.models.Pracownik;
import pl.edu.pg.persistance.repository.CzlowiekRepository;
import pl.edu.pg.persistance.repository.FirmaRepository;
import pl.edu.pg.persistance.repository.HierarchiaRepository;

import java.util.Scanner;

public class DeleteService {
  private final Scanner scanner;

  public DeleteService(Scanner scanner) {
    this.scanner = scanner;
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
        Pracownik pracownik = czlowiekRepository.findByFullNameAndFirma(fullName, nazwaFirmy);
        if (pracownik != null) {
          czlowiekRepository.delete(pracownik);
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
        Pracownik pracownik = new CzlowiekRepository().findByFullNameAndFirma(fullName, scanner.nextLine());
        if (pracownik == null) {
          System.out.println("Nie znaleziono czlowieka o podanym imieniu i nazwisku w tej firmie.");
          return;
        }
        HierarchiaRepository hierarchiaRepository = new HierarchiaRepository();
        try {
          var hierarchia = hierarchiaRepository.fidnByPodwladny(pracownik);
          hierarchia.forEach(hierarchiaRepository::delete);
        } catch (NoResultException e) {
          System.out.println("Nie znaleziono hierarchii dla podanego czlowieka.");
        }
      }
      default -> System.out.println("Nieznany wybor");
    }
  }
}
