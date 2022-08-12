package gui;

import control.CSControllerReinerObserverUndSender;
import de.dhbwka.swe.utils.event.*;
import de.dhbwka.swe.utils.gui.AttributeElement;
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
import gui.customComponents.NavigationBar;
import gui.customComponents.userInput.CustomInputField;
import gui.customComponents.userInput.CustomListField;
import gui.customComponents.userInput.CustomNavBarButton;
import model.*;
import util.CSHelp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
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
import java.util.stream.Collectors;

public class MainComponentMitNavBar extends ObservableComponent implements IGUIEventListener, IUpdateEventListener {

    public enum Commands implements EventCommand {

        BUTTON_PRESSED("MainComponentMitNavBar.button_pressed", Attribute.class),
        REMOVE_KUNDE( "MainComponentMitNavBar.remove_kunde", String.class ),
        UPDATE_IMAGES( "MainComponentMitNavBar.update_images", IDepictable.class );

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

    private ObservableComponent dialogWindow;

    private IDepictable currentObject;

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

    private JPanel createÜbersichtTab() {
        JPanel übersicht_Tab = new JPanel(new BorderLayout(0,0));

        //Kopfzeile
        JPanel header = new JPanel(new BorderLayout(0,0));
        header.setPreferredSize(new Dimension(900, 130));
        header.setBorder(BorderFactory.createMatteBorder(0,0,1,0, CSHelp.tableDividerColor));

        //HeaderCenter
        JPanel header_center = new JPanel(new FlowLayout(FlowLayout.CENTER, 60, 10));
        header_center.setPreferredSize(new Dimension(770, 130));
        header_center.setBackground(CSHelp.main);
        header.add(header_center, BorderLayout.CENTER);

        //HeaderCenterKacheln
        header_center.add(new HeaderTile("Kunden", CSHelp.imageList.get("kundenIcon.png"), 72));
        header_center.add(new HeaderTile("Buchungen", CSHelp.imageList.get("buchungen.png"), 31));
        header_center.add(new HeaderTile("Fahrzeuge", CSHelp.imageList.get("fahrzeuge.png"), 14));
        header_center.add(new HeaderTile("Standorte", CSHelp.imageList.get("standorte.png"), 5));

        //HeaderEast
        JPanel header_east = new JPanel(new BorderLayout(0,0));
        header_east.setBackground(CSHelp.main);
        header_east.setPreferredSize(new Dimension(130, 130));
        header_east.add(new JLabel(CSHelp.imageList.get("logo.png")));
        header_east.setBorder(BorderFactory.createMatteBorder(0,1,0,0, CSHelp.tableDividerColor));
        header.add(header_east, BorderLayout.EAST);

        übersicht_Tab.add(header, BorderLayout.NORTH);

        //Inhalt
        JPanel übersichtContent = new JPanel(new BorderLayout(0,0));
        übersichtContent.setPreferredSize(new Dimension(770, 590));

        //ContentTop
        JPanel contentTop = new JPanel();
        contentTop.setPreferredSize(new Dimension(720, 282));
        contentTop.setBackground(Color.white);
        übersichtContent.add(contentTop, BorderLayout.NORTH);

        //ContentBottom
        JPanel contentBottom = new JPanel(new BorderLayout(0,0));
        contentBottom.setPreferredSize(new Dimension(720, 308));
        contentBottom.setBackground(Color.black);
        übersichtContent.add(contentBottom, BorderLayout.SOUTH);

        //ContentBottomEast
        JPanel contentBottomEast = new JPanel(new BorderLayout(0,0));
        contentBottomEast.setPreferredSize(new Dimension(300, 308));
        contentBottomEast.setBackground(CSHelp.main);

        //ContentBottomEastLabel
        JLabel contentBottomEastLabel = new JLabel();
        contentBottomEastLabel.setIcon(CSHelp.imageList.get("porsche_buchen.png"));
        contentBottomEastLabel.setBorder(new EmptyBorder(25,0,0,0));
        contentBottomEast.add(contentBottomEastLabel, BorderLayout.CENTER);

        //ContentBottomEastButton
        JButton contentBottomEastButton = new JButton("Buchen");
        contentBottomEastButton.setBorder(new EmptyBorder(0,15,5,0));
        contentBottomEastButton.setFont(CSHelp.lato_bold.deriveFont(12f));
        contentBottomEastButton.setForeground(CSHelp.navBarTextActive);
        contentBottomEastButton.setHorizontalAlignment(SwingConstants.LEFT);
        contentBottomEastButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //TODO: Neue Buchung für Fahrzeug "Porsche Taycan" anlegen
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
        contentBottomEast.add(contentBottomEastButton, BorderLayout.SOUTH);

        contentBottom.add(contentBottomEast, BorderLayout.EAST);

        //ContentBottomCenter
        JPanel contentBottomCenter = new JPanel(new BorderLayout(0,0));
        contentBottomCenter.setBackground(Color.darkGray);
        contentBottom.add(contentBottomCenter, BorderLayout.CENTER);



        übersicht_Tab.add(übersichtContent, BorderLayout.CENTER);

        return übersicht_Tab;
    }

