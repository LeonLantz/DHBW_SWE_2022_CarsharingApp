package gui;

import de.dhbwka.swe.utils.event.IGUIEventListener;
import de.dhbwka.swe.utils.gui.ObservableComponent;
import de.dhbwka.swe.utils.gui.SimpleTableComponent;
import de.dhbwka.swe.utils.model.Attribute;
import gui.customComponents.TableRowEditButton;
import gui.renderer.TableCellRenderer;
import model.Anschrift;
import model.Kunde;
import util.CSHelp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

public class KundenTab extends JPanel {

    JPanel header, content, footer;
    private IGUIEventListener observer;

    public KundenTab(IGUIEventListener observableComponent) {

        this.observer = observableComponent;
        this.setLayout(new BorderLayout(0,0));
        this.setPreferredSize(new Dimension(900, 720));

        this.add(createHeader(), BorderLayout.NORTH);
        this.add(createContent(), BorderLayout.CENTER);
        this.add(createFooter(), BorderLayout.SOUTH);
    }

    private SimpleTableComponent createTableComponent() {
        Kunde[] kunden = new Kunde[] {
                new Kunde(new File("src/main/resources/Images/demoAvatar.png"), "Leon", "Lantz", "leon@lantz.de", "14.04.2001", new Anschrift("okay","","","","Herxheim am Berg", "67273", ""), new Date()),
                new Kunde(new File("src/main/resources/Images/demoAvatar.png"), "Max", "Reichmann", "max@reichmann.de", "01.01.2000", new Anschrift("okay","","","","Karlsruhe", "12345", ""), new Date()),
                new Kunde(new File("src/main/resources/Images/demoAvatar.png"), "Lutz", "Gröll", "lutz@gröll.de", "05.05.1959", new Anschrift("okay","","","","Karlsruhe", "12345", ""), new Date()),
        };

        ArrayList namesList = new ArrayList<String>();
        for (String name : Kunde.getAttributeNames(true)) {
            namesList.add(name);
        }
        namesList.add("Edit");
        namesList.add("Delete");
        String[] names = new String[namesList.size()];
        namesList.toArray(names);

        final Vector<Vector<Attribute>> data = new Vector<>();

        for( int i = 0 ; i < kunden.length ; i++ ){
            int finalI = i;
            Vector<Attribute> attributeVector = new Vector<Attribute>( Attribute.filterVisibleAttributes( kunden[i].getAttributes() ) );

            JButton editButton = new JButton();
            editButton.setIcon(CSHelp.button_edit_row);
            editButton.setBorder(BorderFactory.createEmptyBorder());
            editButton.setContentAreaFilled(false);
            JButton deleteButton = new JButton();
            deleteButton.setIcon(CSHelp.button_delete_row);
            deleteButton.setBorder(BorderFactory.createEmptyBorder());
            deleteButton.setContentAreaFilled(false);

            editButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println(kunden[finalI] + "Edit");
                }
            });
            deleteButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println(kunden[finalI] + "Delete");
                }
            });

            attributeVector.add(new Attribute("Edit", kunden[i], JButton.class, editButton, null, true, true, true, false));
            attributeVector.add(new Attribute("Delete", kunden[i], JButton.class, deleteButton, null, true, true, true, false));
            data.add( attributeVector );
        }

        SimpleTableComponent stc = SimpleTableComponent.builder("STC")
                .cellRenderer(new TableCellRenderer(), String.class, JButton.class, Date.class, File.class, Anschrift.class)
                .data(data)
                .columnNames(names)
                .selectionMode(ListSelectionModel.SINGLE_SELECTION)
                .columnWidths(new int[] {50, 100, 100, 130, 110, 180, 120, 35, 35})
                .fixedColumns(new boolean[] {true, true, true, true, true, true, true, true, true})
                //.componentSize(new Dimension(600, 300))
                .observer(observer)
                .sorter()
                .build();

        JScrollPane p = (JScrollPane) stc.getComponent(0);
        p.setBorder(new EmptyBorder(0,0,0,0));

        return stc;
    }


    private JPanel createHeader() {
        header = new JPanel(new BorderLayout(0,0));
        header.setPreferredSize(new Dimension(900, 130));
        header.setBackground(Color.red);

        // Kopfzeile, Komponente links
        JPanel header_west = new JPanel(new BorderLayout(0,0));
        header_west.setBackground(CSHelp.main);
        header_west.setBorder(BorderFactory.createMatteBorder(0,0,1,0, CSHelp.tableDividerColor));
        header.add(header_west, BorderLayout.CENTER);

        JLabel header_west_label = new JLabel("Kunden");
        header_west_label.setFont(CSHelp.lato_bold.deriveFont(40f));
        header_west_label.setBorder(new EmptyBorder(20, 20, 10, 20));
        header_west.add(header_west_label, BorderLayout.SOUTH);

        JPanel header_west_searchBar = new JPanel(new BorderLayout(0,0));
        header_west_searchBar.setBorder(new EmptyBorder(20, 20, 20, 20));
        header_west_searchBar.setBackground(CSHelp.main);

        JTextField header_west_searchBar_textfield = new JTextField();
        header_west_searchBar_textfield.setFont(CSHelp.lato.deriveFont(12f));
        header_west_searchBar_textfield.setBounds(10,10,10,10);
        header_west_searchBar_textfield.setPreferredSize(new Dimension(200, 30));
        header_west_searchBar.add(header_west_searchBar_textfield, BorderLayout.WEST);
        header_west.add(header_west_searchBar, BorderLayout.NORTH);

        // Kopfzeile, Komponente rechts
        JPanel header_east = new JPanel(new BorderLayout(0,0));
        header_east.setBackground(Color.black);
        header_east.setPreferredSize(new Dimension(130, 130));
        header.add(header_east, BorderLayout.EAST);

        return this.header;
    }

    private JPanel createContent() {
        content = new JPanel(new BorderLayout(0,0));
        content.setPreferredSize(new Dimension(900, 520));
        content.setBackground(CSHelp.main);
        content.setBorder(new EmptyBorder(20,20,20,20));
        content.add(createTableComponent());
        return this.content;
    }

    private JPanel createFooter() {
        //Fußzeile
        footer = new JPanel(new BorderLayout(20,20));
        footer.setPreferredSize(new Dimension(900, 80));
        footer.setBackground(CSHelp.main);

        //Fußzeile, Komponente rechts
        JPanel footer_east = new JPanel(new FlowLayout(FlowLayout.CENTER, 0,0));
        footer_east.setBackground(Color.yellow);
        footer_east.setPreferredSize(new Dimension(338,50));
        footer_east.add(createFooterButton());
        footer.add(footer_east, BorderLayout.EAST);

        return this.footer;
    }

    private TableRowEditButton createFooterButton() {
        TableRowEditButton addButton = TableRowEditButton.builder("EBTN")
                .size(new Dimension(300,54))
                .image(CSHelp.button_add_kunde)
                .build();
        return addButton;
    }
}
