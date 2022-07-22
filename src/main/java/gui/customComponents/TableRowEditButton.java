package gui.customComponents;

import de.dhbwka.swe.utils.event.EventCommand;
import de.dhbwka.swe.utils.event.GUIEvent;
import de.dhbwka.swe.utils.event.IGUIEventListener;
import de.dhbwka.swe.utils.gui.GUIConstants;
import de.dhbwka.swe.utils.gui.ObservableComponent;
import de.dhbwka.swe.utils.gui.SimpleTableComponent;
import de.dhbwka.swe.utils.model.Attribute;
import util.CSHelp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class TableRowEditButton extends ObservableComponent {

    public enum Commands implements EventCommand {

        BUTTON_PRESSED("TableRowEditButton.button_pressed", Attribute.class);

        public final Class<?> payloadType;
        public final String cmdText;

        Commands(String cmdText, Class<?> payloadType) {
            this.cmdText = cmdText;
            this.payloadType = payloadType;
        }

        @Override
        public String getCmdText() {
            return null;
        }

        @Override
        public Class<?> getPayloadType() {
            return null;
        }
    }

    private static class CommonAttributes {
        private String id;
        private String value;
        private ImageIcon imageIcon;
        private Dimension size;
    }

    private JButton button;
    private ObservableComponent observer;

    private final CommonAttributes commonAttributes;

    public TableRowEditButton(CommonAttributes commonAttributes) {
        this.commonAttributes = commonAttributes;
    }

    public void initUI() {
        this.setLayout(new BorderLayout(0,0));
        this.button = new JButton();

        if(this.commonAttributes.size != null) {
            this.button.setPreferredSize(this.commonAttributes.size);
        }

        if(this.commonAttributes.imageIcon != null) {
            this.button.setIcon(this.commonAttributes.imageIcon);
        }

        this.button.setBorder(new EmptyBorder(0,0,0,0));

        this.button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TableRowEditButton.this.fireGUIEvent(new GUIEvent(this, Commands.BUTTON_PRESSED, TableRowEditButton.this.commonAttributes.value));
            }
        });

        this.add(button, BorderLayout.CENTER);
    }



    @Override
    public void fireGUIEvent(GUIEvent ge) {
        super.fireGUIEvent(ge);
    }

    public static STBuilder builder(String id) {
        if (id == null || id.isEmpty()) throw new IllegalArgumentException("ID must be given!");
        return new TableRowEditButton.STBuilder(id);
    }

    /**
     * *************************************************************************************************
     * The local builder class
     * *************************************************************************************************
     */
    public static class STBuilder {

        private TableRowEditButton.CommonAttributes commonAttributes;
        private IGUIEventListener listener;

        private STBuilder() {
        }

        private STBuilder(String id) {
            this.commonAttributes = new TableRowEditButton.CommonAttributes();
            this.commonAttributes.id = id;
        }

        public STBuilder value(String value) {
            this.commonAttributes.value = value;
            return this;
        }

        public STBuilder image(ImageIcon imageIcon) {
            this.commonAttributes.imageIcon = imageIcon;
            return this;
        }

        public STBuilder size(Dimension dimension) {
            this.commonAttributes.size = dimension;
            return this;
        }

        public STBuilder observer(IGUIEventListener observer) {
            Objects.requireNonNull( observer, "observer must not be null!" );
            this.listener = observer;
            return this;
        }

        public TableRowEditButton build() {
            TableRowEditButton treb = new TableRowEditButton(this.commonAttributes);
            treb.initUI();
            if (this.listener != null) treb.addObserver(listener);
            return treb;
        }
    }

}
