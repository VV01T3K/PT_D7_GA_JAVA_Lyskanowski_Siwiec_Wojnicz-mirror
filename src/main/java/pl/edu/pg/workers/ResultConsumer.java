package pl.edu.pg.workers;

import pl.edu.pg.buffer.OutputQueue;
import pl.edu.pg.utils.Logger;

public class ResultConsumer implements Runnable {
  @Override
  public void run() {
    while (true) {
      try {
        var data = OutputQueue.getInstance().read();
        if (data.isEmpty()) continue;
        var result = data.get();
        Logger.log("Received result: " + result);
      } catch (Exception e) {
        Logger.error("Error processing result: " + e.getMessage());
      }
    }
  }
}
