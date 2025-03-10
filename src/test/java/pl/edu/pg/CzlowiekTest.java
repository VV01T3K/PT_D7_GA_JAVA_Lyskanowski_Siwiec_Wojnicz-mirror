package pl.edu.pg;

import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class CzlowiekTest {

  @Test
  void dodajDziecko() {
    Czlowiek czlowiek = new Czlowiek("Jan", "Kowalski", 20, new HashSet<>());
    Czlowiek czlowiek2 = new Czlowiek("Jan", "Nowak", 21, new HashSet<>());
    czlowiek2.dodajDziecko(czlowiek);
    assertTrue(czlowiek2.getDzieci().contains(czlowiek));
  }

  @Test
  void testEquals() {
    HashSet<Czlowiek> znajomi = new HashSet<>();
    Czlowiek czlowiek1 = new Czlowiek("Jan", "Kowalski", 20, znajomi);
    Czlowiek czlowiek2 = new Czlowiek("Jan", "Kowalski", 20, znajomi);
    assertEquals(czlowiek1, czlowiek2);
    Czlowiek czlowiek3 = new Czlowiek("Jan", "Kowalski", 21, znajomi);
    assertNotEquals(czlowiek1, czlowiek3);
    assertNotEquals(czlowiek3, czlowiek2);
  }

  @Test
  void compareToNatural() {
    Czlowiek czlowiek1 = new Czlowiek("Jan", "Kowalski", 20, new HashSet<>());
    Czlowiek czlowiek3 = new Czlowiek("Jan", "Kowalski", 21, new HashSet<>());
    assertEquals(0, czlowiek1.compareTo(czlowiek3));
    Czlowiek czlowiek2 = new Czlowiek("Jan", "Nowak", 21, new HashSet<>());
    assertTrue(czlowiek1.compareTo(czlowiek2) < 0);
    Czlowiek czlowiek4 = new Czlowiek("Marek", "Kowalski", 21, new HashSet<>());
    assertTrue(czlowiek1.compareTo(czlowiek4) < 0);

  }
}