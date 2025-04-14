package pl.edu.pg.persistance.repository;

import pl.edu.pg.persistance.PersistenceManager;
import pl.edu.pg.persistance.models.Pracownik;

public class CzlowiekRepository extends Repository<Pracownik> {
  public Pracownik findByFullNameAndFirma(String fullName, String nazwaFirmy) {
    var em = PersistenceManager.getEntityManager();
    var split = fullName.split(" ");
    String imie = split[0];
    String nazwisko = split[1];
    return em.createQuery("SELECT c FROM Pracownik c WHERE c.imie = :imie AND c.nazwisko = :nazwisko AND c.firma.nazwaFirmy = :firma", Pracownik.class)
            .setParameter("imie", imie)
            .setParameter("nazwisko", nazwisko)
            .setParameter("firma", nazwaFirmy)
            .getSingleResult();
  }
}
