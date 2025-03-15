package pl.edu.pg;

import java.util.Comparator;

public class Main {
  public static void main(String[] args) {
    SortModes sortMode = SortModes.UNORDERED; // domyslnie
    Comparator<Czlowiek> comparator = Comparator.naturalOrder();
    if (args.length == 1) {
      switch (args[0]) {
        case "natural":
          sortMode = SortModes.ORDERED;
          break;
        case "age":
          sortMode = SortModes.ORDERED;
          comparator = new SortByAge();
          break;
        case "children":
          sortMode = SortModes.ORDERED;
          comparator = new SortByNumberOfInferiors();
          break;
      }
    }
    CzlowiekContainerFactory.setSortMode(sortMode);
    CzlowiekContainerFactory.setComparator(comparator);

    TestRepo.generateTestData();
    TestRepo.saveJson();
    TestRepo.getHeads().clear();
    TestRepo.loadJson();
    TestRepo.printRecursively();
    // więcej testów w TestRepo main
    // TestRepo.printAll();

  }
}