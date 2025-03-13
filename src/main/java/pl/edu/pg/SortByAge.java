package pl.edu.pg;

import java.util.Comparator;

public class SortByAge implements Comparator<Czlowiek> {
    /*@Override
    public int compare(Czlowiek o1, Czlowiek o2) {
        if (o1 == null && o2 == null)
            return 0;
        else if (o1 == null)
            return -1;
        else if (o2 == null)
            return 1;

        return o1.getWiek() - o2.getWiek(); //z tego co zauwazylem przy tym komparatorze to przy podaniu
        //tego samego wieku osoby przy wypisywaniu są traktowane jako jedna i ta sama
    }*/
    @Override //(przy tym nie ma tego problemu tylko chyba trzeba po wszystkich po kolei sprawdzac ?)
    public int compare(Czlowiek o1, Czlowiek o2) {
        int result = Integer.compare(o1.getWiek(), o2.getWiek());
        if (result == 0) {
            result = o1.getNazwisko().compareTo(o2.getNazwisko());
        }
        if (result == 0) {
            result = o1.getImie().compareTo(o2.getImie());
        }
        return result;
    }
}