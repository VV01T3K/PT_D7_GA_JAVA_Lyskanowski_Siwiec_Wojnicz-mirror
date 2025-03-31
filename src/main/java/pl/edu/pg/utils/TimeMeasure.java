package pl.edu.pg.utils;

public class TimeMeasure {
  private long startTime;
  private long endTime;

  public TimeMeasure() {
    start();
  }

  public void start() {
    startTime = System.currentTimeMillis();
  }

  public void stop() {
    endTime = System.currentTimeMillis();
  }

  public long getElapsedTime() {
    return endTime - startTime;
  }

  public void stopAndReport(String message) {
    stop();
    Logger.log(message + " " + getElapsedTime() + " ms");
  }
}
