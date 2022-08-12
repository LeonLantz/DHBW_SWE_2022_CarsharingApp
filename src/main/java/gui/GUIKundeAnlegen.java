package gui;

import de.dhbwka.swe.utils.event.EventCommand;
import de.dhbwka.swe.utils.event.GUIEvent;
import de.dhbwka.swe.utils.event.IGUIEventListener;
import de.dhbwka.swe.utils.gui.ObservableComponent;
import de.dhbwka.swe.utils.model.IDepictable;
import gui.customComponents.userInput.CustomComboBox;
import gui.customComponents.userInput.CustomInputField;
import gui.customComponents.userInput.CustomListField;
import gui.customComponents.userInput.CustomTextField;
import model.Bild;
import model.Fahrzeugkategorie;
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

    //Map of all CustomInputFields
    private Map<String, CustomInputField> inputFieldMap;

    private IGUIEventListener observer;
    private String randID;

    private JPanel topPanel, bottomPanel, leftPanel, rightPanel;
    private JLabel topLabelValue, topLabelDescription;
    private JButton save_kunde;

    private IDepictable iDepictable;
    //private List bildList, documentList;

    private CustomInputField inputVorname, inputNachname, inputEmail, inputPhone, inputIBAN, inputDOB;


    //constructor for creating new Object
    public GUIKundeAnlegen(IGUIEventListener observer) {
        this.addObserver(observer);
        this.observer = observer;
        initUI(true);
    }

    //constructor for editing an existing Object
    public GUIKundeAnlegen(IGUIEventListener observer, IDepictable iDepictable) {
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
        save_kunde = new JButton("Speichern!");
        save_kunde.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] test = getValues();
                System.out.println("------------------------------");
                for (String t : test) {
                    System.out.println(t);
                }
                System.out.println("------------------------------");
                if(validateInput()) {
                    System.out.println("Speichern!");
                    int a = JOptionPane.showConfirmDialog(GUIKundeAnlegen.this, "Wollen sie den Kunden speichern?", "Bestätigung", JOptionPane.YES_NO_OPTION, 1, CSHelp.imageList.get("profile_picture.png"));
                    if (a == 0) {
                        fireGUIEvent( new GUIEvent(this, Commands.ADD_KUNDE, test ));
                    }
                }
            }
        });
        bottomPanel.add(save_kunde);

        leftPanel = new JPanel();
        rightPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        leftPanel.setPreferredSize(new Dimension(250, 600));
        rightPanel.setPreferredSize(new Dimension(250, 600));
        leftPanel.setBorder(new EmptyBorder(10,10,10,10));
        rightPanel.setBorder(new EmptyBorder(10,10,10,10));


        inputFieldMap = new LinkedHashMap<>();
        inputFieldMap.put("Vorname", new CustomTextField("Bezeichnung", "Fahrzeugbezeichnung"));
        inputFieldMap.put("Nachname", new CustomTextField("Marke", "bsp.: Audi, Mercedes, ..."));
        inputFieldMap.put("Email", new CustomComboBox("Motorisierung", "Diesel, Benzin, Elektro ..", Motorisierung.getArray(), observer));
        inputFieldMap.put("Phone", new CustomTextField("Türen", "Anzahl der Türen"));
        inputFieldMap.put("IBAN", new CustomTextField("Sitze", "Anzahl der Sitze"));
        inputFieldMap.put("DOB", new CustomTextField("Kofferraumvolumen (l)", "Volumen in Liter"));;
        //TODO: Fahrzeugbilder und Dokumente
        inputFieldMap.put("Bilder", new CustomListField("Bilder", "placeholder", this.observer, this.iDepictable ));
        inputFieldMap.put("Dokumente", new CustomListField("Dokumente", "placeholder", this.observer, this.iDepictable ));

        int leftPanelCount = 0;
        for (CustomInputField customInputField : inputFieldMap.values()) {
            if (leftPanelCount < 5) {
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

    public CustomListField getBildList() {
        return (CustomListField) inputFieldMap.get("Bilder");
    }

    @Override
    public boolean validateInput() {
        return false;
    }
}
