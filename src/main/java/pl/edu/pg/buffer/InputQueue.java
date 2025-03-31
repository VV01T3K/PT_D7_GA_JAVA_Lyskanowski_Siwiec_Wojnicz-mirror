package pl.edu.pg.buffer;

import pl.edu.pg.queries.ConsumerQuery;

import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class InputQueue implements IBuffer<ConsumerQuery> {

  private static InputQueue instance = new InputQueue();
  private final BlockingQueue<ConsumerQuery> queries = new LinkedBlockingQueue<>();

  public static InputQueue getInstance() {
    return instance;
  }

  public Optional<ConsumerQuery> read() throws InterruptedException {
    return Optional.of(queries.take());
  }

  public void write(ConsumerQuery data) throws InterruptedException {
    queries.put(data);
  }
}
