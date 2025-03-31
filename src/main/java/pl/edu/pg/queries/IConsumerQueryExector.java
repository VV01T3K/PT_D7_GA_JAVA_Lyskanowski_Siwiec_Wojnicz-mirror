package pl.edu.pg.queries;

public interface IConsumerQueryExector {
  ConsumerQueryResponse execute(String[] args) throws Exception;
}
