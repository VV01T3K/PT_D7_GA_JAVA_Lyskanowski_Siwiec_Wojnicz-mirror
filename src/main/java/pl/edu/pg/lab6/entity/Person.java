package pl.edu.pg.lab6.entity;

import lombok.Data;
import lombok.ToString;

@Data
public class Person {
    private String id;
    private String name;
    private String surname;
    private int age;
    @ToString.Exclude
    private Firm firm;

    @ToString.Include(name = "firmName")
    private String getFirmName() {
        return firm != null ? firm.getName() : "brak firmy";
    }

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
}