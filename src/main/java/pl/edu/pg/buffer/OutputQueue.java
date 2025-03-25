package pl.edu.pg.buffer;

import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import pl.edu.pg.queries.ConsumerQueryResponse;

public class OutputQueue implements IBuffer<ConsumerQueryResponse> {

    private final BlockingQueue<ConsumerQueryResponse> queries = new LinkedBlockingQueue<>();

    public Optional<ConsumerQueryResponse> read() {
        return Optional.ofNullable(queries.poll());
    }

    public void write(ConsumerQueryResponse data) {
        try {
            queries.put(data);
        } catch (InterruptedException e) {
            System.err.println("Error writing to input queue: " + e.getMessage());
        }
    }

    private static OutputQueue instance = new OutputQueue();

    public static OutputQueue getInstance() {
        return instance;
    }
}
