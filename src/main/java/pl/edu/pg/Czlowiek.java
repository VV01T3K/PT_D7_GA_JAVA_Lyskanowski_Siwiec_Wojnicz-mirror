package pl.edu.pg;

import java.util.Objects;
import java.util.Set;

import com.google.gson.annotations.Since;

public class Czlowiek implements Comparable<Czlowiek> {

  @Since(1.0)
  private final String imie;
  @Since(1.0)
  private final String nazwisko;
  @Since(1.0)
  private final int wiek;
  @Since(1.0)
  private final Plec plec;
  @Since(1.0)
  private final MartialStatus stanCywilny;
  @Since(1.0)
  private final String wyksztalcenie;
  @Since(1.0)
  private final String pozycjaZawodowa;
  @Since(1.0)
  private final String numerTelefonu;
  @Since(1.0)
  private Set<Czlowiek> podlegli;

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
    this.podlegli = CzlowiekContainerFactory.chooseSet();
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
    this.podlegli = CzlowiekContainerFactory.chooseSet();
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

  private int getPodlegliCountRecursive(Czlowiek person) {
    int count = 0;
    for (Czlowiek podlegly : person.getPodlegli()) {
      count += getPodlegliCountRecursive(podlegly);
    }
    return count + person.getPodlegli().size();
  }

  public int getAllInferiorsCount() {
    if (this.podlegli.isEmpty())
      return 0;
    return getPodlegliCountRecursive(this);
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
        ", podlegli=[" + podlegli.size() + ']' +
        ", inferiors=['" + getAllInferiorsCount() + ']' +
        '}';
  }

  @Override
  public int hashCode() {
    return Objects.hash(imie, nazwisko, wiek, plec, podlegli, stanCywilny, wyksztalcenie, pozycjaZawodowa,
        numerTelefonu);
  }

  public void printRecursively() {
    printRecursively(this, 0);
  }

  private void printRecursively(Czlowiek head, int depth) {
    String prefix;
    if (depth == 0)
      prefix = "";
    else
      prefix = " ".repeat(depth * 4) + "⮡ ";
    System.out.println(prefix + head.toString());
    for (Czlowiek podlegly : head.getPodlegli()) {
      printRecursively(podlegly, depth + 1);
    }
  }

  public String toStringAllDetails() {
    return "Czlowiek {" +
        "\n    imie='" + imie + '\'' +
        "\n    nazwisko='" + nazwisko + '\'' +
        "\n    wiek=" + wiek +
        "\n    plec=" + plec +
        "\n    stanCywilny='" + stanCywilny + '\'' +
        "\n    wyksztalcenie='" + wyksztalcenie + '\'' +
        "\n    pozycjaZawodowa='" + pozycjaZawodowa + '\'' +
        "\n    numerTelefonu='" + numerTelefonu + '\'' +
        "\n    podlegliCount=[" + podlegli.size() + ']' +
        "\n    inferiorsCount=['" + getAllInferiorsCount() + ']' +
        "\n}";
  }

  public void printAllDetails() {
    System.out.println(toStringAllDetails());
  }

}
