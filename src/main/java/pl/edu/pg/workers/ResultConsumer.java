package pl.edu.pg.workers;

import de.siegmar.fastcsv.writer.CsvWriter;
import pl.edu.pg.buffer.OutputQueue;
import pl.edu.pg.utils.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ResultConsumer implements Runnable {
  private final Path outPath;

  public ResultConsumer(String outPath) {
    this.outPath = Paths.get(outPath);
  }

  @Override
  public void run() {
    try (CsvWriter csvWriter = CsvWriter.builder().build(outPath)) {
      while (!Thread.currentThread().isInterrupted()) {
        try {
          var data = OutputQueue.getInstance().read();
          if (data.isEmpty()) continue;
          var result = data.get();
          Logger.log("Received result: " + result);
          csvWriter.writeRecord(Long.toString(result.getStartTime()), Long.toString(result.getEndTime()), Long.toString(result.getDuration()), result.getResponseType().toString());
        } catch (InterruptedException e) {
          Logger.error("Interrupted consumer: " + e.getMessage());
          Thread.currentThread().interrupt();
        } catch (Exception e) {
          Logger.error("Error processing result: " + e.getMessage());
        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
