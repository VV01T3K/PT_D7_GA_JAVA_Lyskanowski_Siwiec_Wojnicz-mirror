package pl.edu.pg.queries.executors;

import pl.edu.pg.queries.ConsumerQueryResponse;
import pl.edu.pg.queries.IConsumerQueryExector;
import pl.edu.pg.utils.AESUtil;

import java.nio.channels.OverlappingFileLockException;
import java.util.List;

public class DecryptQueryExecutor implements IConsumerQueryExector {
  @Override
  public ConsumerQueryResponse execute(String[] args) {
    try {
      AESUtil.decryptObject(args);
      return new ConsumerQueryResponse(ConsumerQueryResponse.QueryResponseType.DECRYPTED, List.of(args));
    } catch (OverlappingFileLockException ex) {
      throw ex;
    } catch (Exception e) {
      System.err.println("Error decrypting query: " + e.getMessage());
    }
    return null;
  }
}
