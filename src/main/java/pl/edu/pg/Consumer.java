package pl.edu.pg;

import pl.edu.pg.queries.ConsumerQuery;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Consumer {
    List<ConsumerQuery> queries;

    public Consumer() {
        queries = new ArrayList<>();
    }

    public void readQueriesFromFile(String filePath) {
        try {
            Files.readAllLines(Paths.get(filePath))
                    .forEach(line -> queries.add(ConsumerQuery.fromString(line)));
            System.out.println("Queries loaded successfully from " + filePath);
        } catch (Exception e) {
            System.err.println("Error reading queries from file: " + e.getMessage());
        }
    }

    public void processQueries() throws Exception {
        for (ConsumerQuery query : queries) {
            ConsumerQuery.QueryType queryType = query.getQueryType();
            String[] arguments = query.getArguments();
            if (queryType == ConsumerQuery.QueryType.ENCRYPT) {
                AESUtil.encryptObject(arguments);
            } else if (queryType == ConsumerQuery.QueryType.DECRYPT) {
                AESUtil.decryptObject(arguments);
            }
        }
    }

    public void printQueries() {
        for (ConsumerQuery query : queries) {
            System.out.println(query);
        }
    }
}
