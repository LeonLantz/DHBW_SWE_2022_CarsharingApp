package gui;

import de.dhbwka.swe.utils.event.EventCommand;
import de.dhbwka.swe.utils.event.GUIEvent;
import de.dhbwka.swe.utils.event.IGUIEventListener;
import de.dhbwka.swe.utils.gui.ObservableComponent;
import de.dhbwka.swe.utils.model.Attribute;
import de.dhbwka.swe.utils.model.IDepictable;
import gui.customComponents.userInput.*;
import model.*;
import util.CSHelp;
import util.IValidate;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class GUIBuchungAnlegen extends ObservableComponent implements IValidate {

    public enum Commands implements EventCommand {

        ADD_BUCHUNG("GUIBuchungAnlegen.addBuchung", String[].class),
        UPDATE_FAHRZEUGE("GUIBuchungAnlegen.updateFahrzeuge", LocalDate[].class);

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

    //Map of all CustomInputFields
    private Map<String, CustomInputField> _inputFieldMap;
    private List<IDepictable> _allKunden, _allFahrzeuge;
    private Kunde _selectedKunde;
    private Fahrzeug _selectedFahrzeug;
    private List _currentValues;
    private long _days;
    private IDepictable _currentObject;
    private IGUIEventListener _observer;

    private JPanel _topPanel, _bottomPanel, _leftPanel, _rightPanel, _buttonPanel;
    private JLabel _topLabelValue, _topLabelDescription;
    private JButton _saveBuchung, _buttonLoadFahrzeuge, _infoButton;
    private String _randID;

    //constructor for creating new object
    public GUIBuchungAnlegen(IGUIEventListener _observer, List _allKunden) {
        this.addObserver(_observer);
        this._observer = _observer;
        this._allKunden = _allKunden;
        this._allFahrzeuge = new ArrayList<>();
        initUI(true);
    }

    //constructor for editing an existing object
    public GUIBuchungAnlegen(IGUIEventListener _observer, IDepictable _currentObject, Kunde _selectedKunde, Fahrzeug _selectedFahrzeug) {
        this.addObserver(_observer);
        this._currentObject = _currentObject;
        this._observer = _observer;
        this._selectedKunde = _selectedKunde;
        this._selectedFahrzeug = _selectedFahrzeug;
        initUI(false);
    }

    //Init user interface
    private void initUI(Boolean isNewObject) {
        this.setLayout(new BorderLayout(0, 0));
        this.setPreferredSize(new Dimension(500, 700));
        this.setBackground(CSHelp.main);

        Border borderTop = BorderFactory.createMatteBorder(1, 0, 0, 0, CSHelp.navBar);
        ImageIcon imageIconBuchungSpeichern = CSHelp.imageList.get("button_BuchungSpeichern.png");
        ImageIcon imageIconLoadFahrzeug = CSHelp.imageList.get("button_loadFahrzeuge.png");
        ImageIcon imageIconButtonInfo = CSHelp.imageList.get("button_info.png");

        _topPanel = new JPanel(new BorderLayout(0, 0));
        _topPanel.setBackground(Color.white);
        _topPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, CSHelp.navBar));
        _topLabelDescription = new JLabel("Objekt-ID: ");
        _topLabelDescription.setBorder(new EmptyBorder(10, 25, 10, 5));
        _topLabelDescription.setFont(CSHelp.lato.deriveFont(11f));
        _topLabelValue = new JLabel();
        _topLabelValue.setBorder(new EmptyBorder(10, 0, 10, 20));
        _topLabelValue.setFont(CSHelp.lato.deriveFont(9f));
        _topPanel.add(_topLabelDescription, BorderLayout.WEST);
        _topPanel.add(_topLabelValue, BorderLayout.CENTER);

        _bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        _bottomPanel.setBackground(Color.white);
        _bottomPanel.setBorder(borderTop);
        _saveBuchung = new JButton(imageIconBuchungSpeichern);
        _saveBuchung.setBorder(new EmptyBorder(0, 0, 0, 0));
        _saveBuchung.setCursor(new Cursor(Cursor.HAND_CURSOR));
        _saveBuchung.setToolTipText("Buchung speichern");
        _saveBuchung.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] fahrzeugData = getValues();
                if (validateInput()) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd. MMMM YYYY");
                    String message;
                    if (isNewObject) {
                        message = String.format("<html><body> Wollen Sie folgende Buchung speichern? <br><ul><li>%s</li><li>%s</li><li>%s bis %s</li><li>Dauer: %s Tage</li></ul></body></html>", _selectedKunde, _selectedFahrzeug, formatter.format(LocalDate.parse(fahrzeugData[4])), formatter.format(LocalDate.parse(fahrzeugData[5])), _days);
                    } else {
                        message = String.format("<html><body> Wollen Sie folgenden Buchungsstatus speichern? <br><ul><li>%s</ul></body></html>", fahrzeugData[6]);
                    }
                    int a = JOptionPane.showConfirmDialog(GUIBuchungAnlegen.this, message, "Bestätigung", JOptionPane.YES_NO_OPTION, 1, CSHelp.imageList.get("icon_buchung.png"));
                    if (a == 0) {
                        fireGUIEvent(new GUIEvent(this, Commands.ADD_BUCHUNG, fahrzeugData));
                    }
                }
            }
        });
        _bottomPanel.add(_saveBuchung, BorderLayout.CENTER);

        _leftPanel = new JPanel();
        _rightPanel = new JPanel();
        _leftPanel.setBackground(Color.WHITE);
        _rightPanel.setBackground(Color.WHITE);
        _leftPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        _rightPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        _leftPanel.setPreferredSize(new Dimension(250, 600));
        _rightPanel.setPreferredSize(new Dimension(250, 600));
        _leftPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        _rightPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        _inputFieldMap = new LinkedHashMap<>();
        _inputFieldMap.put("Buchungsnummer", new CustomTextField("Buchungsnummer", "wird automatisch generiert"));
        ((CustomTextField) _inputFieldMap.get("Buchungsnummer")).getTextfield().setEnabled(false);
        _inputFieldMap.put("Kunde", new CustomListField("Kunde", this._observer, this._allKunden));
        _inputFieldMap.put("Status", new CustomComboBox("Status", "Buchungsstatus", Buchungsstatus.getArray(), _observer));
        ((CustomComboBox) _inputFieldMap.get("Status")).getComboBox().setSelectedIndex(0);
        ((CustomComboBox) _inputFieldMap.get("Status")).getComboBox().setEnabled(false);
        _inputFieldMap.put("Start", new CustomDatePicker("Start", "Start der Buchung", this._observer));
        _inputFieldMap.put("Ende", new CustomDatePicker("Ende", "Ende der Buchung", this._observer));
        _inputFieldMap.put("Fahrzeug", new CustomListField("Fahrzeug", this._observer, this._allFahrzeuge));
        _inputFieldMap.put("Dokumente", new CustomListField("Dokumente", this._observer, this._currentObject));

        _leftPanel.add(_inputFieldMap.get("Buchungsnummer"));
        _leftPanel.add(_inputFieldMap.get("Kunde"));
        _leftPanel.add(_inputFieldMap.get("Start"));
        _leftPanel.add(_inputFieldMap.get("Ende"));
        _rightPanel.add(_inputFieldMap.get("Status"));
        _rightPanel.add(_inputFieldMap.get("Dokumente"));
        _rightPanel.add(_inputFieldMap.get("Fahrzeug"));

        _buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 7, 0));
        _buttonPanel.setBackground(Color.WHITE);

        _buttonLoadFahrzeuge = new JButton(imageIconLoadFahrzeug);
        _buttonLoadFahrzeuge.setCursor(new Cursor(Cursor.HAND_CURSOR));
        _buttonLoadFahrzeuge.setText("     Fahrzeuge laden     ");
        _buttonLoadFahrzeuge.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String startDate = getDateComponent("Start").getValue();
                String endDate = getDateComponent("Ende").getValue();
                if (startDate != null && endDate != null) {
                    if (CSHelp.isDate(startDate) && CSHelp.isDate(endDate)) {
                        LocalDate start = LocalDate.parse(startDate);
                        LocalDate end = LocalDate.parse(endDate);
                        if (!start.isAfter(end)) {
                            //TODO: days einbinden
                            if (start.isBefore(LocalDate.now())) {
                                JOptionPane.showMessageDialog(GUIBuchungAnlegen.this, "Es kann keine Buchung in der Vergangenheit erstellt werden!", "Buchung in Vergangenheit", JOptionPane.ERROR_MESSAGE);
                            } else {
                                GUIBuchungAnlegen.this._days = ChronoUnit.DAYS.between(start, end) + 1;
                                LocalDate[] dates = new LocalDate[]{start, end};
                                fireGUIEvent(new GUIEvent(this, Commands.UPDATE_FAHRZEUGE, dates));
                            }
                        } else {
                            JOptionPane.showMessageDialog(GUIBuchungAnlegen.this, "Bitte wählen Sie ein Startdatum, das vor dem Enddatum liegt, aus!", "Ende vor Start", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(GUIBuchungAnlegen.this, "Bitte wählen Sie ein Startdatum und ein Enddatum aus!", "Fehlende Daten", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        _infoButton = new JButton(imageIconButtonInfo);
        _infoButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        _infoButton.setBorder(new EmptyBorder(0, 0, 0, 0));
        _infoButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Durch das Betätigen des Buttons 'Fahrzeuge laden' werden alle \nim selektierten Zeitraum verfügbaren Fahrzeuge geladen. \nFür die Filterung sind nur Buchungen mit dem Status 'Aktiv' von Relevanz.", "Bedienungshilfe", JOptionPane.INFORMATION_MESSAGE, imageIconButtonInfo));

        _buttonPanel.add(_buttonLoadFahrzeuge);
        _buttonPanel.add(_infoButton);
        _leftPanel.add(_buttonPanel);

        this.add(_topPanel, BorderLayout.NORTH);
        this.add(_leftPanel, BorderLayout.WEST);
        this.add(_rightPanel, BorderLayout.EAST);
        this.add(_bottomPanel, BorderLayout.SOUTH);

        if (isNewObject) { createBuchung(); } else { editBuchung(); }
    }

    //Get the Start or Ende date component
    public CustomDatePicker getDateComponent(String type) {
        CustomDatePicker customDatePicker = null;
        if (type == "Start") {
            customDatePicker = ((CustomDatePicker) _inputFieldMap.get("Start"));
        } else if (type == "Ende") {
            customDatePicker = ((CustomDatePicker) _inputFieldMap.get("Ende"));
        }
        return customDatePicker;
    }

    //Create new booking / create UUID for element
    private void createBuchung() {
        _randID = UUID.randomUUID().toString();
        _topLabelValue.setText(_randID);
    }

    //Set all values of existing booking
    private void editBuchung() {
        Attribute[] attributes = _currentObject.getAttributeArray();
        _randID = attributes[0].getValue().toString();
        _topLabelValue.setText(_randID);
        _inputFieldMap.get("Buchungsnummer").setValue(attributes[1].getValue().toString());
        ((CustomTextField) _inputFieldMap.get("Buchungsnummer")).getTextfield().setEnabled(false);
        _inputFieldMap.get("Start").setValue(attributes[4].getValue().toString());
        _inputFieldMap.get("Ende").setValue(attributes[5].getValue().toString());
        ((CustomComboBox) _inputFieldMap.get("Status")).getComboBox().setEnabled(true);
        ((CustomComboBox) _inputFieldMap.get("Status")).getComboBox().setSelectedIndex(((Buchungsstatus) attributes[6].getValue()).ordinal());
        _inputFieldMap.get("Start").setEnabled(false);
        _inputFieldMap.get("Ende").setEnabled(false);
        _buttonLoadFahrzeuge.setVisible(false);
        _infoButton.setVisible(false);
        List kundenList = new ArrayList();
        kundenList.add(_selectedKunde);
        getKundenSLC().setListElements(kundenList);
        getKundenSLC().get_slc().selectElement(0);
        List fahrzeugList = new ArrayList();
        fahrzeugList.add(_selectedFahrzeug);
        getFahrzeugSLC().setListElements(fahrzeugList);
        getFahrzeugSLC().get_slc().selectElement(0);
        getKundenSLC().setEnabled(false);
        getFahrzeugSLC().setEnabled(false);

        LocalDate start = (LocalDate) attributes[4].getValue();
        LocalDate end = (LocalDate) attributes[5].getValue();
        _days = ChronoUnit.DAYS.between(start, end) + 1;
    }

    //Get all values set in the input form
    private String[] getValues() {
        _currentValues = new ArrayList<String>();
        _currentValues.add(_randID);
        Kunde kunde = (Kunde) ((CustomListField) _inputFieldMap.get("Kunde")).get_slc().getSelectedElement();
        _selectedKunde = kunde;
        Fahrzeug fahrzeug = (Fahrzeug) ((CustomListField) _inputFieldMap.get("Fahrzeug")).get_slc().getSelectedElement();
        _selectedFahrzeug = fahrzeug;
        String buchungsnummer = "";
        if (kunde != null) {
            buchungsnummer += kunde.getAttributeValueOf(Kunde.Attributes.VORNAME).toString().substring(0, 1);
            buchungsnummer += kunde.getAttributeValueOf(Kunde.Attributes.NACHNAME).toString().substring(0, 1);
        }
        buchungsnummer += "-";
        Random r = new Random();
        buchungsnummer += r.nextInt(1000000);
        _currentValues.add(buchungsnummer);
        if (kunde == null) {
            _currentValues.add(null);
        } else {
            _currentValues.add(kunde.getElementID());
        }
        if (fahrzeug == null) {
            _currentValues.add(null);
        } else {
            _currentValues.add(fahrzeug.getElementID());
        }
        _currentValues.add(_inputFieldMap.get("Start").getValue());
        _currentValues.add(_inputFieldMap.get("Ende").getValue());
        _currentValues.add(_inputFieldMap.get("Status").getValue());
        _currentValues.add(LocalDateTime.now().toString());

        return (String[]) _currentValues.toArray(new String[_currentValues.size()]);
    }

    //Load list of documents assigned to the booking
    public void updateDokumentList(List<IDepictable> dokumente) {
        CustomListField customListField = (CustomListField) _inputFieldMap.get("Dokumente");
        customListField.setListElements(dokumente);
    }

    //Getter
    public CustomListField getKundenSLC() {
        return (CustomListField) _inputFieldMap.get("Kunde");
    }

    public CustomListField getFahrzeugSLC() {
        return (CustomListField) _inputFieldMap.get("Fahrzeug");
    }

    public CustomListField getDokumentSLC() {
        return (CustomListField) _inputFieldMap.get("Dokumente");
    }

    //validateInput() of interface IValidate
    @Override
    public boolean validateInput() {
        if (_currentValues.get(2) == null) {
            JOptionPane.showMessageDialog(null, "Bitte wählen Sie einen Kunden aus!", "Kunde fehlerhaft", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (_currentValues.get(3) == null) {
            JOptionPane.showMessageDialog(null, "Bitte wählen Sie ein Fahrzeug aus! \n(Startdatum/Enddatum auswählen-->Fahrzeuge laden)", "Fahrzeug fehlerhaft", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Probably delete following loop (it is not possible to have invalid characters in Buchung fields...)
        // Semicolon Check
        // !!!!!!!!! DO NOT USE INTELLIJ OPTIMIZER AND TRY TO 'SIMPLIFY' THIS METHOD !!!!!!!!!!!
        // IT WILL BREAK IN CASE THERE IS MORE CODE AFTER IT
        if (!CSHelp.isValueListCsvCompliant(_currentValues, Buchung.getAllAttributeNames())) return false;

        return true;
    }

    /**
     * Checks a single input field if it contains a semicolon and throws a JOptionPane error message in this case
     * This has to be checked in order to work with CSV files properly
     */
    public boolean checkIfSemicolonForCurrentvalue(List values, int valueIndex) {
        if (values.get(valueIndex).toString().contains(";")) {
            String AttributeName = Arrays.stream(Buchung.getAllAttributeNames()).collect(Collectors.toList()).get(valueIndex);
            JOptionPane.showMessageDialog(null, "Feld "+ AttributeName + " enthält Semicolon!", AttributeName+" fehlerhaft", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}
