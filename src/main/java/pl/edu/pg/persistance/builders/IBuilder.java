package pl.edu.pg.persistance.builders;

import java.util.Scanner;

public interface IBuilder<T> {
  IBuilder<T> buildInteractive(Scanner scanner);

  T build();
}
