package pl.edu.pg;

import java.util.Comparator;

public class SortByNumberOfInferiors implements Comparator<Czlowiek> {
    @Override
    public int compare(Czlowiek o1, Czlowiek o2) {
        if (o1 == null || o2 == null)
            throw new NullPointerException();

        int result = Integer.compare(o1.getAllInferiorsCount(), o2.getAllInferiorsCount());
        if (result == 0)
            result = Integer.compare(o1.getPodlegli().size(), o2.getPodlegli().size());
        if (result == 0)
            result = o1.compareTo(o2);
        return result;
    }
}
