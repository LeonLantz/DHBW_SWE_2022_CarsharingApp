package gui.customComponents;

import de.dhbwka.swe.utils.event.EventCommand;
import de.dhbwka.swe.utils.event.GUIEvent;
import de.dhbwka.swe.utils.event.IGUIEventListener;
import de.dhbwka.swe.utils.gui.ButtonElement;
import de.dhbwka.swe.utils.gui.GUIConstants;
import de.dhbwka.swe.utils.gui.ObservableComponent;
import de.dhbwka.swe.utils.gui.SimpleListComponent;
import de.dhbwka.swe.utils.model.IDepictable;
import de.dhbwka.swe.utils.util.IPropertyManager;
import gui.customComponents.userInput.CustomNavBarButton;
import util.CSHelp;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Objects;

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

    JPanel section_1, section_2, section_3;
    CustomNavBarButton navBar_button_übersicht, navBar_button_buchungen, navBar_button_fahrzeuge, navBar_button_kunden, navBar_button_standorte, navBar_button_dokumente;


    public NavigationBar() {
    }

    public NavigationBar(String id) {
        super(id);
    }



    private void initUI() {
        Border border_right = BorderFactory.createMatteBorder(0,0,0,1, CSHelp.navBar);
        Border border_bottom = BorderFactory.createMatteBorder(0,0,1,0, CSHelp.navBar);
        Border border_top = BorderFactory.createMatteBorder(1,0,0,0, CSHelp.navBar);

        //Hauptkomponente: Navigationsbar (links)
        this.setLayout(new BorderLayout(0,0));
        this.setBackground(Color.white);
        this.setPreferredSize(new Dimension(180, 720));
        this.setBorder(border_right);

        //Teilkomponente: Sektion 1
        section_1 = new JPanel();
        section_1.setLayout(new BorderLayout(0,0));
        section_1.setBackground(Color.white);
        section_1.setPreferredSize(new Dimension(180, 130));
        section_1.setBorder(border_bottom);
        this.add(section_1, BorderLayout.NORTH);

        //Profilbild
        JPanel navBar_panel_profilbild = new JPanel(new FlowLayout(FlowLayout.LEFT));
        navBar_panel_profilbild.setBackground(Color.white);
        navBar_panel_profilbild.setPreferredSize(new Dimension(180, 90));
        JLabel imageIcon = new JLabel(CSHelp.imageList.get("gröll.png"));
        imageIcon.setBorder(new EmptyBorder(35,10,0,0));
        navBar_panel_profilbild.add(imageIcon);
        section_1.add(navBar_panel_profilbild, BorderLayout.NORTH);

        //Profilname
        JPanel navBar_panel_profilname = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel navBar_label_profilname = new JLabel("Lutz Gröll");
        navBar_panel_profilname.setBackground(Color.white);
        navBar_label_profilname.setFont(CSHelp.lato_bold.deriveFont(18f));
        navBar_label_profilname.setBorder(new EmptyBorder(0,10,0,0));
        navBar_panel_profilname.add(navBar_label_profilname);
        section_1.add(navBar_panel_profilname);

        //Teilkomponente: Sektion 2
        section_2 = new JPanel(new FlowLayout(0,0,5));
        section_2.setBackground(Color.white);
        section_2.setPreferredSize(new Dimension(180, 280));

        //Button Übersicht
        JPanel navBar_panel_übersicht = new JPanel();
        navBar_panel_übersicht.setBackground(Color.white);
        navBar_button_übersicht = new CustomNavBarButton("Übersicht", CSHelp.imageList.get("übersicht.png"));
        navBar_button_übersicht.getButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NavigationBar.this.fireGUIEvent(new GUIEvent(this, Commands.TAB_CHANGED, "übersicht"));
            }
        });
        navBar_panel_übersicht.add(navBar_button_übersicht, BorderLayout.CENTER);
        navBar_panel_übersicht.setPreferredSize(new Dimension(180, 40));
        section_2.add(navBar_panel_übersicht);

        //Button Buchungen
        JPanel navBar_panel_buchungen = new JPanel();
        navBar_panel_buchungen.setBackground(Color.white);
        navBar_button_buchungen = new CustomNavBarButton("Buchungen", CSHelp.imageList.get("buchungen.png"));
        navBar_button_buchungen.getButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NavigationBar.this.fireGUIEvent(new GUIEvent(this, Commands.TAB_CHANGED, "buchungen"));
            }
        });
        navBar_panel_buchungen.add(navBar_button_buchungen, BorderLayout.CENTER);
        navBar_panel_buchungen.setPreferredSize(new Dimension(180, 40));
        section_2.add(navBar_panel_buchungen);

        //Button Fahrzeuge
        JPanel navBar_panel_fahrzeuge = new JPanel();
        navBar_panel_fahrzeuge.setBackground(Color.white);
        navBar_button_fahrzeuge = new CustomNavBarButton("Fahrzeuge", CSHelp.imageList.get("fahrzeuge.png"));
        navBar_button_fahrzeuge.getButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NavigationBar.this.fireGUIEvent(new GUIEvent(this, Commands.TAB_CHANGED, "fahrzeuge"));
            }
        });
        navBar_panel_fahrzeuge.add(navBar_button_fahrzeuge, BorderLayout.CENTER);
        navBar_panel_fahrzeuge.setPreferredSize(new Dimension(180, 40));
        section_2.add(navBar_panel_fahrzeuge);

        //Button Kunden
        JPanel navBar_panel_kunden = new JPanel();
        navBar_panel_kunden.setBackground(Color.white);
        navBar_button_kunden = new CustomNavBarButton("Kunden", CSHelp.imageList.get("kundenIcon.png"));
        navBar_button_kunden.getButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NavigationBar.this.fireGUIEvent(new GUIEvent(this, Commands.TAB_CHANGED, "kunden"));
            }
        });
        navBar_panel_kunden.add(navBar_button_kunden, BorderLayout.CENTER);
        navBar_panel_kunden.setPreferredSize(new Dimension(180, 40));
        section_2.add(navBar_panel_kunden);

        //Button Standorte
        JPanel navBar_panel_standorte = new JPanel();
        navBar_panel_standorte.setBackground(Color.white);
        navBar_button_standorte = new CustomNavBarButton("Standorte", CSHelp.imageList.get("standorte.png"));
        navBar_button_standorte.getButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NavigationBar.this.fireGUIEvent(new GUIEvent(this, Commands.TAB_CHANGED, "standorte"));
            }
        });
        navBar_panel_standorte.add(navBar_button_standorte, BorderLayout.CENTER);
        navBar_panel_standorte.setPreferredSize(new Dimension(180, 40));
        section_2.add(navBar_panel_standorte);

        //Button Dokumente
        JPanel navBar_panel_dokumente = new JPanel();
        navBar_panel_dokumente.setBackground(Color.white);
        navBar_button_dokumente = new CustomNavBarButton("Dokumente", CSHelp.imageList.get("dokumente.png"));
        navBar_button_dokumente.getButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NavigationBar.this.fireGUIEvent(new GUIEvent(this, Commands.TAB_CHANGED, "dokumente"));
            }
        });
        navBar_panel_dokumente.add(navBar_button_dokumente, BorderLayout.CENTER);
        navBar_panel_dokumente.setPreferredSize(new Dimension(180, 40));
        section_2.add(navBar_panel_dokumente);


        this.add(section_2, BorderLayout.CENTER);

        setActive("übersicht");

        //Teilkomponente: Sektion 3
        section_3 = new JPanel();
        section_3.setBackground(Color.white);
        section_3.setPreferredSize(new Dimension(180, 280));
        section_3.setBorder(border_top);
        this.add(section_3, BorderLayout.SOUTH);

    }

    public void setActive(String title) {
        clearAllButtons();
        switch (title) {
            case "übersicht":
                navBar_button_übersicht.setBackground(CSHelp.navBarItemActive);
                break;
            case "buchungen":
                navBar_button_buchungen.setBackground(CSHelp.navBarItemActive);
                break;
            case "fahrzeuge":
                navBar_button_fahrzeuge.setBackground(CSHelp.navBarItemActive);
                break;
            case "kunden":
                navBar_button_kunden.setBackground(CSHelp.navBarItemActive);
                break;
            case "standorte":
                navBar_button_standorte.setBackground(CSHelp.navBarItemActive);
                break;
            case "dokumente":
                navBar_button_dokumente.setBackground(CSHelp.navBarItemActive);
                break;
        }
    }

    private void clearAllButtons() {
        navBar_button_übersicht.setBackground(Color.white);
        navBar_button_buchungen.setBackground(Color.white);
        navBar_button_fahrzeuge.setBackground(Color.white);
        navBar_button_kunden.setBackground(Color.white);
        navBar_button_standorte.setBackground(Color.white);
        navBar_button_dokumente.setBackground(Color.white);
    }


    public static SLCBuilder builder(String id ) {
        if( id == null || id.isEmpty() ) throw new IllegalArgumentException( "ID must be given!" );
        SLCBuilder slcBuilder = new SLCBuilder();
        slcBuilder.id = id;
        return slcBuilder;
    }

    public static final class SLCBuilder {

        private String title;
        private IPropertyManager propManager;
        private String id;

        private IGUIEventListener listener;

        private SLCBuilder() {
        }

        public SLCBuilder propManager(IPropertyManager propMgr ) {
            this.propManager = propMgr;
            return this;
        }

        public SLCBuilder title(String title ) {
            this.title = title;
            return this;
        }

        /**
         * Assign an initial {@link IGUIEventListener} as observer
         *
         * @param observer the observer
         *
         * @return the current instance for chaining
         */
        public SLCBuilder observer(IGUIEventListener observer ) {
            Objects.requireNonNull( observer, "observer must not be null!" );
            this.listener = observer;
            return this;
        }

        /**
         * Build a SimpleListComponent instance
         *
         * @return the built SimpleListComponent instance
         */
        public NavigationBar build() {
            NavigationBar nvb = new NavigationBar( this.id );
            nvb.setPropertyManager( this.propManager );
            nvb.initUI();
            if( this.listener != null ) nvb.addObserver(listener);
            return nvb;
        }
    }
}
