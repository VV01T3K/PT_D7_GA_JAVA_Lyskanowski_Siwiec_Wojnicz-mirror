package pl.edu.pg.persistance.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Firma implements IModel {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @Column(length = 200)
  private String nazwaFirmy;

  @OneToMany(mappedBy = "firma", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  private List<Pracownik> pracownicy;

  public Firma(String nazwa) {
    this.nazwaFirmy = nazwa;
  }

  public Firma() {
    // Default constructor for JPA
  }

  public Integer getId() {
    return id;
  }

  public String getNazwaFirmy() {
    return nazwaFirmy;
  }

  public void setNazwaFirmy(String nazwaFirmy) {
    this.nazwaFirmy = nazwaFirmy;
  }

  @Override
  public String toString() {
    return "Firma{" +
        "id=" + id +
        ", nazwaFirmy='" + nazwaFirmy + '\'' +
        '}';
  }
}
