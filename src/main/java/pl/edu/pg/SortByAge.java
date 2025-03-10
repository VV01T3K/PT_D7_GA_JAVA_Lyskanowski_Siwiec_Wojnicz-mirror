package pl.edu.pg;

import java.util.Comparator;

public class SortByAge implements Comparator<Czlowiek> {
    @Override
    public int compare(Czlowiek o1, Czlowiek o2) {
        if (o1 == null && o2 == null)
            return 0;
        else if (o1 == null)
            return -1;
        else if (o2 == null)
            return 1;

        return o1.getWiek() - o2.getWiek();
    }
}