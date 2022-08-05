package gui.customComponents.userInput;

import de.dhbwka.swe.utils.gui.CalendarComponent;
import util.CSHelp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.LocalDate;

//TODO: Datepicker

public class CustomDatePicker extends CustomInputField {

    private JLabel field_description;
    private JTextField textField;
    private CalendarComponent calendarComponent;

    public CustomDatePicker(String title, String placeholder) {
        this.title = title;
        this.placeholder = placeholder;

        this.setLayout(new BorderLayout(0,0));
        this.setPreferredSize(new Dimension(200,50));
        this.setBorder(new EmptyBorder(5,10,5,10));
        this.setBackground(Color.WHITE);

        field_description = new JLabel(title);
        field_description.setFont(CSHelp.lato.deriveFont(13f));
        field_description.setForeground(CSHelp.inputFieldText);

        calendarComponent = CalendarComponent.builder( "cC" )
                .date( LocalDate.now() )
                .startYear( 2015 )
                .endYear( 2025 )
                //.propertymanager( this.getPropertyManager() )
                .build();

        textField = new JTextField(placeholder);
        textField.setBorder(BorderFactory.createEmptyBorder(7,12,7,12));
        textField.setFont(CSHelp.lato.deriveFont(12f));
        textField.setForeground(CSHelp.inputFieldPlaceholder);
        textField.setBackground(CSHelp.inputFieldBackground);
        textField.setEditable(false);

        textField.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("okayyy");
                JDialog datePicker = new JDialog();


                datePicker.add(calendarComponent);
                datePicker.setModal(true);
                datePicker.setSize(new Dimension(400,500));
                datePicker.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                datePicker.setVisible(true);
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });



        this.add(field_description, BorderLayout.NORTH);
        this.add(textField, BorderLayout.SOUTH);

    }

    @Override
    public void setValue(String value) {

    }
}
