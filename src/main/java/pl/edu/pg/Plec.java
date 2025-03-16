package pl.edu.pg;

public enum Plec {
    MEZCZYZNA("M"),
    KOBIETA("K");

    private final String displayName;

    Plec(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}