package model;

public enum Fahrzeugkategorie {
    KLEINFAHRZEUG("Kleinfahrzeug"),
    MITTELKLASSE("Mittelklasse"),
    GEHOBENE_MITTELKLASSE("Gehobene_Mittelklasse"),
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
}
