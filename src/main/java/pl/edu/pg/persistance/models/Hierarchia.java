package pl.edu.pg.persistance.models;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

import java.io.Serializable;
import java.util.Objects;

@Entity
public class Hierarchia implements IModel {
  @EmbeddedId
  private HierarchiaId id;
  @ManyToOne
  @MapsId("przelozony")
  private Czlowiek przelozony;
  @ManyToOne
  @MapsId("podwladny")
  private Czlowiek podwladny;

  public Hierarchia(Czlowiek przelozony, Czlowiek podwladny) {
    this.przelozony = przelozony;
    this.podwladny = podwladny;
    this.id = new HierarchiaId(przelozony.getId(), podwladny.getId());
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

  public static class HierarchiaId implements Serializable {
    private Integer przelozony;
    private Integer podwladny;

    public HierarchiaId() {
    }

    public HierarchiaId(Integer przelozony, Integer podwladny) {
      this.przelozony = przelozony;
      this.podwladny = podwladny;
    }

    // Getters & Setters

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof HierarchiaId)) return false;
      HierarchiaId that = (HierarchiaId) o;
      return Objects.equals(przelozony, that.przelozony) &&
              Objects.equals(podwladny, that.podwladny);
    }

    @Override
    public int hashCode() {
      return Objects.hash(przelozony, podwladny);
    }
  }
}
