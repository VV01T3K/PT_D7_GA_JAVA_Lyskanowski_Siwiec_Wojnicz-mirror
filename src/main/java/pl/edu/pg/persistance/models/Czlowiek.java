package pl.edu.pg.persistance.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Czlowiek implements IModel {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @Column(length = 150, nullable = false)
  private String imie;

  @Column(length = 150, nullable = false)
  private String nazwisko;

  @Column(length = 20, nullable = false)
  private String numerTelefonu;

  private Boolean plec;

  @Column(length = 100)
  private String stanCywilny;

  @Column(length = 100)
  private String wyksztalcenie;

  @Column(length = 500)
  private String pozycjaZawodowa;

  @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
  private Firma firma;

  @OneToMany(mappedBy = "podwladny", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Hierarchia> przelozeni;

  @OneToMany(mappedBy = "przelozony", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Hierarchia> podwladni;

  public Czlowiek(String imie, String nazwisko, String numerTelefonu, Boolean plec, String stanCywilny, String wyksztalcenie, String pozycjaZawodowa, Firma firma) {
    this.imie = imie;
    this.nazwisko = nazwisko;
    this.numerTelefonu = numerTelefonu;
    this.plec = plec;
    this.stanCywilny = stanCywilny;
    this.wyksztalcenie = wyksztalcenie;
    this.pozycjaZawodowa = pozycjaZawodowa;
    this.firma = firma;
  }

  public Czlowiek() {
    // Default constructor for JPA
  }

  @Transient
  public List<Czlowiek> getPodwladni() {
    return podwladni.stream()
            .map(Hierarchia::getPodwladny)
            .toList();
  }

  @Transient
  public List<Czlowiek> getPrzelozeni() {
    return przelozeni.stream()
            .map(Hierarchia::getPrzelozony)
            .toList();
  }

  @Override
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getImie() {
    return imie;
  }

  public String getNazwisko() {
    return nazwisko;
  }

  @Override
  public String toString() {
    return "Czlowiek{" +
            "id=" + id +
            ", imie='" + imie + '\'' +
            ", nazwisko='" + nazwisko + '\'' +
            ", numerTelefonu='" + numerTelefonu + '\'' +
            ", plec=" + plec +
            ", stanCywilny='" + stanCywilny + '\'' +
            ", wyksztalcenie='" + wyksztalcenie + '\'' +
            ", pozycjaZawodowa='" + pozycjaZawodowa + '\'' +
            ", firma=" + firma +
            '}';
  }
}
