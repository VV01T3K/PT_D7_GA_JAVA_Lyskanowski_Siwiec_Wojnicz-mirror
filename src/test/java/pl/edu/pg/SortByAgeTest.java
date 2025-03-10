package pl.edu.pg;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.TreeSet;

public class SortByAgeTest {

        @Test
        void compareWithNull() {

                Czlowiek A = new Czlowiek("A", "A", 20, Plec.MEZCZYZNA);

                SortByAge comparator = new SortByAge();

                assertThrows(NullPointerException.class, () -> comparator.compare(null, null));
                assertThrows(NullPointerException.class, () -> comparator.compare(null, A));
                assertThrows(NullPointerException.class, () -> comparator.compare(A, null));

        }

        @Test
        void compareWithSameAge() {

                SortByAge comparator = new SortByAge();

                Czlowiek A = new Czlowiek("A", "A", 20, Plec.MEZCZYZNA);
                Czlowiek AA = new Czlowiek("A", "A", 20, Plec.MEZCZYZNA);
                Czlowiek B = new Czlowiek("B", "B", 20, Plec.MEZCZYZNA);
                Czlowiek BB = new Czlowiek("B", "B", 20, Plec.MEZCZYZNA);

                assertEquals(-1, comparator.compare(A, B)); // o1 < o2
                assertEquals(0, comparator.compare(A, AA)); // o1 == o2
                assertEquals(0, comparator.compare(B, BB)); // o1 == o2

        }

        @Test
        void compareWithDifferentAge() {

                SortByAge comparator = new SortByAge();

                Czlowiek A = new Czlowiek("A", "A", 69, Plec.MEZCZYZNA);
                Czlowiek B = new Czlowiek("B", "B", 42, Plec.MEZCZYZNA);

                assertEquals(1, comparator.compare(A, B)); // o1 > o2

                assertEquals(-1, comparator.compare(B, A)); // o1 < o2

        }

        @Test
        void treeSetTest() {

                Czlowiek A = new Czlowiek("A", "A", 42, Plec.MEZCZYZNA);
                Czlowiek B = new Czlowiek("B", "B", 69, Plec.MEZCZYZNA);
                Czlowiek C = new Czlowiek("C", "C", 21, Plec.MEZCZYZNA);

                TreeSet<Czlowiek> set = new TreeSet<>(new SortByAge());
                set.add(A);
                set.add(B);
                set.add(C);
                assertEquals(3, set.size());

                assertEquals(C, set.first());
                assertEquals(B, set.last());

        }
}
