package gui.customComponents.userInput;

import gui.customComponents.userInput.CustomInputField;
import util.CSHelp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class CustomTextField extends CustomInputField {

    private JLabel field_description;
    private JTextField textfield;

    public CustomTextField(String title, String placeholder) {
        this.title = title;
        this.placeholder = placeholder;
        this.value = "";

        //InitUI
        this.setLayout(new BorderLayout(0,0));
        this.setPreferredSize(new Dimension(200,65));
        this.setBorder(new EmptyBorder(5,10,5,10));
        field_description = new JLabel(title);
        field_description.setFont(CSHelp.lato.deriveFont(13f));
        field_description.setPreferredSize(new Dimension(200, 20));
        field_description.setForeground(CSHelp.inputFieldText);
        field_description.setBorder(new EmptyBorder(5,0,7,0));


        textfield = new JTextField(placeholder);
        textfield.setBorder(BorderFactory.createEmptyBorder(7,12,7,12));
        textfield.setFont(CSHelp.lato.deriveFont(12f));
        textfield.setPreferredSize(new Dimension(200, 30));
        textfield.setForeground(CSHelp.inputFieldPlaceholder);
        textfield.setBackground(CSHelp.inputFieldBackground);
        textfield.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textfield.getText().equals(placeholder)) {
                    textfield.setText("");
                    textfield.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                value = textfield.getText();
                if (textfield.getText().isEmpty()) {
                    textfield.setForeground(CSHelp.inputFieldPlaceholder);
                    textfield.setText(placeholder);
                }
            }
        });
        this.add(field_description, BorderLayout.NORTH);

        JPanel borderPanel = new JPanel(new BorderLayout(0,0));
        borderPanel.setBorder(new LineBorder(CSHelp.inputFieldBorderColor));
        borderPanel.add(textfield);

        this.add(borderPanel, BorderLayout.SOUTH);
    }

    public JTextField getTextfield() {
        return textfield;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
        this.textfield.setForeground(Color.black);
        this.textfield.setText(value);
    }

}
