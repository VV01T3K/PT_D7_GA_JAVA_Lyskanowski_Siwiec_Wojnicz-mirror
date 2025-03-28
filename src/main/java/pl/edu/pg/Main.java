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

    TestRepo.loadJson();
    TestRepo.printRecursively();

    System.out.println("Mapa ludzi i liczby podleglych:");
    var podlegajacyMap = CzlowiekCountMap.czlowiekPodleglajacyCountMap(TestRepo.getAllPeopleStream());
    System.out.println(podlegajacyMap);

    Consumer consumer = new Consumer(); // test consumer
    consumer.readQueriesFromFile("Data/queries.txt");
    try {
      consumer.processQueries();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
