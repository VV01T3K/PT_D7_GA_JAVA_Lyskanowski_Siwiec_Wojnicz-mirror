package pl.edu.pg.persistance.controllers;

import pl.edu.pg.persistance.models.IModel;
import pl.edu.pg.persistance.services.CrudService;

import java.util.Optional;

public class CrudController<T extends IModel> {
  protected final CrudService<T> crudService;

  public CrudController(CrudService<T> crudService) {
    this.crudService = crudService;
  }

  public String save(T instance) {
    try {
      crudService.save(instance);
    } catch (IllegalArgumentException e) {
      return ControllerResponses.BAD_REQUEST.toString();
    }
    return ControllerResponses.DONE.toString();
  }

  public String delete(Integer id) {
    try {
      crudService.delete(id);
    } catch (IllegalArgumentException e) {
      return ControllerResponses.NOT_FOUND.toString();
    }
    return ControllerResponses.DONE.toString();
  }

  public String find(Integer id) {
    Optional<T> firm = crudService.findById(id);
    if (firm.isPresent()) {
      return firm.get().toString();
    } else {
      return ControllerResponses.NOT_FOUND.toString();
    }
  }

  public String findAll() {
    for (T firm : crudService.findAll()) {
      System.out.println(firm);
    }
    return ControllerResponses.DONE.toString();
  }

}
