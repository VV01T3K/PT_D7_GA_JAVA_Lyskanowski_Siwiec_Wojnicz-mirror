package pl.edu.pg.lab6.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.edu.pg.lab6.entity.Firm;
import pl.edu.pg.lab6.repo.FirmRepo;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FirmControllerTest {

    @InjectMocks private FirmController firmController;
    @Mock private FirmRepo firmRepo;
    private static final String TEST_NAME = "TestCompany";
    private static final String TEST_DEPARTMENT = "IT";

    @BeforeEach
    void setUp() {
        firmController = new FirmController(firmRepo);
    }

    @Test
    void testFind_Success() {
        Firm firm = new Firm(TEST_NAME, TEST_DEPARTMENT);
        when(firmRepo.findById(TEST_NAME)).thenReturn(Optional.of(firm));
        String result = firmController.find(TEST_NAME);
        assertEquals(firm.toString(), result);
        verify(firmRepo).findById(TEST_NAME);
    }
    @Test
    void testFind_FirmNotFound() {
        when(firmRepo.findById(TEST_NAME)).thenReturn(Optional.empty());
        String result = firmController.find(TEST_NAME);
        assertEquals(ControllerResponses.NOT_FOUND.toString(), result);
        verify(firmRepo).findById(TEST_NAME);
    }
    @Test
    void testSave_Success() {
        doNothing().when(firmRepo).save(any(Firm.class));
        String result = firmController.save(TEST_NAME, TEST_DEPARTMENT);
        assertEquals(ControllerResponses.DONE.toString(), result);
        verify(firmRepo).save(any(Firm.class));
    }
    @Test
    void testSave_IllegalArgumentException() {
        doThrow(new IllegalArgumentException()).when(firmRepo).save(any(Firm.class));
        String result = firmController.save(TEST_NAME, TEST_DEPARTMENT);
        assertEquals(ControllerResponses.BAD_REQUEST.toString(), result);
        verify(firmRepo).save(any(Firm.class));
    }
    @Test
    void testDelete_Success() {
        doNothing().when(firmRepo).deleteById(TEST_NAME);
        String result = firmController.delete(TEST_NAME);
        assertEquals(ControllerResponses.DONE.toString(), result);
        verify(firmRepo).deleteById(TEST_NAME);
    }
    @Test
    void testDelete_FirmNotFound() {
        doThrow(new IllegalArgumentException()).when(firmRepo).deleteById(TEST_NAME);
        String result = firmController.delete(TEST_NAME);
        assertEquals(ControllerResponses.NOT_FOUND.toString(), result);
        verify(firmRepo).deleteById(TEST_NAME);
    }
}