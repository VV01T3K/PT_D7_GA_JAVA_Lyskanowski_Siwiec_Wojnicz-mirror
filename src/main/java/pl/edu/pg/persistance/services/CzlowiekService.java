package pl.edu.pg.persistance.services;

import pl.edu.pg.persistance.models.Czlowiek;
import pl.edu.pg.persistance.repository.CzlowiekRepository;

public class CzlowiekService extends CrudService<Czlowiek> {
  public CzlowiekService(CzlowiekRepository czlowiekRepo) {
    super(czlowiekRepo);
  }
}
