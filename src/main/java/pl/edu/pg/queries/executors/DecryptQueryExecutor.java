package pl.edu.pg.queries.executors;

import pl.edu.pg.queries.ConsumerQueryResponse;
import pl.edu.pg.queries.IConsumerQueryExector;

import java.util.List;

public class DecryptQueryExecutor implements IConsumerQueryExector {
    @Override
    public ConsumerQueryResponse execute(String[] args) {
        return new ConsumerQueryResponse(ConsumerQueryResponse.QueryResponseType.DECRYPTED, List.of("not implemented"));
    }
}
