package pl.edu.pg.queries;

import java.util.List;

public class ConsumerQueryResponse {
  private static final long systemStartTime = System.currentTimeMillis();
  private final long startTime = System.currentTimeMillis();
  QueryResponseType responseType;
  List<String> responses;
  private long endTime;
  private long duration;

  public ConsumerQueryResponse(QueryResponseType responseType) {
    this.responseType = responseType;
  }

  public QueryResponseType getResponseType() {
    return responseType;
  }

  public long getStartTime() {
    return startTime - systemStartTime;
  }

  public long getEndTime() {
    return endTime - systemStartTime;
  }

  public long getDuration() {
    return duration;
  }

  public ConsumerQueryResponse finalize(List<String> responses) {
    this.responses = responses;
    this.endTime = System.currentTimeMillis();
    this.duration = endTime - startTime;
    return this;
  }

  public enum QueryResponseType {
    DECRYPTED,
    ENCRYPTED
  }

}
