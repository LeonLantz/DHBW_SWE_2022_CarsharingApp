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
import java.util.*;
import java.util.List;

public class GUIKundeAnlegen extends ObservableComponent implements IValidate {

    public enum Commands implements EventCommand {

        ADD_KUNDE( "GUIKundeAnlegen.addKunde", String[].class );

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

    //Data
    private IGUIEventListener _observer;
    private Map<String, CustomInputField> _inputFieldMap;
    private String _randID;
    private IDepictable _currentObject;
    private List _currentValues;

    //User Interface
    private JPanel _topPanel, _bottomPanel, _leftPanel, _rightPanel;
    private JLabel _topLabelValue, _topLabelDescription;
    private JButton _save_kunde;


    //constructor for creating new <Kunde>
    public GUIKundeAnlegen(IGUIEventListener _observer) {
        this.addObserver(_observer);
        this._observer = _observer;
        initUI(true);
    }

    //constructor for editing an existing <Kunde>
    public GUIKundeAnlegen(IGUIEventListener _observer, IDepictable _currentObject) {
        this.addObserver(_observer);
        this._currentObject = _currentObject;
        this._observer = _observer;
        initUI(false);
    }

    private void initUI(Boolean isNewObject) {
        this.setLayout(new BorderLayout(0,0));
        this.setPreferredSize(new Dimension(500,700));
        this.setBackground(CSHelp.main);

        Border borderTop = BorderFactory.createMatteBorder(1,0,0,0, CSHelp.navBar);
        ImageIcon imageIconKunde = CSHelp.imageList.get("button_KundeSpeichern.png");

        _topPanel = new JPanel(new BorderLayout(0,0));
        _topPanel.setBackground(Color.WHITE);
        _topPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, CSHelp.navBar));
        _topLabelDescription = new JLabel("ID: ");
        _topLabelDescription.setBorder(new EmptyBorder(10,20,10,5));
        _topLabelDescription.setFont(CSHelp.lato.deriveFont(12f));
        _topLabelValue = new JLabel();
        _topLabelValue.setBorder(new EmptyBorder(12,0,10,20));
        _topLabelValue.setFont(CSHelp.lato.deriveFont(9f));
        _topPanel.add(_topLabelDescription, BorderLayout.WEST);
        _topPanel.add(_topLabelValue, BorderLayout.CENTER);

        _bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        _bottomPanel.setBackground(Color.white);
        _bottomPanel.setBorder(borderTop);

