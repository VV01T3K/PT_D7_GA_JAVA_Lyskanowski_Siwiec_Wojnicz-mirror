package pl.edu.pg.queries.executors;

import pl.edu.pg.queries.ConsumerQueryResponse;
import pl.edu.pg.queries.IConsumerQueryExector;
import pl.edu.pg.utils.AESUtil;

import java.util.List;

public class EncryptQueryExecutor implements IConsumerQueryExector {
  @Override
  public ConsumerQueryResponse execute(String[] args) throws Exception {
    var response = new ConsumerQueryResponse(ConsumerQueryResponse.QueryResponseType.ENCRYPTED);
    AESUtil.encryptObject(args);
    return response.finalize(List.of(args));
  }
}
