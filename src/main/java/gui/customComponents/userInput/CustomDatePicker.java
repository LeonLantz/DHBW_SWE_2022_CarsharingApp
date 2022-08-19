package gui.customComponents.userInput;

import de.dhbwka.swe.utils.app.CalendarComponentApp;
import de.dhbwka.swe.utils.event.IGUIEventListener;
import de.dhbwka.swe.utils.gui.AttributeComponent;
import de.dhbwka.swe.utils.gui.CalendarComponent;
import de.dhbwka.swe.utils.util.IPropertyManager;
import de.dhbwka.swe.utils.util.PropertyManager;
import util.CSHelp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;

//TODO: Datepicker

public class CustomDatePicker extends CustomInputField {

    private JLabel field_description;
    private JTextField textField;
    private CalendarComponent calendarComponent;
    private IGUIEventListener observer;
    private JDialog dialog;

    public CustomDatePicker(String title, String placeholder, IGUIEventListener observer) {
        this.title = title;
        this.observer = observer;

        this.setLayout(new BorderLayout(0,0));
        this.setPreferredSize(new Dimension(200,65));
        this.setBorder(new EmptyBorder(5,10,5,10));
        this.setBackground(Color.WHITE);

        field_description = new JLabel(title);
        field_description.setFont(CSHelp.lato.deriveFont(13f));
        field_description.setForeground(CSHelp.inputFieldText);



        this.calendarComponent = createCalendarComponent(this.title, null);

        dialog = new JDialog();
        dialog.setModal(true);
        dialog.setSize(new Dimension(400,500));
        dialog.setResizable(false);
        dialog.add(calendarComponent);

        textField = new JTextField(placeholder);
        textField.setBorder(BorderFactory.createEmptyBorder(7,12,7,12));
        textField.setFont(CSHelp.lato.deriveFont(12f));
        textField.setPreferredSize(new Dimension(200, 30));
        textField.setForeground(CSHelp.inputFieldPlaceholder);
        textField.setBackground(CSHelp.inputFieldBackground);

        textField.setEditable(false);

        textField.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                CustomDatePicker.this.dialog.setVisible(true);
            }

            @Override
            public void mousePressed(MouseEvent e) {}

            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}
        });

        JPanel borderPanel = new JPanel(new BorderLayout(0,0));
        borderPanel.setBorder(new LineBorder(CSHelp.inputFieldBorderColor));
        borderPanel.add(field_description, BorderLayout.NORTH);
        borderPanel.add(textField, BorderLayout.CENTER);

        this.add(borderPanel, BorderLayout.SOUTH);
    }

    public CalendarComponent createCalendarComponent( String id, IPropertyManager propManager ) {

        CalendarComponent cc = CalendarComponent.builder( id )
//					.date( LocalDate.of( 2019, 1, 31 ) )
                .date( LocalDate.now() )
                .observer(this.observer)
                .startYear( 2015 )
                .endYear( 2025 )
                .propertymanager( null )
                .build();
        return cc;
    }

    public void setDateValue(String value) {
        this.textField.setText(value);
        this.textField.setForeground(Color.black);
    }

    public JTextField getTextField() {
        return textField;
    }

    public void closeDateDialog() {
        this.dialog.dispose();
    }

    @Override
    public void setValue(String value) {

    }
}
