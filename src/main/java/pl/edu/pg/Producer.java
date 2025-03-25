package pl.edu.pg;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.nio.file.Paths;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import pl.edu.pg.queries.ConsumerQuery;

public class Producer implements Runnable {

    public final static BlockingQueue<ConsumerQuery> queries = new LinkedBlockingQueue<>();

    public static BlockingQueue<ConsumerQuery> getQueryPool() {
        return queries;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + ", executing run() method!");
        loadQueryPool();
    }

    public void loadQueryPool() {
        try {
            Files.readAllLines(Paths.get("Data/queries.txt"))
                    .forEach(line -> {
                        try {
                            queries.put(ConsumerQuery.fromString(line));
                        } catch (Exception e) {
                            System.err.println("Error loading query: " + e.getMessage());
                        }
                    });
            System.out.println("Query pool loaded successfully from Data/queries.txt");
        } catch (Exception e) {
            System.err.println("Error loading query pool: " + e.getMessage());
        }
    }

    private static void saveQueryPool(List<ConsumerQuery> newQueries) {
        try {
            StringBuilder sb = new StringBuilder();
            for (ConsumerQuery query : newQueries) {
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

        List<ConsumerQuery> newQueries = Collections.synchronizedList(new ArrayList<>());
        try {
            Files.walk(Paths.get("Data/separated"))
                    .parallel()
                    .filter(Files::isRegularFile)
                    .forEach(path -> {
                        createQuery(path, ConsumerQuery.QueryType.ENCRYPT, newQueries);
                    });

            Files.walk(Paths.get("Data/encrypted"))
                    .parallel()
                    .filter(Files::isRegularFile)
                    .forEach(path -> {
                        createQuery(path, ConsumerQuery.QueryType.DECRYPT, newQueries);
                    });
        } catch (Exception e) {
            System.err.println("Error creating query pool: " + e.getMessage());
        }

        Collections.shuffle(newQueries);
        saveQueryPool(newQueries);
    }

    public static void createQuery(Path path, ConsumerQuery.QueryType type, List<ConsumerQuery> newQueries) {
        try {
            File file = path.toFile();
            String outPath = Paths.get("Data/encrypted/" + file.getName()).toString();
            if (type == ConsumerQuery.QueryType.ENCRYPT) {
                outPath = outPath.replaceFirst(".json", ".enc");
            }
            if (type == ConsumerQuery.QueryType.DECRYPT) {
                outPath = outPath.replace(".enc", ".json");
                System.out.println("Decrypting: " + file.getName());
                System.out.println("Out: " + outPath);
            }
            String[] arguments = { file.getPath(), outPath,
                    "kluczfd169112f" };
            newQueries.add(new ConsumerQuery(type, arguments));
        } catch (Exception e) {
            System.err.println("Error creating decrypt query: " + e.getMessage());
        }
    }

}