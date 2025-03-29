package pl.edu.pg.utils;

public class Logger {
  public static void log(String message) {
    System.out.printf("[%s]: %s (%d)\n", Thread.currentThread().getName(), message, System.currentTimeMillis());
  }

  public static void error(String message) {
    System.err.printf("[%s]: %s (%d)\n", Thread.currentThread().getName(), message, System.currentTimeMillis());
  }
}
