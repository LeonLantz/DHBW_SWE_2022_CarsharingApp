package gui.renderer;

import model.Buchungsstatus;
import model.Fahrzeug;
import model.Fahrzeugkategorie;
import model.Kunde;
import util.CSHelp;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TableCellRenderer implements javax.swing.table.TableCellRenderer {

    TableHeaderRenderer tableHeaderRenderer = new TableHeaderRenderer();


    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        if( value == null ) {
            return new JLabel( "NULL" );
        }

        Border border = BorderFactory.createMatteBorder(0,0,1,0, CSHelp.tableDividerColor);

        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getTableHeader().setDefaultRenderer(tableHeaderRenderer);
        table.getTableHeader().setReorderingAllowed(false);
        table.setBorder(BorderFactory.createEmptyBorder());
        table.setRowHeight(50);

        try {
            table.getColumn("Bild").setIdentifier("Bild");
            table.getColumn("Bild").setHeaderValue("");
        } catch (Exception ex) {

        }

        table.getColumn("Edit").setIdentifier("Edit");
        table.getColumn("Edit").setHeaderValue("");
        table.getColumn("Edit").setCellEditor(new JButtonEditor());
        table.getColumn("Delete").setCellEditor(new JButtonEditor());
        table.getColumn("Delete").setIdentifier("Delete");
        table.getColumn("Delete").setHeaderValue("");


        Component guiComp = new JLabel( value.toString() );
        guiComp.setBackground(CSHelp.tableCellBackground);

        Class<?> clazz = value.getClass();

        if( clazz ==  String.class ){
            ((JLabel)guiComp).setOpaque(true);
            ((JLabel)guiComp).setFont(CSHelp.lato.deriveFont(12f));
            ((JLabel)guiComp).setForeground(CSHelp.tableCellText);
            ((JLabel)guiComp).setBorder( border );
            if(value.toString().contains("https")) {
                ((JLabel)guiComp).setForeground(Color.BLUE.darker());
                ((JLabel)guiComp).setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
        }else if(clazz == JButton.class) {
            guiComp = (JButton)value;
            ((JButton)guiComp).setBorder( border );
        }else if(clazz == Date.class) {
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");
            DateFormat timeFormat = new SimpleDateFormat("HH:mm");
            String datumString = dateFormat.format(value);
            String timeString = timeFormat.format(value) + " Uhr";
            ((JLabel)guiComp).setText("<html><body>" + datumString + "<br>" + timeString + "</body></html>");
            ((JLabel)guiComp).setOpaque(true);
            guiComp.setFont(CSHelp.lato.deriveFont(9f));
            ((JLabel)guiComp).setForeground(CSHelp.tableCellText);
            ((JLabel)guiComp).setBorder( border );
        }else if (clazz == File.class) {
                guiComp = new JLabel(CSHelp.table_cell_image);
                ((JLabel)guiComp).setBorder( border );
        }else if (clazz == Fahrzeugkategorie.class ) {
            ((JLabel)guiComp).setOpaque(true);
            ((JLabel)guiComp).setText(((Fahrzeugkategorie) value).getBezeichner());
            ((JLabel)guiComp).setFont(CSHelp.lato.deriveFont(12f));
            ((JLabel)guiComp).setBorder( border );
        }else if (clazz == Buchungsstatus.class ) {
            ((JLabel)guiComp).setOpaque(true);
            ((JLabel)guiComp).setText(((Buchungsstatus) value).getBezeichner());
            ((JLabel)guiComp).setFont(CSHelp.lato.deriveFont(12f));
            ((JLabel)guiComp).setBorder( border );
        }else if (clazz == Kunde.class) {
            guiComp = new JLabel(((Kunde)value).getTableCellText());
            guiComp.setFont(CSHelp.lato.deriveFont(12f));
            ((JLabel)guiComp).setForeground(Color.BLUE.darker());
            ((JLabel)guiComp).setBorder( border );
        }else if (clazz == Fahrzeug.class) {
            guiComp = new JLabel(((Fahrzeug)value).getTableCellText());
            ((JLabel)guiComp).setFont(CSHelp.lato.deriveFont(12f));
            ((JLabel)guiComp).setForeground(Color.BLUE.darker());
            ((JLabel)guiComp).setBorder( border );
        }
        return guiComp;
    }


}
