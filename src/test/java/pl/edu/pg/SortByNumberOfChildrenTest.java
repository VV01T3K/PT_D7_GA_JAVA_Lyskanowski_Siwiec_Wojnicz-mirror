package pl.edu.pg;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.TreeSet;

public class SortByNumberOfChildrenTest {

    @Test
    void compareWithNull() {

        Czlowiek A = new Czlowiek("A", "A", 20, Plec.MEZCZYZNA);

        SortByNumberOfChildren comparator = new SortByNumberOfChildren();

        assertThrows(NullPointerException.class, () -> comparator.compare(null, null));
        assertThrows(NullPointerException.class, () -> comparator.compare(null, A));
        assertThrows(NullPointerException.class, () -> comparator.compare(A, null));

    }

    @Test
    void compareWithSameNumberOfChildren() {

        CzlowiekPodlegliFactory.setSortMode(SortModes.ORDERED);
        Czlowiek A = new Czlowiek("A", "A", 20, Plec.MEZCZYZNA);
        Czlowiek B = new Czlowiek("B", "B", 21, Plec.MEZCZYZNA);
        A.dodajPodleglego(B);
        B.dodajPodleglego(A);

        assertEquals(1, A.getPodlegli().size());
        assertEquals(1, B.getPodlegli().size());

        SortByNumberOfChildren comparator = new SortByNumberOfChildren();

        assertEquals(-1, comparator.compare(A, B)); // o1 < o2
        assertEquals(0, comparator.compare(A, A)); // o1 == o2
        assertEquals(0, comparator.compare(B, B)); // o1 == o2

    }

    @Test
    void compareWithDifferentNumberOfChildren() {

        CzlowiekPodlegliFactory.setSortMode(SortModes.ORDERED);
        Czlowiek A = new Czlowiek("A", "A", 20, Plec.MEZCZYZNA);
        Czlowiek B = new Czlowiek("B", "B", 21, Plec.MEZCZYZNA);
        Czlowiek C = new Czlowiek("C", "C", 22, Plec.MEZCZYZNA);
        A.dodajPodleglego(B);
        A.dodajPodleglego(C);

        B.dodajPodleglego(C);

        assertEquals(2, A.getPodlegli().size());
        assertEquals(1, B.getPodlegli().size());

        SortByNumberOfChildren comparator = new SortByNumberOfChildren();

        assertEquals(1, comparator.compare(A, B)); // o1 > o2

        assertEquals(-1, comparator.compare(B, A)); // o1 < o2

    }

    @Test
    void treeSetTest() {

        Czlowiek A = new Czlowiek("A", "A", 42, Plec.MEZCZYZNA);
        Czlowiek B = new Czlowiek("B", "B", 69, Plec.MEZCZYZNA);
        Czlowiek C = new Czlowiek("C", "C", 21, Plec.MEZCZYZNA);
        A.dodajPodleglego(B);
        A.dodajPodleglego(C);
        B.dodajPodleglego(C);

        assertEquals(2, A.getPodlegli().size());
        assertEquals(1, B.getPodlegli().size());
        assertEquals(0, C.getPodlegli().size());

        TreeSet<Czlowiek> set = new TreeSet<>(new SortByNumberOfChildren());
        set.add(A);
        set.add(B);
        set.add(C);
        assertEquals(3, set.size());

        assertEquals(C, set.first());
        assertEquals(A, set.last());

    }
}
