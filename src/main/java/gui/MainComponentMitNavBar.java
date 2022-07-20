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
import gui.customComponents.NavigationBar;
import util.CSHelp;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
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

    private IAppLogger logger = AppLogger.getInstance();

    private List<IDepictable> allElements = new ArrayList<>();
    private List<AttributeElement> allAttributeElements = new ArrayList<>();

    public final static String NB = "NavigationBar";
    public final static String CPÜ = "ContentPanel-Übersicht";
    public final static String CPB = "ContentPanel-Buchungen";
    public final static String CPF = "ContentPanel-Fahrzeuge";
    public final static String CPK = "ContentPanel-Kunden";
    public final static String CPS = "ContentPanel-Standorte";
    public final static String CPD = "ContentPanel-Dokumente";

    private NavigationBar nvb = null;
    private ContentPanel übersichtPanel = null;
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
        buchungenPanel = ContentPanel.builder(CPB)
                .title(title)
                .buttonImage(CSHelp.button_add_kunde)
                .observer(this)
                .propManager(this.propManager)
                .build();
        return buchungenPanel;
    }

    private ContentPanel createFahrzeugeTab(String title) {
        fahrzeugePanel = ContentPanel.builder(CPF)
                .title(title)
                .buttonImage(CSHelp.button_add_fahrzeug)
                .observer(this)
                .propManager(this.propManager)
                .build();
        return fahrzeugePanel;
    }

    private ContentPanel createKundenTab(String title) {
        kundenPanel = ContentPanel.builder(CPK)
                .title(title)
                .buttonImage(CSHelp.button_add_kunde)
                .observer(this)
                .propManager(this.propManager)
                .build();
        return kundenPanel;
    }

    private ContentPanel createStandorteTab(String title) {
        standortePanel = ContentPanel.builder(CPS)
                .title(title)
                .buttonImage(CSHelp.button_add_kunde)
                .observer(this)
                .propManager(this.propManager)
                .build();
        return standortePanel;
    }

    private ContentPanel createDokumenteTab(String title) {
        dokumentePanel = ContentPanel.builder(CPD)
                .title(title)
                .buttonImage(CSHelp.button_add_kunde)
                .observer(this)
                .propManager(this.propManager)
                .build();
        return dokumentePanel;
    }

    @Override
    public void processGUIEvent(GUIEvent ge) {
        //System.out.println(ge.getCmdText());
        //System.out.println(ge.getSource().getClass());
        //System.out.println(ge.getData());

        if(ge.getCmdText().equals(NavigationBar.Commands.TAB_CHANGED.cmdText)) {
            nvb.setActive((String)ge.getData());
            //System.out.println("New tab selected: " + ge.getData());
            this.card.show(this.content, (String) ge.getData());
        }
    }

    @Override
    public void processUpdateEvent(UpdateEvent ue) {


    }
}
