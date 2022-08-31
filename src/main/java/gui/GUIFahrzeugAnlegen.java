package gui;

import de.dhbwka.swe.utils.event.EventCommand;
import de.dhbwka.swe.utils.event.GUIEvent;
import de.dhbwka.swe.utils.event.IGUIEventListener;
import de.dhbwka.swe.utils.gui.ObservableComponent;
import de.dhbwka.swe.utils.model.Attribute;
import de.dhbwka.swe.utils.model.IDepictable;
import gui.customComponents.userInput.CustomComboBox;
import gui.customComponents.userInput.CustomInputField;
import gui.customComponents.userInput.CustomListField;
import gui.customComponents.userInput.CustomTextField;
import model.Fahrzeug;
import model.Fahrzeugkategorie;
import model.Motorisierung;
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
import java.util.*;
import java.util.List;

public class GUIFahrzeugAnlegen extends ObservableComponent implements IValidate {

    public enum Commands implements EventCommand {

        ADD_FAHRZEUG( "GUIFahrzeugAnlegen.addFahrzeug", String[].class ),
        ADD_BILD("GUIFahrzeugAnlegen.addBild", String[].class);

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

    private IGUIEventListener _observer;
    private String _randID;
    private List _currentValues;

    private List<IDepictable> _allStandorte;

    private JPanel topPanel, bottomPanel, leftPanel, centerPanel, rightPanel;
    private JLabel topLabelValue, topLabelDescription;
    private JButton save_fahrzeug;

    private Standort _selectedStandort;

    private IDepictable iDepictable;

    //constructor for creating new Object
    public GUIFahrzeugAnlegen(IGUIEventListener _observer, List allStandorte) {
        this.addObserver(_observer);
        this._observer = _observer;
        this._allStandorte = allStandorte;
        initUI(true);
    }

    //constructor for editing an existing Object
    public GUIFahrzeugAnlegen(IGUIEventListener _observer, IDepictable iDepictable, List allStandorte, Standort selectedStandort) {
        this.addObserver(_observer);
        this.iDepictable = iDepictable;
        this._observer = _observer;
        this._allStandorte = allStandorte;
        this._selectedStandort = selectedStandort;
        initUI(false);
    }