    public class HeaderTile extends JPanel {

        JPanel pSymbol;
        JLabel lCount, lTitle;

        public HeaderTile(String title, ImageIcon imageIcon, int count) {
            this.setLayout(new BorderLayout(0,0));
            this.setPreferredSize(new Dimension(110, 110));
            this.setBorder(new LineBorder(CSHelp.navBar));
            this.setBackground(Color.WHITE);

            //Icon
            pSymbol = new JPanel();
            pSymbol.setBorder(new EmptyBorder(10,0,0,0));
            pSymbol.setBackground(Color.white);
            pSymbol.setPreferredSize(new Dimension(130, 50));
            pSymbol.add(new JLabel(imageIcon), BorderLayout.CENTER);
            this.add(pSymbol, BorderLayout.NORTH);

            //Count
            lCount = new JLabel();
            lCount.setPreferredSize(new Dimension(110, 25));
            lCount.setHorizontalAlignment(JLabel.CENTER);
            lCount.setVerticalAlignment(JLabel.NORTH);
            lCount.setFont(CSHelp.lato_bold.deriveFont(16f));
            lCount.setForeground(CSHelp.tileCountColor);
            this.add(lCount, BorderLayout.CENTER);

            //Title
            lTitle = new JLabel(title);
            lTitle.setPreferredSize(new Dimension(110, 25));
            lTitle.setHorizontalAlignment(JLabel.CENTER);
            lTitle.setVerticalAlignment(JLabel.NORTH);
            lTitle.setFont(CSHelp.lato_bold.deriveFont(10f));
            lTitle.setForeground(CSHelp.tileCountColor);
            this.add(lTitle, BorderLayout.SOUTH);

            updateCount(count);
        }

        public void updateCount(int count) {
            lCount.setText(String.valueOf(count));
        }
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
        //System.out.println(ge.getData());

        if (ge.getData() == null) {
            return;
        }

