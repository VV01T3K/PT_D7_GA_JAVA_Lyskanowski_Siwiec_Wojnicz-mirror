package pl.edu.pg;

import java.util.*;

public class CzlowiekContainerFactory {
  private static SortModes sortMode = SortModes.UNORDERED;
  private static Comparator<Czlowiek> comparator = Comparator.naturalOrder();

  public static void setSortMode(SortModes sortMode) {
    CzlowiekContainerFactory.sortMode = sortMode;
  }

  public static void setComparator(Comparator<Czlowiek> comparator) {
    CzlowiekContainerFactory.comparator = comparator;
  }

  public static Set<Czlowiek> chooseSet() {
    if (sortMode == SortModes.ORDERED) {
      if (comparator == null)
        return new TreeSet<>();
      else
        return new TreeSet<>(comparator);
    } else {
      return new HashSet<>();
    }
  }

  public static <T> Map<Czlowiek, T> chooseMap() {
    if (sortMode == SortModes.ORDERED) {
      if (comparator == null)
        return new TreeMap<>();
      else
        return new TreeMap<>(comparator);
    } else {
      return new HashMap<>();
    }
  }

}
