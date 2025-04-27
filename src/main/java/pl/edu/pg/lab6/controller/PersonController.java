package pl.edu.pg.lab6.controller;

import pl.edu.pg.lab6.entity.Firm;
import pl.edu.pg.lab6.entity.Person;
import pl.edu.pg.lab6.repo.PersonRepo;

import java.util.Optional;

public class PersonController implements IController{
    private final PersonRepo personRepo;

    public PersonController(PersonRepo personRepo) {
        this.personRepo = personRepo;
    }

    public String save(String name, String surname,int age, Firm firm) {
        try {
            personRepo.save(new Person(name, surname, age , firm));
        }
        catch(IllegalArgumentException e) {
            return ControllerResponses.BAD_REQUEST.toString();
        }
        return ControllerResponses.DONE.toString();
    }

    public String delete(String name) {
        try {
            personRepo.deleteById(name);
        }
        catch (IllegalArgumentException e){
            return ControllerResponses.NOT_FOUND.toString();
        }
        return ControllerResponses.DONE.toString();
    }

    public String find(String name){
        Optional<Person> person = personRepo.findById(name);
        if (person.isPresent()) {
            return person.get().toString();
        } else {
            return ControllerResponses.NOT_FOUND.toString();
        }
    }

    public String findAll() {
        for (Person person : personRepo.findAll()) {
            System.out.println(person);
        }
        return ControllerResponses.DONE.toString();
    }
    public String deleteByFirm(String name) {
        try {
            personRepo.deleteByFirm(name);
        }
        catch (IllegalArgumentException e){
            return ControllerResponses.NOT_FOUND.toString();
        }
        return ControllerResponses.DONE.toString();
    }
}
