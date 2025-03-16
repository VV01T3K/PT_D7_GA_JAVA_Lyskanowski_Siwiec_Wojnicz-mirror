package pl.edu.pg;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CzlowiekCountMapTest {
  @Test
  void UtworzonaMapaPodleglychPowinnaBycObliczonaPrawidlowo() {
    CzlowiekContainerFactory.setSortMode(SortModes.UNORDERED);
    Czlowiek podlegly1 = new Czlowiek("Jan", "Kowalski", 20, Plec.MEZCZYZNA);
    Czlowiek podlegly2 = new Czlowiek("Jan", "Nowak", 21, Plec.MEZCZYZNA);
    Czlowiek podlegly3 = new Czlowiek("Marek", "Kowalski", 21, Plec.MEZCZYZNA);
    Czlowiek rodzic = new Czlowiek("Marek", "Nowak", 21, Plec.MEZCZYZNA);
    podlegly2.dodajPodleglego(podlegly3);
    podlegly1.dodajPodleglego(podlegly2);
    rodzic.dodajPodleglego(podlegly1);

    Set<Czlowiek> ludzie = Set.of(rodzic, podlegly1, podlegly2, podlegly3);

    CzlowiekCountMap czlowiekCountMap = CzlowiekCountMap.czlowiekPodleglajacyCountMap(ludzie.stream());
    assertEquals(4, czlowiekCountMap.czlowiekPodleglajacyCountMap.size());
    assertEquals(3, czlowiekCountMap.czlowiekPodleglajacyCountMap.get(rodzic));
    assertEquals(2, czlowiekCountMap.czlowiekPodleglajacyCountMap.get(podlegly1));
    assertEquals(1, czlowiekCountMap.czlowiekPodleglajacyCountMap.get(podlegly2));
    assertEquals(0, czlowiekCountMap.czlowiekPodleglajacyCountMap.get(podlegly3));
  }

}