package pl.edu.pg.lab6.repo;

import pl.edu.pg.lab6.entity.Firm;
import pl.edu.pg.lab6.entity.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PersonRepo implements IRepo<Person,String>{
    private final List<Person> people = new ArrayList<>();
    @Override
    public void save(Person person) throws IllegalArgumentException {
        if(person == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }
        if(person.getName() == null || person.getName().isEmpty() ||
                person.getSurname() == null || person.getSurname().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        for(Person existingPerson : people) {
            if (existingPerson.getId().equals(person.getId())) {
                throw new IllegalArgumentException("Person with this ID already exists");
            }
        }
        people.add(person);
        // jakos dodac osobe do listy pracownikow w firmie bez wstrzykiwania repo
    }

    @Override
    public Optional<Person> findById(String id) throws IllegalArgumentException {
        if(id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        for (Person person : people) {
            if (person.getId().equals(id)) {
                return Optional.of(person);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Person> findAll() {
        return new ArrayList<>(people);
    }

    @Override
    public void deleteById(String id) throws IllegalArgumentException {
        for(Person person : people) {
            if (person.getId().equals(id)) {
                people.remove(person);
                return;
            }
        }
        throw new IllegalArgumentException("Person with this ID does not exist");
    }
    @Override
    public String toString() {
        return "PersonRepo{" +
                "people=" + people +
                '}';
    }
}