    private void initUI(Boolean isNewObject) {
        this.setLayout(new BorderLayout(0,0));
        this.setPreferredSize(new Dimension(500,700));
        this.setBackground(Color.WHITE);

        Border borderTop = BorderFactory.createMatteBorder(1,0,0,0, CSHelp.navBar);
        ImageIcon imageIconFahrzeug = CSHelp.imageList.get("button_FahrzeugSpeichern.png");

        topPanel = new JPanel(new BorderLayout(0,0));
        topPanel.setBackground(Color.white);
        topLabelDescription = new JLabel("ID: ");
        topLabelDescription.setBorder(new EmptyBorder(10,20,0,5));
        topLabelDescription.setFont(CSHelp.lato.deriveFont(12f));
        topLabelValue = new JLabel();
        topLabelValue.setBorder(new EmptyBorder(12,0,0,20));
        topLabelValue.setFont(CSHelp.lato.deriveFont(9f));
        topPanel.add(topLabelDescription, BorderLayout.WEST);
        topPanel.add(topLabelValue, BorderLayout.CENTER);


        bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(Color.white);

        bottomPanel.setBorder(borderTop);

        save_fahrzeug = new JButton(imageIconFahrzeug);
        save_fahrzeug.setBorder(new EmptyBorder(0,0,0,0));
        save_fahrzeug.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] fahrzeugData = getValues();
                if(validateInput()) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd. MMMM YYYY");
                    String message;
                    if (isNewObject) {
                        message = String.format("<html><body> Wollen Sie folgendes Fahrzeug speichern? <br><ul><li>%s, %s</li><li>%s</li><li>%s Türen, %s Sitze</li><li>%s l Kofferraum</li><li>%s kg</li><li>%s</li><li>Führerscheinklasse: %s</li><li>Nummernschild: %s</li><li>%s</li><li>TÜV gültig bis: %s</li><li>Standort: %s</li></ul></body></html>", fahrzeugData[2], fahrzeugData[1], fahrzeugData[3], fahrzeugData[4], fahrzeugData[5], fahrzeugData[6], fahrzeugData[7], fahrzeugData[8], fahrzeugData[9], fahrzeugData[10], fahrzeugData[12], formatter.format(LocalDate.parse(fahrzeugData[11])), _selectedStandort);
                    } else {
                        message = String.format("<html><body> Wollen Sie folgende Anpassungen speichern? <br><ul><li>%s, %s</li><li>%s</li><li>%s Türen, %s Sitze</li><li>%s l Kofferraum</li><li>%s kg</li><li>%s</li><li>Führerscheinklasse: %s</li><li>Nummernschild: %s</li><li>%s</li><li>TÜV gültig bis: %s</li><li>Standort: %s</li></ul></body></html>", fahrzeugData[2], fahrzeugData[1], fahrzeugData[3], fahrzeugData[4], fahrzeugData[5], fahrzeugData[6], fahrzeugData[7], fahrzeugData[8], fahrzeugData[9], fahrzeugData[10], fahrzeugData[12], formatter.format(LocalDate.parse(fahrzeugData[11])), _selectedStandort);
                    }
                    int a = JOptionPane.showConfirmDialog(GUIFahrzeugAnlegen.this, message, "Bestätigung", JOptionPane.YES_NO_OPTION, 1, CSHelp.imageList.get("icon_fahrzeug.png"));
                    if (a == 0) {
                        fireGUIEvent(new GUIEvent(this, Commands.ADD_FAHRZEUG, fahrzeugData));
                    }
                }
            }
        });
        bottomPanel.add(save_fahrzeug);

        leftPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        centerPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        rightPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        leftPanel.setBackground(Color.WHITE);
        centerPanel.setBackground(Color.WHITE);
        rightPanel.setBackground(Color.WHITE);
        leftPanel.setPreferredSize(new Dimension(250, 400));
        centerPanel.setPreferredSize(new Dimension(250, 400));
        rightPanel.setPreferredSize(new Dimension(250, 400));
        leftPanel.setBorder(new EmptyBorder(10,10,10,10));
        centerPanel.setBorder(new EmptyBorder(10,10,10,10));
        rightPanel.setBorder(new EmptyBorder(10,10,10,10));


        _inputFieldMap = new LinkedHashMap<>();
        _inputFieldMap.put("Bezeichnung", new CustomTextField("Bezeichnung", "Fahrzeugbezeichnung"));
        _inputFieldMap.put("Marke", new CustomTextField("Marke", "bsp.: Audi, Mercedes, ..."));
        _inputFieldMap.put("Motorisierung", new CustomComboBox("Motorisierung", "Diesel, Benzin, Elektro ..", Motorisierung.getArray(), _observer));
        _inputFieldMap.put("Türen", new CustomTextField("Türen", "Anzahl der Türen"));
        _inputFieldMap.put("Sitze", new CustomTextField("Sitze", "Anzahl der Sitze"));
        _inputFieldMap.put("Kofferraumvolumen", new CustomTextField("Kofferraumvolumen (l)", "Volumen in Liter"));
        _inputFieldMap.put("Gewicht", new CustomTextField("Gewicht (kg)", "Gewicht in kg"));
        _inputFieldMap.put("Fahrzeugkategorie", new CustomComboBox("Fahrzeugkategorie", "bsp.: Mittelklasse, ...", Fahrzeugkategorie.getArray(), _observer));
        _inputFieldMap.put("Führerscheinklasse", new CustomTextField("Führerscheinklasse", "bsp.: A, B, ..."));
        _inputFieldMap.put("Nummernschild",  new CustomTextField("Nummernschild", "bsp.: DÜW LL 140"));
        _inputFieldMap.put("Bilder", new CustomListField("Bilder", this._observer, this.iDepictable ));
        _inputFieldMap.put("Dokumente", new CustomListField("Dokumente", this._observer, this.iDepictable ));
        _inputFieldMap.put("Standort", new CustomListField("Standort", this._observer, this._allStandorte));
        _inputFieldMap.put("TüvBis", new CustomDatePicker("Tüv bis", "Tüv bis", this._observer));
        _inputFieldMap.put("Farbe", new CustomTextField("Farbe", "Farbe des Fahrzeuges"));


        leftPanel.add(_inputFieldMap.get("Bezeichnung"));
        leftPanel.add(_inputFieldMap.get("Marke"));
        leftPanel.add(_inputFieldMap.get("Motorisierung"));
        leftPanel.add(_inputFieldMap.get("Türen"));
        leftPanel.add(_inputFieldMap.get("Standort"));

        centerPanel.add(_inputFieldMap.get("Sitze"));
        centerPanel.add(_inputFieldMap.get("Kofferraumvolumen"));
        centerPanel.add(_inputFieldMap.get("Gewicht"));
        centerPanel.add(_inputFieldMap.get("Fahrzeugkategorie"));
        centerPanel.add(_inputFieldMap.get("Führerscheinklasse"));
        centerPanel.add(_inputFieldMap.get("Nummernschild"));

        rightPanel.add(_inputFieldMap.get("TüvBis"));
        rightPanel.add(_inputFieldMap.get("Farbe"));
        rightPanel.add(_inputFieldMap.get("Bilder"));
        rightPanel.add(_inputFieldMap.get("Dokumente"));

        this.add(topPanel, BorderLayout.NORTH);
        this.add(centerPanel, BorderLayout.CENTER);
        this.add(leftPanel, BorderLayout.WEST);
        this.add(rightPanel, BorderLayout.EAST);
        this.add(bottomPanel, BorderLayout.SOUTH);


        if (isNewObject) {
            createFahrzeug();
        } else {
            editFahrzeug();
        }
    }

    private void createFahrzeug() {
        _randID = UUID.randomUUID().toString();
        topLabelValue.setText(_randID);
    }

    private void editFahrzeug() {
        Attribute[] attributes = iDepictable.getAttributeArray();
        //;/ok.png;A3 Sportback 30 TFSI;Audi;V8;5;5;300;1,469;Mittelklasse;B;DÜW L 140;2022-07-30;blau;2022-07-26T11:39:48.590687;
        _randID = attributes[0].getValue().toString();
        topLabelValue.setText(_randID);
        _inputFieldMap.get("Bezeichnung").setValue(attributes[1].getValue().toString());
        _inputFieldMap.get("Marke").setValue(attributes[2].getValue().toString());
        int motorisierungIndex = Motorisierung.fromString(attributes[3].getValue().toString()).ordinal();
        _inputFieldMap.get("Motorisierung").setValue(String.valueOf(motorisierungIndex));
        _inputFieldMap.get("Türen").setValue(attributes[4].getValue().toString());
        _inputFieldMap.get("Sitze").setValue(attributes[5].getValue().toString());
        _inputFieldMap.get("Kofferraumvolumen").setValue(attributes[6].getValue().toString());
        _inputFieldMap.get("Gewicht").setValue(attributes[7].getValue().toString());
        int fahrzeugkategorieIndex = Fahrzeugkategorie.fromString(attributes[8].getValue().toString()).ordinal();
        _inputFieldMap.get("Fahrzeugkategorie").setValue(String.valueOf(fahrzeugkategorieIndex));
        _inputFieldMap.get("Führerscheinklasse").setValue(attributes[9].getValue().toString());
        _inputFieldMap.get("Nummernschild").setValue(attributes[10].getValue().toString());
        _inputFieldMap.get("TüvBis").setValue(attributes[11].getValue().toString());
        _inputFieldMap.get("Farbe").setValue(attributes[12].getValue().toString());
        ((CustomListField)_inputFieldMap.get("Standort")).get_slc().selectElement((Standort) attributes[13].getValue());

        save_fahrzeug.requestFocus();
    }

    public void updateBildList(List<IDepictable> bilder) {
        CustomListField customListField = (CustomListField) _inputFieldMap.get("Bilder");
        customListField.setListElements(bilder);
    }

    //Load list of documents assigned to the booking
    public void updateDokumentList(List<IDepictable> dokumente) {
        CustomListField customListField = (CustomListField) _inputFieldMap.get("Dokumente");
        customListField.setListElements(dokumente);
    }

    private String[] getValues() {
        _currentValues = new ArrayList<String>();
        _currentValues.add(_randID);
        _currentValues.add(_inputFieldMap.get("Bezeichnung").getValue());
        _currentValues.add(_inputFieldMap.get("Marke").getValue());
        _currentValues.add(_inputFieldMap.get("Motorisierung").getValue());
        _currentValues.add(_inputFieldMap.get("Türen").getValue());
        _currentValues.add(_inputFieldMap.get("Sitze").getValue());
        _currentValues.add(_inputFieldMap.get("Kofferraumvolumen").getValue());
        _currentValues.add(_inputFieldMap.get("Gewicht").getValue());
        _currentValues.add(_inputFieldMap.get("Fahrzeugkategorie").getValue());
        _currentValues.add(_inputFieldMap.get("Führerscheinklasse").getValue());
        _currentValues.add(_inputFieldMap.get("Nummernschild").getValue());
        _currentValues.add(_inputFieldMap.get("TüvBis").getValue());
        _currentValues.add(_inputFieldMap.get("Farbe").getValue());
        Standort standort = (Standort) ((CustomListField) _inputFieldMap.get("Standort")).get_slc().getSelectedElement();
        if (standort == null) {
            _currentValues.add(null);
        } else {
            _currentValues.add(standort.getElementID());
            _selectedStandort = standort;
        }
        _currentValues.add(LocalDateTime.now().toString());
        return (String[]) _currentValues.toArray(new String[_currentValues.size()]);
    }

    @Override
    public boolean validateInput() {
        System.out.println(_currentValues.get(3));
        if (_currentValues.get(1) == null) {
            //JOptionPane.showMessageDialog(null, "Bitte wählen Sie einen Kunden aus!", "Kunde fehlerhaft", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (_currentValues.get(2) == null) {
            //JOptionPane.showMessageDialog(null, "Bitte wählen Sie ein Fahrzeug aus! \n(Startdatum/Enddatum auswählen-->Fahrzeuge laden)", "Fahrzeug fehlerhaft", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (_currentValues.get(3).toString().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Bitte wählen Sie eine Antriebsart (Motorisierung) aus!", "Motorisierung fehlerhaft", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!CSHelp.isNumber(_currentValues.get(4).toString())) {
            JOptionPane.showMessageDialog(null, "Bitte geben Sie einen ganzzahlig, numerischen Wert für das Feld Türen ein!", "Türen fehlerhaft", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!CSHelp.isNumber(_currentValues.get(5).toString())) {
            JOptionPane.showMessageDialog(null, "Bitte geben Sie einen ganzzahlig, numerischen Wert für das Feld Sitze ein!", "Sitze fehlerhaft", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!CSHelp.isNumber(_currentValues.get(6).toString())) {
            JOptionPane.showMessageDialog(null, "Bitte geben Sie einen ganzzahlig, numerischen Wert für das Feld Kofferraumvolumen ein!", "Kofferraumvolumen fehlerhaft", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!CSHelp.isNumber(_currentValues.get(7).toString())) {
            JOptionPane.showMessageDialog(null, "Bitte geben Sie einen ganzzahlig, numerischen Wert für das Feld Gewicht ein!", "Gewicht fehlerhaft", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (_currentValues.get(8).toString().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Bitte wählen Sie eine Fahrzeugkategorie aus!", "Fahrzeugkategorie fehlerhaft", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (0 == 1) {
            //JOptionPane.showMessageDialog(null, "Bitte geben Sie einen ganzzahlig, numerischen Wert für das Feld Gewicht ein!", "Gewicht fehlerhaft", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (0 == 1) {
            //JOptionPane.showMessageDialog(null, "Bitte geben Sie einen ganzzahlig, numerischen Wert für das Feld Gewicht ein!", "Gewicht fehlerhaft", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (_currentValues.get(11) == null) {
            JOptionPane.showMessageDialog(null, "Bitte wählen Sie das Ablaufdatum des Tüv aus!", "TüvBis fehlerhaft", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (0 == 1) {
            //JOptionPane.showMessageDialog(null, "Bitte wählen Sie das Ablaufdatum des Tüv aus!", "TüvBis fehlerhaft", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (_currentValues.get(13) == null) {
            JOptionPane.showMessageDialog(null, "Bitte ordnen Sie dem Fahrzeug einen Standort zu!\nJedes Fahrzeug benötigt einen freien Stellplatz.", "Standort fehlerhaft", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Semicolon Check
        // !!!!!!!!! DO NOT USE INTELLIJ OPTIMIZER AND TRY TO 'SIMPLIFY' THIS METHOD !!!!!!!!!!!
        // IT WILL BREAK IN CASE THERE IS MORE CODE AFTER IT
        if (!CSHelp.areFormFieldValuesCsvCompliant(_currentValues, Fahrzeug.getAllAttributeNames())) return false;

        return true;
    }
    
    //Get the TüvBis date component
    public CustomDatePicker getDateComponent() {
        return ((CustomDatePicker) _inputFieldMap.get("TüvBis"));
    }

    public CustomListField getBildList() {
        return (CustomListField) _inputFieldMap.get("Bilder");
    }
    public CustomListField getStandortList() {
        return (CustomListField) _inputFieldMap.get("Standort");
    }


}
