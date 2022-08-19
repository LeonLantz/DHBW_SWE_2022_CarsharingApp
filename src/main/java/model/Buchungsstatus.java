package model;

import java.util.ArrayList;
import java.util.List;

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

    public static String[] getArray() {
        List<String> list = new ArrayList<>();
        for (Buchungsstatus buchungsstatus : Buchungsstatus.values()) {
            list.add(buchungsstatus.bezeichner);
        }
        return list.toArray(new String[list.size()]);
    }

    Buchungsstatus(String bezeichner) {
        this.bezeichner = bezeichner;
    }

    public String getBezeichner() {
        return bezeichner;
    }
}
