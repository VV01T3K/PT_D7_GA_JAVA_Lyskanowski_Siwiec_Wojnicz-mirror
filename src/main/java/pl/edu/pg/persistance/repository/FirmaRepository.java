package pl.edu.pg.persistance.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import pl.edu.pg.persistance.PersistenceManager;
import pl.edu.pg.persistance.models.Firma;
import pl.edu.pg.persistance.models.Hierarchia;
import pl.edu.pg.persistance.models.Pracownik;

import java.util.List;
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

  @Override
  public void delete(Firma entity) {
    EntityManager em = PersistenceManager.getEntityManager();
    var transaction = em.getTransaction();

    try {
      transaction.begin();

      // Find all employees in the company
      List<Pracownik> pracownicy = em.createQuery(
          "SELECT p FROM Pracownik p WHERE p.firma = :firma", Pracownik.class)
          .setParameter("firma", entity)
          .getResultList();

      // Delete all hierarchies involving these employees
      for (Pracownik pracownik : pracownicy) {
        // Delete hierarchies where this employee is a superior
        em.createQuery("DELETE FROM Hierarchia h WHERE h.przelozony = :pracownik")
            .setParameter("pracownik", pracownik)
            .executeUpdate();

        // Delete hierarchies where this employee is a subordinate
        em.createQuery("DELETE FROM Hierarchia h WHERE h.podwladny = :pracownik")
            .setParameter("pracownik", pracownik)
            .executeUpdate();
      }

      // Now refresh the entity to get the latest state
      em.flush();
      em.refresh(entity);

      // Now we can safely delete the firm and its employees (cascade)
      if (entity.getId() != null) {
        em.remove(em.contains(entity) ? entity : em.merge(entity));
      }

      transaction.commit();
    } catch (Exception e) {
      if (transaction.isActive()) {
        transaction.rollback();
      }
      System.err.println("Error deleting company: " + e.getMessage());
      e.printStackTrace();
    }
  }
}