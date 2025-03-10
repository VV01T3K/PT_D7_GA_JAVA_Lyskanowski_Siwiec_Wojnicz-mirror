package pl.edu.pg;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CzlowiekTest {

  @Test
  void dodajDziecko() {
    Czlowiek czlowiek = new Czlowiek("Jan", "Kowalski", 20);
    Czlowiek czlowiek2 = new Czlowiek("Jan", "Nowak", 21);
    czlowiek2.dodajDziecko(czlowiek);
    assertTrue(czlowiek2.getDzieci().contains(czlowiek));
  }

  @Test
  void testEquals() {
    Czlowiek czlowiek1 = new Czlowiek("Jan", "Kowalski", 20);
    Czlowiek czlowiek2 = new Czlowiek("Jan", "Kowalski", 20);
    assertEquals(czlowiek1, czlowiek2);
    Czlowiek czlowiek3 = new Czlowiek("Jan", "Kowalski", 21);
    assertNotEquals(czlowiek1, czlowiek3);
    assertNotEquals(czlowiek3, czlowiek2);
  }

  @Test
  void compareToNatural() {
    Czlowiek czlowiek1 = new Czlowiek("Jan", "Kowalski", 20);
    Czlowiek czlowiek3 = new Czlowiek("Jan", "Kowalski", 21);
    assertEquals(0, czlowiek1.compareTo(czlowiek3));
    Czlowiek czlowiek2 = new Czlowiek("Jan", "Nowak", 21);
    assertTrue(czlowiek1.compareTo(czlowiek2) < 0);
    Czlowiek czlowiek4 = new Czlowiek("Marek", "Kowalski", 21);
    assertTrue(czlowiek1.compareTo(czlowiek4) < 0);
  }
}