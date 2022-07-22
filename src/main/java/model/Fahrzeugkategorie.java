package model;

public enum Fahrzeugkategorie {
    KLEINFAHRZEUG("okay"),
    MITTELKLASSE("okay"),
    GEHOBENE_MITTELKLASSE("okay"),
    TRANSPORTFAHRZEUG("okay");

    String bezeichner;

    Fahrzeugkategorie(String bezeichner) {
        this.bezeichner = bezeichner;
    }

    public String getBezeichner() {
        return bezeichner;
    }
}
