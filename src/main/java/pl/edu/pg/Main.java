package pl.edu.pg;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import pl.edu.pg.utils.Logger;
import pl.edu.pg.workers.Producer;
import pl.edu.pg.workers.QueryWorker;
import pl.edu.pg.workers.ResultConsumer;

import java.util.Comparator;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class Main {
  @Parameter(names = {"--order", "-o"}, description = "Set ordering mode", required = false)
  Order order = Order.NATURAL;
  @Parameter(names = {"--threads", "-t"}, description = "Set number of threads for workers (default: 2)", required = false)
  int threads = 2;
  @Parameter(names = {"--help", "-h"}, help = true)
  boolean help;

  public static void main(String[] args) {
    Main main = new Main();
    var jct = JCommander.newBuilder()
            .addObject(main)
            .build();
    jct.parse(args);
    if (main.help) {
      jct.usage();
      return;
    }
    main.setOrdering();
    main.run();
  }

  private void setOrdering() {
    SortModes sortMode = SortModes.UNORDERED; // domyslnie
    Comparator<Czlowiek> comparator = Comparator.naturalOrder();
    switch (order) {
      case NATURAL:
        sortMode = SortModes.ORDERED;
        break;
      case AGE:
        sortMode = SortModes.ORDERED;
        comparator = new SortByAge();
        break;
      case CHILDREN:
        sortMode = SortModes.ORDERED;
        comparator = new SortByNumberOfInferiors();
        break;
    }
    CzlowiekContainerFactory.setSortMode(sortMode);
    CzlowiekContainerFactory.setComparator(comparator);
  }

  private void run() {
    try (var workerPool = Executors.newFixedThreadPool(threads);
         var queuePool = Executors.newFixedThreadPool(2)) {
      for (int i = 0; i < threads; i++) {
        workerPool.submit(new QueryWorker());
      }
      queuePool.submit(new Producer());
      queuePool.submit(new ResultConsumer(String.format("results-%d.csv", threads)));
      setupHandlers(queuePool, workerPool);
      queuePool.shutdownNow();
      workerPool.shutdownNow();
      Logger.error("Shutdown!");
    }
  }

  private void setupHandlers(ExecutorService... toClose) {
    Thread shutdownHook = new Thread(() -> {
      for (var closeable : toClose) {
        closeable.shutdownNow();
      }
      for (var closeable : toClose) {
        try {
          closeable.awaitTermination(4500, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
          Logger.error("Interrupted closer: " + e.getMessage());
        }
      }
      Logger.log("Graceful shutdown!");
    });
    Runtime.getRuntime().addShutdownHook(shutdownHook);
    waitForExit();
    Runtime.getRuntime().exit(0);
  }

  private void waitForExit() {
    try (Scanner scanner = new Scanner(System.in)) {
      while (true) {
        String line = scanner.nextLine();
        Logger.error("Received: " + line);
        if (line.equals("exit")) {
          break;
        }
      }
    }
  }

  enum Order {
    NATURAL, AGE, CHILDREN
  }
}
