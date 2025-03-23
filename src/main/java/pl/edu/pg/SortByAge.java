package pl.edu.pg;

import java.io.Serializable;
import java.util.Comparator;

public class SortByAge implements Comparator<Czlowiek>, Serializable {
    @Override
    public int compare(Czlowiek o1, Czlowiek o2) {
        if (o1 == null || o2 == null)
            throw new NullPointerException();

        int result = Integer.compare(o1.getWiek(), o2.getWiek());
        if (result == 0)
            result = o1.compareTo(o2);
        return result;
    }
}