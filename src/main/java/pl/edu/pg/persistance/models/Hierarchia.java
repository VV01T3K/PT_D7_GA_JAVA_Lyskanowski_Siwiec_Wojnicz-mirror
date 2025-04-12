package pl.edu.pg.persistance.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Hierarchia implements IModel {
  @Id
  @ManyToOne
  private Czlowiek przelozony;
  @Id
  @ManyToOne
  private Czlowiek podwladny;

  public Hierarchia(Czlowiek przelozony, Czlowiek podwladny) {
    this.przelozony = przelozony;
    this.podwladny = podwladny;
  }

  public Hierarchia() {
    // Default constructor for JPA
  }

  public Czlowiek getPrzelozony() {
    return przelozony;
  }

  public Czlowiek getPodwladny() {
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
