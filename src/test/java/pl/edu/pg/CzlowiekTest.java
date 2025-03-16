package pl.edu.pg;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CzlowiekTest {

  @Test
  void dodajpodlegly() {
    Czlowiek czlowiek = new Czlowiek("Jan", "Kowalski", 20, Plec.MEZCZYZNA);
    Czlowiek czlowiek2 = new Czlowiek("Jan", "Nowak", 21, Plec.MEZCZYZNA);
    czlowiek2.dodajPodleglego(czlowiek);
    assertTrue(czlowiek2.getPodlegli().contains(czlowiek));
  }

  @Test
  void testEquals() {
    Czlowiek czlowiek1 = new Czlowiek("Jan", "Kowalski", 20, Plec.MEZCZYZNA);
    Czlowiek czlowiek2 = new Czlowiek("Jan", "Kowalski", 20, Plec.MEZCZYZNA);
    assertEquals(czlowiek1, czlowiek2);
    Czlowiek czlowiek3 = new Czlowiek("Jan", "Kowalski", 21, Plec.MEZCZYZNA);
    assertNotEquals(czlowiek1, czlowiek3);
    assertNotEquals(czlowiek3, czlowiek2);
  }

  @Test
  void compareToNatural() {
    Czlowiek czlowiek1 = new Czlowiek("Jan", "Kowalski", 20, Plec.MEZCZYZNA);
    Czlowiek czlowiek3 = new Czlowiek("Jan", "Kowalski", 20, Plec.MEZCZYZNA);
    assertEquals(0, czlowiek1.compareTo(czlowiek3));
    Czlowiek czlowiek2 = new Czlowiek("Jan", "Nowak", 21, Plec.MEZCZYZNA);
    assertTrue(czlowiek1.compareTo(czlowiek2) < 0);
    Czlowiek czlowiek4 = new Czlowiek("Marek", "Kowalski", 21, Plec.MEZCZYZNA);
    assertTrue(czlowiek1.compareTo(czlowiek4) < 0);
  }
}