package pl.edu.pg;

// import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.TimeUnit;

public class Main {
  public static void main(String[] args) {
    // SortModes sortMode = SortModes.UNORDERED; // domyslnie
    // Comparator<Czlowiek> comparator = Comparator.naturalOrder();
    // if (args.length == 1) {
    // switch (args[0]) {
    // case "natural":
    // sortMode = SortModes.ORDERED;
    // break;
    // case "age":
    // sortMode = SortModes.ORDERED;
    // comparator = new SortByAge();
    // break;
    // case "children":
    // sortMode = SortModes.ORDERED;
    // comparator = new SortByNumberOfInferiors();
    // break;
    // }
    // }
    // CzlowiekContainerFactory.setSortMode(sortMode);
    // CzlowiekContainerFactory.setComparator(comparator);

    // TestRepo.loadJson();
    // TestRepo.printRecursively();

    // System.out.println("Mapa ludzi i liczby podleglych:");
    // var podlegajacyMap =
    // CzlowiekCountMap.czlowiekPodleglajacyCountMap(TestRepo.getAllPeopleStream());
    // System.out.println(podlegajacyMap);

    Producer producer = new Producer();
    Thread producerThread = new Thread(producer);
    producerThread.start();

    System.out.println(Producer.getQueryPool().size() + " queries in the pool.");

    System.out.println("Starting thread pool execution...");
    long startTime = System.currentTimeMillis();

    int poolSize = 15;
    ExecutorService executor = Executors.newFixedThreadPool(poolSize);

    AtomicInteger counter = new AtomicInteger(0);

    try {
      producerThread.join();
    } catch (InterruptedException e) {
      System.err.println("Producer thread join was interrupted: " + e.getMessage());
    }

    for (int i = 0; i < poolSize; i++) {
      executor.execute(new Worker(counter));
    }

    // Shut down the thread pool
    executor.shutdown();

    try {
      // Wait indefinitely for all tasks to complete
      executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
    } catch (InterruptedException e) {
      executor.shutdownNow();
      Thread.currentThread().interrupt();
    }

    long endTime = System.currentTimeMillis();
    long totalTime = endTime - startTime;
    System.out.println("All tasks completed (or timeout occurred).");
    System.out.println("Total execution time: " + totalTime + " ms");

    System.out.println("Total number of queries executed: " + counter.get());
  }
}