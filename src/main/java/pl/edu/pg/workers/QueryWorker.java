package pl.edu.pg.workers;

import pl.edu.pg.buffer.InputQueue;
import pl.edu.pg.buffer.OutputQueue;
import pl.edu.pg.queries.executors.QueryExecutorFactory;
import pl.edu.pg.utils.Logger;
import pl.edu.pg.utils.TimeMeasure;

import java.nio.channels.OverlappingFileLockException;

public class QueryWorker implements Runnable {

  @Override
  public void run() {
    while (!Thread.currentThread().isInterrupted()) {
      try {
        var timer = new TimeMeasure();
        var data = InputQueue.getInstance().read();
        if (data.isEmpty()) continue;
        var query = data.get();
        Logger.log("Processing query: " + query);
        try {
          timer.start();
          var response = QueryExecutorFactory.createQueryExecutor(query).execute(query.getArguments());
          timer.stopAndReport("Query processed:" + query);
          OutputQueue.getInstance().write(response);
        } catch (OverlappingFileLockException ex) {
          // Retry later
          Logger.error("Query was locked, try again later: " + ex.getMessage());
          InputQueue.getInstance().write(query);
        }
      } catch (InterruptedException e) {
        Logger.error("Interrupted worker: " + e.getMessage());
        Thread.currentThread().interrupt();
      } catch (Exception e) {
        Logger.error("Error processing query: " + e.getMessage());
      }
    }
  }
}
