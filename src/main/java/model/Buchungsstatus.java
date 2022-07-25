package model;

public enum Buchungsstatus {
    ERSTELLT("Erstellt"),
    AKTIV("Aktiv"),
    ABGESCHLOSSEN("Abgeschlossen"),
    FRISTGERECHT_STORNIERT("Fristgerecht storniert"),
    NICHT_FRISTGERECHT_STORNIERT("Nicht fristgerecht storniert");

    private String bezeichner;

    Buchungsstatus(String bezeichner) {
        this.bezeichner = bezeichner;
    }

    public String getBezeichner() {
        return bezeichner;
    }
}