        if(ge.getCmdText().equals(NavigationBar.Commands.TAB_CHANGED.cmdText)) {
            nvb.setActive((String)ge.getData());
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
        } else if (ge.getCmdText().equals(CustomTableComponent.Commands.DELETE_ROW.cmdText)) {
            int n = JOptionPane.showConfirmDialog(this, "Wollen Sie das Objekt wirklich löschen?", "Bestätigung", JOptionPane.YES_NO_OPTION, 1, CSHelp.imageList.get("profile_picture.png"));
            if(n == 0) {
                fireGUIEvent(ge);
            }
        } else if (ge.getCmdText().equals(CustomTableComponent.Commands.EDIT_ROW.cmdText)) {
            currentObject = (IDepictable)ge.getData();
            System.out.println(currentObject);

            if (currentObject.getClass() == Kunde.class) {
                dialogWindow = new GUIKundeAnlegen(this, currentObject);
                fireGUIEvent(new GUIEvent(this, Commands.UPDATE_IMAGES, currentObject));
                CSHelp.createJDialog(dialogWindow, new Dimension(500, 400));
            }else if (currentObject.getClass() == Fahrzeug.class) {
                dialogWindow = new GUIFahrzeugAnlegen(this, currentObject);
                fireGUIEvent(new GUIEvent(this, Commands.UPDATE_IMAGES, currentObject));
                CSHelp.createJDialog(dialogWindow, new Dimension(500, 700));
            }
        } else if (ge.getCmd().equals(CustomListField.Commands.ADD_BILD)) {
            fireGUIEvent(ge);
        } else if (ge.getCmd().equals(SimpleListComponent.Commands.ELEMENT_SELECTED)) {
            if (ge.getData().getClass() == Bild.class) {
                Bild bild = (Bild) ge.getData();

                ImageIcon imageIcon = bild.getImage();
                String[] options = new String[]{"Schließen", "Löschen"};
                int answer = JOptionPane.showOptionDialog(null, "", "Bildname: " + bild.getAttributeValueOf(Bild.Attributes.TITLE), JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, imageIcon, options, options[0]);
                if (answer == 0) {
                    //TODO: Clear list selection
                    if(currentObject.getClass() == Kunde.class) {
                        ((GUIKundeAnlegen)dialogWindow).getBildList().getSlc().clearSelection();
                    }else if (currentObject.getClass() == Fahrzeug.class) {
                        ((GUIFahrzeugAnlegen)dialogWindow).getBildList().getSlc().clearSelection();
                    }
                } else if (answer == 1) {
                    System.out.println("Löschen");
                    fireGUIEvent(new GUIEvent(this, CSControllerReinerObserverUndSender.Commands.DELETE_BILD, bild));
                }
            }
        } else if (ge.getCmd().equals(GUIFahrzeugAnlegen.Commands.ADD_FAHRZEUG)) {
            fireGUIEvent(ge);
        }
    }

    @Override
    public void processUpdateEvent(UpdateEvent ue) {

        System.out.println("UPDATE EVENT TRIGGERED");

        if( ue.getCmd() == CSControllerReinerObserverUndSender.Commands.SET_KUNDEN ) {
            List<Kunde> lstKunde = (List<Kunde>)ue.getData();
            IDepictable[] modelData = new IDepictable[lstKunde.size()];
            lstKunde.toArray(modelData);
            kundenTable.setModelData(modelData);
        } else if ( ue.getCmd() == CSControllerReinerObserverUndSender.Commands.SET_FAHRZEUGE ) {
            List<Fahrzeug> lstFahrzeug = (List<Fahrzeug>)ue.getData();
            IDepictable[] modelData = new IDepictable[lstFahrzeug.size()];
            lstFahrzeug.toArray(modelData);
            fahrzeugeTable.setModelData(modelData);
        } else if ( ue.getCmd() == CSControllerReinerObserverUndSender.Commands.SET_BILDER ) {
            List<IPersistable> allImages = (List<IPersistable>) ue.getData();
            List<Bild> bildList = new ArrayList<>();
            for (IPersistable iPersistable : allImages) {
                bildList.add((Bild) iPersistable);
            }
            String primaryKey = currentObject.getElementID();

            System.out.println(primaryKey);
            System.out.println("----------------");
            System.out.println(bildList);

            bildList = bildList.stream()
                    .filter(b -> b.getSecondaryKey().equals(primaryKey))
                    .collect(Collectors.toList());

            if (currentObject.getClass() == Kunde.class) {

            }else if (currentObject.getClass() == Fahrzeug.class) {
                ((GUIFahrzeugAnlegen)dialogWindow).updateBildList(bildList);
                System.out.println(bildList);
            }
        }

    }
}
