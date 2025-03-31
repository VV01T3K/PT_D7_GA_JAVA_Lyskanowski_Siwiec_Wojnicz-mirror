package pl.edu.pg.buffer;

import java.util.Optional;

public interface IBuffer<T> {
  // Returns value if queue is not empty
  Optional<T> read() throws InterruptedException;

  void write(T data) throws InterruptedException;
}
