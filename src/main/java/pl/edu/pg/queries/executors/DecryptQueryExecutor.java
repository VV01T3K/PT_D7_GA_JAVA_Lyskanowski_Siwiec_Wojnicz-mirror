package pl.edu.pg.queries.executors;

import pl.edu.pg.queries.ConsumerQueryResponse;
import pl.edu.pg.queries.IConsumerQueryExector;
import pl.edu.pg.utils.AESUtil;

import java.util.List;

public class DecryptQueryExecutor implements IConsumerQueryExector {
  @Override
  public ConsumerQueryResponse execute(String[] args) throws Exception {
    AESUtil.decryptObject(args);
    return new ConsumerQueryResponse(ConsumerQueryResponse.QueryResponseType.DECRYPTED, List.of(args));
  }
}
