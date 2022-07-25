package gui;

import de.dhbwka.swe.utils.event.*;
import de.dhbwka.swe.utils.gui.AttributeElement;
import de.dhbwka.swe.utils.gui.ObservableComponent;
import de.dhbwka.swe.utils.model.IDepictable;
import de.dhbwka.swe.utils.util.AppLogger;
import de.dhbwka.swe.utils.util.IAppLogger;
import de.dhbwka.swe.utils.util.IPropertyManager;
import de.dhbwka.swe.utils.util.PropertyManager;
import gui.customComponents.ContentPanel;
import gui.customComponents.CustomTableComponent;
import gui.customComponents.NavigationBar;
import model.*;
import util.CSHelp;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainComponentMitNavBar extends ObservableComponent implements IGUIEventListener, IUpdateEventListener {

    public enum Commands implements EventCommand {

        ;

        @Override
        public String getCmdText() {
            return null;
        }

        @Override
        public Class<?> getPayloadType() {
            return null;
        }
    }

    private CardLayout card;
    private JPanel content;

    private IPropertyManager propManager = null;

    private final IAppLogger logger = AppLogger.getInstance();

    private final List<IDepictable> allElements = new ArrayList<>();
    private final List<AttributeElement> allAttributeElements = new ArrayList<>();

    public final static String NB = "NavigationBar";
    public final static String CPÜ = "ContentPanel-Übersicht";
    public final static String CPB = "ContentPanel-Buchungen";
    public final static String CPF = "ContentPanel-Fahrzeuge";
    public final static String CPK = "ContentPanel-Kunden";
    public final static String CPS = "ContentPanel-Standorte";
    public final static String CPD = "ContentPanel-Dokumente";

    private NavigationBar nvb = null;
    private final ContentPanel übersichtPanel = null;
    private ContentPanel buchungenPanel = null;
    private ContentPanel fahrzeugePanel = null;
    private ContentPanel kundenPanel = null;
    private ContentPanel standortePanel = null;
    private ContentPanel dokumentePanel = null;

    public MainComponentMitNavBar(PropertyManager propertyManager) {
        this.propManager = propertyManager;
        CSHelp.init();
        initUI();
    }

    private void initUI() {
        this.setLayout( new BorderLayout(0,0) );

        card = new CardLayout();
        content = new JPanel();

        content.setLayout(card);

        content.add(createÜbersichtTab(), "übersicht");
        content.add(createBuchungenTab("Buchungen"), "buchungen");
        content.add(createFahrzeugeTab("Fahrzeuge"), "fahrzeuge");
        content.add(createKundenTab("Kunden"), "kunden");
        content.add(createStandorteTab("Standorte"), "standorte");
        content.add(createDokumenteTab("Dokumente"), "dokumente");

        this.add(createNavBar(), BorderLayout.WEST);
        this.add(content, BorderLayout.CENTER);
    }

    private JPanel createNavBar() {
        JPanel navigationBar = new JPanel(new BorderLayout(0,0));
        nvb = NavigationBar.builder(NB)
                .propManager(this.propManager)
                .observer(this)
                .build();
        navigationBar.add(nvb);
        return navigationBar;
    }

    private JPanel createÜbersichtTab() {
        JPanel demo = new JPanel();
        demo.setBackground(CSHelp.main);
        demo.add(new JLabel("Übersicht"), BorderLayout.CENTER);
        return demo;
    }

    private ContentPanel createBuchungenTab(String title) {

        CustomTableComponent ctc = CustomTableComponent.builder("buchungen")
                .observer(this)
                .propManager(this.propManager)
                .columnWidths(new int[]{145, 140, 140, 75, 75, 140, 75, 33, 33})
                .modelClass(Buchung.class)
                .modelData(new Buchung[]{
                        new Buchung("B1232", new Kunde(new File("src/main/resources/Images/profile_picture.png"), "Leon Christian", "Lantz", "leon@lantz.de", "0174758123", "DE9123123123124124", "14.04.2001", new Date()), new Fahrzeug(null, new File("src/main/resources/Images/audi.jpg"), "A3 Sportback 30 TFSI", "Audi", "V8", 5, 5, 80, "1654kg", Fahrzeugkategorie.GEHOBENE_MITTELKLASSE, "B", "DÜW L 140", new Date(), "Grau", new Date()), new Date(), new Date(), Buchungsstatus.FRISTGERECHT_STORNIERT, new Date())
                })
                .build();

        buchungenPanel = ContentPanel.builder(CPB)
                .title(title)
                .table(ctc)
                .buttonImage(CSHelp.imageList.get("kunden.png"))
                .observer(this)
                .propManager(this.propManager)
                .build();
        return buchungenPanel;
    }

    private ContentPanel createFahrzeugeTab(String title) {
        CustomTableComponent ctc = CustomTableComponent.builder("fahrzeuge")
                .observer(this)
                .propManager(this.propManager)
                .columnWidths(new int[]{50, 150, 100, 50, 80, 115, 110, 60, 75, 33, 33})
                .modelClass(Fahrzeug.class)
                .modelData(new Fahrzeug[]{
                        new Fahrzeug(null, new File("src/main/resources/Images/audi.jpg"), "A3 Sportback 30 TFSI", "Audi", "V8", 5, 5, 80, "1654kg", Fahrzeugkategorie.GEHOBENE_MITTELKLASSE, "B", "DÜW L 140", new Date(), "Grau", new Date())
                })
                .build();

        fahrzeugePanel = ContentPanel.builder(CPF)
                .title(title)
                .buttonImage(CSHelp.imageList.get("fahrzeug.png"))
                .observer(this)
                .table(ctc)
                .propManager(this.propManager)
                .build();
        System.out.println("FahrzeugTab erfolgreich erstellt");
        return fahrzeugePanel;
    }

    private ContentPanel createKundenTab(String title) {
        CustomTableComponent ctc = CustomTableComponent.builder("kunden")
                .observer(this)
                .propManager(this.propManager)
                .columnWidths(new int[]{50, 100, 140, 150, 130, 145, 75, 33, 33})
                .modelClass(Kunde.class)
                .modelData(new Kunde[]{
                new Kunde(new File("src/main/resources/Images/profile_picture.png"), "Leon", "Lantz", "leon@lantz.de", "0174758123", "DE9123123123124124", "14.04.2001", new Date()),
                new Kunde(new File("src/main/resources/Images/profile_picture.png"), "Max", "Reichmann", "max@reichmann.de", "12345", "DE1111111111111111", "01.01.2001", new Date()),
                new Kunde(new File("src/main/resources/Images/profile_picture.png"), "Lutz", "Gröll", "lutz@gröll.de", "+4988923211", "DE912324124", "01.01.1950", new Date()),
                new Kunde(new File("src/main/resources/Images/profile_picture.png"), "Maximilian", "Nintemann", "maximilian@nintemann.de", "+49 911 911 911", "DE91232412423212", "01.01.1950", new Date())
                })
                .build();

        kundenPanel = ContentPanel.builder(CPK)
                .title(title)
                .table(ctc)
                .buttonImage(CSHelp.imageList.get("kunden.png"))
                .observer(this)
                .propManager(this.propManager)
                .build();
        System.out.println("KundenTab erfolgreich erstellt");
        return kundenPanel;
    }

    private ContentPanel createStandorteTab(String title) {
        CustomTableComponent ctc = CustomTableComponent.builder("standorte")
                .observer(this)
                .propManager(this.propManager)
                .columnWidths(new int[]{100, 100, 100, 100, 100, 100, 100, 90, 33, 33})
                .modelClass(Standort.class)
                .modelData(new Standort[]{
                        new Standort("Erzbergstraße 40", "76133", "Karlsruhe", "Deutschland", "https://goo.gl/maps/ZBwnxvrJ5YhAxkcM7", "4", new Date())
                })
                .build();

        standortePanel = ContentPanel.builder(CPS)
                .title(title)
                .table(ctc)
                .buttonImage(CSHelp.imageList.get("kunden.png"))
                .observer(this)
                .propManager(this.propManager)
                .build();
        return standortePanel;
    }

    private ContentPanel createDokumenteTab(String title) {
        dokumentePanel = ContentPanel.builder(CPD)
                .title(title)
                .buttonImage(CSHelp.imageList.get("kunden.png"))
                .observer(this)
                .propManager(this.propManager)
                .build();
        return dokumentePanel;
    }

    @Override
    public void processGUIEvent(GUIEvent ge) {
        System.out.println(ge.getCmdText());
        //System.out.println(ge.getSource().getClass());
        System.out.println(ge.getData());


        if(ge.getCmdText().equals(NavigationBar.Commands.TAB_CHANGED.cmdText)) {
            nvb.setActive((String)ge.getData());
            //System.out.println("New tab selected: " + ge.getData());
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    MainComponentMitNavBar.this.card.show(MainComponentMitNavBar.this.content, (String) ge.getData());
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            });
            //this.card.show(this.content, (String) ge.getData());
        }
    }

    @Override
    public void processUpdateEvent(UpdateEvent ue) {


    }
}
