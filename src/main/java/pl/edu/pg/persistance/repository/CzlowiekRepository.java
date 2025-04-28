package pl.edu.pg.persistance.repository;

import jakarta.persistence.NoResultException;
import pl.edu.pg.persistance.PersistenceManager;
import pl.edu.pg.persistance.models.Czlowiek;

import java.util.Optional;

public class CzlowiekRepository extends Repository<Czlowiek> {
  public CzlowiekRepository() {
    super(Czlowiek.class);
  }

  public Optional<Czlowiek> findByFullNameAndFirma(String fullName, String nazwaFirmy) {
    var em = PersistenceManager.getEntityManager();
    var split = fullName.split(" ");
    String imie = split[0];
    String nazwisko = split[1];
    try {
      if (nazwaFirmy == null || nazwaFirmy.isEmpty()) {
        return Optional.ofNullable(
                em.createQuery("SELECT c FROM Czlowiek c WHERE c.imie = :imie AND c.nazwisko = :nazwisko", Czlowiek.class)
                        .setParameter("imie", imie)
                        .setParameter("nazwisko", nazwisko)
                        .getSingleResult()
        );
      }
      return Optional.ofNullable(em.createQuery("SELECT c FROM Czlowiek c WHERE c.imie = :imie AND c.nazwisko = :nazwisko AND c.firma.nazwaFirmy = :firma", Czlowiek.class)
              .setParameter("imie", imie)
              .setParameter("nazwisko", nazwisko)
              .setParameter("firma", nazwaFirmy)
              .getSingleResult());
    } catch (NoResultException ex) {
      return Optional.empty();
    }
  }
}
