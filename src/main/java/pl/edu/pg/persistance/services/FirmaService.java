package pl.edu.pg.persistance.services;

import pl.edu.pg.persistance.models.Czlowiek;
import pl.edu.pg.persistance.models.Firma;
import pl.edu.pg.persistance.repository.FirmaRepository;

import java.util.Optional;

public class FirmaService extends CrudService<Firma> {

  public FirmaService(FirmaRepository firmaRepo) {
    super(firmaRepo);
  }

  public Optional<Firma> addPracownik(int firmaId, Czlowiek czlowiek) {
    Optional<Firma> firma = repository.findById(firmaId);
    if (firma.isPresent()) {
      var pracownicy = firma.get().getPracownicy();
      pracownicy.add(czlowiek);
      repository.update(firma.get());
      return Optional.of(firma.get());
    } else {
      throw new IllegalArgumentException("Firma not found");
    }
  }
}
