package model;

import java.util.ArrayList;
import java.util.List;

public enum Fahrzeugkategorie {
    KLEINFAHRZEUG("Kleinfahrzeug"),
    MITTELKLASSE("Mittelklasse"),
    MITTELKLASSE_PLUS("Mittelklasse_Plus"),
    LUXUSKLASSE("Luxusklasse"),
    TRANSPORTFAHRZEUG("Transportfahrzeug");

    String bezeichner;

    Fahrzeugkategorie(String bezeichner) {
        this.bezeichner = bezeichner;
    }

    public String getBezeichner() {
        return bezeichner;
    }

    public static Fahrzeugkategorie fromString(String bezeichner) {
        for (Fahrzeugkategorie f : Fahrzeugkategorie.values()) {
            if (f.bezeichner.equalsIgnoreCase(bezeichner)) {
                return f;
            }
        }
        return null;
    }

    public static String[] getArray() {
        List<String> list = new ArrayList<>();
        for (Fahrzeugkategorie f : Fahrzeugkategorie.values()) {
            list.add(f.bezeichner);
        }
        return list.toArray(new String[list.size()]);
    }
}
