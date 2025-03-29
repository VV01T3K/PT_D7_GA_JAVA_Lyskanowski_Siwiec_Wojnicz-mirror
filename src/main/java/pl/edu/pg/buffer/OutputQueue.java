package pl.edu.pg.buffer;

import pl.edu.pg.queries.ConsumerQueryResponse;

import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class OutputQueue implements IBuffer<ConsumerQueryResponse> {

  private static OutputQueue instance = new OutputQueue();
  private final BlockingQueue<ConsumerQueryResponse> queries = new LinkedBlockingQueue<>();

  public static OutputQueue getInstance() {
    return instance;
  }

  public Optional<ConsumerQueryResponse> read() throws InterruptedException {
    return Optional.of(queries.take());
  }

  public void write(ConsumerQueryResponse data) throws InterruptedException {
    queries.put(data);
  }
}
