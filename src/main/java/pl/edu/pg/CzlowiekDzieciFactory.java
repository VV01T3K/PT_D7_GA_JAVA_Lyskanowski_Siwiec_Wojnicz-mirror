package pl.edu.pg;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class CzlowiekDzieciFactory {
  private static SortModes sortMode = SortModes.UNORDERED;
  private static Comparator<Czlowiek> comparator = Comparator.naturalOrder();

  public static void setSortMode(SortModes sortMode) {
    CzlowiekDzieciFactory.sortMode = sortMode;
  }

  public static void setComparator(Comparator<Czlowiek> comparator) {
    CzlowiekDzieciFactory.comparator = comparator;
  }

  public static Set<Czlowiek> createDzieci() {
    if (sortMode == SortModes.ORDERED) {
      if (comparator == null) return new TreeSet<>();
      else return new TreeSet<>(comparator);
    } else {
      return new HashSet<>();
    }
  }
}