        _save_kunde = new JButton(imageIconKunde);
        _save_kunde.setBorder(new EmptyBorder(0,0,0,0));
        _save_kunde.setContentAreaFilled(false);
        _save_kunde.setBorderPainted(false);
        _save_kunde.setFocusPainted(false);
        _save_kunde.setOpaque(false);
        _save_kunde.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] kundenData = getValues();
                if(validateInput()) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd. MMMM YYYY");
                    String message = String.format("<html><body> Wollen Sie folgenden Kunden speichern? <br><ul><li>%s, %s</li><li>%s</li><li>%s</li><li>%s</li><li>Geburtsdatum: %s</li></ul></body></html>", kundenData[2], kundenData[1], kundenData[3], kundenData[4], kundenData[5], formatter.format(LocalDate.parse(kundenData[6])));
                    int a = JOptionPane.showConfirmDialog(GUIKundeAnlegen.this, message, "Bestätigung", JOptionPane.YES_NO_OPTION, 1, CSHelp.imageList.get("icon_person.png"));
                    if (a == 0) {
                        fireGUIEvent( new GUIEvent(this, Commands.ADD_KUNDE, kundenData));
                    }
                }
            }
        });
        _bottomPanel.add(_save_kunde);

        _leftPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        _rightPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        _leftPanel.setPreferredSize(new Dimension(250, 600));
        _rightPanel.setPreferredSize(new Dimension(250, 600));
        _leftPanel.setBorder(new EmptyBorder(10,10,10,10));
        _rightPanel.setBorder(new EmptyBorder(10,10,10,10));

        _inputFieldMap = new LinkedHashMap<>();
        _inputFieldMap.put("Vorname", new CustomTextField("Vorname", "Vorname des Kunden"));
        _inputFieldMap.put("Nachname", new CustomTextField("Nachname", "Nachname des Kunden"));
        _inputFieldMap.put("Email", new CustomTextField("Email", "z.B. max@mustermann.de"));
        _inputFieldMap.put("Phone", new CustomTextField("Telefon", "z.B. 017495622312"));
        _inputFieldMap.put("IBAN", new CustomTextField("IBAN", "z.B. DE0212030 [...]"));
        _inputFieldMap.put("DOB", new CustomDatePicker("Geburtsdatum", "Geburtsdatum des Kunden", _observer));
        _inputFieldMap.put("Bilder", new CustomListField("Bilder", this._observer, this._currentObject));
        _inputFieldMap.put("Dokumente", new CustomListField("Dokumente", this._observer, this._currentObject));

        _leftPanel.add(_inputFieldMap.get("Vorname"));
        _leftPanel.add(_inputFieldMap.get("Nachname"));
        _leftPanel.add(_inputFieldMap.get("Email"));
        _leftPanel.add(_inputFieldMap.get("Phone"));
        _leftPanel.add(_inputFieldMap.get("IBAN"));
        _rightPanel.add(_inputFieldMap.get("DOB"));
        _rightPanel.add(_inputFieldMap.get("Bilder"));
        _rightPanel.add(_inputFieldMap.get("Dokumente"));
        _leftPanel.setBackground(Color.WHITE);
        _rightPanel.setBackground(Color.WHITE);

        this.add(_topPanel, BorderLayout.NORTH);
        this.add(_leftPanel, BorderLayout.WEST);
        this.add(_rightPanel, BorderLayout.EAST);
        this.add(_bottomPanel, BorderLayout.SOUTH);

        if (isNewObject) createKunde();
        else editKunde();
    }

    private void createKunde() {
        _randID = UUID.randomUUID().toString();
        _topLabelValue.setText(_randID);
    }

    private void editKunde() {
        Attribute[] attributes = _currentObject.getAttributeArray();
        _randID = attributes[0].getValue().toString();
        _topLabelValue.setText(_randID);
        _inputFieldMap.get("Vorname").setValue(attributes[1].getValue().toString());
        _inputFieldMap.get("Nachname").setValue(attributes[2].getValue().toString());
        _inputFieldMap.get("Email").setValue(attributes[3].getValue().toString());
        _inputFieldMap.get("Phone").setValue(attributes[4].getValue().toString());
        _inputFieldMap.get("IBAN").setValue(attributes[5].getValue().toString());
        _inputFieldMap.get("DOB").setValue(attributes[6].getValue().toString());
//        _inputFieldMap.get("Sitze").setValue(attributes[5].getValue().toString());
//        _inputFieldMap.get("Kofferraumvolumen").setValue(attributes[6].getValue().toString());
//        _inputFieldMap.get("Gewicht").setValue(attributes[7].getValue().toString());
//        int fahrzeugkategorieIndex = Fahrzeugkategorie.fromString(attributes[8].getValue().toString()).ordinal();
//        _inputFieldMap.get("Fahrzeugkategorie").setValue(String.valueOf(fahrzeugkategorieIndex));
//        _inputFieldMap.get("Führerscheinklasse").setValue(attributes[9].getValue().toString());
//        _inputFieldMap.get("Nummernschild").setValue(attributes[10].getValue().toString());
//        _inputFieldMap.get("TüvBis").setValue(attributes[11].getValue().toString());
//        _inputFieldMap.get("Farbe").setValue(attributes[12].getValue().toString());
//        ((CustomListField)_inputFieldMap.get("Standort")).get_slc().selectElement((Standort) attributes[13].getValue());

        _save_kunde.requestFocus();
    }

    private String[] getValues() {
        _currentValues = new ArrayList<String>();
        _currentValues.add(_randID);
        _currentValues.add(_inputFieldMap.get("Vorname").getValue());
        _currentValues.add(_inputFieldMap.get("Nachname").getValue());
        _currentValues.add(_inputFieldMap.get("Email").getValue());
        _currentValues.add(_inputFieldMap.get("Phone").getValue());
        _currentValues.add(_inputFieldMap.get("IBAN").getValue());
        _currentValues.add(_inputFieldMap.get("DOB").getValue());
        _currentValues.add(LocalDateTime.now().toString());
        return (String[]) _currentValues.toArray(new String[_currentValues.size()]);
    }

    public void updateBildList(List<IDepictable> bilder) {
        CustomListField customListField = (CustomListField) _inputFieldMap.get("Bilder");
        customListField.setListElements(bilder);
    }

    public void updateDokumentList(List<IDepictable> dokumente) {
        CustomListField customListField = (CustomListField) _inputFieldMap.get("Dokumente");
        customListField.setListElements(dokumente);
    }

    @Override
    public boolean validateInput() {
        if (!CSHelp.isEmail((String) _currentValues.get(3))) {
            JOptionPane.showMessageDialog(null, "Bitte geben Sie eine valide Email ein!", "Email fehlerhaft", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!CSHelp.isIBAN((String) _currentValues.get(5))) {
            JOptionPane.showMessageDialog(null, "Bitte geben Sie eine valide, deutsche IBAN an! \nFormat: DE + 20 Ziffern", "IBAN fehlerhaft", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (_currentValues.get(6) == null) {
            JOptionPane.showMessageDialog(null, "Bitte wählen Sie das Geburtsdatum des Kunden aus!", "Geburtsdatum fehlerhaft", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        // Semicolon Check
        // !!!!!!!!! DO NOT USE INTELLIJ OPTIMIZER AND TRY TO 'SIMPLIFY' THIS METHOD !!!!!!!!!!!
        // IT WILL BREAK IN CASE THERE IS MORE CODE AFTER IT
        if (!CSHelp.areFormFieldValuesCsvCompliant(_currentValues, Kunde.getAllAttributeNames())) return false;

        return true;
    }

    public CustomListField getBildList() {
        return (CustomListField) _inputFieldMap.get("Bilder");
    }

    public CustomDatePicker getDateComponent() {
        return ((CustomDatePicker) _inputFieldMap.get("DOB"));
    }
}
