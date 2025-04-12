package pl.edu.pg.persistance.builders;

import pl.edu.pg.persistance.models.Czlowiek;
import pl.edu.pg.persistance.models.Firma;
import pl.edu.pg.persistance.repository.FirmaRepository;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class CzlowiekBuilder implements IBuilder<Czlowiek> {

  String imie;
  String nazwisko;
  String numerTelefonu;
  Boolean plec;
  String stanCywilny;
  String wyksztalcenie;
  String pozycjaZawodowa;
  Firma firma;

  public CzlowiekBuilder dodajFirme(String nazwaFirmy) throws NoSuchElementException {
    if (nazwaFirmy.equals("brak")) {
      this.firma = null;
    } else {
      firma = new FirmaRepository().findByName(nazwaFirmy).get();
    }
    return this;
  }

  @Override
  public IBuilder<Czlowiek> buildInteractive(Scanner scanner) throws NoSuchElementException {
    System.out.println("Podaj imie:");
    imie = scanner.nextLine();
    System.out.println("Podaj nazwisko:");
    nazwisko = scanner.nextLine();
    System.out.println("Podaj numer telefonu:");
    numerTelefonu = scanner.nextLine();
    System.out.println("Podaj plec (M/K):");
    plec = scanner.nextLine().equals("M");
    System.out.println("Podaj stan cywilny:");
    stanCywilny = scanner.nextLine();
    System.out.println("Podaj wyksztalcenie:");
    wyksztalcenie = scanner.nextLine();
    System.out.println("Podaj pozycje zawodowa:");
    pozycjaZawodowa = scanner.nextLine();
    System.out.println("Podaj nazwe firmy (jesli nie dotyczy, wpisz 'brak'):");
    String firmaNazwa = scanner.nextLine();
    return this.dodajFirme(firmaNazwa);
  }

  @Override
  public Czlowiek build() {
    return new Czlowiek(imie, nazwisko, numerTelefonu, plec, stanCywilny, wyksztalcenie, pozycjaZawodowa, firma);
  }
}
