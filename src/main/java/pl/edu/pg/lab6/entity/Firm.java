package pl.edu.pg.lab6.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Firm {
    private String name;
    private String department;
    private List<Person> employees = new ArrayList<>();

    public Firm(String name, String department) {
        this.name = name;
        this.department = department;
    }

    public void addEmployee(Person person) {
        if (person == null) {
            throw new IllegalArgumentException("Person cannot be null");
        }
        if (!employees.contains(person)) {
            employees.add(person);
        }

        if (person.getFirm() != this) {
            person.setFirm(this);
        }
    }
}
