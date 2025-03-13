package pl.edu.pg;

import java.util.Objects;
import java.util.Set;

public class Czlowiek implements Comparable<Czlowiek> {
  private final String imie;
  private final String nazwisko;
  private final int wiek;
  private final Plec plec;
  Set<Czlowiek> dzieci;

  Czlowiek(String imie, String nazwisko, int wiek, Plec plec) {
    this.imie = imie;
    this.nazwisko = nazwisko;
    this.wiek = wiek;
    this.plec = plec;
    this.dzieci = CzlowiekDzieciFactory.chooseSet();
  }

  public int getWiek() {
    return wiek;
  }

  public String getImie() {
    return imie;
  }

  public String getNazwisko() {
    return nazwisko;
  }

  public Plec getPlec() {
    return plec;
  }

  public Set<Czlowiek> getDzieci() {
    return dzieci;
  }

  public void dodajDziecko(Czlowiek dziecko) {
    dzieci.add(dziecko);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    Czlowiek czlowiek = (Czlowiek) o;
    return wiek == czlowiek.wiek && Objects.equals(imie, czlowiek.imie) && plec == czlowiek.plec &&
        Objects.equals(nazwisko, czlowiek.nazwisko) && Objects.equals(dzieci, czlowiek.dzieci);
  }

  @Override
  public int compareTo(Czlowiek other) {
    if (this.imie.compareToIgnoreCase(other.imie) != 0) {
      return this.imie.compareToIgnoreCase(other.imie);
    }
    return this.nazwisko.compareToIgnoreCase(other.nazwisko);
  }

  @Override
  public String toString() {
    return "Czlowiek{" +
        "imie='" + imie + '\'' +
        ", nazwisko='" + nazwisko + '\'' +
        ", wiek=" + wiek +
        ", plec=" + plec +
        ", dzieci=" + dzieci +
        '}';
  }

  @Override
  public int hashCode() {
    return Objects.hash(imie, nazwisko, wiek, plec, dzieci);
  }

  public void wypiszBezDzieci() {
    System.out.println("Czlowiek{" +
        "imie='" + imie + '\'' +
        ", nazwisko='" + nazwisko + '\'' +
        ", wiek=" + wiek +
        ", plec=" + (plec == Plec.MEZCZYZNA ? "M" : "K") +
        '}');
  }

  public void wypiszRekurencjnie(int spacje) {
    System.out.printf("  ".repeat(spacje));
    wypiszBezDzieci();
    if (dzieci.isEmpty()) {
      return;
    }
    for (Czlowiek d : dzieci) {
      d.wypiszRekurencjnie(spacje + 2);
    }
  }
}
