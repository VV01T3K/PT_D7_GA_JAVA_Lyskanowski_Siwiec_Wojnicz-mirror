package pl.edu.pg.buffer;

import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import pl.edu.pg.queries.ConsumerQuery;

public class InputQueue implements IBuffer<ConsumerQuery> {

    private final BlockingQueue<ConsumerQuery> queries = new LinkedBlockingQueue<>();

    public Optional<ConsumerQuery> read() {
        return Optional.ofNullable(queries.poll());
    }

    public void write(ConsumerQuery data) {
        try {
            queries.put(data);
        } catch (InterruptedException e) {
            System.err.println("Error writing to input queue: " + e.getMessage());
        }
    }

    private static InputQueue instance = new InputQueue();

    public static InputQueue getInstance() {
        return instance;
    }
}
