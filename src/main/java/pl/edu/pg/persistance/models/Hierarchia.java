package pl.edu.pg.persistance.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;

@Entity
public class Hierarchia implements IModel {
  @Id
  @ManyToOne(fetch = FetchType.EAGER)
  private Pracownik przelozony;

  @Id
  @ManyToOne(fetch = FetchType.EAGER)
  private Pracownik podwladny;

  public Hierarchia(Pracownik przelozony, Pracownik podwladny) {
    this.przelozony = przelozony;
    this.podwladny = podwladny;
  }

  public Hierarchia() {
    // Default constructor for JPA
  }

  public Pracownik getPrzelozony() {
    return przelozony;
  }

  public Pracownik getPodwladny() {
    return podwladny;
  }

  @Override
  public Integer getId() {
    return przelozony.getId();
  }

  @Override
  public String toString() {
    return "Hierarchia{" +
        "przelozony=" + przelozony +
        ", podwladny=" + podwladny +
        '}';
  }
}
