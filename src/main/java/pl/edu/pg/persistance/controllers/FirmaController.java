package pl.edu.pg.persistance.controllers;

import pl.edu.pg.persistance.models.Firma;
import pl.edu.pg.persistance.services.FirmaService;

import java.util.Optional;

public class FirmaController extends CrudController<Firma> {
  public FirmaController(FirmaService firmaService) {
    super(firmaService);
  }

  public String save(String name) {
    return save(name, Optional.empty());
  }

  public String save(String name, Optional<Integer> id) {
    var firma = id.map(integer -> new Firma(name, integer)).orElseGet(() -> new Firma(name));
    return save(firma);
  }

}
