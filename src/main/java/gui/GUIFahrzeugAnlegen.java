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
import model.Bild;
import model.Buchung;
import model.Fahrzeug;
import model.Fahrzeugkategorie;
import model.Motorisierung;
import util.CSHelp;
import util.IValidate;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
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
    private Map<String, CustomInputField> inputFieldMap;

    private IGUIEventListener observer;
    private String randID;

    private JPanel topPanel, bottomPanel, leftPanel, rightPanel;
    private JLabel topLabelValue, topLabelDescription;
    private JButton save_fahrzeug;

    private IDepictable iDepictable;

    //constructor for creating new Object
    public GUIFahrzeugAnlegen(IGUIEventListener observer) {
        this.addObserver(observer);
        this.observer = observer;
        initUI(true);
    }

    //constructor for editing an existing Object
    public GUIFahrzeugAnlegen(IGUIEventListener observer, IDepictable iDepictable) {
        this.addObserver(observer);
        this.iDepictable = iDepictable;
        this.observer = observer;
        initUI(false);
    }

    private void initUI(Boolean isNewObject) {
        this.setLayout(new BorderLayout(0,0));
        this.setPreferredSize(new Dimension(500,700));
        this.setBackground(Color.WHITE);

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
        Border borderTop = BorderFactory.createMatteBorder(1,0,0,0, CSHelp.navBar);
        bottomPanel.setBorder(borderTop);
        ImageIcon imageIconFahrzeug = CSHelp.imageList.get("button_FahrzeugSpeichern.png");
        save_fahrzeug = new JButton(imageIconFahrzeug);
        save_fahrzeug.setBorder(new EmptyBorder(0,0,0,0));
        save_fahrzeug.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] test = getValues();
                if(validateInput()) {
                    int a = JOptionPane.showConfirmDialog(GUIFahrzeugAnlegen.this, "Wollen sie das Fahrzeug speichern?", "Bestätigung", JOptionPane.YES_NO_OPTION, 1, CSHelp.imageList.get("icon_person.png"));
                    if (a == 0) {
                        fireGUIEvent( new GUIEvent(this, Commands.ADD_FAHRZEUG, test ));
//                        Component component = (Component) e.getSource();
//                        JDialog dialog = (JDialog) SwingUtilities.getRoot(component);
//                        dialog.dispose();
                    }
                }
            }
        });
        bottomPanel.add(save_fahrzeug);

        leftPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        rightPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        leftPanel.setBackground(Color.WHITE);
        rightPanel.setBackground(Color.WHITE);
        leftPanel.setPreferredSize(new Dimension(250, 600));
        rightPanel.setPreferredSize(new Dimension(250, 600));
        leftPanel.setBorder(new EmptyBorder(10,10,10,10));
        rightPanel.setBorder(new EmptyBorder(10,10,10,10));


        inputFieldMap = new LinkedHashMap<>();
        inputFieldMap.put("Bezeichnung", new CustomTextField("Bezeichnung", "Fahrzeugbezeichnung"));
        inputFieldMap.put("Marke", new CustomTextField("Marke", "bsp.: Audi, Mercedes, ..."));
        inputFieldMap.put("Motorisierung", new CustomComboBox("Motorisierung", "Diesel, Benzin, Elektro ..", Motorisierung.getArray(), observer));
        inputFieldMap.put("Türen", new CustomTextField("Türen", "Anzahl der Türen"));
        inputFieldMap.put("Sitze", new CustomTextField("Sitze", "Anzahl der Sitze"));
        inputFieldMap.put("Kofferraumvolumen", new CustomTextField("Kofferraumvolumen (l)", "Volumen in Liter"));
        inputFieldMap.put("Gewicht", new CustomTextField("Gewicht (kg)", "Gewicht in kg"));
        inputFieldMap.put("Fahrzeugkategorie", new CustomComboBox("Fahrzeugkategorie", "bsp.: Mittelklasse, ...", Fahrzeugkategorie.getArray(), observer));
        inputFieldMap.put("Führerscheinklasse", new CustomTextField("Führerscheinklasse", "bsp.: A, B, ..."));
        inputFieldMap.put("Nummernschild",  new CustomTextField("Nummernschild", "bsp.: DÜW LL 140"));
        //TODO: Fahrzeugbilder und Dokumente
        inputFieldMap.put("TüvBis", new CustomTextField("TüvBis", "Format: TT.MM.YYYY"));
        inputFieldMap.put("Farbe", new CustomTextField("Farbe", "Farbe des Fahrzeuges"));
        inputFieldMap.put("Bilder", new CustomListField("Bilder", this.observer, this.iDepictable ));
        inputFieldMap.put("Dokumente", new CustomListField("Dokumente", this.observer, this.iDepictable ));


        int leftPanelCount = 0;
        for (CustomInputField customInputField : inputFieldMap.values()) {
            if (leftPanelCount < 8) {
                leftPanel.add(customInputField);
            } else {
                rightPanel.add(customInputField);
            }
            leftPanelCount++;
        }

        this.add(topPanel, BorderLayout.NORTH);
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
        randID = UUID.randomUUID().toString();
        topLabelValue.setText(randID);
    }

    private void editFahrzeug() {
        Attribute[] attributes = iDepictable.getAttributeArray();
        //;/ok.png;A3 Sportback 30 TFSI;Audi;V8;5;5;300;1,469;Mittelklasse;B;DÜW L 140;2022-07-30;blau;2022-07-26T11:39:48.590687;
        randID = attributes[0].getValue().toString();
        topLabelValue.setText(randID);
        inputFieldMap.get("Bezeichnung").setValue(attributes[1].getValue().toString());
        inputFieldMap.get("Marke").setValue(attributes[2].getValue().toString());
        int motorisierungIndex = Motorisierung.fromString(attributes[3].getValue().toString()).ordinal();
        inputFieldMap.get("Motorisierung").setValue(String.valueOf(motorisierungIndex));
        inputFieldMap.get("Türen").setValue(attributes[4].getValue().toString());
        inputFieldMap.get("Sitze").setValue(attributes[5].getValue().toString());
        inputFieldMap.get("Kofferraumvolumen").setValue(attributes[6].getValue().toString());
        inputFieldMap.get("Gewicht").setValue(attributes[7].getValue().toString());
        int fahrzeugkategorieIndex = Fahrzeugkategorie.fromString(attributes[8].getValue().toString()).ordinal();
        inputFieldMap.get("Fahrzeugkategorie").setValue(String.valueOf(fahrzeugkategorieIndex));
        inputFieldMap.get("Führerscheinklasse").setValue(attributes[9].getValue().toString());
        inputFieldMap.get("Nummernschild").setValue(attributes[10].getValue().toString());
        inputFieldMap.get("TüvBis").setValue(attributes[11].getValue().toString());
        inputFieldMap.get("Farbe").setValue(attributes[12].getValue().toString());

        save_fahrzeug.requestFocus();
    }

    public void updateBildList(List<IDepictable> bilder) {
        CustomListField customListField = (CustomListField) inputFieldMap.get("Bilder");
        customListField.setListElements(bilder);
    }

    private String[] getValues() {
        List<String> allValues = new ArrayList<>();
        allValues.add(randID);
        for(CustomInputField customInputField : inputFieldMap.values()) {
            if(customInputField.getClass() != CustomListField.class) {
                allValues.add(customInputField.getValue());
            }
        }
        allValues.add(LocalDateTime.now().toString());
        return allValues.toArray(new String[allValues.size()]);
    }

    @Override
    public boolean validateInput() {
        boolean state = true;
        if (!(CSHelp.isNumber(inputFieldMap.get("Sitze").getValue()))) {
            state = false;
        }
        if (!(CSHelp.isNumber(inputFieldMap.get("Türen").getValue()))) {
            state = false;
        }

        for (int i = 0; i < _currentValues.size(); i++) {
            if (!this.checkIfSemicolonForCurrentvalue(_currentValues, i)) return false;
        }

        return state;
    }

    public boolean checkIfSemicolonForCurrentvalue(List values, int valueIndex) {
        if (values.get(valueIndex).toString().contains(";")) {
            String AttributeName = Arrays.stream(Fahrzeug.getAllAttributeNames()).toList().get(valueIndex).toString();
            JOptionPane.showMessageDialog(null, "Feld "+ AttributeName + " enthält Semicolon!", AttributeName+" fehlerhaft", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public CustomListField getBildList() {
        return (CustomListField) inputFieldMap.get("Bilder");
    }


}
