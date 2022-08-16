package control;

import de.dhbwka.swe.utils.event.EventCommand;
import de.dhbwka.swe.utils.event.GUIEvent;
import de.dhbwka.swe.utils.event.IGUIEventListener;
import de.dhbwka.swe.utils.event.IUpdateEventListener;
import de.dhbwka.swe.utils.event.IUpdateEventSender;
import de.dhbwka.swe.utils.event.UpdateEvent;
import de.dhbwka.swe.utils.model.IDepictable;
import de.dhbwka.swe.utils.model.IPersistable;
import de.dhbwka.swe.utils.util.AppLogger;
import de.dhbwka.swe.utils.util.CommonEntityManager;
import de.dhbwka.swe.utils.util.IAppLogger;
import gui.GUIKundeAnlegen;
import gui.MainComponentMitNavBar;
import gui.customComponents.CustomTableComponent;
import gui.GUIFahrzeugAnlegen;
import gui.customComponents.userInput.CustomListField;
import model.*;
import util.CSHelp;
import util.CSVHelper;
import util.ElementFactory;
import util.WorkingCSVReader;
import util.WorkingCSVWriter;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class CSControllerReinerObserverUndSender implements IGUIEventListener, IUpdateEventSender {

    /**
     * Folgende Commands deklarieren! Es sind die Commands, welche der Controller
     * z.B. an die MainGUI als UpdateEvent sendet
     * Die Commands müssen nur oben deklariert werden, der Rest des enums ist copy/paste (s. SWE-utils-GUIs)
     */
    public enum Commands implements EventCommand {

        /**
         * Command:  ID + gelieferter Payload-Typ
         */
        SET_BUCHUNGEN("Controller.setBuchungen", List.class),
        SET_FAHRZEUGE("Controller.setFahrzeuge", List.class),
        SET_KUNDEN("Controller.setKunden", List.class),
        SET_STANDORTE("Controller.setStandorte", List.class),
        SET_BILDER("Controller.setBilder", IDepictable.class),
        DELETE_BILD("Controller.deleteBild", IDepictable.class),
        SET_STATISTICS("Controller.setStatistics", Integer[].class);

        public final Class<?> payloadType;
        public final String cmdText;

        Commands(String cmdText, Class<?> payloadType) {
            this.cmdText = cmdText;
            this.payloadType = payloadType;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getCmdText() {
            return this.cmdText;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Class<?> getPayloadType() {
            return this.payloadType;
        }
    }

    /**
     * alle Listener, die auf Updates warten
     */
    List<EventListener> allListeners = new ArrayList<>();

    /**
     * entityManager für die Elemente (können auch mehrere EntityManager sein ...
     */
    CommonEntityManager entityManager = new CommonEntityManager();

    /**
     * entityFactory für die Elemente
     */
    ElementFactory elementFactory = new ElementFactory(entityManager);

    IAppLogger _logger = AppLogger.getInstance();

    WorkingCSVReader _workingCSVReader;

    private static final String sp = File.separator;

    public CSControllerReinerObserverUndSender() {
		_logger.setSeverity(  IAppLogger.Severity.DEBUG_LOW );
    }

    //TODO: probably delete unused parameter "propFile"

    /**
     * "start" the controller.
     * Das muss separat gemacht werden, da beim Verknüpfen der Observer (Controller+MainGUI)
     * das Laden der Daten bereits durchgeführt wurde und somit der UpdateEvent "ins Leere" ging
     */
    public void init(String csvDirectory, String propFile) {
        try {
            this.loadCSVData(csvDirectory);
            this.fireUpdateEvent(new UpdateEvent(this, Commands.SET_KUNDEN, entityManager.findAll(Kunde.class)));
            this.fireUpdateEvent(new UpdateEvent(this, Commands.SET_BUCHUNGEN, entityManager.findAll(Buchung.class)));
            this.fireUpdateEvent(new UpdateEvent(this, Commands.SET_FAHRZEUGE, entityManager.findAll(Fahrzeug.class)));
            this.fireUpdateEvent(new UpdateEvent(this, Commands.SET_STANDORTE, entityManager.findAll(Standort.class)));

            this.fireUpdateEvent(new UpdateEvent(this, Commands.SET_STATISTICS, getCounts()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Integer[] getCounts() {
        Integer[] counts = new Integer[]{
                entityManager.findAll(Kunde.class).size(),
                entityManager.findAll(Buchung.class).size(),
                entityManager.findAll(Fahrzeug.class).size(),
                entityManager.findAll(Standort.class).size()
        };
        return counts;
    }

    private void loadCSVData(String csvDirectory) throws IOException {
        Map<String, Class> modelClasses = new LinkedHashMap<>();
        modelClasses.put("Kunden.csv", Kunde.class);
        modelClasses.put("Fahrzeuge.csv", Fahrzeug.class);
        modelClasses.put("Buchungen.csv", Buchung.class);
        modelClasses.put("Bilder.csv", Bild.class);
        modelClasses.put("Standorte.csv", Standort.class);


        for (String fileName : modelClasses.keySet()) {
            _workingCSVReader = new WorkingCSVReader(csvDirectory + fileName, ";", true);
            List<String[]> csvData = _workingCSVReader.readData();

            csvData.forEach(e -> {
                try {
                    elementFactory.createElement(modelClasses.get(fileName), e);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            });
        }
    }

    public void writeAllCSVData(String csvDirectory) throws IOException {
        //TODO: add all persistable model classes
        List<String[]> KundenCSVOut = CSVHelper.getPersistedKundenCSVFormatted(this.entityManager);
        List<String[]> FahrzeugeCSVOut = CSVHelper.getPersistedFahrzeugeCSVFormatted(this.entityManager);
        List<String[]> BilderCSVOut = CSVHelper.getPersistedBilderCSVFormatted(this.entityManager);
        List<String[]> StandorteCSVOut = CSVHelper.getPersistedStandorteCSVFormatted(this.entityManager);
        List<String[]> BuchungenCSVOut = CSVHelper.getPersistedBuchungenCSVFormatted(this.entityManager);

        //TODO: generate 'headerLine' dynamically (according to current model attributes)
        this.writeCSVData(csvDirectory+"Kunden.csv", KundenCSVOut, ";", "#ID;ImageFile;Vorname;Nachname;Email;Phone;IBAN;dateOfBirth;last_edited;");
        this.writeCSVData(csvDirectory+"Fahrzeuge.csv", FahrzeugeCSVOut, ";", "#ID;Bezeichnung;Marke;Motor;Türen;Sitze;Kofferraumvolumen;Gewicht;Fahrzeugkategorie;Führerscheinklasse;Nummernschild;Tüv_Bis;Farbe;last_edited;");
        this.writeCSVData(csvDirectory+"Bilder.csv", BilderCSVOut, ";", "#ID;Title;FilePath;Key;");
        this.writeCSVData(csvDirectory+"Standorte.csv", StandorteCSVOut, ";", "#ID;STRASSE;PLZ;ORT;LAND;KOORDINATEN;KAPAZITÄT;LAST_EDIT;");
        this.writeCSVData(csvDirectory+"Buchungen.csv", BuchungenCSVOut, ";", "#ID;Buchungsnummer;Kunde;Fahrzeug;Start;End;Status;last_edited;");

    }

    public void writeCSVData(String csvFilename, List<String[]> csvData, String separator, String headerLine) throws IOException {
        WorkingCSVWriter workingCSVWriter = new WorkingCSVWriter(csvFilename, separator, headerLine);
        workingCSVWriter.writeData(csvData);
    }


    // fuer alle GUI-Elemente, die aktualisiert werden sollen:
    @Override
    public boolean addObserver(EventListener eL) {
        return this.allListeners.add(eL);
    }

    @Override
    public boolean removeObserver(EventListener eL) {
        return this.allListeners.remove(eL);
    }

    // die GUI-Events verarbeiten, die von den GUIs kommen:
    @Override
    public void processGUIEvent(GUIEvent ge) {
        _logger.debug("Hier ist der Controller!   Event: " + ge);

        if (ge.getCmd().equals(MainComponentMitNavBar.Commands.BUTTON_PRESSED)) {
            if (ge.getData() == Kunde.class) {
                GUIKundeAnlegen guiKundeAnlegen = new GUIKundeAnlegen(this);
                CSHelp.createJDialog(guiKundeAnlegen, new Dimension(500, 400));
            } else if (ge.getData() == Fahrzeug.class) {
                GUIFahrzeugAnlegen guiFahrzeugAnlegen = new GUIFahrzeugAnlegen(this);
                CSHelp.createJDialog(guiFahrzeugAnlegen, new Dimension(500, 700));
            } else if (ge.getData() == Standort.class) {
                //TODO: Standort anlegen und bearbeiten ausarbeiten
            }
        } else if (ge.getCmd().equals(GUIFahrzeugAnlegen.Commands.ADD_FAHRZEUG)) {
            String[] fahrzeugAttribute = (String[]) ge.getData();
            try {
                this.elementFactory.createElement(Fahrzeug.class, fahrzeugAttribute);
                this.fireUpdateEvent(new UpdateEvent(this, Commands.SET_FAHRZEUGE, entityManager.findAll(Fahrzeug.class)));
                this.fireUpdateEvent(new UpdateEvent(this, Commands.SET_STATISTICS, getCounts()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (ge.getCmd().equals(CustomTableComponent.Commands.DELETE_ENTITY)) {
            //TODO: Element löschen
            entityManager.remove((IPersistable)ge.getData());
            fireUpdateEvent( new UpdateEvent(this, Commands.SET_KUNDEN, entityManager.findAll(Kunde.class) ) );
            fireUpdateEvent( new UpdateEvent(this, Commands.SET_FAHRZEUGE, entityManager.findAll(Fahrzeug.class) ) );
            fireUpdateEvent( new UpdateEvent(this, Commands.SET_BILDER, entityManager.findAll(Bild.class) ) );

            //---
            for (IPersistable b : entityManager.findAll(Buchung.class)) {
                Buchung buchung = (Buchung)b;
                Kunde kunde = buchung.getAttributeValueOf(Buchung.Attributes.KUNDE);
                Fahrzeug fahrzeug = buchung.getAttributeValueOf(Buchung.Attributes.FAHRZEUG);
                if (kunde != null) {
                    if (entityManager.find(Kunde.class, kunde.getElementID()) == null) {
                        try {
                            buchung.setAttributeValueOf(Buchung.Attributes.STATUS, Buchungsstatus.INVALIDE);
                            buchung.setAttributeValueOf(Buchung.Attributes.KUNDE, new Kunde());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                if (fahrzeug != null) {
                    if (entityManager.find(Fahrzeug.class, fahrzeug.getElementID()) == null) {
                        try {
                            buchung.setAttributeValueOf(Buchung.Attributes.STATUS, Buchungsstatus.INVALIDE);
                            buchung.setAttributeValueOf(Buchung.Attributes.FAHRZEUG, new Fahrzeug());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }




            //---
            fireUpdateEvent( new UpdateEvent(this, Commands.SET_BUCHUNGEN, entityManager.findAll(Buchung.class) ) );
            this.fireUpdateEvent(new UpdateEvent(this, Commands.SET_STATISTICS, getCounts()));
        } else if (ge.getCmd().equals(CustomListField.Commands.ADD_BILD)) {
            try {
                this.elementFactory.createElement(Bild.class, (String[]) ge.getData());
                this.fireUpdateEvent(new UpdateEvent(this, Commands.SET_BILDER, entityManager.findAll(Bild.class)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (ge.getCmd().equals(MainComponentMitNavBar.Commands.UPDATE_IMAGES)) {
            this.fireUpdateEvent(new UpdateEvent(this, Commands.SET_BILDER, entityManager.findAll(Bild.class)));
        } else if (ge.getCmd().equals(Commands.DELETE_BILD)) {
            this.entityManager.remove((IPersistable) ge.getData());
            this.fireUpdateEvent(new UpdateEvent(this, Commands.SET_BILDER, entityManager.findAll(Bild.class)));
        }
    }
		
	/**
	 * zum Senden von UpdateEvents an entsprechende Listener (IUpdateEventListener)
	 * @param ue
	 */
	private void fireUpdateEvent( UpdateEvent ue ) {
		for (EventListener eventListener : allListeners) {
			if( eventListener instanceof IUpdateEventListener ) {
				((IUpdateEventListener)eventListener).processUpdateEvent(ue);
			}
		}
	}

	public List<EventListener> getAllListeners() {
		return allListeners;
	}

	public CommonEntityManager getEntityManager() {
		return entityManager;
	}

	public ElementFactory getElementFactory() {
		return elementFactory;
	}
}
