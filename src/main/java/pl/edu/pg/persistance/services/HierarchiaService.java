package pl.edu.pg.persistance.services;

import pl.edu.pg.persistance.models.Hierarchia;
import pl.edu.pg.persistance.repository.HierarchiaRepository;

public class HierarchiaService extends CrudService<Hierarchia> {
  public HierarchiaService(HierarchiaRepository hierarchiaRepo) {
    super(hierarchiaRepo);
  }
}
