package pl.edu.pg.queries.executors;

import pl.edu.pg.queries.ConsumerQuery;
import pl.edu.pg.queries.IConsumerQueryExector;

public class QueryExecutorFactory {
    public static IConsumerQueryExector createQueryExecutor(ConsumerQuery query) {
        return switch (query.queryType) {
            case DECRYPT -> new DecryptQueryExecutor();
            case ENCRYPT -> new EncryptQueryExecutor();
        };
    }
}
