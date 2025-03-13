package pl.edu.pg;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static java.lang.Math.min;

public class CzlowiekDzieciFactory {
  private static SortModes sortMode = SortModes.UNORDERED;
  private static Comparator<Czlowiek> comparator = Comparator.naturalOrder();

  public static void setSortMode(SortModes sortMode) {
    CzlowiekDzieciFactory.sortMode = sortMode;
  }

  public static void setComparator(Comparator<Czlowiek> comparator) {
    CzlowiekDzieciFactory.comparator = comparator;
  }

  public static Set<Czlowiek> chooseSet() {
    if (sortMode == SortModes.ORDERED) {
      if (comparator == null) return new TreeSet<>();
      else return new TreeSet<>(comparator);
    } else {
      return new HashSet<>();
    }
  }
  public static Set<Czlowiek> wypelnijDzieci(String filePath) {
    Set<Czlowiek> osoby = chooseSet();
    ArrayList<Czlowiek> dziadkowie = new ArrayList<>();
    ArrayList<Czlowiek> babcie = new ArrayList<>();
    ArrayList<Czlowiek> mamusie = new ArrayList<>();
    ArrayList<Czlowiek> tatusie = new ArrayList<>();
    Stack<Czlowiek> sredni_wiek = new Stack<>();
    Stack<Czlowiek> dzieci = new Stack<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath)))
    {
      String linia;
      while((linia = reader.readLine()) != null)
      {
        String[] parts = linia.split(";");
        int wiek=Integer.parseInt(parts[2]);
        Czlowiek c = new Czlowiek(parts[0],parts[1],wiek,parts[3].equals("M")?Plec.MEZCZYZNA:Plec.KOBIETA);
        if(wiek<30)
        {
          if(c.getPlec()==Plec.MEZCZYZNA)
            dzieci.add(c);
        }
        else if(wiek<60)
        {
          sredni_wiek.push(c);
          if(c.getPlec()==Plec.MEZCZYZNA)
            tatusie.add(c);
          else  mamusie.add(c);
        }
        else
        {
          if(c.getPlec()==Plec.MEZCZYZNA)
            dziadkowie.add(c);
          else  babcie.add(c);
        }
      }
    }
    catch (IOException e) {
      System.out.println("Błąd podczas wczytywania pliku: " + e.getMessage());
    }
    Collections.shuffle(dziadkowie);
    Collections.shuffle(babcie);
    Collections.shuffle(mamusie);
    Collections.shuffle(tatusie);
    Collections.shuffle(sredni_wiek);
    Collections.shuffle(dzieci);
    Random rand = new Random();
    for(int i=0 ; (i < min(dziadkowie.size(),babcie.size())) && (!sredni_wiek.isEmpty()) ; i++)
    {
      int los=1;
      while(los > 0 && (!sredni_wiek.isEmpty()))
      {
        los = rand.nextInt(2);
        dziadkowie.get(i).dodajDziecko(sredni_wiek.getLast());
        babcie.get(i).dodajDziecko(sredni_wiek.getLast());
        sredni_wiek.pop();
      }
      i=i%(min(dziadkowie.size(),babcie.size())-1);
    }

    for(int i=0 ; (i < min(tatusie.size(),mamusie.size())) && (!dzieci.isEmpty()) ; i++)
    {
      int los=1;
      while(los > 0 && (!dzieci.isEmpty()))
      {
        los = rand.nextInt(2);
        tatusie.get(i).dodajDziecko(dzieci.getLast());
        mamusie.get(i).dodajDziecko(dzieci.getLast());
        System.out.println(dzieci.pop());
      }
      i=i%(min(tatusie.size(),mamusie.size())-1);
    }
      osoby.addAll(dziadkowie);
      osoby.addAll(babcie);
    return osoby;
  }
}
