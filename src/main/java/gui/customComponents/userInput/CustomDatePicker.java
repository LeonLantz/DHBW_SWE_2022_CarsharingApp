package gui.customComponents.userInput;

import de.dhbwka.swe.utils.event.IGUIEventListener;
import de.dhbwka.swe.utils.gui.CalendarComponent;
import de.dhbwka.swe.utils.util.IPropertyManager;
import util.CSHelp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;

public class CustomDatePicker extends CustomInputField {

    private JLabel _field_description;
    private JTextField _textField;
    private CalendarComponent _calendarComponent;
    private IGUIEventListener _observer;
    private JDialog _dialog;
    private JPanel _borderPanel;
    private boolean _enabled;

    public CustomDatePicker(String title, String placeholder, IGUIEventListener observer) {
        this.title = title;
        this.placeholder = placeholder;
        this._observer = observer;
        this._enabled = true;
        initUI();
    }

    private void initUI() {
        this.setLayout(new BorderLayout(0,0));
        this.setPreferredSize(new Dimension(200,65));
        this.setBorder(new EmptyBorder(5,10,5,10));
        this.setBackground(Color.WHITE);

        _field_description = new JLabel(title);
        _field_description.setFont(CSHelp.lato.deriveFont(13f));
        _field_description.setForeground(CSHelp.inputFieldText);
        _calendarComponent = createCalendarComponent(this.title, null);
        _dialog = new JDialog();
        _dialog.setModal(true);
        _dialog.setSize(new Dimension(400,500));
        _dialog.setResizable(false);
        _dialog.add(_calendarComponent);
        _textField = new JTextField(placeholder);
        _textField.setBorder(BorderFactory.createEmptyBorder(7,12,7,12));
        _textField.setFont(CSHelp.lato.deriveFont(12f));
        _textField.setPreferredSize(new Dimension(200, 30));
        _textField.setForeground(CSHelp.inputFieldPlaceholder);
        _textField.setBackground(CSHelp.inputFieldBackground);
        _textField.setEditable(false);
        _textField.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (_enabled) CustomDatePicker.this._dialog.setVisible(true);
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
        _borderPanel = new JPanel(new BorderLayout(0,0));
        _borderPanel.setBorder(new LineBorder(CSHelp.inputFieldBorderColor));
        _borderPanel.add(_field_description, BorderLayout.NORTH);
        _borderPanel.add(_textField, BorderLayout.CENTER);

        this.add(_borderPanel, BorderLayout.SOUTH);
    }

    public CalendarComponent createCalendarComponent( String id, IPropertyManager propManager ) {
        CalendarComponent cc = CalendarComponent.builder( id )
//					.date( LocalDate.of( 2019, 1, 31 ) )
                .date( LocalDate.now() )
                .observer(this._observer)
                .startYear( 1950 )
                .endYear( 2025 )
                .propertymanager( null )
                .build();
        return cc;
    }


    public void closeDateDialog() {
        this._dialog.dispose();
    }

    @Override
    public void setValue(String value) {
        this.value = value;
        this._textField.setText(value);
        this._textField.setForeground(Color.black);
    }

    @Override
    public void setEnabled(boolean enabled) {
        this._enabled = enabled;
    }
}
