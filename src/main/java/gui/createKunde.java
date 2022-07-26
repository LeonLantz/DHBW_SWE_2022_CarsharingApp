package gui;

import javax.swing.*;
import java.awt.*;

public class createKunde extends JDialog {

    public createKunde() {
        initUI();
    }

    private void initUI() {
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setBackground(Color.green);
        this.setPreferredSize(new Dimension(500, 700));
        this.setLayout(new BorderLayout());
    }
}
