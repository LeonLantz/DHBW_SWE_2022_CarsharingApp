package gui.customComponents;

import de.dhbwka.swe.utils.event.EventCommand;
import de.dhbwka.swe.utils.event.GUIEvent;
import de.dhbwka.swe.utils.event.IGUIEventListener;
import de.dhbwka.swe.utils.gui.ObservableComponent;
import gui.customComponents.userInput.CustomNavBarButton;
import util.CSHelp;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NavigationBar extends ObservableComponent {
    public enum Commands implements EventCommand {
        TAB_CHANGED( "navigationBar.tab_changed", String.class );

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

    JPanel _sectionOne, _sectionTwo, _sectionThree;
    CustomNavBarButton _navbarButtonÜbersicht, _navbarButtonBuchungen, _navbarButtonFahrzeuge, _navbarButtonKunden, _navbarButtonStandorte, _navbarButtonDokumente;
    JPanel navbarPanelÜbersicht, navbarPanelBuchungen, navbarPanelFahrzeuge, navbarPanelKunden, navbarPanelStandorte, navbarPanelDokumente;
    public NavigationBar(IGUIEventListener observer) {
        this.addObserver(observer);
        this.initUI();
    }

    private void initUI() {
        Border borderRight = BorderFactory.createMatteBorder(0,0,0,1, CSHelp.navBar);
        Border borderBottom = BorderFactory.createMatteBorder(0,0,1,0, CSHelp.navBar);
        Border borderTop = BorderFactory.createMatteBorder(1,0,0,0, CSHelp.navBar);

        //Hauptkomponente: Navigationsbar (links)
        this.setLayout(new BorderLayout(0,0));
        this.setPreferredSize(new Dimension(180, 720));
        this.setBackground(Color.white);
        this.setBorder(borderRight);

        //Teilkomponente: Sektion 1 (oben)
        _sectionOne = new JPanel(new BorderLayout(0,0));
        _sectionOne.setPreferredSize(new Dimension(180, 130));
        _sectionOne.setBackground(Color.white);
        _sectionOne.setBorder(borderBottom);
        this.add(_sectionOne, BorderLayout.NORTH);

        //Teilkomponente: Sektion 1 (oben) - Profilbild
        JPanel navbarPanelProfilbild = new JPanel(new FlowLayout(FlowLayout.LEFT));
        navbarPanelProfilbild.setPreferredSize(new Dimension(180, 90));
        navbarPanelProfilbild.setBackground(Color.white);
        JLabel imageIcon = new JLabel(CSHelp.imageList.get("image_user.png"));
        imageIcon.setBorder(new EmptyBorder(35,10,0,0));
        navbarPanelProfilbild.add(imageIcon);
        _sectionOne.add(navbarPanelProfilbild, BorderLayout.NORTH);

        //Teilkomponente: Sektion 1 (oben) - Profilname
        JPanel navbarPanelProfilname = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel navbarLabelProfilname = new JLabel("Lutz Gröll");
        navbarPanelProfilname.setBackground(Color.white);
        navbarLabelProfilname.setFont(CSHelp.lato_bold.deriveFont(18f));
        navbarLabelProfilname.setBorder(new EmptyBorder(0,10,0,30));
        navbarPanelProfilname.add(navbarLabelProfilname);
        ImageIcon arrow = CSHelp.imageList.get("icon_arrow.png");
        navbarPanelProfilname.add(new JLabel(arrow));
        _sectionOne.add(navbarPanelProfilname);

        //Teilkomponente: Sektion 2
        _sectionTwo = new JPanel(new FlowLayout(0,0,5));
        _sectionTwo.setPreferredSize(new Dimension(180, 280));
        _sectionTwo.setBackground(Color.white);

        //Button Übersicht
        navbarPanelÜbersicht = new JPanel();
        navbarPanelÜbersicht.setPreferredSize(new Dimension(180, 40));
        navbarPanelÜbersicht.setBackground(Color.white);
        _navbarButtonÜbersicht = new CustomNavBarButton("Übersicht", CSHelp.imageList.get("icon_übersicht.png"));
        _navbarButtonÜbersicht.getButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NavigationBar.this.fireGUIEvent(new GUIEvent(this, Commands.TAB_CHANGED, "übersicht"));
            }
        });
        navbarPanelÜbersicht.add(_navbarButtonÜbersicht, BorderLayout.CENTER);
        _sectionTwo.add(navbarPanelÜbersicht);

        //Button Buchungen
        navbarPanelBuchungen = new JPanel();
        navbarPanelBuchungen.setPreferredSize(new Dimension(180, 40));
        navbarPanelBuchungen.setBackground(Color.white);
        _navbarButtonBuchungen = new CustomNavBarButton("Buchungen", CSHelp.imageList.get("icon_buchung.png"));
        _navbarButtonBuchungen.getButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NavigationBar.this.fireGUIEvent(new GUIEvent(this, Commands.TAB_CHANGED, "buchungen"));
            }
        });
        navbarPanelBuchungen.add(_navbarButtonBuchungen, BorderLayout.CENTER);
        _sectionTwo.add(navbarPanelBuchungen);

        //Button Fahrzeuge
        navbarPanelFahrzeuge = new JPanel();
        navbarPanelFahrzeuge.setPreferredSize(new Dimension(180, 40));
        navbarPanelFahrzeuge.setBackground(Color.white);
        _navbarButtonFahrzeuge = new CustomNavBarButton("Fahrzeuge", CSHelp.imageList.get("icon_fahrzeug.png"));
        _navbarButtonFahrzeuge.getButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NavigationBar.this.fireGUIEvent(new GUIEvent(this, Commands.TAB_CHANGED, "fahrzeuge"));
            }
        });
        navbarPanelFahrzeuge.add(_navbarButtonFahrzeuge, BorderLayout.CENTER);
        _sectionTwo.add(navbarPanelFahrzeuge);

        //Button Kunden
        JPanel navBar_panel_kunden = new JPanel();
        navBar_panel_kunden.setPreferredSize(new Dimension(180, 40));
        navBar_panel_kunden.setBackground(Color.white);
        _navbarButtonKunden = new CustomNavBarButton("Kunden", CSHelp.imageList.get("icon_kunde.png"));
        _navbarButtonKunden.getButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NavigationBar.this.fireGUIEvent(new GUIEvent(this, Commands.TAB_CHANGED, "kunden"));
            }
        });
        navBar_panel_kunden.add(_navbarButtonKunden, BorderLayout.CENTER);
        _sectionTwo.add(navBar_panel_kunden);

        //Button Standorte
        JPanel navBar_panel_standorte = new JPanel();
        navBar_panel_standorte.setPreferredSize(new Dimension(180, 40));
        navBar_panel_standorte.setBackground(Color.white);
        _navbarButtonStandorte = new CustomNavBarButton("Standorte", CSHelp.imageList.get("icon_standort.png"));
        _navbarButtonStandorte.getButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NavigationBar.this.fireGUIEvent(new GUIEvent(this, Commands.TAB_CHANGED, "standorte"));
            }
        });
        navBar_panel_standorte.add(_navbarButtonStandorte, BorderLayout.CENTER);
        _sectionTwo.add(navBar_panel_standorte);

        //Button Dokumente
        JPanel navBar_panel_dokumente = new JPanel();
        navBar_panel_dokumente.setPreferredSize(new Dimension(180, 40));
        navBar_panel_dokumente.setBackground(Color.white);
        _navbarButtonDokumente = new CustomNavBarButton("Dokumente", CSHelp.imageList.get("icon_dokument.png"));
        _navbarButtonDokumente.getButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NavigationBar.this.fireGUIEvent(new GUIEvent(this, Commands.TAB_CHANGED, "dokumente"));
            }
        });
        navBar_panel_dokumente.add(_navbarButtonDokumente, BorderLayout.CENTER);
        _sectionTwo.add(navBar_panel_dokumente);

        this.add(_sectionTwo, BorderLayout.CENTER);

        //Teilkomponente: Sektion 3
        _sectionThree = new JPanel(new BorderLayout(20,20));
        _sectionThree.setPreferredSize(new Dimension(180, 280));
        _sectionThree.setBackground(Color.white);
        _sectionThree.setBorder(borderTop);
        JLabel copyrightLabel = new JLabel();
        copyrightLabel.setText("© Max Reichmann, Leon Lantz");
        copyrightLabel.setBorder(new EmptyBorder(0,10,5,10));
        copyrightLabel.setFont(CSHelp.lato.deriveFont(11f));
        _sectionThree.add(copyrightLabel, BorderLayout.SOUTH);
        this.add(_sectionThree, BorderLayout.SOUTH);

        //Zeige zu Beginn standardmäßig Panel "Übersicht" als aktiv an
        this.setActive("übersicht");
    }

    public void setActive(String title) {
        this.clearAllButtons();
        Color navBarItemActive = CSHelp.navBarItemActive;
        switch (title) {
            case "übersicht":
                _navbarButtonÜbersicht.setBackground(navBarItemActive);
                break;
            case "buchungen":
                _navbarButtonBuchungen.setBackground(navBarItemActive);
                break;
            case "fahrzeuge":
                _navbarButtonFahrzeuge.setBackground(navBarItemActive);
                break;
            case "kunden":
                _navbarButtonKunden.setBackground(navBarItemActive);
                break;
            case "standorte":
                _navbarButtonStandorte.setBackground(navBarItemActive);
                break;
            case "dokumente":
                _navbarButtonDokumente.setBackground(navBarItemActive);
                break;
        }
    }

    private void clearAllButtons() {
        _navbarButtonÜbersicht.setBackground(Color.white);
        _navbarButtonBuchungen.setBackground(Color.white);
        _navbarButtonFahrzeuge.setBackground(Color.white);
        _navbarButtonKunden.setBackground(Color.white);
        _navbarButtonStandorte.setBackground(Color.white);
        _navbarButtonDokumente.setBackground(Color.white);
    }
}
