package gui.customComponents.userInput;

import util.CSHelp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class CustomNavBarButton extends JPanel {

    private JLabel imageLabel;
    private JButton button;

    public CustomNavBarButton(String title, ImageIcon imageIcon) {
        this.setPreferredSize(new Dimension(160, 40));
        this.setBackground(Color.white);

        this.button = new JButton(title);
        this.button.setFont(CSHelp.lato_bold.deriveFont(11.5f));
        this.button.setBorder(new EmptyBorder(0,18,0,0));
        this.button.setContentAreaFilled(false);
        this.button.setBorderPainted(false);
        this.button.setFocusPainted(false);
        this.button.setOpaque(false);
        this.button.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {}

            @Override
            public void mousePressed(MouseEvent e) {}

            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {
                CustomNavBarButton.this.button.setForeground(CSHelp.navBarTextActive);
                CustomNavBarButton.this.button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                CustomNavBarButton.this.button.setForeground(Color.BLACK);
                CustomNavBarButton.this.button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.add(new JLabel(imageIcon));
        this.add(button);

    }

    public JButton getButton() {
        return button;
    }
}
