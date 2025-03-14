package pl.edu.pg;

import java.util.Comparator;
import java.util.Set;

public class Main {
  public static void main(String[] args) {
    SortModes sortMode = SortModes.UNORDERED; // domyslnie
    Comparator<Czlowiek> comparator = Comparator.naturalOrder();
    if (args.length == 1) {
      if (args[0].equals("natural"))
        sortMode = SortModes.ORDERED;
      else if (args[0].equals("age")) {
        sortMode = SortModes.ORDERED;
        comparator = new SortByAge();
      } else if (args[0].equals("children")) {
        sortMode = SortModes.ORDERED;
        comparator = new SortByNumberOfChildren();
      }
    }
    CzlowiekPodlegliFactory.setSortMode(sortMode);
    CzlowiekPodlegliFactory.setComparator(comparator);
    Set<Czlowiek> dziadkowie = CzlowiekPodlegliFactory.wypelnijPodlegli("src/main/dane.txt");
    for (Czlowiek d : dziadkowie) {
      System.out.println(d);// normalne wypisywanie
    }
    System.out.println("------------------------");
    for (Czlowiek d : dziadkowie) {
      // wypisywanie rekursywne
      d.wypiszRekurencjnie(0);
    }
  }
}