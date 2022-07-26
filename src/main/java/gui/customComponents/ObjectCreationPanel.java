package gui.customComponents;

import control.CSControllerReinerObserverUndSender;
import de.dhbwka.swe.utils.event.EventCommand;
import de.dhbwka.swe.utils.event.GUIEvent;
import de.dhbwka.swe.utils.event.IGUIEventListener;
import de.dhbwka.swe.utils.gui.ObservableComponent;
import de.dhbwka.swe.utils.model.IDepictable;
import de.dhbwka.swe.utils.util.IPropertyManager;
import gui.MainComponentMitNavBar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Objects;

public class ObjectCreationPanel extends ObservableComponent {

    public enum Commands implements EventCommand {

        ADD_KUNDE( "ObjectCreationPanel.addKunde", String[].class );

        public final Class<?> payloadType;
        public final String cmdText;

        Commands(String cmdText, Class<?> payloadType) {
            this.cmdText = cmdText;
            this.payloadType = payloadType;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getCmdText() {
            return this.cmdText;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Class<?> getPayloadType() {
            return this.payloadType;
        }
    }



    private void initUI() {
        this.setLayout(new GridLayout(5,2,0, 0));
        JTextField textField = new JTextField();
        textField.setBackground(Color.lightGray);
        textField.setText("Das ist ein Textfeld");
        JTextField textField2 = new JTextField();
        JTextField textField3 = new JTextField();
        JTextField textField4 = new JTextField();
        JTextField textField5 = new JTextField();
        JTextField textField6 = new JTextField();


        this.add(new JTextField());
        this.add(new JTextField());
        this.add(new JTextField());
        this.add(new JTextField());
        this.add(new JTextField());
        this.add(new JTextField());
        this.add(new JTextField());
        this.add(new JTextField());
        this.add(new JTextField());
        this.add(new JTextField());

        JButton button = new JButton("Add Kunde");
        String[] values = new String[]{
                "12345","/hhh/","Max","Reichmann","max.kanns@gmail.com","0177823211","DE22321812321","2000-01-01","2022-04-25"
        };
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fireGUIEvent( new GUIEvent(this, Commands.ADD_KUNDE, values ));
            }
        });


        this.add(button);

        this.setPreferredSize(new Dimension(500,700));
    }


    public static SLCBuilder builder(String id ) {
        if( id == null || id.isEmpty() ) throw new IllegalArgumentException( "ID must be given!" );
        SLCBuilder slcBuilder = new SLCBuilder();
        slcBuilder.id = id;
        return slcBuilder;
    }

    public static final class SLCBuilder {

        private String id;
        private IPropertyManager propManager;
        private IGUIEventListener listener;


        private SLCBuilder() {
        }

        public SLCBuilder propManager(IPropertyManager propMgr ) {
            this.propManager = propMgr;
            return this;
        }

        public SLCBuilder observer(IGUIEventListener observer ) {
            Objects.requireNonNull( observer, "observer must not be null!" );
            this.listener = observer;
            return this;
        }

        public ObjectCreationPanel build() {
            ObjectCreationPanel ocp = new ObjectCreationPanel();
            ocp.setPropertyManager( this.propManager );
            if( this.listener != null ) ocp.addObserver(listener);
            ocp.initUI();
            return ocp;
        }
    }


}
