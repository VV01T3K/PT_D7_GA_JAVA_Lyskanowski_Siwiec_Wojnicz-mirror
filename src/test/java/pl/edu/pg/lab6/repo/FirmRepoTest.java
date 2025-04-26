package pl.edu.pg.lab6.repo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.edu.pg.lab6.entity.Firm;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class FirmRepoTest {
    private FirmRepo firmRepo;
    @BeforeEach
    void setUp() {
        firmRepo = new FirmRepo();
    }
    @Test
    void deleteById_Success() {
        Firm firm = new Firm("TestCompany", "IT");
        firmRepo.save(firm);
        firmRepo.deleteById("TestCompany");
        Optional<Firm> foundFirm = firmRepo.findById("TestCompany");
        assertFalse(foundFirm.isPresent());
    }
    @Test
    void deleteById_DoesntExist() {
        Firm firm = new Firm("TestCompany", "IT");
        firmRepo.save(firm);
        assertThrows(IllegalArgumentException.class, () -> firmRepo.deleteById("RandomCompany"));
    }
    @Test
    void testSave_AlreadyExists(){
        Firm firm1 = new Firm ("TestCompany","IT");
        Firm firm2 = new Firm ("TestCompany","Finance");
        firmRepo.save(firm1);
        assertThrows(IllegalArgumentException.class, () -> firmRepo.save(firm2));
    }
    @Test
    void testFindById_Exists(){
        Firm firm = new Firm ("TestCompany","IT");
        firmRepo.save(firm);
        Optional<Firm> foundFirm = firmRepo.findById("TestCompany");
        assertTrue(foundFirm.isPresent());
        assertEquals(firm, foundFirm.get());
    }
    @Test
    void testFindById_DoestExist() {
        Firm firm = new Firm ("TestCompany","IT");
        firmRepo.save(firm);
        Optional<Firm> foundFirm = firmRepo.findById("RandomCompany");
        assertFalse(foundFirm.isPresent());
    }
}