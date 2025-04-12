package pl.edu.pg.persistance.builders;

import pl.edu.pg.persistance.models.Firma;

import java.util.Scanner;

public class FirmaBuilder implements IBuilder<Firma> {
  private String nazwa;

  @Override
  public IBuilder<Firma> buildInteractive(Scanner scanner) {
    System.out.println("Podaj nazwe firmy:");
    nazwa = scanner.nextLine();
    return this;
  }

  @Override
  public Firma build() {
    return new Firma(nazwa);
  }
}
