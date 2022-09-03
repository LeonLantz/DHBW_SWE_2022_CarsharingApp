package gui;

import control.CSControllerReinerObserverUndSender;
import de.dhbwka.swe.utils.event.*;
import de.dhbwka.swe.utils.gui.ObservableComponent;
import de.dhbwka.swe.utils.gui.SimpleListComponent;
import de.dhbwka.swe.utils.model.Attribute;
import de.dhbwka.swe.utils.model.IDepictable;
import de.dhbwka.swe.utils.model.IPersistable;
import de.dhbwka.swe.utils.util.AppLogger;
import de.dhbwka.swe.utils.util.IAppLogger;
import de.dhbwka.swe.utils.util.IPropertyManager;
import de.dhbwka.swe.utils.util.PropertyManager;
import gui.customComponents.ContentPanel;
import gui.customComponents.CustomTableComponent;
import gui.customComponents.Dashboard;
import gui.customComponents.NavigationBar;
import model.*;
import util.CSHelp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainComponentMitNavBar extends ObservableComponent implements IGUIEventListener, IUpdateEventListener {
    public enum Commands implements EventCommand {
        BUTTON_PRESSED("MainComponentMitNavBar.button_pressed", Class.class);

        public final Class<?> payloadType;
        public final String cmdText;

        Commands(String cmdText, Class<?> payloadType) {
            this.cmdText = cmdText;
            this.payloadType = payloadType;
        }

        @Override
        public String getCmdText() {
            return this.cmdText;
        }

        @Override
        public Class<?> getPayloadType() {
            return this.payloadType;
        }
    }

    private CardLayout _cardLayout;
    private JPanel _contentPanel;
    private String csvDirectory;
    private IPropertyManager _propManager = null;
    private Dashboard _dashboard;

    private final IAppLogger _logger = AppLogger.getInstance();

    public final static String NAVIGATION_BAR = "NavigationBar";
    public final static String CONTENT_PANEL_ÜBERSICHT = "ContentPanel-Übersicht";
    public final static String CONTENT_PANEL_BUCHUNGEN = "ContentPanel-Buchungen";
    public final static String CONTENT_PANEL_FAHRZEUGE = "ContentPanel-Fahrzeuge";
    public final static String CONTENT_PANEL_KUNDEN = "ContentPanel-Kunden";
    public final static String CONTENT_PANEL_STANDORTE = "ContentPanel-Standorte";
    public final static String CONTENT_PANEL_DOKUMENTE = "ContentPanel-Dokumente";

    private NavigationBar _navigationBar = null;
    private JPanel _übersichtPanel = null;
    private ContentPanel _buchungenPanel = null;
    private ContentPanel _fahrzeugePanel = null;
    private ContentPanel _kundenPanel = null;
    private ContentPanel _standortePanel = null;
    private ContentPanel _dokumentePanel = null;

    private CustomTableComponent _buchungenTable;
    private CustomTableComponent _fahrzeugeTable;
    private CustomTableComponent _kundenTable;
    private CustomTableComponent _standorteTable;
    private CustomTableComponent _dokumenteTable;

    private HeaderTile _tileKunden, _tileBuchungen, _tileFahrzeuge, _tileStandorte;

    public MainComponentMitNavBar(PropertyManager propertyManager, String csvDirectory) throws Exception {
        this._propManager = propertyManager;
        this.csvDirectory = csvDirectory;
        CSHelp.init();
        this.initUI();
    }

    private void initUI() {
        this.setLayout(new BorderLayout(0, 0));

        _cardLayout = new CardLayout();
        _contentPanel = new JPanel();
        _contentPanel.setLayout(_cardLayout);
        _contentPanel.add(createÜbersichtTab(), "übersicht");
        _contentPanel.add(createBuchungenTab("Buchungen"), "buchungen");
        _contentPanel.add(createFahrzeugeTab("Fahrzeuge"), "fahrzeuge");
        _contentPanel.add(createKundenTab("Kunden"), "kunden");
        _contentPanel.add(createStandorteTab("Standorte"), "standorte");
        _contentPanel.add(createDokumenteTab("Dokumente"), "dokumente");

        this.add(createNavBar(), BorderLayout.WEST);
        this.add(_contentPanel, BorderLayout.CENTER);
    }

    private NavigationBar createNavBar() {
        this._navigationBar = new NavigationBar(this);
        return this._navigationBar;
    }

    private ContentPanel createBuchungenTab(String title) {
        _buchungenTable = CustomTableComponent.builder(title + "-Table")
                .observer(this)
                .propManager(this._propManager)
                .columnWidths(new int[]{35, 120, 100, 120, 90, 90, 140, 99, 33, 33})
                .modelClass(Buchung.class)
                .build();

        _buchungenPanel = ContentPanel.builder(CONTENT_PANEL_BUCHUNGEN)
                .title(title)
                .table(_buchungenTable)
                .addButton(new NewObjectButton(CSHelp.imageList.get("button_neueBuchungAnlegen.png"), Buchung.class))
                .observer(this)
                .propManager(this._propManager)
                .build();

        return _buchungenPanel;
    }

    private ContentPanel createFahrzeugeTab(String title) {
        _fahrzeugeTable = CustomTableComponent.builder(title + "-Table")
                .observer(this)
                .propManager(this._propManager)
                .columnWidths(new int[]{35, 150, 100, 65, 50, 80, 115, 100, 99, 33, 33})
                .modelClass(Fahrzeug.class)
                .build();

        _fahrzeugePanel = ContentPanel.builder(CONTENT_PANEL_FAHRZEUGE)
                .title(title)
                .addButton(new NewObjectButton(CSHelp.imageList.get("button_neuesFahrzeugAnlegen.png"), Fahrzeug.class))
                .observer(this)
                .table(_fahrzeugeTable)
                .propManager(this._propManager)
                .build();

        return _fahrzeugePanel;
    }

    private ContentPanel createKundenTab(String title) {
        _kundenTable = CustomTableComponent.builder(title + "-Table")
                .observer(this)
                .propManager(this._propManager)
                .columnWidths(new int[]{35, 100, 100, 180, 150, 130, 99, 33, 33})
                .modelClass(Kunde.class)
                .build();

        _kundenPanel = ContentPanel.builder(CONTENT_PANEL_KUNDEN)
                .title(title)
                .table(_kundenTable)
                .addButton(new NewObjectButton(CSHelp.imageList.get("button_neuenKundenAnlegen.png"), Kunde.class))
                .observer(this)
                .propManager(this._propManager)
                .build();

        return _kundenPanel;
    }

    private ContentPanel createStandorteTab(String title) {
        _standorteTable = CustomTableComponent.builder(title + "-Table")
                .observer(this)
                .propManager(this._propManager)
                .columnWidths(new int[]{35, 60, 100, 140, 220, 80, 60, 99, 33, 33})
                .modelClass(Standort.class)
                .build();

        _standortePanel = ContentPanel.builder(CONTENT_PANEL_STANDORTE)
                .title(title)
                .table(_standorteTable)
                //new NewObjectButton(CSHelp.imageList.get("button_neuenStandortAnlegen.png"), Standort.class)
                .addButton(null)
                .observer(this)
                .propManager(this._propManager)
                .build();

        return _standortePanel;
    }

    private ContentPanel createDokumenteTab(String title) {
        _dokumenteTable = CustomTableComponent.builder(title + "-Table")
                .observer(this)
                .propManager(this._propManager)
                .columnWidths(new int[]{50, 200, 420, 124, 33, 33})
                .modelClass(Dokument.class)
                .build();

        _dokumentePanel = ContentPanel.builder(CONTENT_PANEL_DOKUMENTE)
                .title(title)
                .observer(this)
                .table(_dokumenteTable)
                .addButton(null)
                .propManager(this._propManager)
                .build();

        return _dokumentePanel;
    }

    private JPanel createÜbersichtTab() {
        _übersichtPanel = new JPanel(new BorderLayout(0, 0));

        //Kopfzeile
        JPanel header = new JPanel(new BorderLayout(0, 0));
        header.setPreferredSize(new Dimension(900, 130));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, CSHelp.tableDividerColor));

        //HeaderCenter
        JPanel headerCenter = new JPanel(new FlowLayout(FlowLayout.CENTER, 60, 10));
        headerCenter.setPreferredSize(new Dimension(770, 130));
        headerCenter.setBackground(CSHelp.main);
        header.add(headerCenter, BorderLayout.CENTER);

        //HeaderCenterKacheln
        _tileBuchungen = new HeaderTile("Buchungen", CSHelp.imageList.get("icon_buchung.png"), 0);
        _tileFahrzeuge = new HeaderTile(" Fahrzeuge", CSHelp.imageList.get("icon_fahrzeug.png"), 0);
        _tileKunden = new HeaderTile("Kunden", CSHelp.imageList.get("icon_kunde.png"), 0);
        _tileStandorte = new HeaderTile("Standorte", CSHelp.imageList.get("icon_standort.png"), 0);
        headerCenter.add(_tileBuchungen);
        headerCenter.add(_tileFahrzeuge);
        headerCenter.add(_tileKunden);
        headerCenter.add(_tileStandorte);

        //HeaderEast
        JPanel headerEast = new JPanel(new BorderLayout(0, 0));
        headerEast.setBackground(CSHelp.main);
        headerEast.setPreferredSize(new Dimension(130, 130));
        headerEast.add(new JLabel(CSHelp.imageList.get("image_logo.png")));
        headerEast.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, CSHelp.tableDividerColor));
        header.add(headerEast, BorderLayout.EAST);

        _dashboard = new Dashboard(this, csvDirectory);

        _übersichtPanel.add(header, BorderLayout.NORTH);
        _übersichtPanel.add(_dashboard, BorderLayout.CENTER);

