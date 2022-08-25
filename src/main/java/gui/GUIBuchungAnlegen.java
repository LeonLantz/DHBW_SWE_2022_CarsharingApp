package gui;

import de.dhbwka.swe.utils.event.EventCommand;
import de.dhbwka.swe.utils.event.GUIEvent;
import de.dhbwka.swe.utils.event.IGUIEventListener;
import de.dhbwka.swe.utils.gui.ObservableComponent;
import de.dhbwka.swe.utils.gui.SimpleListComponent;
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
import java.awt.event.WindowEvent;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;

public class GUIBuchungAnlegen extends ObservableComponent implements IValidate {

    public enum Commands implements EventCommand {

        ADD_BUCHUNG( "GUIBuchungAnlegen.addBuchung", String[].class ),
        UPDATE_FAHRZEUGE( "GUIBuchungAnlegen.updateFahrzeuge", LocalDate[].class );

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

    private JPanel topPanel, fahrzeugePanel, bottomPanel, leftPanel, rightPanel;
    private JLabel topLabelValue, topLabelDescription;
    private JButton save_buchung;

    private String randID;

    private IDepictable iDepictable;

    private List<IDepictable> alleKunden, alleFahrzeuge;

    private Kunde selectedKunde;
    private Fahrzeug selectedFahrzeug;
    private JButton buttonLoadFahrzeuge;

    private List currentValues;

    //constructor for creating new Object
    public GUIBuchungAnlegen(IGUIEventListener observer, List alleKunden) {
        this.addObserver(observer);
        this.observer = observer;
        this.alleKunden = alleKunden;
        this.alleFahrzeuge = new ArrayList<>();
        initUI(true);
    }

    //constructor for editing an existing Object
    public GUIBuchungAnlegen(IGUIEventListener observer, IDepictable iDepictable, Kunde selectedKunde, Fahrzeug selectedFahrzeug) {
        this.addObserver(observer);
        this.iDepictable = iDepictable;
        this.observer = observer;
        this.selectedKunde = selectedKunde;
        this.selectedFahrzeug = selectedFahrzeug;
        initUI(false);
    }

