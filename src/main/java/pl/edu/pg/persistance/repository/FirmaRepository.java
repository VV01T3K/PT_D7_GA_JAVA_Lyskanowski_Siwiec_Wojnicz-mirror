package pl.edu.pg.persistance.repository;

import jakarta.persistence.NoResultException;
import pl.edu.pg.persistance.PersistenceManager;
import pl.edu.pg.persistance.models.Firma;

import java.util.Optional;

public class FirmaRepository extends Repository<Firma> {
  public Optional<Firma> findByName(String name) {
    var em = PersistenceManager.getEntityManager();
    try {
      return Optional.of(em
              .createQuery("SELECT f FROM Firma f WHERE f.nazwaFirmy = :name", Firma.class)
              .setMaxResults(1)
              .setParameter("name", name)
              .getSingleResult());
    } catch (NoResultException e) {
      return Optional.empty();
    } catch (Exception e) {
      System.out.println("Wystąpił błąd podczas wyszukiwania firmy: " + e.getMessage());
      return Optional.empty();
    }
  }
}