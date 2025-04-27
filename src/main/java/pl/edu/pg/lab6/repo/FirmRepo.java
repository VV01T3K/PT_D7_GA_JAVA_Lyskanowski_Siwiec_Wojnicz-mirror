package pl.edu.pg.lab6.repo;

import pl.edu.pg.lab6.entity.Firm;
import pl.edu.pg.lab6.entity.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FirmRepo implements IRepo<Firm, String> {
    private final List<Firm> firms = new ArrayList<>();

    @Override
    public void save(Firm firm) throws IllegalArgumentException {
        if (firm == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }
        for (Firm existingFirm : firms) {
            if (existingFirm.getName().equals(firm.getName())) {
                throw new IllegalArgumentException("Firm with this name already exists");
            }
        }
        firms.add(firm);
    }

    @Override
    public Optional<Firm> findById(String s) throws IllegalArgumentException {
        if (s == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        for (Firm firm : firms) {
            if (firm.getName().equals(s)) {
                return Optional.of(firm);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Firm> findAll() {
        return new ArrayList<>(firms);
    }

    @Override
    public void deleteById(String s) throws IllegalArgumentException {
        for (Firm firm : firms) {
            if (firm.getName().equals(s)) {
                firms.remove(firm);
                return;
            }
        }
        throw new IllegalArgumentException("Firm with this ID does not exist");
    }

    public void addEmployee(Firm firm, Person person) {
        if (firm == null || person == null) {
            throw new IllegalArgumentException("Firm and Person cannot be null");
        }
        firm.addEmployee(person);
    }

    @Override
    public String toString() {
        return "FirmRepo{" +
                "firms=" + firms +
                '}';
    }
}