    private void initUI(Boolean isNewObject) {
        this.setLayout(new BorderLayout(0,0));
        this.setPreferredSize(new Dimension(500,700));
        this.setBackground(CSHelp.main);

        topPanel = new JPanel(new BorderLayout(0,0));
        topPanel.setBackground(Color.white);
        topLabelDescription = new JLabel("ID: ");
        topLabelDescription.setBorder(new EmptyBorder(20,20,0,5));
        topLabelDescription.setFont(CSHelp.lato.deriveFont(11f));
        topLabelValue = new JLabel();
        topLabelValue.setBorder(new EmptyBorder(20,0,0,20));
        topLabelValue.setFont(CSHelp.lato.deriveFont(9f));
        topPanel.add(topLabelDescription, BorderLayout.WEST);
        topPanel.add(topLabelValue, BorderLayout.CENTER);


        bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(Color.white);
        Border borderTop = BorderFactory.createMatteBorder(1,0,0,0, CSHelp.navBar);
        bottomPanel.setBorder(borderTop);
        ImageIcon imageIconBuchung = CSHelp.imageList.get("button_BuchungSpeichern.png");
        save_buchung = new JButton(imageIconBuchung);
        save_buchung.setBorder(new EmptyBorder(0,0,0,0));
        save_buchung.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] fahrzeugData = getValues();
                if(validateInput()) {
                    int a = JOptionPane.showConfirmDialog(GUIBuchungAnlegen.this, "Wollen Sie die Buchung speichern?", "Bestätigung", JOptionPane.YES_NO_OPTION, 1, CSHelp.imageList.get("icon_person.png"));
                    if (a == 0) {
                        fireGUIEvent( new GUIEvent(this, Commands.ADD_BUCHUNG, fahrzeugData ));
                    }
                }
            }
        });
        bottomPanel.add(save_buchung, BorderLayout.CENTER);

        leftPanel = new JPanel();
        rightPanel = new JPanel();
        leftPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        rightPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        leftPanel.setPreferredSize(new Dimension(250, 600));
        rightPanel.setPreferredSize(new Dimension(250, 600));
        leftPanel.setBorder(new EmptyBorder(10,10,10,10));
        rightPanel.setBorder(new EmptyBorder(10,10,10,10));

        inputFieldMap = new LinkedHashMap<>();

        //TODO: Buchungsnummer-Feld soll erst beim Speichern generiert werden
        inputFieldMap.put("Buchungsnummer", new CustomTextField("Buchungsnummer", "wird automatisch generiert"));
        //((CustomTextField)inputFieldMap.get("Buchungsnummer")).getTextfield().setEnabled(false);
        inputFieldMap.put("Kunde", new CustomListField("Kunde", this.observer, this.alleKunden));
        inputFieldMap.put("Status", new CustomComboBox("Status", "Buchungsstatus", Buchungsstatus.getArray(), observer));
        ((CustomComboBox)inputFieldMap.get("Status")).getComboBox().setSelectedIndex(0);
        ((CustomComboBox)inputFieldMap.get("Status")).getComboBox().setEnabled(false);
        inputFieldMap.put("Start", new CustomDatePicker("Start", "Start der Buchung", this.observer));
        inputFieldMap.put("Ende", new CustomDatePicker("Ende", "Ende der Buchung", this.observer));
        inputFieldMap.put("Fahrzeug", new CustomListField("Fahrzeug", this.observer, this.alleFahrzeuge));
        inputFieldMap.put("Dokumente", new CustomListField("Dokumente", this.observer, this.iDepictable ));
        //inputFieldMap.put("Dokumente", new CustomListField("Dokumente", "placeholder", this.observer, this.iDepictable ));


        leftPanel.add(inputFieldMap.get("Buchungsnummer"));
        leftPanel.add(inputFieldMap.get("Kunde"));
        leftPanel.add(inputFieldMap.get("Start"));
        leftPanel.add(inputFieldMap.get("Ende"));
        rightPanel.add(inputFieldMap.get("Status"));
        rightPanel.add(inputFieldMap.get("Dokumente"));
        rightPanel.add(inputFieldMap.get("Fahrzeug"));

        buttonLoadFahrzeuge = new JButton("Verfügbare Fahrzeuge laden");
        buttonLoadFahrzeuge.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String startDate = getDateComponent("Start").getValue();
                String endDate = getDateComponent("Ende").getValue();
                if (CSHelp.isDate(startDate) && CSHelp.isDate(endDate)) {
                    LocalDate start = LocalDate.parse(startDate);
                    LocalDate end = LocalDate.parse(endDate);
                    if (!start.isAfter(end)) {
                        long days = ChronoUnit.DAYS.between(start, end) + 1;
                        System.out.println(days);
                        LocalDate[] dates = new LocalDate[]{ start, end };
                        fireGUIEvent(new GUIEvent(this, Commands.UPDATE_FAHRZEUGE, dates));
                    }else {
                        System.out.println("Ende ist vor Start");
                    }
                } else {
                    //TODO: Bitte zwei valide Daten auswählen
                }
            }
        });
        leftPanel.add(buttonLoadFahrzeuge);

        leftPanel.setBackground(Color.WHITE);
        rightPanel.setBackground(Color.WHITE);

        this.add(topPanel, BorderLayout.NORTH);
        this.add(leftPanel, BorderLayout.WEST);
        this.add(rightPanel, BorderLayout.EAST);
        this.add(bottomPanel, BorderLayout.SOUTH);

        if (isNewObject) {
            createBuchung();
        } else {
            editBuchung();
        }
    }

    private String[] getKunden() {
        String[] alleKundenArray = new String[this.alleKunden.size()];
        int index = 0;
        for (IDepictable kunde : this.alleKunden) {
            alleKundenArray[index] = kunde.toString();
            index ++;
        }
        return alleKundenArray;
    }

    public CustomDatePicker getDateComponent(String type) {
        CustomDatePicker customDatePicker = null;
        if (type == "Start") {
            customDatePicker =  ((CustomDatePicker) inputFieldMap.get("Start"));
        }else if (type == "Ende") {
            customDatePicker =  ((CustomDatePicker) inputFieldMap.get("Ende"));
        }
        return customDatePicker;
    }

    private void createBuchung() {
        randID = UUID.randomUUID().toString();
        topLabelValue.setText(randID);
    }

    private void editBuchung() {
        Attribute[] attributes = iDepictable.getAttributeArray();
        randID = attributes[0].getValue().toString();
        topLabelValue.setText(randID);
        inputFieldMap.get("Buchungsnummer").setValue(attributes[1].getValue().toString());
        ((CustomTextField)inputFieldMap.get("Buchungsnummer")).getTextfield().setEnabled(false);

        inputFieldMap.get("Start").setValue(attributes[4].getValue().toString());
        inputFieldMap.get("Ende").setValue(attributes[5].getValue().toString());

        ((CustomComboBox)inputFieldMap.get("Status")).getComboBox().setEnabled(true);
        ((CustomComboBox)inputFieldMap.get("Status")).getComboBox().setSelectedIndex(((Buchungsstatus)attributes[6].getValue()).ordinal());


        ((CustomDatePicker)inputFieldMap.get("Start")).setEnabled(false);
        ((CustomDatePicker)inputFieldMap.get("Ende")).setEnabled(false);

        buttonLoadFahrzeuge.setVisible(false);


        List kundenList = new ArrayList();
        kundenList.add(selectedKunde);
        getKundenSLC().setListElements(kundenList);
        getKundenSLC().getSlc().selectElement(0);
        List fahrzeugList = new ArrayList();
        fahrzeugList.add(selectedFahrzeug);
        getFahrzeugSLC().setListElements(fahrzeugList);
        getFahrzeugSLC().getSlc().selectElement(0);
        getKundenSLC().setEnabled(false);
        getFahrzeugSLC().setEnabled(false);
    }

    private String[] getValues() {
        currentValues = new ArrayList<String>();
        currentValues.add(randID);
        currentValues.add(inputFieldMap.get("Buchungsnummer").getValue());
        Kunde kunde = (Kunde) ((CustomListField)inputFieldMap.get("Kunde")).getSlc().getSelectedElement();
        Fahrzeug fahrzeug = (Fahrzeug) ((CustomListField)inputFieldMap.get("Fahrzeug")).getSlc().getSelectedElement();
        if (kunde == null) {
            currentValues.add(null);
        } else {
            currentValues.add(kunde.getElementID());
        }
        if (fahrzeug == null) {
            currentValues.add(null);
        } else {
            currentValues.add(fahrzeug.getElementID());
        }
        currentValues.add(inputFieldMap.get("Start").getValue());
        currentValues.add(inputFieldMap.get("Ende").getValue());
        currentValues.add(inputFieldMap.get("Status").getValue());
        currentValues.add(LocalDateTime.now().toString());

        return (String[]) currentValues.toArray(new String[currentValues.size()]);
    }

    public CustomListField getKundenSLC() {
        return (CustomListField) inputFieldMap.get("Kunde");
    }

    public CustomListField getFahrzeugSLC() {
        return (CustomListField) inputFieldMap.get("Fahrzeug");
    }
    public CustomListField getDokumentSLC() {
        return (CustomListField) inputFieldMap.get("Dokumente");
    }

    public void updateDokumentList(List<IDepictable> dokumente) {
        CustomListField customListField = (CustomListField) inputFieldMap.get("Dokumente");
        customListField.setListElements(dokumente);
    }

    @Override
    public boolean validateInput() {
        if (currentValues.get(1).toString().isEmpty()) {
            JOptionPane.showMessageDialog(null,  "Bitte geben Sie eine valide Buchungsnummer ein!", "Buchungsnummer fehlerhaft", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (currentValues.get(2) == null) {
            JOptionPane.showMessageDialog(null,  "Bitte wählen Sie einen Kunden aus!", "Kunde fehlerhaft", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (currentValues.get(3) == null) {
            JOptionPane.showMessageDialog(null,  "Bitte wählen Sie ein Fahrzeug aus!", "Fahrzeug fehlerhaft", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}
