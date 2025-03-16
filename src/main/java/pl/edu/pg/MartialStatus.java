package pl.edu.pg;

public enum MartialStatus {
    SEPARATED("separated"),
    WIDOWED("widowed"),
    MARRIED("married"),
    DIVORCED("divorced"),
    NEVER_MARRIED("never married");

    private final String displayName;

    MartialStatus(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

    public static MartialStatus fromString(String text) {
        for (MartialStatus b : MartialStatus.values()) {
            if (b.displayName.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
}