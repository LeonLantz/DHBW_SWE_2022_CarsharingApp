package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.dhbwka.swe.utils.model.IPersistable;
import de.dhbwka.swe.utils.util.CommonEntityManager;
import model.Bild;
import model.Fahrzeug;
import model.Kunde;
import model.Standort;

public class CSVHelper {
  //TODO: make methods dynamic for each model class (duplicate code)
  //  --> create generic interface with new getAttributeList(), let it get extended by every model class


  // TODO: ❗️❗️️️️️❗️️️️️️️️️ONLY USE CSVPositions as Attributes, since only those should be written

  public static List<String[]> getPersistedKundenCSVFormatted(CommonEntityManager entityManager) {
    List<IPersistable> allPersistedClassElements = entityManager.findAll(Kunde.class);
    List<String[]> data = new ArrayList<>();
    for (Object PersistableElement : allPersistedClassElements) {
      String[] attValue = new String[((Kunde) PersistableElement).getAttributes().toArray().length];
      for (int i = 0; i < attValue.length; i++) {
        attValue[i] = ((Kunde) PersistableElement).getAttributes().get(i).getValue().toString();
      }
      data.add(attValue);
    }
    return data;
  }

  public static List<String[]> getPersistedFahrzeugeCSVFormatted(CommonEntityManager entityManager) {
    List<IPersistable> allPersistedClassElements = entityManager.findAll(Fahrzeug.class);
    List<String[]> data = new ArrayList<>();
    for (Object PersistableElement : allPersistedClassElements) {
      String[] attValue = new String[((Fahrzeug) PersistableElement).getAttributes().toArray().length];
      for (int i = 0; i < attValue.length; i++) {
        attValue[i] = ((Fahrzeug) PersistableElement).getAttributes().get(i).getValue().toString();
      }
      data.add(attValue);
    }
    return data;
  }

  public static List<String[]> getPersistedBilderCSVFormatted(CommonEntityManager entityManager) {
    List<IPersistable> allPersistedClassElements = entityManager.findAll(Bild.class);
    List<String[]> data = new ArrayList<>();
    for (Object PersistableElement : allPersistedClassElements) {
      String[] attValue = new String[((Bild) PersistableElement).getAttributes().toArray().length];
      for (int i = 0; i < attValue.length; i++) {
        attValue[i] = ((Bild) PersistableElement).getAttributes().get(i).getValue().toString();
      }
      data.add(attValue);
    }
    return data;
  }

  public static List<String[]> getPersistedStandorteCSVFormatted(CommonEntityManager entityManager) {
    List<IPersistable> allPersistedClassElements = entityManager.findAll(Standort.class);
    List<String[]> data = new ArrayList<>();
    for (Object PersistableElement : allPersistedClassElements) {
      String[] attValue = new String[((Standort) PersistableElement).getAttributes().toArray().length];
      for (int i = 0; i < attValue.length; i++) {
        attValue[i] = ((Standort) PersistableElement).getAttributes().get(i).getValue().toString();
      }
      data.add(attValue);
    }
    return data;
  }

// --------------------------------------------------------------------------------------------------------

  public static String getKundenHeaderLineCSVFormatted(String separator) {
    Kunde.CSVPositions[] attNames = Kunde.CSVPositions.values();
    String out = "";
    for (Object o : Arrays.stream(attNames).toArray()) {
      out += o.toString() + separator;
    }
    return out;
  }

  public static String getFahrzeugeHeaderLineCSVFormatted(String separator) {
    Fahrzeug.CSVPositions[] attNames = Fahrzeug.CSVPositions.values();
    String out = "";
    for (Object o : Arrays.stream(attNames).toArray()) {
      out += o.toString() + separator;
    }
    return out;
  }

  public static String getBilderHeaderLineCSVFormatted(String separator) {
    Bild.CSVPositions[] attNames = Bild.CSVPositions.values();
    String out = "";
    for (Object o : Arrays.stream(attNames).toArray()) {
      out += o.toString() + separator;
    }
    return out;
  }

  public static String getStandorteHeaderLineCSVFormatted(String separator) {
    Standort.CSVPositions[] attNames = Standort.CSVPositions.values();
    String out = "";
    for (Object o : Arrays.stream(attNames).toArray()) {
      out += o.toString() + separator;
    }
    return out;
  }

}