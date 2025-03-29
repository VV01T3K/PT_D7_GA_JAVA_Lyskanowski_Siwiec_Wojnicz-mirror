package pl.edu.pg;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import pl.edu.pg.workers.Producer;
import pl.edu.pg.workers.QueryWorker;
import pl.edu.pg.workers.ResultConsumer;

import java.util.Comparator;
import java.util.concurrent.Executors;


public class Main {
  @Parameter(names = {"--order", "-o"}, description = "Set ordering mode", required = false)
  Order order = Order.NATURAL;

  @Parameter(names = {"--threads", "-t"}, description = "Set number of threads for workers (default: 2)", required = false)
  int threads = 2;

  public static void main(String[] args) {
    Main main = new Main();
    JCommander.newBuilder()
            .addObject(main)
            .build()
            .parse(args);
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
         var outputPool = Executors.newSingleThreadExecutor()) {
      for (int i = 0; i < threads; i++) {
        workerPool.submit(new QueryWorker());
      }
      outputPool.submit(new ResultConsumer());
      new Producer().run();
    }
  }

  enum Order {
    NATURAL, AGE, CHILDREN
  }
}
