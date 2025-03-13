package pl.edu.pg;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CzlowiekDzieciFactoryTest {

  @Test
  void dzieciPowinnyBycPrawidlowoSortowaneNaturalnie() {
    CzlowiekDzieciFactory.setSortMode(SortModes.ORDERED);
    Czlowiek dziecko1 = new Czlowiek("Jan", "Kowalski", 20,Plec.MEZCZYZNA);
    Czlowiek dziecko2 = new Czlowiek("Jan", "Nowak", 21,Plec.MEZCZYZNA);
    Czlowiek dziecko3 = new Czlowiek("Marek", "Kowalski", 21,Plec.MEZCZYZNA);
    Czlowiek dziecko4 = new Czlowiek("Marek", "Nowak", 21,Plec.MEZCZYZNA);
    Czlowiek rodzic = new Czlowiek("Jan", "Kowalski", 20,Plec.MEZCZYZNA);
    rodzic.dodajDziecko(dziecko1);
    rodzic.dodajDziecko(dziecko2);
    rodzic.dodajDziecko(dziecko3);
    rodzic.dodajDziecko(dziecko4);
    assertEquals(List.of(dziecko1, dziecko2, dziecko3, dziecko4), new ArrayList<>(rodzic.getDzieci()));
  }

  @Test
  void dzieciPowinnyBycPrawidlowoSortowaneZgodnieZKomparator() {
    CzlowiekDzieciFactory.setSortMode(SortModes.ORDERED);
    CzlowiekDzieciFactory.setComparator(Comparator.comparing(Czlowiek::getWiek));
    Czlowiek dziecko1 = new Czlowiek("Jan", "Kowalski", 20,Plec.MEZCZYZNA);
    Czlowiek dziecko2 = new Czlowiek("Jan", "Nowak", 19,Plec.MEZCZYZNA);
    Czlowiek dziecko3 = new Czlowiek("Marek", "Kowalski", 18,Plec.MEZCZYZNA);
    Czlowiek dziecko4 = new Czlowiek("Marek", "Nowak", 21,Plec.MEZCZYZNA);
    Czlowiek rodzic = new Czlowiek("Jan", "Kowalski", 50,Plec.MEZCZYZNA);
    rodzic.dodajDziecko(dziecko1);
    rodzic.dodajDziecko(dziecko2);
    rodzic.dodajDziecko(dziecko3);
    rodzic.dodajDziecko(dziecko4);
    assertEquals(List.of(dziecko3, dziecko2, dziecko1, dziecko4), new ArrayList<>(rodzic.getDzieci()));
  }

  @Test
  void dzieciPowinnyBycNiesortowane() {
    CzlowiekDzieciFactory.setSortMode(SortModes.UNORDERED);
    Czlowiek dziecko1 = new Czlowiek("Jan", "Kowalski", 20,Plec.MEZCZYZNA);
    Czlowiek dziecko2 = new Czlowiek("Jan", "Nowak", 21,Plec.MEZCZYZNA);
    Czlowiek dziecko3 = new Czlowiek("Marek", "Kowalski", 21,Plec.MEZCZYZNA);
    Czlowiek rodzic = new Czlowiek("Marek", "Nowak", 21,Plec.MEZCZYZNA);
    rodzic.dodajDziecko(dziecko1);
    rodzic.dodajDziecko(dziecko2);
    rodzic.dodajDziecko(dziecko3);
    assertEquals(List.of(dziecko1, dziecko3, dziecko2), new ArrayList<>(rodzic.getDzieci()));
  }
}