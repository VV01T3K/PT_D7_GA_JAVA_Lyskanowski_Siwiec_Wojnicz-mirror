package pl.edu.pg;

import java.util.concurrent.atomic.AtomicInteger;

public class Worker implements Runnable {

    private AtomicInteger counter;

    public Worker(AtomicInteger counter) {
        this.counter = counter;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + ", executing run() method!");
        while (true) {
            Query query = Producer.getQueryPool().poll();
            if (query == null) {
                System.out.println("No more queries to execute, exiting...");
                break;
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Executing query: " + query);
            counter.incrementAndGet();
            System.out.println("Query executed successfully!");
        }
    }

}
