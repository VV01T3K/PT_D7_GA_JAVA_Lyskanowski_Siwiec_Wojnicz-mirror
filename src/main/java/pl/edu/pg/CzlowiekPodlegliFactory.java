package pl.edu.pg;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static java.lang.Math.min;

public class CzlowiekPodlegliFactory {
  private static SortModes sortMode = SortModes.UNORDERED;
  private static Comparator<Czlowiek> comparator = Comparator.naturalOrder();

  public static void setSortMode(SortModes sortMode) {
    CzlowiekPodlegliFactory.sortMode = sortMode;
  }

  public static void setComparator(Comparator<Czlowiek> comparator) {
    CzlowiekPodlegliFactory.comparator = comparator;
  }

  public static Set<Czlowiek> chooseSet() {
    if (sortMode == SortModes.ORDERED) {
      if (comparator == null)
        return new TreeSet<>();
      else
        return new TreeSet<>(comparator);
    } else {
      return new HashSet<>();
    }
  }

  private static void randomizuj(ArrayList<Czlowiek> mezczyzni, ArrayList<Czlowiek> kobiety,
      Stack<Czlowiek> potomstwo) {
    Random rand = new Random();
    Collections.shuffle(potomstwo);
    Collections.shuffle(mezczyzni);
    Collections.shuffle(kobiety);
    for (int i = 0; (i < min(mezczyzni.size(), kobiety.size())) && (!potomstwo.isEmpty()); i++) {
      int los = 1;
      while (los > 0 && (!potomstwo.isEmpty())) {
        los = rand.nextInt(2);
        mezczyzni.get(i).dodajPodleglego(potomstwo.getLast());
        kobiety.get(i).dodajPodleglego(potomstwo.getLast());
        potomstwo.pop();
      }
      i = i % (min(mezczyzni.size(), kobiety.size()) - 1);
    }
  }

  public static Set<Czlowiek> wypelnijPodlegli(String filePath) {
    Set<Czlowiek> osoby = chooseSet();
    ArrayList<Czlowiek> dziadkowie = new ArrayList<>();
    ArrayList<Czlowiek> babcie = new ArrayList<>();
    ArrayList<Czlowiek> mamusie = new ArrayList<>();
    ArrayList<Czlowiek> tatusie = new ArrayList<>();
    Stack<Czlowiek> sredni_wiek = new Stack<>();
    Stack<Czlowiek> Podlegli = new Stack<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
      String linia;
      while ((linia = reader.readLine()) != null) {
        String[] parts = linia.split(";");
        int wiek = Integer.parseInt(parts[2]);
        Czlowiek c = new Czlowiek(parts[0], parts[1], wiek, parts[3].equals("M") ? Plec.MEZCZYZNA : Plec.KOBIETA);
        if (wiek < 30)
          Podlegli.push(c);
        else if (wiek < 60) {
          sredni_wiek.push(c);
          (c.getPlec() == Plec.MEZCZYZNA ? tatusie : mamusie).add(c);
        } else
          (c.getPlec() == Plec.MEZCZYZNA ? dziadkowie : babcie).add(c);
      }
    } catch (IOException e) {
      System.out.println("Błąd podczas wczytywania pliku: " + e.getMessage());
    }
    randomizuj(dziadkowie, babcie, sredni_wiek);
    randomizuj(tatusie, mamusie, Podlegli);
    osoby.addAll(dziadkowie);
    osoby.addAll(babcie);
    return osoby;
  }
}
