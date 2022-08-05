package gui;

import gui.customComponents.userInput.GUIFahrzeugAnlegen;

import javax.swing.*;
import java.awt.*;

public class CreateKundeView extends JDialog {

    public CreateKundeView() {
        initUI();
    }

    private void initUI() {
        this.setLayout(new BorderLayout());
        this.setModal(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setSize(new Dimension(500, 700));
    }

    public void setContent(GUIFahrzeugAnlegen ocp) {
        this.add(ocp, BorderLayout.CENTER);
    }
}
