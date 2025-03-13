package pl.edu.pg;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SortByAgeTest {

    @Test
    void compareWithNull() {

        SortByAge comparator = new SortByAge();

        Czlowiek A = new Czlowiek("A", "A", 20,Plec.MEZCZYZNA);

        assertEquals(0, comparator.compare(null, null)); // null === null

        assertEquals(-1, comparator.compare(null, A)); // null == o1

        assertEquals(1, comparator.compare(A, null)); // o2 == null

    }

    @Test
    void compareWithSameAge() {

        SortByAge comparator = new SortByAge();

        Czlowiek A = new Czlowiek("A", "A", 20,Plec.MEZCZYZNA);
        Czlowiek B = new Czlowiek("B", "B", 20,Plec.MEZCZYZNA);

        assertEquals(0, comparator.compare(A, B)); // o1 == o2
        assertEquals(0, comparator.compare(A, A));
        assertEquals(0, comparator.compare(B, B));

    }

    @Test
    void compareWithDifferentAge() {

        SortByAge comparator = new SortByAge();

        Czlowiek A_old = new Czlowiek("A", "A", 20,Plec.MEZCZYZNA);
        Czlowiek B_young = new Czlowiek("B", "B", 19,Plec.MEZCZYZNA);

        assertEquals(1, comparator.compare(A_old, B_young)); // o1 > o2

        assertEquals(-1, comparator.compare(B_young, A_old)); // o1 < o2

    }
}
