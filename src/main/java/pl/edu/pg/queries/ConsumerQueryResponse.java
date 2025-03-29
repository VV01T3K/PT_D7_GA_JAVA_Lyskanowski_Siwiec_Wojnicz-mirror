package pl.edu.pg.queries;

import java.util.List;

public class ConsumerQueryResponse {
  QueryResponseType responseType;
  List<String> responses;

  public ConsumerQueryResponse(QueryResponseType responseType, List<String> responses) {
    this.responseType = responseType;
    this.responses = responses;
  }

  public enum QueryResponseType {
    DECRYPTED,
    ENCRYPTED
  }

}
