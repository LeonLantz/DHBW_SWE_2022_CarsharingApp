package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.dhbwka.swe.utils.model.Attribute;
import de.dhbwka.swe.utils.model.IPersistable;
import de.dhbwka.swe.utils.util.CommonEntityManager;
import model.*;

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
            if (specificAtt.getName() == Fahrzeug.Attributes.STANDORT.getName()) {
              SingleEntityRecord[i] = ((Standort)specificAtt.getValue()).getElementID();
            } else {
              SingleEntityRecord[i] = specificAtt.getValue().toString();
            }
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

  public static List<String[]> getPersistedDokumenteCSVFormatted(CommonEntityManager entityManager) {
    List<String[]> CSVRecords = new ArrayList<>();
    List<String> CSVAttNames = Stream.of(Dokument.CSVPositions.values()).map(Dokument.CSVPositions::name).collect(Collectors.toList());
    List<IPersistable> allPersistedModelEntities = entityManager.findAll(Dokument.class);

    for (Object entity : allPersistedModelEntities) {
      String[] SingleEntityRecord = new String[CSVAttNames.size()];
      Attribute[] entityAttsAndValues = ((Dokument) entity).getAttributeArray();
      for (Attribute specificAtt : entityAttsAndValues) {
        for (int i = 0; i < CSVAttNames.size(); i++) {
          if (specificAtt.getName() == Dokument.Attributes.valueOf(CSVAttNames.get(i)).getName()) {
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
  
  public static List<String[]> getPersistedBuchungenCSVFormatted(CommonEntityManager entityManager) {
    List<IPersistable> allPersistedClassElements = entityManager.findAll(Buchung.class);
    List<String[]> data = new ArrayList<>();
    for (Object PersistableElement : allPersistedClassElements) {
      String[] attValue = new String[((Buchung) PersistableElement).getAttributes().toArray().length];
      for (int i = 0; i < attValue.length; i++) {
        Attribute a = ((Buchung) PersistableElement).getAttributes().get(i);
        if (a.getClazz() == Kunde.class) {
          if (a.getValue() == null) {
            attValue[i] = null;
          }else {
            attValue[i] = ((Kunde)a.getValue()).getElementID();
          }
        }else if (a.getClazz() == Fahrzeug.class) {
          if (a.getValue() == null) {
            attValue[i] = null;
          }else {
            attValue[i] = ((Fahrzeug)a.getValue()).getElementID();
          }
        }else if (a.getClazz() == Buchungsstatus.class) {
          attValue[i] = ((Buchungsstatus)a.getValue()).getBezeichner();
        }else {
          attValue[i] = a.getValue().toString();
        }
      }
      data.add(attValue);
    }
    return data;
  }

// --------------------------------------------------------------------------------------------------------

  public static String getKundenHeaderLineCSVFormatted(String separator) {
    Kunde.CSVPositions[] attNames = Kunde.CSVPositions.values();
    StringBuilder out = new StringBuilder("#");
    for (Object o : Arrays.stream(attNames).toArray()) {
      out.append(o.toString()).append(separator);
    }
    return out.toString();
  }

  public static String getFahrzeugeHeaderLineCSVFormatted(String separator) {
    Fahrzeug.CSVPositions[] attNames = Fahrzeug.CSVPositions.values();
    StringBuilder out = new StringBuilder("#");
    for (Object o : Arrays.stream(attNames).toArray()) {
      out.append(o.toString()).append(separator);
    }
    return out.toString();
  }

  public static String getBilderHeaderLineCSVFormatted(String separator) {
    Bild.CSVPositions[] attNames = Bild.CSVPositions.values();
    StringBuilder out = new StringBuilder("#");
    for (Object o : Arrays.stream(attNames).toArray()) {
      out.append(o.toString()).append(separator);
    }
    return out.toString();
  }

  public static String getDokumenteHeaderLineCSVFormatted(String separator) {
    Dokument.CSVPositions[] attNames = Dokument.CSVPositions.values();
    StringBuilder out = new StringBuilder("#");
    for (Object o : Arrays.stream(attNames).toArray()) {
      out.append(o.toString()).append(separator);
    }
    return out.toString();
  }

  public static String getStandorteHeaderLineCSVFormatted(String separator) {
    Standort.CSVPositions[] attNames = Standort.CSVPositions.values();
    StringBuilder out = new StringBuilder("#");
    for (Object o : Arrays.stream(attNames).toArray()) {
      out.append(o.toString()).append(separator);
    }
    return out.toString();
  }

  public static String getBuchungenHeaderLineCSVFormatted(String separator) {
    Buchung.CSVPositions[] attNames = Buchung.CSVPositions.values();
    StringBuilder out = new StringBuilder("#");
    for (Object o : Arrays.stream(attNames).toArray()) {
      out.append(o.toString()).append(separator);
    }
    return out.toString();
  }
}
