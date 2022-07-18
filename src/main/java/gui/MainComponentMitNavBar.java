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

    //JPanel pane1, pane2, pane3, cardPane;
    CardLayout card;
    JPanel content;

    private IPropertyManager propManager = null;

    private IAppLogger logger = AppLogger.getInstance();

    private java.util.List<IDepictable> allElements = new ArrayList<>();
    private List<AttributeElement> allAttributeElements = new ArrayList<>();

    private NavigationBar nvb = null;

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
        content.add(createBuchungenTab(), "buchungen");
        content.add(createFahrzeugeTab(), "fahrzeuge");
        content.add(createKundenTab(), "kunden");
        content.add(createStandorteTab(), "standorte");
        content.add(createDokumenteTab(), "dokumente");

        this.add(createNavBar(), BorderLayout.WEST);
        this.add(content, BorderLayout.CENTER);
    }

    private JPanel createNavBar() {
        JPanel navigationBar = new JPanel(new BorderLayout(0,0));
        nvb = NavigationBar.builder("NVB")
                .propManager(this.propManager)
                .observer(this)
                .build();
        navigationBar.add(nvb);
        return navigationBar;
    }

    private JPanel createÜbersichtTab() {
        JPanel demo = new JPanel();
        demo.setBackground(CSHelp.main);
        demo.add(new JLabel("Fahrzeuge"), BorderLayout.CENTER);
        return demo;
    }

    private JPanel createBuchungenTab() {
        ContentPanel contentPanel = new ContentPanel("Buchungen",CSHelp.button_add_kunde);
        return contentPanel;
    }

    private JPanel createFahrzeugeTab() {
        ContentPanel contentPanel = new ContentPanel("Fahrzeuge",CSHelp.button_add_kunde);
        return contentPanel;
    }

    private JPanel createKundenTab() {
        ContentPanel contentPanel = new ContentPanel("Kunden",CSHelp.button_add_kunde);
        return contentPanel;
    }

    private JPanel createStandorteTab() {
        ContentPanel contentPanel = new ContentPanel("Standorte",CSHelp.button_add_kunde);
        return contentPanel;
    }

    private JPanel createDokumenteTab() {
        ContentPanel contentPanel = new ContentPanel("Dokumente",CSHelp.button_add_kunde);
        return contentPanel;
    }


    @Override
    public void processGUIEvent(GUIEvent ge) {
//        System.out.println(ge.getCmdText());
//        System.out.println(ge.getSource().getClass());
//        System.out.println(ge.getData());

        if(ge.getCmdText().equals(NavigationBar.Commands.TAB_CHANGED.cmdText)) {
            System.out.println("New tab selected: " + ge.getData());
            this.card.show(this.content, (String) ge.getData());
        }
        
/*
        if(ge.getCmdText().equals(SimpleTableComponent.Commands.ATTRIBUTE_ROW_SELECTED.cmdText)) {

            //System.out.println(ge.getData().getClass());
            Attribute[] attribute = (Attribute[]) ge.getData();
            Kunde kunde = (Kunde) attribute[0].getDedicatedInstance();
            System.out.println(kunde.getElementID());

            JDialog dialog = new JDialog();
            dialog.setTitle(kunde.getElementID());
            dialog.setSize(new Dimension(500,500));
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setModal(true);
            dialog.setVisible(true);
        }
*/
    }

    @Override
    public void processUpdateEvent(UpdateEvent ue) {

    }
}
