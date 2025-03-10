package pl.edu.pg;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SortByAgeTest {

    @Test
    void compareWithNull() {
        SortByAge sortByAge = new SortByAge();

        Czlowiek A = new Czlowiek("A", "A", 20, null);

        assertEquals(0, sortByAge.compare(null, null)); // null === null

        assertEquals(-1, sortByAge.compare(null, A)); // null == o1

        assertEquals(1, sortByAge.compare(A, null)); // o2 == null
    }

    @Test
    void compareWithSameAge() {
        SortByAge sortByAge = new SortByAge();

        Czlowiek A = new Czlowiek("A", "A", 20, null);
        Czlowiek B = new Czlowiek("B", "B", 20, null);

        assertEquals(0, sortByAge.compare(A, B)); // o1 == o2
        assertEquals(0, sortByAge.compare(A, A));
        assertEquals(0, sortByAge.compare(B, B));
    }

    @Test
    void compareWithDifferentAge() {
        SortByAge sortByAge = new SortByAge();

        Czlowiek A_old = new Czlowiek("A", "A", 20, null);
        Czlowiek B_young = new Czlowiek("B", "B", 19, null);

        assertEquals(1, sortByAge.compare(A_old, B_young)); // o1 > o2

        assertEquals(-1, sortByAge.compare(B_young, A_old)); // o1 < o2
    }
}
