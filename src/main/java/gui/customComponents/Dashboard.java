package gui.customComponents;

import de.dhbwka.swe.utils.event.IGUIEventListener;
import de.dhbwka.swe.utils.gui.ObservableComponent;
import util.CSHelp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Dashboard extends ObservableComponent {

    private JPanel _topPanel, _bottomPanel;

    public Dashboard(IGUIEventListener observer) {
        this.addObserver(observer);
        this.initUI();
    }

    private void initUI() {
        this.setLayout(new BorderLayout(0,0));
        this.setBackground(Color.white);

        _topPanel = new JPanel(new BorderLayout(0,0));
        _topPanel.setPreferredSize(new Dimension(770, 295));
        //_topPanel.setBackground(Color.red);
        this.add(_topPanel, BorderLayout.NORTH);

        _bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0,0));
        _bottomPanel.setPreferredSize(new Dimension(770, 295));
        _bottomPanel.setBackground(CSHelp.main);
        _bottomPanel.add(getBeliebteFahrzeugePanel(), BorderLayout.EAST);
        this.add(_bottomPanel, BorderLayout.SOUTH);

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
}
