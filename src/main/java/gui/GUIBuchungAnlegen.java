package gui;

import de.dhbwka.swe.utils.event.EventCommand;
import de.dhbwka.swe.utils.event.GUIEvent;
import de.dhbwka.swe.utils.event.IGUIEventListener;
import de.dhbwka.swe.utils.gui.ObservableComponent;
import de.dhbwka.swe.utils.gui.SimpleListComponent;
import de.dhbwka.swe.utils.model.IDepictable;
import gui.customComponents.userInput.CustomComboBox;
import gui.customComponents.userInput.CustomInputField;
import gui.customComponents.userInput.CustomListField;
import gui.customComponents.userInput.CustomTextField;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GUIBuchungAnlegen extends ObservableComponent implements IValidate {

    public enum Commands implements EventCommand {

        ADD_BUCHUNG( "GUIBuchungAnlegen.addBuchung", String[].class );

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

    private JPanel topPanel, bottomPanel, leftPanel, rightPanel;
    private JLabel topLabelValue, topLabelDescription;
    private JButton save_buchung;

    private String randID;

    private IDepictable iDepictable;

    private List<IDepictable> alleKunden, alleFahrzeuge;

    //constructor for creating new Object
    public GUIBuchungAnlegen(IGUIEventListener observer, List alleKunden, List alleFahrzeuge) {
        this.addObserver(observer);
        this.observer = observer;
        this.alleKunden = alleKunden;
        this.alleFahrzeuge = alleFahrzeuge;
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


        bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.lightGray);
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
        bottomPanel.add(save_buchung);

        leftPanel = new JPanel();
        rightPanel = new JPanel();
        leftPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        rightPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        leftPanel.setPreferredSize(new Dimension(250, 600));
        rightPanel.setPreferredSize(new Dimension(250, 600));
        leftPanel.setBorder(new EmptyBorder(10,10,10,10));
        rightPanel.setBorder(new EmptyBorder(10,10,10,10));

        inputFieldMap = new LinkedHashMap<>();
        inputFieldMap.put("Buchungsnummer", new CustomTextField("Buchungsnummer", "Buchungsnummer"));

        inputFieldMap.put("Kunde", new CustomListField("Kunde", this.observer, this.alleKunden));
        inputFieldMap.put("Start", new CustomTextField("Start", "Start"));
        inputFieldMap.put("Status", new CustomComboBox("Status", "Buchungsstatus", Buchungsstatus.getArray(), observer));
        inputFieldMap.put("Fahrzeug", new CustomListField("Fahrzeug", this.observer, this.alleFahrzeuge));
        inputFieldMap.put("Ende", new CustomTextField("Ende", "Ende"));
        //inputFieldMap.put("Dokumente", new CustomListField("Dokumente", "placeholder", this.observer, this.iDepictable ));

        int leftPanelCount = 0;
        for (CustomInputField customInputField : inputFieldMap.values()) {
            if (leftPanelCount < 3) {
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
            createKunde();
        } else {
            editKunde();
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

    private void createKunde() {

    }

    private void editKunde() {

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
