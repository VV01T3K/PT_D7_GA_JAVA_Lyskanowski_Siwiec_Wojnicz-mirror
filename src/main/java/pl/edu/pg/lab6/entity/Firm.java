package pl.edu.pg.lab6.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public class Firm {
    private String name;
    private String department;
    private List<Person> employees = new ArrayList<>();
    public Firm(String name, String department) {
        this.name = name;
        this.department = department;
    }

    @Override
    public String toString() {
        return "Firm{" +
                "name='" + name + '\'' +
                ", department='" + department + '\'' +
                ", employees=" + employees +
                '}';
    }
    public void addEmployee(Person person) {
        if (person == null) {
            throw new IllegalArgumentException("Person cannot be null");
        }
        if (!employees.contains(person)) {
            employees.add(person);
        }

        // Upewnij się, że osoba ma ustawioną referencję do tej firmy
        if (person.getFirm() != this) {
            person.setFirm(this);
        }
    }
}
