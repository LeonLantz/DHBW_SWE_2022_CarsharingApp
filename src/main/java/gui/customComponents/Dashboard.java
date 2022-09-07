package gui.customComponents;

import de.dhbwka.swe.utils.event.GUIEvent;
import de.dhbwka.swe.utils.event.IGUIEventListener;
import de.dhbwka.swe.utils.gui.ObservableComponent;
import de.dhbwka.swe.utils.gui.SimpleTableComponent;
import de.dhbwka.swe.utils.model.Attribute;
import de.dhbwka.swe.utils.model.IDepictable;
import gui.MainComponentMitNavBar;
import gui.renderer.TableCellRendererDashboard;
import model.*;
import util.CSHelp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

public class Dashboard extends ObservableComponent {

    private JPanel _topPanel, _bottomPanel;
    private String csvDirectory;
    private IGUIEventListener _observer;

    private SimpleTableComponent _neueKundenTable;
    private String[] columnNames;
    final Vector<Vector<Attribute>> data = new Vector<>();

    private static final String sp = System.getProperty("file.separator");

    public Dashboard(IGUIEventListener observer, String csvDirectory) {
        this.addObserver(observer);
        this._observer = observer;
        this.csvDirectory = csvDirectory;
        this.initUI();
    }

    private void initUI() {
        this.setLayout(new BorderLayout(0,0));
        this.setBackground(Color.white);

        _topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0,0));
        _topPanel.setPreferredSize(new Dimension(770, 295));
        _topPanel.setBackground(CSHelp.main);
        _topPanel.add(getButtonPanel());
        _topPanel.add(getStatisticPanel());
        _topPanel.add(getCalendarPanel());
        this.add(_topPanel, BorderLayout.NORTH);

        _bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0,0));
        _bottomPanel.setPreferredSize(new Dimension(770, 295));
        _bottomPanel.setBackground(CSHelp.main);
        _bottomPanel.add(getNeueKundenPanel());
        _bottomPanel.add(getBeliebteFahrzeugePanel());
        this.add(_bottomPanel, BorderLayout.SOUTH);

    }

    private JPanel getCalendarPanel() {
        JPanel borderPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        borderPanel.setPreferredSize(new Dimension(295, 295));
        borderPanel.setBackground(CSHelp.main);

        JPanel panelMain = new JPanel(new FlowLayout(FlowLayout.LEADING,0,0));
        panelMain.setBackground(CSHelp.main);
        panelMain.setPreferredSize(new Dimension(250, 264));

        JLabel imageLabel = new JLabel(CSHelp.imageList.get("dashboard_calendar.png"));
        imageLabel.setBorder(BorderFactory.createLineBorder(CSHelp.inputFieldBorderColor, 1, true));
        //imageLabel.setBorder(new EmptyBorder(16, 23, 17 ,24));
        panelMain.add(imageLabel);

        borderPanel.add(panelMain);
        return borderPanel;
    }

    private JPanel getStatisticPanel() {
        JPanel borderPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        borderPanel.setPreferredSize(new Dimension(382, 295));

        JPanel panelMain = new JPanel(new FlowLayout(FlowLayout.LEADING,0,0));
        panelMain.setBackground(CSHelp.main);
        panelMain.setPreferredSize(new Dimension(382, 264));

        JLabel imageLabel = new JLabel(CSHelp.imageList.get("dashboard_statistic.png"));
        imageLabel.setBorder(BorderFactory.createLineBorder(CSHelp.inputFieldBorderColor, 1, true));
        //imageLabel.setBorder(new EmptyBorder(16, 23, 17 ,24));
        panelMain.add(imageLabel);

        borderPanel.add(panelMain);
        return borderPanel;
    }

    private JPanel getButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 45,12));
        buttonPanel.setPreferredSize(new Dimension(220, 295));

        JButton buttonBuchung = new JButton();
        buttonBuchung.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonBuchung.setBorder(new EmptyBorder(0,0,0,0));
        buttonBuchung.setIcon(CSHelp.imageList.get("dashboard_buchung.png"));
        buttonBuchung.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Dashboard.this.fireGUIEvent(new GUIEvent(this, NavigationBar.Commands.TAB_CHANGED, "buchungen"));
                Dashboard.this.fireGUIEvent(new GUIEvent(this, MainComponentMitNavBar.Commands.BUTTON_PRESSED, Buchung.class));
            }
        });

        JButton buttonKunde = new JButton();
        buttonKunde.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonKunde.setBorder(new EmptyBorder(0,0,0,0));
        buttonKunde.setIcon(CSHelp.imageList.get("dashboard_kunde.png"));
        buttonKunde.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Dashboard.this.fireGUIEvent(new GUIEvent(this, NavigationBar.Commands.TAB_CHANGED, "kunden"));
                Dashboard.this.fireGUIEvent(new GUIEvent(this, MainComponentMitNavBar.Commands.BUTTON_PRESSED, Kunde.class));
            }
        });

        JButton buttonFahrzeug = new JButton();
        buttonFahrzeug.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonFahrzeug.setBorder(new EmptyBorder(0,0,0,0));
        buttonFahrzeug.setIcon(CSHelp.imageList.get("dashboard_fahrzeug.png"));
        buttonFahrzeug.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Dashboard.this.fireGUIEvent(new GUIEvent(this, NavigationBar.Commands.TAB_CHANGED, "fahrzeuge"));
                Dashboard.this.fireGUIEvent(new GUIEvent(this, MainComponentMitNavBar.Commands.BUTTON_PRESSED, Fahrzeug.class));
            }
        });

        JPanel downloadPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        downloadPanel.setPreferredSize(new Dimension(146, 70));
        JButton buttonDownload = new JButton();
        buttonDownload.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonDownload.setBorder(new EmptyBorder(0,0,0,0));
        buttonDownload.setIcon(CSHelp.imageList.get("dashboard_download.png"));
        buttonDownload.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String resourceDirectory = CSHelp.getAbsoluteResourceDirectory();
                    String[] folderNames = csvDirectory.split(sp);
                    String csvDirectoryName = sp + folderNames[folderNames.length-1];
                    Desktop.getDesktop().open(new File(resourceDirectory + csvDirectoryName));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        downloadPanel.add(buttonDownload);

        buttonPanel.add(buttonBuchung);
        buttonPanel.add(buttonFahrzeug);
        buttonPanel.add(buttonKunde);
        buttonPanel.add(downloadPanel);

        return buttonPanel;
    }


    private JPanel getBeliebteFahrzeugePanel() {
        JPanel borderPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 30));
        borderPanel.setPreferredSize(new Dimension(295, 295));
        borderPanel.setBackground(CSHelp.main);

        JPanel panelMain = new JPanel(new FlowLayout(FlowLayout.LEADING,0,0));
        panelMain.setBackground(Color.white);
        panelMain.setPreferredSize(new Dimension(250, 250));
        panelMain.setBorder(BorderFactory.createLineBorder(CSHelp.inputFieldBorderColor, 1, true));

        JPanel panelTop = new JPanel(new BorderLayout(0,0));
        panelTop.setBackground(Color.white);
        panelTop.setPreferredSize(new Dimension(248, 50));

        ImageIcon icon = CSHelp.imageList.get("icon_porsche.png");
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setBorder(new EmptyBorder(2,7,0,15));
        panelTop.add(iconLabel, BorderLayout.WEST);

        JLabel headerLabel = new JLabel();
        headerLabel.setFont(CSHelp.lato.deriveFont(12f));
        headerLabel.setText("<html><body><b>Beliebte Fahrzeuge</b><br>Porsche Taycan</body></html>");
        headerLabel.setBorder(new EmptyBorder(4,0,0,0));
        panelTop.add(headerLabel, BorderLayout.CENTER);

        panelMain.add(panelTop);

        ImageIcon imageIcon = CSHelp.imageList.get("image_porscheTaycan.png");
        panelMain.add(new JLabel(imageIcon));

        JLabel label = new JLabel("<html><body>Soul electrified. Der Taycan. Ihr Einstieg in die <br>Elektromobilität.</body></html>");
        label.setBorder(new EmptyBorder(10,10,0,10));
        label.setFont(CSHelp.lato.deriveFont(10f));
        label.setForeground(CSHelp.dashboardTextColor);
        panelMain.add(label);

        JButton button = new JButton("Buchen");
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(7,10,0,10));
        button.setFont(CSHelp.lato_bold.deriveFont(11f));
        button.setForeground(CSHelp.navBarTextActive);
        panelMain.add(button);

        borderPanel.add(panelMain, BorderLayout.CENTER);

        return borderPanel;
    }

    private JPanel getNeueKundenPanel() {

        final Vector<Vector<Attribute>> data = new Vector<>();
        this.columnNames = new String[]{"Name", "zul. bearb."};

        List<IDepictable> li = new ArrayList<>();
        IDepictable m = new Kunde();
        li.add(m);

        _neueKundenTable = SimpleTableComponent.builder("STC")
                .cellRenderer(new TableCellRendererDashboard(), String.class, LocalDateTime.class)
                .data(li)
                .columnNames(columnNames)
                .selectionMode(ListSelectionModel.SINGLE_SELECTION)
                //new int[]{50, 100, 100, 170, 130, 100, 140, 35, 35}
                .columnWidths(new int[]{410, 90})
                .fixedColumns(new boolean[]{true, true})
                .observer(_observer)
                .build();

        JScrollPane p = (JScrollPane) _neueKundenTable.getComponent(0);
        p.getComponent(1).setPreferredSize(new Dimension(0,0));
        p.setBackground(CSHelp.main);
        p.setBorder(new EmptyBorder(0,0,0,0));

        _neueKundenTable.setPreferredSize(new Dimension(530, 170));
        _neueKundenTable.setBorder(new EmptyBorder(10,10,10,10));

        JPanel borderPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 18));
        borderPanel.setPreferredSize(new Dimension(580, 270));
        borderPanel.setBorder(new EmptyBorder(0,10,10,10));
        borderPanel.setBackground(CSHelp.main);

        JPanel contentPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        contentPanel.setPreferredSize(new Dimension(542, 250));
        contentPanel.setBorder(BorderFactory.createLineBorder(CSHelp.inputFieldBorderColor, 1, true));
        contentPanel.setBackground(Color.white);

        JPanel textPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        textPanel.setPreferredSize(new Dimension(560, 70));
        textPanel.setBackground(Color.white);
        JLabel neueKundenLabel = new JLabel("Neue Kunden");
        neueKundenLabel.setFont(CSHelp.lato_bold.deriveFont(17f));
        neueKundenLabel.setBorder(new EmptyBorder(22,10,10,10));
        contentPanel.add(neueKundenLabel);

        contentPanel.add(_neueKundenTable);

        borderPanel.add(contentPanel);

        return borderPanel;
    }

    class SortKunden implements Comparator<Kunde> {
        @Override
        public int compare(Kunde o1, Kunde o2) {
            LocalDateTime ldt1 = o1.getAttributeValueOf(Kunde.Attributes.LAST_EDITED);
            LocalDateTime ldt2 = o2.getAttributeValueOf(Kunde.Attributes.LAST_EDITED);
            return ldt2.compareTo(ldt1);
        }
    }

    public void setNeueKundenData(IDepictable[] modelData) {
        this.data.removeAllElements();
        Kunde[] kundeModelData = new Kunde[modelData.length];
        for (int i=0; i<kundeModelData.length; i++) {
            kundeModelData[i] = (Kunde) modelData[i];
        }

        Arrays.sort(kundeModelData, new SortKunden());
        if (kundeModelData.length > 3) {
            kundeModelData = Arrays.copyOfRange(kundeModelData, 0,3);
        }

        for (int i = 0; i < kundeModelData.length; i++) {
            Vector<Attribute> attributeVector = new Vector<Attribute>();
            List<Attribute> attributeList = kundeModelData[i].getAttributes();
            String nameString = attributeList.get(1).getValue().toString() + " " + attributeList.get(2).getValue().toString();
            Attribute name = new Attribute("Name", "", String.class, nameString, null, true, true, false, false);
            attributeVector.add(name);

            LocalDateTime localDateTime = (LocalDateTime) attributeList.get(7).getValue();
            Attribute dateTime = new Attribute("DateTime", "", LocalDateTime.class, localDateTime, null, true, true, false, false);
            attributeVector.add(dateTime);

            this.data.add(attributeVector);
        }
        this._neueKundenTable.setData(data, this.columnNames);
    }
}
