package model;

import java.util.ArrayList;
import java.util.List;

public enum Motorisierung {
    BENZIN("Benzin"),
    DIESEL("Diesel"),
    ELEKTRO("Elektro");

    String bezeichner;

    Motorisierung(String bezeichner) {
        this.bezeichner = bezeichner;
    }

    public String getBezeichner() {
        return bezeichner;
    }

    public static Motorisierung fromString(String bezeichner) {
        for (Motorisierung motorisierung : Motorisierung.values()) {
            if (motorisierung.bezeichner.equalsIgnoreCase(bezeichner)) {
                return motorisierung;
            }
        }
        return null;
    }

    public static String[] getArray() {
        List<String> list = new ArrayList<>();
        list.add("--Wählen--");
        for (Motorisierung motorisierung : Motorisierung.values()) {
            list.add(motorisierung.bezeichner);
        }
        return list.toArray(new String[list.size()]);
    }
}
