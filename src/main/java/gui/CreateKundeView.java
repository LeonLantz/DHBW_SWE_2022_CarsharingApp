package gui;

import de.dhbwka.swe.utils.gui.ObservableComponent;
import gui.customComponents.userInput.GUIFahrzeugAnlegen;

import javax.swing.*;
import java.awt.*;

public class CreateKundeView extends JDialog {

    public CreateKundeView(ObservableComponent observableComponent) {
        this.setLayout(new BorderLayout());
        this.setModal(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setSize(new Dimension(500, 700));
        this.add(observableComponent, BorderLayout.CENTER);
    }
}
