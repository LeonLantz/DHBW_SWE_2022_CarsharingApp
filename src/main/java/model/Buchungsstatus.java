package model;

public enum Buchungsstatus {
    ERSTELLT("Erstellt"),
    AKTIV("Aktiv"),
    ABGESCHLOSSEN("Abgeschlossen"),
    FRISTGERECHT_STORNIERT("Fristgerecht storniert"),
    INVALIDE("Invalide"),
    NICHT_FRISTGERECHT_STORNIERT("Nicht fristgerecht storniert");

    private String bezeichner;

    public static Buchungsstatus fromString(String bezeichner) {
        for (Buchungsstatus f : Buchungsstatus.values()) {
            if (f.bezeichner.equalsIgnoreCase(bezeichner)) {
                return f;
            }
        }
        return null;
    }

    Buchungsstatus(String bezeichner) {
        this.bezeichner = bezeichner;
    }

    public String getBezeichner() {
        return bezeichner;
    }
}