//        //Inhalt
//        JPanel content = new JPanel(new BorderLayout(0, 0));
//        content.setPreferredSize(new Dimension(770, 590));

//        //ContentTop
//        JPanel contentTop = new JPanel(new BorderLayout(0, 0));
//        contentTop.setPreferredSize(new Dimension(720, 282));
//        contentTop.setBackground(CSHelp.main);
//        content.add(contentTop, BorderLayout.NORTH);
//
//        //ContentBottom
//        JPanel contentBottom = new JPanel(new BorderLayout(0, 0));
//        contentBottom.setPreferredSize(new Dimension(720, 308));
//        contentBottom.setBackground(Color.black);
//        content.add(contentBottom, BorderLayout.SOUTH);
//
//        //ContentBottomEast
//        JPanel contentBottomEast = new JPanel(new BorderLayout(0, 0));
//        contentBottomEast.setPreferredSize(new Dimension(300, 308));
//        contentBottomEast.setBackground(CSHelp.main);
//
//        //ContentBottomEastLabel
//        JLabel contentBottomEastLabel = new JLabel();
//        contentBottomEastLabel.setIcon(CSHelp.imageList.get("image_porscheBuchen.png"));
//        contentBottomEastLabel.setBorder(new EmptyBorder(25, 0, 0, 0));
//        contentBottomEast.add(contentBottomEastLabel, BorderLayout.CENTER);
//
//        //ContentBottomEastButton
//        JButton contentBottomEastButton = new JButton("Buchen");
//        contentBottomEastButton.setBorder(new EmptyBorder(0, 15, 5, 0));
//        contentBottomEastButton.setFont(CSHelp.lato_bold.deriveFont(12f));
//        contentBottomEastButton.setForeground(CSHelp.navBarTextActive);
//        contentBottomEastButton.setHorizontalAlignment(SwingConstants.LEFT);
//        contentBottomEastButton.addMouseListener(new MouseListener() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                //TODO: Neue Buchung für Fahrzeug "Porsche Taycan" anlegen
//            }
//
//            @Override
//            public void mousePressed(MouseEvent e) {
//            }
//
//            @Override
//            public void mouseReleased(MouseEvent e) {
//            }
//
//            @Override
//            public void mouseEntered(MouseEvent e) {
//                MainComponentMitNavBar.this.setCursor(new Cursor(Cursor.HAND_CURSOR));
//            }
//
//            @Override
//            public void mouseExited(MouseEvent e) {
//                MainComponentMitNavBar.this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
//            }
//        });
//        contentBottomEast.add(contentBottomEastButton, BorderLayout.SOUTH);
//        contentBottom.add(contentBottomEast, BorderLayout.EAST);
//
//        //ContentBottomCenter
//        JPanel contentBottomCenter = new JPanel(new BorderLayout(0, 0));
//        contentBottomCenter.setBackground(CSHelp.main);
//        contentBottom.add(contentBottomCenter, BorderLayout.CENTER);
//

        return _übersichtPanel;
    }

    private class HeaderTile extends JPanel {
        JPanel _tileSymbol;
        JLabel _countLabel, _titleLabel;

        public HeaderTile(String title, ImageIcon imageIcon, int count) {
            this.setLayout(new BorderLayout(0, 0));
            this.setPreferredSize(new Dimension(110, 110));
            this.setBackground(Color.WHITE);
            this.setBorder(new LineBorder(CSHelp.navBar));

            //tileSymbol (Icon)
            _tileSymbol = new JPanel();
            _tileSymbol.setPreferredSize(new Dimension(130, 50));
            _tileSymbol.setBorder(new EmptyBorder(10, 0, 0, 0));
            _tileSymbol.setBackground(Color.white);
            _tileSymbol.add(new JLabel(imageIcon), BorderLayout.CENTER);
            this.add(_tileSymbol, BorderLayout.NORTH);

            //Count
            _countLabel = new JLabel();
            _countLabel.setPreferredSize(new Dimension(110, 25));
            _countLabel.setFont(CSHelp.lato_bold.deriveFont(16f));
            _countLabel.setHorizontalAlignment(JLabel.CENTER);
            _countLabel.setVerticalAlignment(JLabel.NORTH);
            _countLabel.setForeground(CSHelp.tileCountColor);
            this.add(_countLabel, BorderLayout.CENTER);

            //Title
            _titleLabel = new JLabel(title);
            _titleLabel.setPreferredSize(new Dimension(110, 25));
            _titleLabel.setFont(CSHelp.lato_bold.deriveFont(10f));
            _titleLabel.setHorizontalAlignment(JLabel.CENTER);
            _titleLabel.setVerticalAlignment(JLabel.NORTH);
            _titleLabel.setForeground(CSHelp.tileCountColor);
            this.add(_titleLabel, BorderLayout.SOUTH);

            this.updateCount(count);
        }

        public void updateCount(int count) {
            _countLabel.setText(String.valueOf(count));
        }
    }

    public class NewObjectButton extends JButton {

        public NewObjectButton(ImageIcon imageIcon, Class modelClass) {
            this.setPreferredSize(new Dimension(300, 54));
            this.setBorder(new EmptyBorder(0, 0, 0, 0));
            this.setIcon(imageIcon);

            this.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    MainComponentMitNavBar.this.fireGUIEvent(new GUIEvent(this, MainComponentMitNavBar.Commands.BUTTON_PRESSED, modelClass));
                }

                @Override
                public void mousePressed(MouseEvent e) {
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                }

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

        if (ge.getData() == null) {
            return;
        }
        // Nutzer wählt einen Button in der NavigationBar aus
        else if (ge.getCmdText().equals(NavigationBar.Commands.TAB_CHANGED.cmdText)) {
            _navigationBar.setActive((String) ge.getData());
            MainComponentMitNavBar.this._cardLayout.show(MainComponentMitNavBar.this._contentPanel, (String) ge.getData());
        }
        // Button zum Löschen eines Tabelleneintrags (Entity) wird gedrückt
        else if (ge.getCmdText().equals(CustomTableComponent.Commands.DELETE_ENTITY.cmdText)) {
            Class currentClass = ge.getData().getClass();
            String message = "";
            ImageIcon imageIcon = null;
            if (currentClass == Buchung.class) {
                message = "<html> Wollen Sie die Buchung<br><b>" + ge.getData() +  "</b><br>wirklich löschen?</html>";
                imageIcon = CSHelp.imageList.get("icon_buchung.png");
            } else if (currentClass == Fahrzeug.class) {
                message = "<html> Wollen Sie das Fahrzeug<br><b>" + ge.getData() +  "</b><br>wirklich löschen?</html>";
                imageIcon = CSHelp.imageList.get("icon_fahrzeug.png");
            } else if (currentClass == Kunde.class) {
                message = "<html> Wollen Sie den Kunden<br><b>" + ge.getData() +  "</b><br>wirklich löschen?</html>";
                imageIcon = CSHelp.imageList.get("icon_kunde.png");
            } else if (currentClass == Standort.class) {
                message = "<html> Wollen Sie den Standort<br><b>" + ge.getData() +  "</b><br>wirklich löschen?</html>";
                imageIcon = CSHelp.imageList.get("icon_standort.png");
            } else if (currentClass == Dokument.class) {
                message = "<html> Wollen Sie das Dokument<br><b>" + ge.getData() +  "</b><br>wirklich löschen?</html>";
                imageIcon = CSHelp.imageList.get("icon_dokument.png");
            }
            int answer = JOptionPane.showConfirmDialog(this, new JLabel(message), "Bestätigung", JOptionPane.YES_NO_OPTION, 1, imageIcon);
            if (answer == 0) {
                fireGUIEvent(ge);
            }
        }
        // Sonstiges Event wird an Controller weitergeleitet
        else {
            fireGUIEvent(ge);
        }
    }

    @Override
    public void processUpdateEvent(UpdateEvent ue) {
        this._logger.info("UPDATE EVENT TRIGGERED: " + ue.getCmd());

        if (ue.getCmd() == CSControllerReinerObserverUndSender.Commands.SET_KUNDEN) {
            List<Kunde> lstKunde = (List<Kunde>) ue.getData();
            if (lstKunde.isEmpty()) {
                lstKunde.add(new Kunde());
            }
            IDepictable[] modelData = new IDepictable[lstKunde.size()];
            lstKunde.toArray(modelData);
            _kundenTable.setModelData(modelData);

            _dashboard.setNeueKundenData(modelData);

        } else if (ue.getCmd() == CSControllerReinerObserverUndSender.Commands.SET_BUCHUNGEN) {
            List<Buchung> lstBuchung = (List<Buchung>) ue.getData();
            if (lstBuchung.isEmpty()) {
                lstBuchung.add(new Buchung());
            }
            IDepictable[] modelData = new IDepictable[lstBuchung.size()];
            lstBuchung.toArray(modelData);
            _buchungenTable.setModelData(modelData);
        } else if (ue.getCmd() == CSControllerReinerObserverUndSender.Commands.SET_FAHRZEUGE) {
            List<Fahrzeug> lstFahrzeug = (List<Fahrzeug>) ue.getData();
            if (lstFahrzeug.isEmpty()) {
                lstFahrzeug.add(new Fahrzeug());
            }
            IDepictable[] modelData = new IDepictable[lstFahrzeug.size()];
            lstFahrzeug.toArray(modelData);
            _fahrzeugeTable.setModelData(modelData);
        } else if (ue.getCmd() == CSControllerReinerObserverUndSender.Commands.SET_STANDORTE) {
            List<Standort> lstStandort = (List<Standort>) ue.getData();
            if (lstStandort.isEmpty()) {
                lstStandort.add(new Standort());
            }
            IDepictable[] modelData = new IDepictable[lstStandort.size()];
            lstStandort.toArray(modelData);
            _standorteTable.setModelData(modelData);
        } else if (ue.getCmd() == CSControllerReinerObserverUndSender.Commands.SET_DOKUMENTE) {
            List<Dokument> lstDokumente = (List<Dokument>) ue.getData();
            if (lstDokumente.isEmpty()) {
                lstDokumente.add(new Dokument());
            }
            IDepictable[] modelData = new IDepictable[lstDokumente.size()];
            lstDokumente.toArray(modelData);
            _dokumenteTable.setModelData(modelData);
        } else if (ue.getCmd() == CSControllerReinerObserverUndSender.Commands.SET_STATISTICS) {
            Integer[] counts = (Integer[]) ue.getData();
            _tileKunden.updateCount(counts[0]);
            _tileBuchungen.updateCount(counts[1]);
            _tileFahrzeuge.updateCount(counts[2]);
            _tileStandorte.updateCount(counts[3]);
        }
    }
}
