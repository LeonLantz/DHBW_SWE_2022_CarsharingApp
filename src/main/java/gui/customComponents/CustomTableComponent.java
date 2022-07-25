package gui.customComponents;

import de.dhbwka.swe.utils.event.EventCommand;
import de.dhbwka.swe.utils.event.GUIEvent;
import de.dhbwka.swe.utils.event.IGUIEventListener;
import de.dhbwka.swe.utils.gui.ObservableComponent;
import de.dhbwka.swe.utils.gui.SimpleTableComponent;
import de.dhbwka.swe.utils.model.Attribute;
import de.dhbwka.swe.utils.model.IDepictable;
import de.dhbwka.swe.utils.util.IPropertyManager;
import gui.renderer.TableCellRenderer;
import model.*;
import util.CSHelp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.Vector;

public class CustomTableComponent extends ObservableComponent {

    // Jede CustomTableComponent ist 900 Pixel breit (40 Pixel Border) --> SUM(columnWidths) = 860

    public enum Commands implements EventCommand {

        ROW_SELECTED( "customtablecomponent.tab_changed", String.class ),
        EDIT_ROW("customtablecomponent.row_edited", Kunde.class);

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

    private final IGUIEventListener observer;

    private int[] columnWidths = null;
    private Class modelClass = null;
    private IDepictable[] modelData = null;

    public CustomTableComponent(String id, IGUIEventListener observer) {
        this.observer = observer;
    }

    private void initUI() {
        this.setLayout(new BorderLayout(0,0));
        this.setPreferredSize(new Dimension(900, 520));
        this.setBorder(new EmptyBorder(20, 20, 20, 20));


        ArrayList namesList = new ArrayList<String>();

        if (Fahrzeug.class.equals(modelClass)) {
            for (String name : Fahrzeug.getAttributeNames(true)) {
                namesList.add(name);
            }
        } else if (Kunde.class.equals(modelClass)) {
            for (String name : Kunde.getAttributeNames(true)) {
                namesList.add(name);
            }
        } else if (Standort.class.equals(modelClass)) {
            for (String name : Standort.getAttributeNames(true)) {
                namesList.add(name);
            }
        }else if (Buchung.class.equals(modelClass)) {
            for (String name : Buchung.getAttributeNames(true)) {
                namesList.add(name);
            }
        }

        namesList.add("Edit");
        namesList.add("Delete");
        String[] names = new String[namesList.size()];
        namesList.toArray(names);

        final Vector<Vector<Attribute>> data = new Vector<>();

        for (int i = 0; i < modelData.length; i++) {
            int finalI = i;
            Vector<Attribute> attributeVector = new Vector<Attribute>(Attribute.filterVisibleAttributes(modelData[i].getAttributes()));

            JButton editButton = new JButton();
            editButton.setIcon(CSHelp.imageList.get("edit.png"));
            editButton.setBorder(BorderFactory.createEmptyBorder());
            editButton.setContentAreaFilled(false);
            JButton deleteButton = new JButton();
            deleteButton.setIcon(CSHelp.imageList.get("delete.png"));
            deleteButton.setBorder(BorderFactory.createEmptyBorder());
            deleteButton.setContentAreaFilled(false);

            editButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //System.out.println(kunden[finalI] + "Edit");
                    CustomTableComponent.this.fireGUIEvent(new GUIEvent(this, Commands.EDIT_ROW, modelData[finalI]));
                }
            });
            deleteButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                }
            });

            attributeVector.add(new Attribute("Edit", modelData[i], JButton.class, editButton, null, true, true, true, false));
            attributeVector.add(new Attribute("Delete", modelData[i], JButton.class, deleteButton, null, true, true, true, false));
            data.add( attributeVector );
        }
        SimpleTableComponent stc = SimpleTableComponent.builder("STC")
                .cellRenderer(new TableCellRenderer(), String.class, JButton.class, Date.class, File.class, Fahrzeugkategorie.class, Kunde.class, Fahrzeug.class, Buchungsstatus.class)
                .data(data)
                .columnNames(names)
                .selectionMode(ListSelectionModel.SINGLE_SELECTION)
                //new int[]{50, 100, 100, 170, 130, 100, 140, 35, 35}
                .columnWidths(this.columnWidths)
                .fixedColumns(new boolean[]{true, true, true, true, true, true, true, true, true, true, true, true})
                .observer(observer)
                .sorter()
                .build();
        JScrollPane p = (JScrollPane) stc.getComponent(0);
        System.out.println(stc.getComponentCount());
        p.getComponent(1).setPreferredSize(new Dimension(4,0));
        p.setBackground(CSHelp.main);
        p.setBorder(new EmptyBorder(0,0,0,0));
        this.add(stc, BorderLayout.CENTER);
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

        private int[] columnWidths;
        private Class modelClass;
        private IDepictable[] modelData;

        private SLCBuilder() {
        }

        public SLCBuilder propManager(IPropertyManager propMgr ) {
            this.propManager = propMgr;
            return this;
        }

        /**
         * Assign an initial {@link IGUIEventListener} as observer
         *
         * @param observer the observer
         *
         * @return the current instance for chaining
         */
        public SLCBuilder observer(IGUIEventListener observer ) {
            Objects.requireNonNull( observer, "observer must not be null!" );
            this.listener = observer;
            return this;
        }

        public SLCBuilder columnWidths(int[] columnWidths) {
            this.columnWidths = columnWidths;
            return this;
        }

        public SLCBuilder modelClass(Class modelClass) {
            this.modelClass = modelClass;
            return this;
        }

        public SLCBuilder modelData(IDepictable[] modelData) {
            this.modelData = modelData;
            return this;
        }

        /**
         * Build a SimpleListComponent instance
         *
         * @return the built SimpleListComponent instance
         */
        public CustomTableComponent build() {
            CustomTableComponent ctc = new CustomTableComponent( this.id, this.listener);
            ctc.setPropertyManager( this.propManager );
            ctc.columnWidths = this.columnWidths;
            ctc.modelClass = this.modelClass;
            ctc.modelData = this.modelData;
            ctc.initUI();
            if( this.listener != null ) ctc.addObserver(listener);
            return ctc;
        }
    }
}
