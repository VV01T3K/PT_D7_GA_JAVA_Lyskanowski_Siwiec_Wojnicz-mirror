package pl.edu.pg;


import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
  public static void main(String[] args) throws Exception {
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

    //lab2 - watki
    DataSet dataSet = new DataSet(); //przechowywany klucz zeby dalo sie deszyfrowac
    dataSet.createData("src/queries.enc");
    //TODO - dodaawnie do kolejki jak w processQueries i zrobic to w watkach
    // na razie cos wolniej bylo a nie szybciej :<
    dataSet.processQueriesNoQueue("src/queries.enc");
  }
}