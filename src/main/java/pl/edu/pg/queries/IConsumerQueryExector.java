package pl.edu.pg.queries;

import java.nio.channels.OverlappingFileLockException;

public interface IConsumerQueryExector {
  ConsumerQueryResponse execute(String[] args) throws OverlappingFileLockException;
}
