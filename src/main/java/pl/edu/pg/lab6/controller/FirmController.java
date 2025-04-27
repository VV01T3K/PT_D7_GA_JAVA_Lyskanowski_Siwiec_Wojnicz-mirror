package pl.edu.pg.lab6.controller;

import pl.edu.pg.lab6.entity.Firm;
import pl.edu.pg.lab6.repo.FirmRepo;

import java.util.Optional;

public class FirmController implements IController {
    private final FirmRepo firmRepo;

    public FirmController(FirmRepo firmRepo) {
        this.firmRepo = firmRepo;
    }

    public String save(String name, String department) {
        try {
            firmRepo.save(new Firm(name, department));
        }
        catch(IllegalArgumentException e) {
            return ControllerResponses.BAD_REQUEST.toString();
        }
        return ControllerResponses.DONE.toString();
    }

    public String delete(String name) {
        try {
            firmRepo.deleteById(name);
        }
        catch (IllegalArgumentException e){
            return ControllerResponses.NOT_FOUND.toString();
        }
        return ControllerResponses.DONE.toString();
    }

    public String find(String name){
        Optional<Firm> firm = firmRepo.findById(name);
        if (firm.isPresent()) {
            return firm.get().toString();
        } else {
            return ControllerResponses.NOT_FOUND.toString();
        }
    }

    public String findAll() {
        for (Firm firm : firmRepo.findAll()) {
            System.out.println(firm);
        }
        return ControllerResponses.DONE.toString();
    }
}
