package pl.edu.pg.persistance.controllers;

import pl.edu.pg.persistance.models.Czlowiek;
import pl.edu.pg.persistance.services.CzlowiekService;

import java.util.Optional;

public class CzlowiekController extends CrudController<Czlowiek> {
  public CzlowiekController(CzlowiekService czlowiekService) {
    super(czlowiekService);
  }

  public String save(String firstName, String numerTelefonu, String lastName) {
    return save(firstName, lastName, numerTelefonu, Optional.empty());
  }

  public String save(String firstName, String lastName, String numerTelefonu, Optional<Integer> id) {
    var czlowiek = new Czlowiek(firstName, lastName, numerTelefonu, null, null, null, null, null);
    id.ifPresent(czlowiek::setId);
    return save(czlowiek);
  }
}
