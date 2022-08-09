package gui;

import de.dhbwka.swe.utils.gui.ObservableComponent;

import javax.swing.*;
import java.awt.*;

public class EditIDepicatableDialog extends JDialog {

    public EditIDepicatableDialog(ObservableComponent observableComponent, Dimension dimension) {
        this.setLayout(new BorderLayout());
        this.setModal(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setSize(dimension);
        this.add(observableComponent, BorderLayout.CENTER);
    }
}
