package pl.edu.pg.lab6.repo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.edu.pg.lab6.entity.Firm;
import pl.edu.pg.lab6.entity.Person;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PersonRepoTest {
    private PersonRepo personRepo;
    private Firm testFirm;
    @BeforeEach
    void setUp() {
        personRepo = new PersonRepo();
        testFirm = new Firm("TestCompany", "IT");
    }

    @Test
    void deleteById_Success() {
        Person person = new Person("Jan", "Kowalski", 30, testFirm);
        personRepo.save(person);
        personRepo.deleteById("Jan_Kowalski");
        Optional<Person> foundPerson = personRepo.findById("Jan_Kowalski");
        assertFalse(foundPerson.isPresent());
    }

    @Test
    void deleteById_DoesntExist() {
        Person person = new Person("Jan", "Kowalski", 30, testFirm);
        personRepo.save(person);
        assertThrows(IllegalArgumentException.class, () -> personRepo.deleteById("Kowal_Jan"));
    }

    @Test
    void testSave_AlreadyExists() {
        Person person1 = new Person("Jan", "Kowalski", 30, testFirm);
        Person person2 = new Person("Jan", "Kowalski", 40, testFirm);
        personRepo.save(person1);
        assertThrows(IllegalArgumentException.class, () -> personRepo.save(person2));
    }

    @Test
    void testSave_NullPerson() {
        assertThrows(IllegalArgumentException.class, () -> personRepo.save(null));
    }

    @Test
    void testSave_EmptyName() {
        Person person = new Person("", "Kowalski", 30, testFirm);
        assertThrows(IllegalArgumentException.class, () -> personRepo.save(person));
    }

    @Test
    void testSave_EmptySurname() {
        Person person = new Person("Jan", "", 30, testFirm);
        assertThrows(IllegalArgumentException.class, () -> personRepo.save(person));
    }

    @Test
    void testFindById_Exists() {
        Person person = new Person("Jan", "Kowalski", 30, testFirm);
        personRepo.save(person);
        Optional<Person> foundPerson = personRepo.findById("Jan_Kowalski");
        assertTrue(foundPerson.isPresent());
        assertEquals(person, foundPerson.get());
    }

    @Test
    void testFindById_DoesntExist() {
        Person person = new Person("Jan", "Kowalski", 30, testFirm);
        personRepo.save(person);
        Optional<Person> foundPerson = personRepo.findById("Nowak_Kot");
        assertFalse(foundPerson.isPresent());
    }

    @Test
    void testFindById_NullId() {
        assertThrows(IllegalArgumentException.class, () -> personRepo.findById(null));
    }

    @Test
    void testFindAll_Success() {
        Person person1 = new Person("Jan", "Kowalski", 30, testFirm);
        Person person2 = new Person("Anna", "Nowak", 25, testFirm);
        personRepo.save(person1);
        personRepo.save(person2);
        List<Person> people = personRepo.findAll();
        assertEquals(2, people.size());
        assertTrue(people.contains(person1));
        assertTrue(people.contains(person2));
    }

    @Test
    void testFindAll_Empty() {
        assertThrows(IllegalStateException.class, () -> personRepo.findAll());
    }
}