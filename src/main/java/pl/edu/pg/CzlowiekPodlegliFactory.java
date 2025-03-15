package pl.edu.pg;

import java.util.*;

public class CzlowiekPodlegliFactory {
  private static SortModes sortMode = SortModes.UNORDERED;
  private static Comparator<Czlowiek> comparator = Comparator.naturalOrder();

  public static void setSortMode(SortModes sortMode) {
    CzlowiekPodlegliFactory.sortMode = sortMode;
  }

  public static void setComparator(Comparator<Czlowiek> comparator) {
    CzlowiekPodlegliFactory.comparator = comparator;
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

}
