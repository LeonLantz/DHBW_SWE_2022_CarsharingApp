package gui;

import control.CSControllerReinerObserverUndSender;
import de.dhbwka.swe.utils.event.*;
import de.dhbwka.swe.utils.gui.AttributeElement;
import de.dhbwka.swe.utils.gui.ObservableComponent;
import de.dhbwka.swe.utils.model.Attribute;
import de.dhbwka.swe.utils.model.IDepictable;
import de.dhbwka.swe.utils.util.AppLogger;
import de.dhbwka.swe.utils.util.IAppLogger;
import de.dhbwka.swe.utils.util.IPropertyManager;
import de.dhbwka.swe.utils.util.PropertyManager;
import gui.customComponents.ContentPanel;
import gui.customComponents.CustomTableComponent;
import gui.customComponents.NavigationBar;
import gui.customComponents.userInput.CustomNavBarButton;
import model.*;
import util.CSHelp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainComponentMitNavBar extends ObservableComponent implements IGUIEventListener, IUpdateEventListener {

    public enum Commands implements EventCommand {

        BUTTON_PRESSED("MainComponentMitNavBar.button_pressed", Attribute.class),
        REMOVE_KUNDE( "MainComponentMitNavBar.remove_kunde", String.class );

        public final Class<?> payloadType;
        public final String cmdText;

        Commands(String cmdText, Class<?> payloadType) {
            this.cmdText = cmdText;
            this.payloadType = payloadType;
        }
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

    private CustomTableComponent buchungenTable;
    private CustomTableComponent fahrzeugeTable;
    private CustomTableComponent kundenTable;
    private CustomTableComponent standorteTable;
    private CustomTableComponent dokumenteTable;

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

        buchungenTable = CustomTableComponent.builder("buchungen")
                .observer(this)
                .propManager(this.propManager)
                .columnWidths(new int[]{145, 140, 140, 75, 75, 140, 75, 33, 33})
                .modelClass(Buchung.class)
                .build();

        buchungenPanel = ContentPanel.builder(CPB)
                .title(title)
                .table(buchungenTable)
                .addButton(new NewObjectButton(CSHelp.imageList.get("kunden.png"), Buchung.class))
                .observer(this)
                .propManager(this.propManager)
                .build();
        return buchungenPanel;
    }

    private ContentPanel createFahrzeugeTab(String title) {
        fahrzeugeTable = CustomTableComponent.builder(title+"-Table")
                .observer(this)
                .propManager(this.propManager)
                .columnWidths(new int[]{150, 100, 50, 80, 115, 110, 89, 100, 33, 33})
                .modelClass(Fahrzeug.class)
                .build();

        fahrzeugePanel = ContentPanel.builder(CPF)
                .title(title)
                .addButton(new NewObjectButton(CSHelp.imageList.get("kunden.png"), Fahrzeug.class))
                .observer(this)
                .table(fahrzeugeTable)
                .propManager(this.propManager)
                .build();
        System.out.println("FahrzeugTab erfolgreich erstellt");
        return fahrzeugePanel;
    }

    private ContentPanel createKundenTab(String title) {
         kundenTable = CustomTableComponent.builder(title+"-Table")
                .observer(this)
                .propManager(this.propManager)
                .columnWidths(new int[]{100, 100, 160, 130, 130, 149, 33, 33})
                .modelClass(Kunde.class)
                .build();

        kundenPanel = ContentPanel.builder(CPK)
                .title(title)
                .table(kundenTable)
                .addButton(new NewObjectButton(CSHelp.imageList.get("kunden.png"), Kunde.class))
                .observer(this)
                .propManager(this.propManager)
                .build();
        System.out.println("KundenTab erfolgreich erstellt");
        return kundenPanel;
    }

    private ContentPanel createStandorteTab(String title) {
         standorteTable = CustomTableComponent.builder("standorte")
                .observer(this)
                .propManager(this.propManager)
                .columnWidths(new int[]{100, 100, 100, 100, 100, 100, 100, 90, 33, 33})
                .modelClass(Standort.class)
                .build();

        standortePanel = ContentPanel.builder(CPS)
                .title(title)
                .table(standorteTable)
                .addButton(new NewObjectButton(CSHelp.imageList.get("kunden.png"), Standort.class))
                .observer(this)
                .propManager(this.propManager)
                .build();
        return standortePanel;
    }

    private ContentPanel createDokumenteTab(String title) {
        dokumentePanel = ContentPanel.builder(CPD)
                .title(title)
                .observer(this)
                .addButton(new NewObjectButton(CSHelp.imageList.get("kunden.png"), Standort.class))
                .propManager(this.propManager)
                .build();
        return dokumentePanel;
    }

    public class NewObjectButton extends JButton {

        public NewObjectButton(ImageIcon imageIcon, Class modelClass) {
            this.setPreferredSize(new Dimension(300,54));
            this.setBorder(new EmptyBorder(0,0,0,0));
            this.setIcon(imageIcon);

            this.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    MainComponentMitNavBar.this.fireGUIEvent(new GUIEvent(this, MainComponentMitNavBar.Commands.BUTTON_PRESSED, modelClass));
                }

                @Override
                public void mousePressed(MouseEvent e) {}

                @Override
                public void mouseReleased(MouseEvent e) {}

                @Override
                public void mouseEntered(MouseEvent e) {
                    MainComponentMitNavBar.this.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    MainComponentMitNavBar.this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            });
        }
    }

    @Override
    public void processGUIEvent(GUIEvent ge) {
        //System.out.println(ge.getCmdText());
        //System.out.println(ge.getSource().getClass());
//        System.out.println(ge.getData());


        if(ge.getCmdText().equals(NavigationBar.Commands.TAB_CHANGED.cmdText)) {
            nvb.setActive((String)ge.getData());
            //System.out.println("New tab selected: " + ge.getData());
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    MainComponentMitNavBar.this.card.show(MainComponentMitNavBar.this.content, (String) ge.getData());
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            });
            //this.card.show(this.content, (String) ge.getData());
        } else if (ge.getCmdText().equals(CustomTableComponent.Commands.DELETE_ROW.cmdText)) {
            int n = JOptionPane.showConfirmDialog(this, "Wollen Sie das Objekt wirklich löschen?", "Bestätigung", JOptionPane.YES_NO_OPTION, 1, CSHelp.imageList.get("profile_picture.png"));
            if(n == 0) {
                fireGUIEvent(ge);
            }
        } else if (ge.getCmdText().equals(CustomTableComponent.Commands.EDIT_ROW.cmdText)) {
            fireGUIEvent(ge);
        }
    }

    @Override
    public void processUpdateEvent(UpdateEvent ue) {

        System.out.println("UPDATE");

        if( ue.getCmd() == CSControllerReinerObserverUndSender.Commands.SET_KUNDEN ) {
            List<Kunde> lstKunde = (List<Kunde>)ue.getData();
            IDepictable[] modelData = new IDepictable[lstKunde.size()];
            lstKunde.toArray(modelData);
            kundenTable.setModelData(modelData);
        }else if( ue.getCmd() == CSControllerReinerObserverUndSender.Commands.SET_FAHRZEUGE ) {
            List<Fahrzeug> lstFahrzeug = (List<Fahrzeug>)ue.getData();
            IDepictable[] modelData = new IDepictable[lstFahrzeug.size()];
            lstFahrzeug.toArray(modelData);
            fahrzeugeTable.setModelData(modelData);
        }

    }
}
