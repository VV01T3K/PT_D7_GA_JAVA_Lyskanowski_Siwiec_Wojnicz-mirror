package pl.edu.pg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.nio.file.Paths;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import pl.edu.pg.buffer.InputQueue;

import pl.edu.pg.queries.ConsumerQuery;

public class Producer implements Runnable {

    static final InputQueue inputQueue = InputQueue.getInstance();

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + ", executing run() method!");
        loadQueryPool();
    }

    public void loadQueryPool() {
        try {
            Files.readAllLines(Paths.get("Data/queries.txt"))
                    .forEach(line -> {
                        inputQueue.write(ConsumerQuery.fromString(line));
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

            String inPath = file.getPath();
            String outPath;
            String iv = "";
            String key = "";

            if (type == ConsumerQuery.QueryType.ENCRYPT) {
                if (!file.getName().contains(".json")) {
                    System.err.println("File is not a JSON file: " + file.getName() + " - skipping");
                    return;
                }
                outPath = "Data/out/encrypted/" + file.getName().replaceFirst(".json", ".enc");
                key = AESUtil.generateKey();
                iv = AESUtil.generateIV();
            } else {
                if (!file.getName().contains(".enc")) {
                    System.err.println("File is not an encrypted file: " + file.getName() + " - skipping");
                    return;
                }
                outPath = "Data/out/decrypted/" + file.getName().replaceFirst(".enc", ".json");
                // key and iv HAVE TO be same as in encryption
            }
            String[] arguments = { inPath, outPath, key, iv };
            newQueries.add(new ConsumerQuery(type, arguments));
        } catch (Exception e) {
            System.err.println("Error creating decrypt query: " + e.getMessage());
        }
    }

}