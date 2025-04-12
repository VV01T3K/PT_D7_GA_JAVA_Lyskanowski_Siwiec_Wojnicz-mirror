package pl.edu.pg.persistance.repository;

import pl.edu.pg.persistance.PersistenceManager;
import pl.edu.pg.persistance.models.Czlowiek;
import pl.edu.pg.persistance.models.Hierarchia;

import java.util.List;

public class HierarchiaRepository extends Repository<Hierarchia> {
  public List<Hierarchia> fidnByPodwladny(Czlowiek podwladny) {
    var em = PersistenceManager.getEntityManager();
    return em.createQuery("SELECT h FROM Hierarchia h WHERE h.podwladny = :podwladny", Hierarchia.class)
            .setParameter("podwladny", podwladny)
            .getResultList();
  }
}
