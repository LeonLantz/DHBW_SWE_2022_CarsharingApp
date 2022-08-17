package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.dhbwka.swe.utils.model.Attribute;
import de.dhbwka.swe.utils.model.IPersistable;
import de.dhbwka.swe.utils.util.CommonEntityManager;
import model.Bild;
import model.Fahrzeug;
import model.Kunde;
import model.Standort;

public class CSVHelper {
  //TODO: make methods dynamic for each model class (duplicate code)
  //  --> create generic interface with new getAttributeList() or similar, let it get extended by every model class


  // Following methods provide all persisted entities for a given model class in a specific CSV-formatted form
  // Only attributes for CSV writing are provided (according to CSVPosition Enums in model classes)
  public static List<String[]> getPersistedKundenCSVFormatted(CommonEntityManager entityManager) {
    List<String[]> CSVRecords = new ArrayList<>();
    List<String> CSVAttNames = Stream.of(Kunde.CSVPositions.values()).map(Kunde.CSVPositions::name).collect(Collectors.toList());
    List<IPersistable> allPersistedModelEntities = entityManager.findAll(Kunde.class);

    for (Object entity : allPersistedModelEntities) {
      String[] SingleEntityRecord = new String[CSVAttNames.size()];
      Attribute[] entityAttsAndValues = ((Kunde) entity).getAttributeArray();
      for (Attribute specificAtt : entityAttsAndValues) {
        for (int i = 0; i < CSVAttNames.size(); i++) {
          if (specificAtt.getName() == Kunde.Attributes.valueOf(CSVAttNames.get(i)).getName()) {
            SingleEntityRecord[i] = specificAtt.getValue().toString();
            break;
          }
        }
      }
      CSVRecords.add(SingleEntityRecord);
    }
    return CSVRecords;
  }

  public static List<String[]> getPersistedFahrzeugeCSVFormatted(CommonEntityManager entityManager) {
    List<String[]> CSVRecords = new ArrayList<>();
    List<String> CSVAttNames = Stream.of(Fahrzeug.CSVPositions.values()).map(Fahrzeug.CSVPositions::name).collect(Collectors.toList());
    List<IPersistable> allPersistedModelEntities = entityManager.findAll(Fahrzeug.class);

    for (Object entity : allPersistedModelEntities) {
      String[] SingleEntityRecord = new String[CSVAttNames.size()];
      Attribute[] entityAttsAndValues = ((Fahrzeug) entity).getAttributeArray();
      for (Attribute specificAtt : entityAttsAndValues) {
        for (int i = 0; i < CSVAttNames.size(); i++) {
          if (specificAtt.getName() == Fahrzeug.Attributes.valueOf(CSVAttNames.get(i)).getName()) {
            SingleEntityRecord[i] = specificAtt.getValue().toString();
            break;
          }
        }
      }
      CSVRecords.add(SingleEntityRecord);
    }
    return CSVRecords;
  }

  public static List<String[]> getPersistedBilderCSVFormatted(CommonEntityManager entityManager) {
    List<String[]> CSVRecords = new ArrayList<>();
    List<String> CSVAttNames = Stream.of(Bild.CSVPositions.values()).map(Bild.CSVPositions::name).collect(Collectors.toList());
    List<IPersistable> allPersistedModelEntities = entityManager.findAll(Bild.class);

    for (Object entity : allPersistedModelEntities) {
      String[] SingleEntityRecord = new String[CSVAttNames.size()];
      Attribute[] entityAttsAndValues = ((Bild) entity).getAttributeArray();
      for (Attribute specificAtt : entityAttsAndValues) {
        for (int i = 0; i < CSVAttNames.size(); i++) {
          if (specificAtt.getName() == Bild.Attributes.valueOf(CSVAttNames.get(i)).getName()) {
            SingleEntityRecord[i] = specificAtt.getValue().toString();
            break;
          }
        }
      }
      CSVRecords.add(SingleEntityRecord);
    }
    return CSVRecords;
  }

  public static List<String[]> getPersistedStandorteCSVFormatted(CommonEntityManager entityManager) {
    List<String[]> CSVRecords = new ArrayList<>();
    List<String> CSVAttNames = Stream.of(Standort.CSVPositions.values()).map(Standort.CSVPositions::name).collect(Collectors.toList());
    List<IPersistable> allPersistedModelEntities = entityManager.findAll(Standort.class);

    for (Object entity : allPersistedModelEntities) {
      String[] SingleEntityRecord = new String[CSVAttNames.size()];
      Attribute[] entityAttsAndValues = ((Standort) entity).getAttributeArray();
      for (Attribute specificAtt : entityAttsAndValues) {
        for (int i = 0; i < CSVAttNames.size(); i++) {
          if (specificAtt.getName() == Standort.Attributes.valueOf(CSVAttNames.get(i)).getName()) {
            SingleEntityRecord[i] = specificAtt.getValue().toString();
            break;
          }
        }
      }
      CSVRecords.add(SingleEntityRecord);
    }
    return CSVRecords;
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
