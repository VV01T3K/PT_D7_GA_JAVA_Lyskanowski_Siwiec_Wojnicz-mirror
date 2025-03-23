package pl.edu.pg;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.nio.file.Paths;
import java.nio.file.Files;

public class Producer implements Runnable {

    public final static BlockingQueue<Query> queries = new LinkedBlockingQueue<>();

    public static BlockingQueue<Query> getQueryPool() {
        return queries;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + ", executing run() method!");
        loadQueryPool();
        // queries.forEach(System.out::println);
        System.out.println("Loaded " + Producer.getQueryPool().size() + " queries");
    }

    public void loadQueryPool() {
        try {
            Files.readAllLines(Paths.get("Data/queries.txt"))
                    .forEach(line -> {
                        try {
                            queries.put(Query.fromString(line));
                        } catch (Exception e) {
                            System.err.println("Error loading query: " + e.getMessage());
                        }
                    });
            System.out.println("Query pool loaded successfully from Data/queries.txt");
        } catch (Exception e) {
            System.err.println("Error loading query pool: " + e.getMessage());
        }
    }

    private static void saveQueryPool(ArrayList<Query> newQueries) {
        try {
            StringBuilder sb = new StringBuilder();
            for (Query query : newQueries) {
                sb.append(query.toString()).append(System.lineSeparator());
            }

            Files.write(Paths.get("Data/queries.txt"), sb.toString().getBytes());
            System.out.println("Query pool saved successfully to Data/queries.txt");
        } catch (Exception e) {
            System.err.println("Error saving query pool: " + e.getMessage());
        }
    }

    public static void generateQueryPool() {
        try {
            Files.createDirectories(Paths.get("Data/encrypted"));
            Files.createDirectories(Paths.get("Data/out/encrypted"));
            Files.createDirectories(Paths.get("Data/out/decrypted"));
        } catch (Exception e) {
            System.err.println("Error creating directories: " + e.getMessage());
        }

        ArrayList<Query> newQueries = new ArrayList<>();
        Random random = new Random();

        try {
            Files.walk(Paths.get("Data/separated"))
                    .filter(Files::isRegularFile)
                    .map(java.nio.file.Path::toFile)
                    .forEach(file -> {
                        try {
                            if (random.nextDouble() <= 0.7) {
                                newQueries.add(new Query(
                                        Query.QueryType.ENCRYPT,
                                        file.getPath(),
                                        "Data/out/encrypted/" + file.getName()));
                            }
                        } catch (Exception e) {
                            System.err.println("Error creating query: " + e.getMessage());
                        }
                    });

            Files.walk(Paths.get("Data/encrypted"))
                    .filter(Files::isRegularFile)
                    .map(java.nio.file.Path::toFile)
                    .forEach(file -> {
                        try {
                            if (random.nextDouble() <= 0.7) {
                                newQueries.add(new Query(
                                        Query.QueryType.DECRYPT,
                                        file.getPath(),
                                        "Data/out/decrypted/" + file.getName()));
                            }
                        } catch (Exception e) {
                            System.err.println("Error creating query: " + e.getMessage());
                        }
                    });
        } catch (Exception e) {
            System.err.println("Error creating query pool: " + e.getMessage());
        }
        Collections.shuffle(newQueries);
        saveQueryPool(newQueries);
    }

}