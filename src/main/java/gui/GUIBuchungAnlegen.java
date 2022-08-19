package gui;

import de.dhbwka.swe.utils.event.EventCommand;
import de.dhbwka.swe.utils.event.GUIEvent;
import de.dhbwka.swe.utils.event.IGUIEventListener;
import de.dhbwka.swe.utils.gui.ObservableComponent;
import de.dhbwka.swe.utils.gui.SimpleListComponent;
import de.dhbwka.swe.utils.model.IDepictable;
import gui.customComponents.userInput.*;
import model.Bild;
import model.Buchungsstatus;
import model.Motorisierung;
import util.CSHelp;
import util.IValidate;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    //constructor for creating new Object
    public GUIBuchungAnlegen(IGUIEventListener observer, List alleKunden) {
        this.addObserver(observer);
        this.observer = observer;
        this.alleKunden = alleKunden;
        this.alleFahrzeuge = new ArrayList<>();
        initUI(true);
    }

    //constructor for editing an existing Object
    public GUIBuchungAnlegen(IGUIEventListener observer, IDepictable iDepictable) {
        this.addObserver(observer);
        this.iDepictable = iDepictable;
        this.observer = observer;
        initUI(false);
    }

    private void initUI(Boolean isNewObject) {
        this.setLayout(new BorderLayout(0,0));
        this.setPreferredSize(new Dimension(500,700));
        this.setBackground(CSHelp.main);

        topPanel = new JPanel(new BorderLayout(0,0));
        topLabelDescription = new JLabel("ID: ");
        topLabelDescription.setBorder(new EmptyBorder(20,20,0,5));
        topLabelDescription.setFont(CSHelp.lato.deriveFont(11f));
        topLabelValue = new JLabel();
        topLabelValue.setBorder(new EmptyBorder(20,0,0,20));
        topLabelValue.setFont(CSHelp.lato.deriveFont(9f));
        topPanel.add(topLabelDescription, BorderLayout.WEST);
        topPanel.add(topLabelValue, BorderLayout.CENTER);


        bottomPanel = new JPanel(new BorderLayout(0,0));
//        bottomPanel.setBackground(Color.lightGray);
        save_buchung = new JButton("Speichern!");
        save_buchung.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] test = getValues();
                if(validateInput()) {
                    int a = JOptionPane.showConfirmDialog(GUIBuchungAnlegen.this, "Wollen Sie die Buchung speichern?", "Bestätigung", JOptionPane.YES_NO_OPTION, 1, CSHelp.imageList.get("icon_person.png"));
                    if (a == 0) {
                        fireGUIEvent( new GUIEvent(this, Commands.ADD_BUCHUNG, test ));
                    }
                }
            }
        });
        bottomPanel.add(save_buchung, BorderLayout.SOUTH);

        leftPanel = new JPanel();
        rightPanel = new JPanel();
        leftPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        rightPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        leftPanel.setPreferredSize(new Dimension(250, 600));
        rightPanel.setPreferredSize(new Dimension(250, 600));
        leftPanel.setBorder(new EmptyBorder(10,10,10,10));
        rightPanel.setBorder(new EmptyBorder(10,10,10,10));

        inputFieldMap = new LinkedHashMap<>();

        //TODO: Buchungsnummer-Feld soll nicht editierbar sein und erst beim Speichern generiert werden
        inputFieldMap.put("Buchungsnummer", new CustomTextField("Buchungsnummer", "wird automatisch generiert"));
        inputFieldMap.put("Kunde", new CustomListField("Kunde", this.observer, this.alleKunden));
        inputFieldMap.put("Status", new CustomComboBox("Status", "Buchungsstatus", Buchungsstatus.getArray(), observer));
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

        JButton buttonLoadFahrzeuge = new JButton("Verfügbare Fahrzeuge laden");
        buttonLoadFahrzeuge.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String startDate = getDateComponent("Start").getTextField().getText();
                String endDate = getDateComponent("Ende").getTextField().getText();
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

    public CustomListField getKundenSLC() {
        return (CustomListField) inputFieldMap.get("Kunde");
    }

    public CustomListField getFahrzeugSLC() {
        return (CustomListField) inputFieldMap.get("Fahrzeug");
    }

    @Override
    public boolean validateInput() {
        return false;
    }
}
