package pl.edu.pg.persistance.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class Hierarchia {
  @Id
  @OneToOne
  private Czlowiek przelozony;

  @Id
  @OneToOne
  private Czlowiek podwladny;

  public Hierarchia(Czlowiek przelozony, Czlowiek podwladny) {
    this.przelozony = przelozony;
    this.podwladny = podwladny;
  }

  public Hierarchia() {
    // Default constructor for JPA
  }
}
