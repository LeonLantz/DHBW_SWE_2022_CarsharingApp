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

        Border border_right = BorderFactory.createMatteBorder(0,0,0,1, CSHelp.navBar);
        Border border_bottom = BorderFactory.createMatteBorder(0,0,1,0, CSHelp.navBar);
        Border border_top = BorderFactory.createMatteBorder(1,0,0,0, CSHelp.navBar);

        //Hauptkomponente: Navigationsbar (links)
        JPanel navBar = new JPanel(new BorderLayout(0,0));
        navBar.setBackground(Color.white);
        navBar.setPreferredSize(new Dimension(180, 720));
        navBar.setBorder(border_right);

        //Teilkomponente: Sektion 1
        JPanel section_1 = new JPanel();
        section_1.setBackground(Color.white);
        section_1.setPreferredSize(new Dimension(180, 130));
        section_1.setBorder(border_bottom);
        navBar.add(section_1, BorderLayout.NORTH);

        //Teilkomponente: Sektion 2
        JPanel section_2 = new JPanel(new FlowLayout(0,0,0));
        section_2.setBackground(Color.white);
        section_2.setPreferredSize(new Dimension(180, 240));

        //Button Übersicht
        JPanel navBar_panel_übersicht = new JPanel();
        navBar_panel_übersicht.setBackground(Color.white);
        JButton navbar_button_übersicht = new JButton("Übersicht");
        navbar_button_übersicht.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                card.show(content, "übersicht");
            }
        });
        navBar_panel_übersicht.add(navbar_button_übersicht, BorderLayout.CENTER);
        navBar_panel_übersicht.setPreferredSize(new Dimension(180, 40));
        section_2.add(navBar_panel_übersicht);

        //Button Buchungen
        JPanel navBar_panel_buchungen = new JPanel();
        navBar_panel_buchungen.setBackground(Color.white);
        JButton navBar_button_buchungen = new JButton("Buchungen");
        navBar_button_buchungen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                card.show(content, "buchungen");
            }
        });
        navBar_panel_buchungen.add(navBar_button_buchungen, BorderLayout.CENTER);
        navBar_panel_buchungen.setPreferredSize(new Dimension(180, 40));
        section_2.add(navBar_panel_buchungen);

        //Button Fahrzeuge
        JPanel navBar_panel_fahrzeuge = new JPanel();
        navBar_panel_fahrzeuge.setBackground(Color.white);
        JButton navBar_button_fahrzeuge = new JButton("Fahrzeuge");
        navBar_button_fahrzeuge.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                card.show(content, "fahrzeuge");
            }
        });
        navBar_panel_fahrzeuge.add(navBar_button_fahrzeuge, BorderLayout.CENTER);
        navBar_panel_fahrzeuge.setPreferredSize(new Dimension(180, 40));
        section_2.add(navBar_panel_fahrzeuge);

        //Button Kunden
        JPanel navBar_panel_kunden = new JPanel();
        navBar_panel_kunden.setBackground(Color.white);
        JButton navBar_button_kunden = new JButton("Kunden");
        navBar_button_kunden.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                card.show(content, "kunden");
            }
        });
        navBar_panel_kunden.add(navBar_button_kunden, BorderLayout.CENTER);
        navBar_panel_kunden.setPreferredSize(new Dimension(180, 40));
        section_2.add(navBar_panel_kunden);

        //Button Standorte
        JPanel navBar_panel_standorte = new JPanel();
        navBar_panel_standorte.setBackground(Color.white);
        JButton navBar_button_standorte = new JButton("Standorte");
        navBar_button_standorte.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                card.show(content, "standorte");
            }
        });
        navBar_panel_standorte.add(navBar_button_standorte, BorderLayout.CENTER);
        navBar_panel_standorte.setPreferredSize(new Dimension(180, 40));
        section_2.add(navBar_panel_standorte);

        //Button Dokumente
        JPanel navBar_panel_dokumente = new JPanel();
        navBar_panel_dokumente.setBackground(Color.white);
        JButton navBar_button_dokumente = new JButton("Dokumente");
        navBar_button_dokumente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                card.show(content, "dokumente");
            }
        });
        navBar_panel_dokumente.add(navBar_button_dokumente, BorderLayout.CENTER);
        navBar_panel_dokumente.setPreferredSize(new Dimension(180, 40));
        section_2.add(navBar_panel_dokumente);


        navBar.add(section_2, BorderLayout.CENTER);

        //Teilkomponente: Sektion 3
        JPanel section_3 = new JPanel();
        section_3.setBackground(Color.white);
        section_3.setPreferredSize(new Dimension(180, 320));
        section_3.setBorder(border_top);
        navBar.add(section_3, BorderLayout.SOUTH);

        return navBar;
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
        //System.out.println(ge.getCmdText());
        //System.out.println(ge.getSource().getClass());
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
