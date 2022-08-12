package gui.customComponents.userInput;

import de.dhbwka.swe.utils.event.IGUIEventListener;
import util.CSHelp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class CustomComboBox extends CustomInputField {

    private JLabel field_description;
    private JComboBox comboBox;
    private String[] comboItems;

    public CustomComboBox(String title, String placeholder, String[] comboItems, IGUIEventListener observer) {
        this.addObserver(observer);
        this.title = title;
        this.placeholder = placeholder;
        this.comboItems = comboItems;
        this.value = "";

        this.setLayout(new BorderLayout(0,0));
        this.setPreferredSize(new Dimension(200,65));
        this.setBorder(new EmptyBorder(5,10,5,10));
        this.setBackground(Color.WHITE);

        field_description = new JLabel(title);
        field_description.setFont(CSHelp.lato.deriveFont(13f));
        field_description.setBackground(Color.blue);
        field_description.setPreferredSize(new Dimension(200, 20));
        field_description.setForeground(CSHelp.inputFieldText);

        comboBox = new JComboBox<String>(comboItems) {
            @Override
            public Object getSelectedItem() {
                Object selected = super.getSelectedItem();

                if (selected == null) {
                    selected = "- Keine Auswahl";

                }
                return selected;
            }
        };
        comboBox.setSelectedIndex(-1);
        comboBox.setFont(CSHelp.lato.deriveFont(12f));
        comboBox.setPreferredSize(new Dimension(200, 40));
        comboBox.setForeground(CSHelp.inputFieldText);
        comboBox.setBorder(new EmptyBorder(7,0,0,0));

        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                value = comboBox.getSelectedItem().toString();
            }
        });

        this.add(field_description, BorderLayout.NORTH);
        this.add(comboBox, BorderLayout.CENTER);
    }

    public String getSelectedItem() {
        return comboBox.getSelectedItem().toString();
    }

    @Override
    public void setValue(String value) {
        this.comboBox.setSelectedIndex(Integer.parseInt(value));
    }
}
