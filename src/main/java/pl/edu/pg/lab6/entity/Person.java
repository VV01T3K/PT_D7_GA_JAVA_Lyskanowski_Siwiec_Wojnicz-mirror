package pl.edu.pg.lab6.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
public class Person {
    private String id;
    private String name;
    private String surname;
    private int age;
    private Firm firm;

    public Person(String name, String surname, int age, Firm firm) {
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.firm = firm;
        updateId();
    }

    public void setName(String name) {
        this.name = name;
        updateId();
    }

    public void setSurname(String surname) {
        this.surname = surname;
        updateId();
    }

    private void updateId() {
        this.id = name + "_" + surname;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id  +
                ", name=" + name  +
                ", surname=" + surname  +
                ", age=" + age +
                ", firmName=" + (firm != null ? firm.getName() : "brak firmy") +
                '}';
    }
}