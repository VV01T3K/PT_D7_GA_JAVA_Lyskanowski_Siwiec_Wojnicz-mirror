package pl.edu.pg;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SortByNumberOfChildrenTest {

    @Test
    void compareWithNull() {

        CzlowiekDzieciFactory.setSortMode(SortModes.ORDERED);
        Czlowiek A = new Czlowiek("A", "A", 20,Plec.MEZCZYZNA);
        Czlowiek B = new Czlowiek("B", "B", 21,Plec.MEZCZYZNA);
        Czlowiek C = new Czlowiek("C", "C", 22,Plec.MEZCZYZNA);
        A.dodajDziecko(B);
        A.dodajDziecko(C);

        assertEquals(2, A.getDzieci().size());

        SortByNumberOfChildren comparator = new SortByNumberOfChildren();

        assertEquals(0, comparator.compare(null, null)); // null === null

        assertEquals(-1, comparator.compare(null, A)); // null == o1

        assertEquals(1, comparator.compare(A, null)); // o2 == null

    }

    @Test
    void compareWithSameAge() {

        CzlowiekDzieciFactory.setSortMode(SortModes.ORDERED);
        Czlowiek A = new Czlowiek("A", "A", 20,Plec.MEZCZYZNA);
        Czlowiek B = new Czlowiek("B", "B", 21,Plec.MEZCZYZNA);
        A.dodajDziecko(B);
        B.dodajDziecko(A);

        assertEquals(1, A.getDzieci().size());
        assertEquals(1, B.getDzieci().size());

        SortByNumberOfChildren comparator = new SortByNumberOfChildren();

        assertEquals(0, comparator.compare(A, B)); // o1 == o2
        assertEquals(0, comparator.compare(A, A));
        assertEquals(0, comparator.compare(B, B));

    }

    @Test
    void compareWithDifferentAge() {

        CzlowiekDzieciFactory.setSortMode(SortModes.ORDERED);
        Czlowiek A = new Czlowiek("A", "A", 20,Plec.MEZCZYZNA);
        Czlowiek B = new Czlowiek("B", "B", 21,Plec.MEZCZYZNA);
        Czlowiek C = new Czlowiek("C", "C", 22,Plec.MEZCZYZNA);
        A.dodajDziecko(B);
        A.dodajDziecko(C);

        B.dodajDziecko(C);

        assertEquals(2, A.getDzieci().size());
        assertEquals(1, B.getDzieci().size());

        SortByNumberOfChildren comparator = new SortByNumberOfChildren();

        assertEquals(1, comparator.compare(A, B)); // o1 > o2

        assertEquals(-1, comparator.compare(B, A)); // o1 < o2

    }
}
