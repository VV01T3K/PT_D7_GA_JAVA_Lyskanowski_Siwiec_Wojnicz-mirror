package pl.edu.pg;

import java.util.Objects;
import java.util.Set;

public class Czlowiek implements Comparable<Czlowiek> {
  private final String imie;
  private final String nazwisko;
  private final int wiek;
  private final Plec plec;
  private Set<Czlowiek> podlegli;

  private final MartialStatus stanCywilny;
  private final String wyksztalcenie;
  private final String pozycjaZawodowa;
  private final String numerTelefonu;

  Czlowiek(String imie,
      String nazwisko,
      int wiek,
      Plec plec,
      MartialStatus stanCywilny,
      String wyksztalcenie,
      String pozycjaZawodowa,
      String numerTelefonu) {
    this.imie = imie;
    this.nazwisko = nazwisko;
    this.wiek = wiek;
    this.plec = plec;
    this.podlegli = CzlowiekPodlegliFactory.chooseSet();
    this.stanCywilny = stanCywilny;
    this.wyksztalcenie = wyksztalcenie;
    this.pozycjaZawodowa = pozycjaZawodowa;
    this.numerTelefonu = numerTelefonu;
  }

  Czlowiek(String imie,
      String nazwisko,
      int wiek,
      Plec plec) {
    this.imie = imie;
    this.nazwisko = nazwisko;
    this.wiek = wiek;
    this.plec = plec;
    this.podlegli = CzlowiekPodlegliFactory.chooseSet();
    this.stanCywilny = MartialStatus.NEVER_MARRIED;
    this.wyksztalcenie = "";
    this.pozycjaZawodowa = "";
    this.numerTelefonu = "";
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

  public MartialStatus getStanCywilny() {
    return stanCywilny;
  }

  public String getWyksztalcenie() {
    return wyksztalcenie;
  }

  public String getPozycjaZawodowa() {
    return pozycjaZawodowa;
  }

  public String getNumerTelefonu() {
    return numerTelefonu;
  }

  public Set<Czlowiek> getPodlegli() {
    return podlegli;
  }

  public void dodajPodleglego(Czlowiek osoba) {
    podlegli.add(osoba);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    Czlowiek czlowiek = (Czlowiek) o;
    return wiek == czlowiek.wiek &&
        Objects.equals(imie, czlowiek.imie) &&
        Objects.equals(plec, czlowiek.plec) &&
        Objects.equals(nazwisko, czlowiek.nazwisko) &&
        Objects.equals(podlegli, czlowiek.podlegli) &&
        Objects.equals(stanCywilny, czlowiek.stanCywilny) &&
        Objects.equals(wyksztalcenie, czlowiek.wyksztalcenie) &&
        Objects.equals(pozycjaZawodowa, czlowiek.pozycjaZawodowa) &&
        Objects.equals(numerTelefonu, czlowiek.numerTelefonu);
  }

  @Override
  public int compareTo(Czlowiek other) {
    // tutaj coś pewnie trzeba zmienić
    if (this.imie.compareToIgnoreCase(other.imie) != 0)
      return this.imie.compareToIgnoreCase(other.imie);
    if (this.nazwisko.compareToIgnoreCase(other.nazwisko) != 0)
      return this.nazwisko.compareToIgnoreCase(other.nazwisko);
    if (this.wiek != other.wiek)
      return Integer.compare(this.wiek, other.wiek);
    if (this.podlegli.size() != other.podlegli.size())
      return Integer.compare(this.podlegli.size(), other.podlegli.size());
    return this.plec.compareTo(other.plec);
  }

  @Override
  public String toString() {
    return "Czlowiek{" +
        "imie='" + imie + '\'' +
        ", nazwisko='" + nazwisko + '\'' +
        ", wiek=" + wiek +
        ", plec=" + plec +
        ", Podlegli=" + podlegli +
        '}';
  }

  @Override
  public int hashCode() {
    return Objects.hash(imie, nazwisko, wiek, plec, podlegli, stanCywilny, wyksztalcenie, pozycjaZawodowa,
        numerTelefonu);
  }

  public void wypiszBezPodlegli() {
    System.out.println("Czlowiek{" +
        "imie='" + imie + '\'' +
        ", nazwisko='" + nazwisko + '\'' +
        ", wiek=" + wiek +
        ", plec=" + plec +
        '}');
  }

  public void wypiszRekurencjnie(int spacje) {
    System.out.printf("  ".repeat(spacje));
    wypiszBezPodlegli();
    if (podlegli.isEmpty()) {
      return;
    }
    for (Czlowiek d : podlegli) {
      d.wypiszRekurencjnie(spacje + 2);
    }
  }
}
