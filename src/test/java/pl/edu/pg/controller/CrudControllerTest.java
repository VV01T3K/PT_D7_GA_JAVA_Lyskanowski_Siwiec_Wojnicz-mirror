package pl.edu.pg.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.edu.pg.DatabaseTestingFixture;
import pl.edu.pg.persistance.controllers.ControllerResponses;
import pl.edu.pg.persistance.controllers.CrudController;
import pl.edu.pg.persistance.models.Firma;
import pl.edu.pg.persistance.repository.FirmaRepository;
import pl.edu.pg.persistance.services.FirmaService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(DatabaseTestingFixture.class)
@ExtendWith(MockitoExtension.class)
public class CrudControllerTest {
  private static final String TEST_NAME = "TestCompany";
  @Mock
  private FirmaRepository firmRepo;
  private CrudController<Firma> controller;

  @BeforeEach
  public void createController() {
    controller = new CrudController<>(new FirmaService(firmRepo));
  }

  @Test
  void testFind_Success() {
    Firma firm = new Firma(TEST_NAME, 1);
    when(firmRepo.findById(1)).thenReturn(Optional.of(firm));
    String result = controller.find(1);
    assertEquals(firm.toString(), result);
  }

  @Test
  void testFind_FirmNotFound() {
    when(firmRepo.findById(1)).thenReturn(Optional.empty());
    String result = controller.find(1);
    assertEquals(ControllerResponses.NOT_FOUND.toString(), result);
  }

  @Test
  void testSave_Success() {
    when(firmRepo.save(any(Firma.class))).thenReturn(null);
    String result = controller.save(new Firma(TEST_NAME, 1));
    assertEquals(ControllerResponses.DONE.toString(), result);
    verify(firmRepo).save(any(Firma.class));
  }

  @Test
  void testSave_IllegalArgumentException() {
    doThrow(new IllegalArgumentException()).when(firmRepo).save(any(Firma.class));
    String result = controller.save(new Firma(TEST_NAME, 1));
    assertEquals(ControllerResponses.BAD_REQUEST.toString(), result);
    verify(firmRepo).save(any(Firma.class));
  }

  @Test
  void testDelete_Success() {
    var firma = new Firma(TEST_NAME, 1);
    when(firmRepo.findById(firma.getId())).thenReturn(Optional.of(firma));
    doNothing().when(firmRepo).delete(firma);
    String result = controller.delete(firma.getId());
    assertEquals(ControllerResponses.DONE.toString(), result);
  }

  @Test
  void testDelete_FirmNotFound() {
    when(firmRepo.findById(1)).thenReturn(Optional.empty());
    String result = controller.delete(1);
    assertEquals(ControllerResponses.NOT_FOUND.toString(), result);
  }
}
