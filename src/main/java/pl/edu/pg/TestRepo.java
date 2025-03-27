package pl.edu.pg;

import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class TestRepo {

  private static final TestRepoGenerator generator = new TestRepoGenerator();
  private static Set<Czlowiek> heads = CzlowiekContainerFactory.chooseSet();
  private static TestRepoJsonLoader loader = new TestRepoJsonLoader(1.0, "Data/", "people.json");

  public static void setLoader(TestRepoJsonLoader loader) {
    TestRepo.loader = loader;
  }

  public static Set<Czlowiek> getHeads() {
    return heads;
  }

  // sorted
  public static Stream<Czlowiek> getAllPeopleStream() {
    return flattendTree().stream();
  }

  public static void saveJson() {
    loader.saveJson(heads);
  }

  public static void saveJsonAsSeparateFiles() {
    loader.saveJson(heads.stream());
  }

  public static void loadJson() {
    heads = loader.readJson();
  }

  public static void generateTestData(int n) {
    heads = generator.generateTestData(n);
  }

  // unsorted
  public static void recursivelyApplyFunction(Consumer<Czlowiek> function) {
    for (Czlowiek head : heads) {
      function.accept(head);
      recursivelyApplyFunction(head, function);
    }
  }

  // unsorted
  private static void recursivelyApplyFunction(Czlowiek current, Consumer<Czlowiek> function) {
    for (Czlowiek podlegly : current.getPodlegli()) {
      function.accept(podlegly);
      recursivelyApplyFunction(podlegly, function);
    }
  }

  // sorted
  private static Set<Czlowiek> flattendTree() {
    Set<Czlowiek> all = CzlowiekContainerFactory.chooseSet();
    for (Czlowiek head : heads) {
      all.add(head);
      recursivelyApplyFunction(head, all::add);
    }
    return all;
  }

  public static void printRecursively() {
    for (Czlowiek head : heads) {
      head.printRecursively();
    }
  }

  public static void main(String[] args) {

    CzlowiekContainerFactory.setSortMode(SortModes.ORDERED);
    CzlowiekContainerFactory.setComparator(new SortByNumberOfInferiors());

    long startTime = System.currentTimeMillis();
    generateTestData(50);
    // printRecursively();

    saveJson();

    // saveJsonAsSeparateFiles();

    Producer.generateQueryPool();

    Producer producer = new Producer();
    Thread thread = new Thread(producer);
    thread.start();
    try {
      thread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    long endTime = System.currentTimeMillis();
    System.out.println("Time taken to generate and save data: " + (endTime - startTime) + "ms");
  }
}