package gui.renderer;

import model.Fahrzeugkategorie;
import util.CSHelp;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.File;
import java.io.IOException;
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

        table.getColumn("Bild").setIdentifier("Bild");
        table.getColumn("Bild").setHeaderValue("");
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
        }else if(clazz == JButton.class) {
            guiComp = (JButton)value;
            ((JButton)guiComp).setBorder( border );
        }else if(clazz == Date.class) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            ((JLabel)guiComp).setText(dateFormat.format(value));
            ((JLabel)guiComp).setOpaque(true);
            guiComp.setFont(CSHelp.lato.deriveFont(9f));
            ((JLabel)guiComp).setForeground(CSHelp.tableCellText);
            ((JLabel)guiComp).setBorder( border );
        }else if (clazz == File.class) {
            try {
                guiComp = new JLabel(CSHelp.createCircle((File)value));
                ((JLabel)guiComp).setBorder( border );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if (clazz == Fahrzeugkategorie.class) {
            ((JLabel)guiComp).setOpaque(true);
            ((JLabel)guiComp).setText(((Fahrzeugkategorie) value).getBezeichner());
            ((JLabel)guiComp).setFont(CSHelp.lato.deriveFont(12f));
            ((JLabel)guiComp).setBorder( border );
        }
//        else if (clazz == Anschrift.class) {
//            guiComp = new JLabel(((Anschrift) value).getPlz() + ", " + ((Anschrift) value).getOrt());
//            ((JLabel)guiComp).setFont(CSHelp.lato.deriveFont(12f));
//            ((JLabel)guiComp).setForeground(CSHelp.tableCellText);
//            ((JLabel)guiComp).setBorder( border );
//        }
        return guiComp;
    }


}
