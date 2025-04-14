package pl.edu.pg.persistance.services;

import jakarta.persistence.NoResultException;
import pl.edu.pg.persistance.PersistenceManager;
import pl.edu.pg.persistance.models.Firma;
import pl.edu.pg.persistance.models.Pracownik;

import java.util.List;
import java.util.Scanner;

public class QueryService {

  private final Scanner scanner;

  public QueryService(Scanner scanner) {
    this.scanner = scanner;
  }

  public List<Pracownik> queryLudziZWyksztalceniem(String wyksztalcenie) {
    var em = PersistenceManager.getEntityManager();
    var query = em.createQuery("SELECT c FROM Pracownik c WHERE c.wyksztalcenie = :wysztalcenie", Pracownik.class);
    query.setParameter("wysztalcenie", wyksztalcenie);
    try {
      return query.getResultList();
    } catch (NoResultException e) {
      System.out.println("Nie znaleziono zadnych ludzi z takim wyksztalceniem");
      return List.of();
    }
  }

  public List<Firma> queryFirmyWKtorychPracujeLudzi(int liczbaLudzi) {
    var em = PersistenceManager.getEntityManager();
    var query = em.createQuery("SELECT f FROM Firma f JOIN f.pracownicy p GROUP BY f.id HAVING count(p.id) >= :liczbaLudzi", Firma.class);
    query.setParameter("liczbaLudzi", liczbaLudzi);
    try {
      return query.getResultList();
    } catch (NoResultException e) {
      System.out.println("Nie znaleziono zadnych firm z taka liczba ludzi");
      return List.of();
    }
  }

  public List<Pracownik> queryWszyscyPrzelozeni() {
    var em = PersistenceManager.getEntityManager();
    var query = em.createQuery("SELECT DISTINCT h.przelozony from Hierarchia h", Pracownik.class);
    try {
      return query.getResultList();
    } catch (NoResultException e) {
      System.out.println("Nie znaleziono zadnych przelozonych");
      return List.of();
    }
  }

  public List<Pracownik> queryLudzieNiePracujacyWFirmach() {
    var em = PersistenceManager.getEntityManager();
    var query = em.createQuery("SELECT c FROM Pracownik c WHERE c.firma IS NULL", Pracownik.class);
    try {
      return query.getResultList();
    } catch (NoResultException e) {
      System.out.println("Nie znaleziono zadnych ludzi niepracujacych w firmach");
      return List.of();
    }
  }

  public List<Firma> queryFirmyBezPracownikow() {
    var em = PersistenceManager.getEntityManager();
    var query = em.createQuery("SELECT f FROM Firma f WHERE f.pracownicy IS EMPTY", Firma.class);
    try {
      return query.getResultList();
    } catch (NoResultException e) {
      System.out.println("Nie znaleziono zadnych firm bez pracownikow");
      return List.of();
    }
  }

  public void query() {
    System.out.println("Co chcesz zapytac?");
    System.out.println("1. Ludzi z wyksztalceniem");
    System.out.println("2. Firmy w ktorych pracuje X ludzi");
    System.out.println("3. Wszyscy przelozeni");
    System.out.println("4. Ludzie niepracujacy w firmach");
    System.out.println("5. Firmy bez pracownikow");
    String choice = scanner.nextLine();
    switch (choice) {
      case "1" -> {
        System.out.println("Podaj wyksztalcenie:");
        String wyksztalcenie = scanner.nextLine();
        queryLudziZWyksztalceniem(wyksztalcenie).forEach(System.out::println);
      }
      case "2" -> {
        System.out.println("Podaj liczbe ludzi:");
        int liczbaLudzi = Integer.parseInt(scanner.nextLine());
        queryFirmyWKtorychPracujeLudzi(liczbaLudzi).forEach(System.out::println);
      }
      case "3" -> queryWszyscyPrzelozeni().forEach(System.out::println);
      case "4" -> queryLudzieNiePracujacyWFirmach().forEach(System.out::println);
      case "5" -> queryFirmyBezPracownikow().forEach(System.out::println);
      default -> System.out.println("Nieznany wybor");
    }
  }

}
