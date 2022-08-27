package control;

import de.dhbwka.swe.utils.event.EventCommand;
import de.dhbwka.swe.utils.event.GUIEvent;
import de.dhbwka.swe.utils.event.IGUIEventListener;
import de.dhbwka.swe.utils.event.IUpdateEventListener;
import de.dhbwka.swe.utils.event.IUpdateEventSender;
import de.dhbwka.swe.utils.event.UpdateEvent;
import de.dhbwka.swe.utils.gui.CalendarComponent;
import de.dhbwka.swe.utils.gui.ObservableComponent;
import de.dhbwka.swe.utils.gui.SimpleListComponent;
import de.dhbwka.swe.utils.model.IDepictable;
import de.dhbwka.swe.utils.model.IPersistable;
import de.dhbwka.swe.utils.util.AppLogger;
import de.dhbwka.swe.utils.util.CommonEntityManager;
import de.dhbwka.swe.utils.util.IAppLogger;
import gui.GUIBuchungAnlegen;
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

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

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
        SET_DOKUMENTE("Controller.setDokumente", List.class),
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
        _logger.setSeverity(IAppLogger.Severity.DEBUG_LOW);
    }

    private Class _currentObjectClass;
    private IDepictable _currentObject;
    private ObservableComponent _dialogWindowComponent;

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
            this.fireUpdateEvent(new UpdateEvent(this, Commands.SET_DOKUMENTE, entityManager.findAll(Dokument.class)));

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
        modelClasses.put("Dokumente.csv", Dokument.class);
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

    private String getAbsolutWorkingDirectory() {
        String jarPath = "";
        try {
            jarPath = URLDecoder.decode(getClass().getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        // Incase a maven jar is run
        if (jarPath.endsWith(".jar")) {
            return jarPath.substring(0, jarPath.lastIndexOf(sp)) + "/classes";
        }
        return jarPath.substring(0, jarPath.lastIndexOf(sp));
    }

    public void writeAllCSVData(String csvDirectory) throws IOException {
        //TODO: add all persistable model classes
        List<String[]> KundenCSVOut = CSVHelper.getPersistedKundenCSVFormatted(this.entityManager);
        List<String[]> FahrzeugeCSVOut = CSVHelper.getPersistedFahrzeugeCSVFormatted(this.entityManager);
        List<String[]> BilderCSVOut = CSVHelper.getPersistedBilderCSVFormatted(this.entityManager);
        List<String[]> DokumenteCSVOut = CSVHelper.getPersistedDokumenteCSVFormatted(this.entityManager);
        List<String[]> StandorteCSVOut = CSVHelper.getPersistedStandorteCSVFormatted(this.entityManager);
        List<String[]> BuchungenCSVOut = CSVHelper.getPersistedBuchungenCSVFormatted(this.entityManager);

        String separator = ";";
        this.writeCSVData(csvDirectory + "Kunden.csv", KundenCSVOut, separator, CSVHelper.getKundenHeaderLineCSVFormatted(separator));
        this.writeCSVData(csvDirectory + "Fahrzeuge.csv", FahrzeugeCSVOut, separator, CSVHelper.getFahrzeugeHeaderLineCSVFormatted(separator));
        this.writeCSVData(csvDirectory + "Bilder.csv", BilderCSVOut, separator, CSVHelper.getBilderHeaderLineCSVFormatted(separator));
        this.writeCSVData(csvDirectory + "Dokumente.csv", DokumenteCSVOut, separator, CSVHelper.getDokumenteHeaderLineCSVFormatted(separator));
        this.writeCSVData(csvDirectory + "Standorte.csv", StandorteCSVOut, separator, CSVHelper.getStandorteHeaderLineCSVFormatted(separator));
        this.writeCSVData(csvDirectory + "Buchungen.csv", BuchungenCSVOut, separator, CSVHelper.getBuchungenHeaderLineCSVFormatted(separator));
    }

    public void writeCSVData(String csvFilename, List<String[]> csvData, String separator, String headerLine) throws IOException {
        WorkingCSVWriter workingCSVWriter = new WorkingCSVWriter(csvFilename, separator, headerLine);
        workingCSVWriter.writeData(csvData);
    }

    private List<IDepictable> getBilderByKey(String primaryKey) {
        List<IPersistable> alleBilder = entityManager.findAll(Bild.class);
        List<Bild> lstBild = new ArrayList<>();
        for (IPersistable iPersistable : alleBilder) {
            lstBild.add((Bild) iPersistable);
        }
        lstBild = lstBild.stream()
                .filter(b -> b.getSecondaryKey().equals(primaryKey))
                .collect(Collectors.toList());

        List<IDepictable> returnList = new ArrayList<>();
        for (Bild bild : lstBild) {
            returnList.add(bild);
        }
        return returnList;
    }

    private List<IDepictable> getDokumenteByKey(String primaryKey) {
        List<IPersistable> alleDokumente = entityManager.findAll(Dokument.class);
        List<Dokument> lstDokument = new ArrayList<>();
        for (IPersistable iPersistable : alleDokumente) {
            lstDokument.add((Dokument) iPersistable);
        }
        lstDokument = lstDokument.stream()
                .filter(b -> b.getSecondaryKey().equals(primaryKey))
                .collect(Collectors.toList());

        List<IDepictable> returnList = new ArrayList<>();
        for (Dokument dokument : lstDokument) {
            returnList.add(dokument);
        }
        return returnList;
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

        // Button zum Erstellen eines neuen Entities (Kunde, Fahrzeug, ...) wurde gedrückt
        if (ge.getCmd().equals(MainComponentMitNavBar.Commands.BUTTON_PRESSED)) {
            _currentObjectClass = (Class) ge.getData();
            if (_currentObjectClass == Buchung.class) {
                _dialogWindowComponent = new GUIBuchungAnlegen(this, entityManager.findAll(Kunde.class));
                CSHelp.createJDialog(_dialogWindowComponent, new Dimension(500, 550));
            } else if (_currentObjectClass == Fahrzeug.class) {
                _dialogWindowComponent = new GUIFahrzeugAnlegen(this);
                CSHelp.createJDialog(_dialogWindowComponent, new Dimension(500, 720));
            } else if (_currentObjectClass == Kunde.class) {
                _dialogWindowComponent = new GUIKundeAnlegen(this);
                CSHelp.createJDialog(_dialogWindowComponent, new Dimension(500, 400));
            } else if (_currentObjectClass == Standort.class) {
                //TODO: Standort anlegen und bearbeiten ausarbeiten
            }
        }
        // Button zum Bearbeiten eines Tabelleneintrags (Entity) wurde gedrückt
        else if (ge.getCmd().equals(CustomTableComponent.Commands.EDIT_ROW)) {
            _currentObjectClass = ((IDepictable) ge.getData()).getClass();
            _currentObject = (IDepictable) ge.getData();

            if (_currentObjectClass == Buchung.class) {
                Kunde kunde = ((Buchung)_currentObject).getAttributeValueOf(Buchung.Attributes.KUNDE);
                Fahrzeug fahrzeug = ((Buchung)_currentObject).getAttributeValueOf(Buchung.Attributes.FAHRZEUG);
                _dialogWindowComponent = new GUIBuchungAnlegen(this, _currentObject, kunde, fahrzeug);
                ((GUIBuchungAnlegen) _dialogWindowComponent).updateDokumentList(this.getDokumenteByKey(_currentObject.getElementID()));
                CSHelp.createJDialog(_dialogWindowComponent, new Dimension(500, 550));
            } else if (_currentObjectClass == Fahrzeug.class) {
                _dialogWindowComponent = new GUIFahrzeugAnlegen(this, _currentObject);
                ((GUIFahrzeugAnlegen) _dialogWindowComponent).updateBildList(this.getBilderByKey(_currentObject.getElementID()));
                CSHelp.createJDialog(_dialogWindowComponent, new Dimension(500, 720));
            } else if (_currentObjectClass == Kunde.class) {
                _dialogWindowComponent = new GUIKundeAnlegen(this, _currentObject);
                //((GUIFahrzeugAnlegen) _dialogWindowComponent).updateBildList(this.getBilderByKey(currentObject.getElementID()));
                CSHelp.createJDialog(_dialogWindowComponent, new Dimension(500, 400));
            } else if (_currentObjectClass == Standort.class) {

            }
        }
        // Verfügbare Fahrzeuge für entsprechenden Zeitraum setzen
        else if (ge.getCmd().equals(GUIBuchungAnlegen.Commands.UPDATE_FAHRZEUGE)) {
            ArrayList<String> blockedFahrzeugeIDs = new ArrayList<>();
            LocalDate[] newDates = (LocalDate[]) ge.getData();

            List<IPersistable> allUnfilteredBuchungen = entityManager.findAll(Buchung.class);
            allUnfilteredBuchungen.forEach(buchung -> {
                // Check if Buchung is Active
                if (((Buchung) buchung).getAttributeValueOf(Buchung.Attributes.STATUS).toString().equalsIgnoreCase("AKTIV")) {
                    LocalDate oldStart = ((Buchung) buchung).getAttributeValueOf(Buchung.Attributes.START_DATE);
                    LocalDate oldEnd = ((Buchung) buchung).getAttributeValueOf(Buchung.Attributes.END_DATE);
                    // Check if old and new date ranges overlap
                    if (!(newDates[1].isBefore(oldStart) || newDates[0].isAfter(oldEnd))) {
                        blockedFahrzeugeIDs.add(((Fahrzeug)((Buchung) buchung).getAttributeValueOf(Buchung.Attributes.FAHRZEUG)).getElementID());
                    }
                }
            });

            // Filter Car List by using only unblocked cars
            List<IPersistable> blockedFahrzeugList = entityManager.findAll(Fahrzeug.class);
            blockedFahrzeugeIDs.forEach(blockedFahrzeugID -> {
                blockedFahrzeugList.removeIf(fahrzeug -> (((Fahrzeug) fahrzeug).getElementID().equals(blockedFahrzeugID)));
            });

            ((GUIBuchungAnlegen) _dialogWindowComponent).getFahrzeugSLC().setListElements(blockedFahrzeugList);
        }
        // Eintrag einer SimpleListComponent wurde ausgewählt
        else if (ge.getCmd().equals(SimpleListComponent.Commands.ELEMENT_SELECTED)) {
            SimpleListComponent slc = null;

            if (ge.getData() == null) {
                return;
            }

            if (ge.getData().getClass() == Bild.class) {
                if (_currentObjectClass == Kunde.class) {
                    slc = ((GUIKundeAnlegen) _dialogWindowComponent).getBildList().get_slc();
                } else if (_currentObjectClass == Fahrzeug.class) {
                    slc = ((GUIFahrzeugAnlegen) _dialogWindowComponent).getBildList().get_slc();
                }
                Bild bild = (Bild) ge.getData();
                String filePath = getAbsolutWorkingDirectory() + bild.getAttributeValueOf(Bild.Attributes.FILEPATH);
                ImageIcon imageIcon = new ImageIcon(filePath);
                String[] options = new String[]{"Schließen", "Löschen"};
                int answer = JOptionPane.showOptionDialog(_dialogWindowComponent, "", "Bildname: " + bild.getAttributeValueOf(Bild.Attributes.TITLE), JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, imageIcon, options, options[0]);
                if (answer == 1) {
                    this.entityManager.remove((IPersistable) ge.getData());
                    if (_currentObjectClass == Fahrzeug.class) {
                        ((GUIFahrzeugAnlegen) _dialogWindowComponent).updateBildList(this.getBilderByKey(_currentObject.getElementID()));
                    } else if (_currentObjectClass == Kunde.class) {

                    }
                }
                slc.clearSelection();

            } else if (ge.getData().getClass() == Kunde.class) {
                CustomListField clfKunden = ((GUIBuchungAnlegen) _dialogWindowComponent).getKundenSLC();
//                int a = JOptionPane.showConfirmDialog(null, "Wollen Sie der Buchung den Kunden: " + clfKunden.get_slc().getSelectedElement().toString() + " zuordnen?", "Sicher?", JOptionPane.YES_NO_OPTION);
//                if (a == 0) {
//                    //TODO: Neuen Kunden zuordnen
//                }
            } else if (ge.getData().getClass() == Fahrzeug.class) {
                CustomListField clfFahrzeuge = ((GUIBuchungAnlegen) _dialogWindowComponent).getFahrzeugSLC();
                System.out.println(clfFahrzeuge.get_slc().getSelectedElement());
            } else if (ge.getData().getClass() == Dokument.class) {
                Dokument dokument = (Dokument) ge.getData();
                if (_currentObjectClass == Buchung.class) {
                    slc = ((GUIBuchungAnlegen) _dialogWindowComponent).getDokumentSLC().get_slc();
                    String path = dokument.getAttributeValueOf(Dokument.Attributes.FILEPATH);
                    File file = new File(getAbsolutWorkingDirectory() + path);
                    JLabel label = new JLabel("<html> Wollen Sie das Dokument <b>" + dokument.toString() +  "</b> wirklich öffnen?</html>");
                    ImageIcon icon = CSHelp.imageList.get("icon_dokument.png");
                    int answer = JOptionPane.showOptionDialog(_dialogWindowComponent, label, "Dokument öffnen?", JOptionPane.YES_NO_OPTION, JOptionPane.OK_OPTION, icon, null, null);
                    if (answer == 0) {
                        if (Desktop.isDesktopSupported()) {
                            try {
                                Desktop.getDesktop().open(file);
                            } catch (IOException ex) {
                                // no application registered for PDFs
                            }
                        }
                    }
                    slc.clearSelection();
                } else if (_currentObjectClass == Fahrzeug.class) {

                }
            }
        }
        // Entity: Fahrzeug hinzufügen
        else if (ge.getCmd().equals(GUIFahrzeugAnlegen.Commands.ADD_FAHRZEUG)) {
            String[] fahrzeugAttribute = (String[]) ge.getData();
            try {
                this.elementFactory.createElement(Fahrzeug.class, fahrzeugAttribute);
                this.fireUpdateEvent(new UpdateEvent(this, Commands.SET_FAHRZEUGE, entityManager.findAll(Fahrzeug.class)));
                this.fireUpdateEvent(new UpdateEvent(this, Commands.SET_STATISTICS, getCounts()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // Entity: Buchung hinzufügen
        else if (ge.getCmd().equals(GUIBuchungAnlegen.Commands.ADD_BUCHUNG)) {
            String[] buchungAttribute = (String[]) ge.getData();
            try {
                this.elementFactory.createElement(Buchung.class, buchungAttribute);
                this.fireUpdateEvent(new UpdateEvent(this, Commands.SET_BUCHUNGEN, entityManager.findAll(Buchung.class)));
                this.fireUpdateEvent(new UpdateEvent(this, Commands.SET_STATISTICS, getCounts()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // Entity: Bild hinzufügen
        else if (ge.getCmd().equals(CustomListField.Commands.ADD_BILD)) {
            String[] bildData = (String[]) ge.getData();
            try {
                this.elementFactory.createElement(Bild.class, bildData );
                if (_currentObjectClass == Buchung.class) {
                    //((GUIBuchungAnlegen) _dialogWindowComponent).updateBildList(this.getBilderByKey(bildData[3]));
                } else if (_currentObjectClass == Fahrzeug.class) {
                    ((GUIFahrzeugAnlegen) _dialogWindowComponent).updateBildList(this.getBilderByKey(bildData[3]));
                } else if (_currentObjectClass == Kunde.class) {
                    //((GUIKundeAnlegen) _dialogWindowComponent).updateBildList(this.getBilderByKey(currentObject.getElementID()));
                } else if (_currentObjectClass == Standort.class) {

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // Entity: Dokument hinzufügen
        else if (ge.getCmd().equals(CustomListField.Commands.ADD_DOKUMENT)) {
            String[] dokumentData = (String[]) ge.getData();
            try {
                this.elementFactory.createElement(Dokument.class, dokumentData );
                this.fireUpdateEvent(new UpdateEvent(this, Commands.SET_DOKUMENTE, entityManager.findAll(Dokument.class)));
                if (_currentObjectClass == Buchung.class) {
                    ((GUIBuchungAnlegen) _dialogWindowComponent).updateDokumentList(this.getDokumenteByKey(dokumentData[3]));
                } else if (_currentObjectClass == Fahrzeug.class) {

                } else if (_currentObjectClass == Kunde.class) {
                    //((GUIKundeAnlegen) _dialogWindowComponent).updateBildList(this.getBilderByKey(currentObject.getElementID()));
                } else if (_currentObjectClass == Standort.class) {

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // Entity löschen
        else if (ge.getCmd().equals(CustomTableComponent.Commands.DELETE_ENTITY)) {
            entityManager.remove((IPersistable) ge.getData());
            fireUpdateEvent(new UpdateEvent(this, Commands.SET_KUNDEN, entityManager.findAll(Kunde.class)));
            fireUpdateEvent(new UpdateEvent(this, Commands.SET_FAHRZEUGE, entityManager.findAll(Fahrzeug.class)));
            fireUpdateEvent(new UpdateEvent(this, Commands.SET_STANDORTE, entityManager.findAll(Standort.class)));
            this.fireUpdateEvent(new UpdateEvent(this, Commands.SET_DOKUMENTE, entityManager.findAll(Dokument.class)));

            for (IPersistable b : entityManager.findAll(Buchung.class)) {
                Buchung buchung = (Buchung) b;
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
            this.fireUpdateEvent(new UpdateEvent(this, Commands.SET_BUCHUNGEN, entityManager.findAll(Buchung.class)));
            this.fireUpdateEvent(new UpdateEvent(this, Commands.SET_STATISTICS, getCounts()));
        }

        else if (ge.getCmd().equals(CalendarComponent.Commands.DATE_SELECTED)) {
            if (_currentObjectClass == Buchung.class) {
                String fieldType = ((CalendarComponent)ge.getSource()).getID();
                ((GUIBuchungAnlegen)_dialogWindowComponent).getDateComponent(fieldType).setValue(ge.getData().toString());
                ((GUIBuchungAnlegen)_dialogWindowComponent).getDateComponent(fieldType).closeDateDialog();
                ((GUIBuchungAnlegen)_dialogWindowComponent).getFahrzeugSLC().get_slc().removeAllListElements();
            }
        }
    }

    /**
     * zum Senden von UpdateEvents an entsprechende Listener (IUpdateEventListener)
     *
     * @param ue
     */
    private void fireUpdateEvent(UpdateEvent ue) {
        for (EventListener eventListener : allListeners) {
            if (eventListener instanceof IUpdateEventListener) {
                ((IUpdateEventListener) eventListener).processUpdateEvent(ue);
            }
        }
    }
}
