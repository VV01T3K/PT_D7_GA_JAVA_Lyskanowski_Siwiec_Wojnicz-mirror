package pl.edu.pg;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CzlowiekPodlegliFactoryTest {

  @Test
  void PodlegliPowinnyBycPrawidlowoSortowaneNaturalnie() {
    CzlowiekPodlegliFactory.setSortMode(SortModes.ORDERED);
    Czlowiek podlegly1 = new Czlowiek("Jan", "Kowalski", 20, Plec.MEZCZYZNA);
    Czlowiek podlegly2 = new Czlowiek("Jan", "Nowak", 21, Plec.MEZCZYZNA);
    Czlowiek podlegly3 = new Czlowiek("Marek", "Kowalski", 21, Plec.MEZCZYZNA);
    Czlowiek podlegly4 = new Czlowiek("Marek", "Nowak", 21, Plec.MEZCZYZNA);
    Czlowiek rodzic = new Czlowiek("Jan", "Kowalski", 20, Plec.MEZCZYZNA);
    rodzic.dodajPodleglego(podlegly1);
    rodzic.dodajPodleglego(podlegly2);
    rodzic.dodajPodleglego(podlegly3);
    rodzic.dodajPodleglego(podlegly4);
    assertEquals(List.of(podlegly1, podlegly2, podlegly3, podlegly4), new ArrayList<>(rodzic.getPodlegli()));
  }

  @Test
  void PodlegliPowinnyBycPrawidlowoSortowaneZgodnieZKomparator() {
    CzlowiekPodlegliFactory.setSortMode(SortModes.ORDERED);
    CzlowiekPodlegliFactory.setComparator(Comparator.comparing(Czlowiek::getWiek));
    Czlowiek podlegly1 = new Czlowiek("Jan", "Kowalski", 20, Plec.MEZCZYZNA);
    Czlowiek podlegly2 = new Czlowiek("Jan", "Nowak", 19, Plec.MEZCZYZNA);
    Czlowiek podlegly3 = new Czlowiek("Marek", "Kowalski", 18, Plec.MEZCZYZNA);
    Czlowiek podlegly4 = new Czlowiek("Marek", "Nowak", 21, Plec.MEZCZYZNA);
    Czlowiek rodzic = new Czlowiek("Jan", "Kowalski", 50, Plec.MEZCZYZNA);
    rodzic.dodajPodleglego(podlegly1);
    rodzic.dodajPodleglego(podlegly2);
    rodzic.dodajPodleglego(podlegly3);
    rodzic.dodajPodleglego(podlegly4);
    assertEquals(List.of(podlegly3, podlegly2, podlegly1, podlegly4), new ArrayList<>(rodzic.getPodlegli()));
  }

  @Test
  void PodlegliPowinnyBycNiesortowane() {
    CzlowiekPodlegliFactory.setSortMode(SortModes.UNORDERED);
    Czlowiek podlegly1 = new Czlowiek("Jan", "Kowalski", 20, Plec.MEZCZYZNA);
    Czlowiek podlegly2 = new Czlowiek("Jan", "Nowak", 21, Plec.MEZCZYZNA);
    Czlowiek podlegly3 = new Czlowiek("Marek", "Kowalski", 21, Plec.MEZCZYZNA);
    Czlowiek rodzic = new Czlowiek("Marek", "Nowak", 21, Plec.MEZCZYZNA);
    rodzic.dodajPodleglego(podlegly1);
    rodzic.dodajPodleglego(podlegly2);
    rodzic.dodajPodleglego(podlegly3);
    assertAll(
        () -> assertEquals(true, rodzic.getPodlegli().contains(podlegly1)),
        () -> assertEquals(true, rodzic.getPodlegli().contains(podlegly2)),
        () -> assertEquals(true, rodzic.getPodlegli().contains(podlegly3)));
  }
}