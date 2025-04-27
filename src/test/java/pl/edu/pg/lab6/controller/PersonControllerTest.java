package pl.edu.pg.lab6.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.edu.pg.lab6.entity.Firm;
import pl.edu.pg.lab6.entity.Person;
import pl.edu.pg.lab6.repo.PersonRepo;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonControllerTest {
    @InjectMocks private PersonController personController;
    @Mock private PersonRepo personRepo;
    private static final String NAME = "Jan";
    private static final String SURNAME = "Kowalski";
    private static final int AGE = 30;
    private static final Firm FIRM = new Firm("HRejterzy", "IT");
    @BeforeEach
    void setUp() {
        personController = new PersonController(personRepo);
    }
    @Test
    void TestFind_NotFound() {
        when(personRepo.findById(NAME)).thenReturn(Optional.empty());
        String result = personController.find(NAME);
        assertEquals(ControllerResponses.NOT_FOUND.toString(), result);
    }
    @Test
    void TestFind_Found() {
        Person person = new Person(NAME, SURNAME, AGE, FIRM);
        when(personRepo.findById(NAME)).thenReturn(Optional.of(person));
        String result = personController.find(NAME);
        assertEquals(person.toString(), result);
        verify(personRepo).findById(NAME);
    }
    @Test
    void TestSave_Success() {;
        doNothing().when(personRepo).save(any(Person.class));
        String result = personController.save(NAME, SURNAME, AGE, FIRM);
        assertEquals(ControllerResponses.DONE.toString(), result);
        verify(personRepo).save(any(Person.class));
    }
    @Test
    void TestSave_IllegalArgumentException() {
        doThrow(new IllegalArgumentException()).when(personRepo).save(any(Person.class));
        String result = personController.save(NAME, SURNAME, AGE, FIRM);
        assertEquals(ControllerResponses.BAD_REQUEST.toString(), result);
        verify(personRepo).save(any(Person.class));
    }
    @Test
    void TestDelete_Success() {
        doNothing().when(personRepo).deleteById(NAME);
        String result = personController.delete(NAME);
        assertEquals(ControllerResponses.DONE.toString(), result);
        verify(personRepo).deleteById(NAME);
    }
    @Test
    void TestDelete_NotFound() {
        doThrow(new IllegalArgumentException()).when(personRepo).deleteById(NAME);
        String result = personController.delete(NAME);
        assertEquals(ControllerResponses.NOT_FOUND.toString(), result);
        verify(personRepo).deleteById(NAME);
    }
}