package pl.edu.pg.queries.executors;

import pl.edu.pg.queries.ConsumerQueryResponse;
import pl.edu.pg.queries.IConsumerQueryExector;
import pl.edu.pg.utils.AESUtil;

import java.util.List;

public class DecryptQueryExecutor implements IConsumerQueryExector {
  @Override
  public ConsumerQueryResponse execute(String[] args) throws Exception {
    ConsumerQueryResponse response = new ConsumerQueryResponse(ConsumerQueryResponse.QueryResponseType.DECRYPTED);
    AESUtil.decryptObject(args);
    return response.finalize(List.of(args));
  }
}
