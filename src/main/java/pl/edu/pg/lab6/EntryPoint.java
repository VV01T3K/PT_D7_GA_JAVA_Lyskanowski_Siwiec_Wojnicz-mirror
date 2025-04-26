package pl.edu.pg.lab6;

import pl.edu.pg.lab6.entity.Firm;
import pl.edu.pg.lab6.entity.Person;
import pl.edu.pg.lab6.repo.FirmRepo;
import pl.edu.pg.lab6.repo.PersonRepo;


public class EntryPoint {
    public static void main(String[] args) {
        FirmRepo firmRepo = new FirmRepo();
        PersonRepo personRepo = new PersonRepo();
        Firm firm = DataGenerator.generateFirm();
        Person person = DataGenerator.generatePerson(firm);
        firmRepo.save(firm);
        personRepo.save(person);
        System.out.println(firmRepo);
        System.out.println(personRepo);
    }
}
