package pl.edu.pg.persistance.models;

import jakarta.persistence.*;

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

  @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private Firma firma;

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

  @Override
  public Integer getId() {
    return id;
  }

  public String getImie() {
    return imie;
  }

  public String getNazwisko() {
    return nazwisko;
  }
}
