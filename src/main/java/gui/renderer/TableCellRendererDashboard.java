package gui.renderer;

import util.CSHelp;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TableCellRendererDashboard implements javax.swing.table.TableCellRenderer {

    TableHeaderRendererDashboard tableHeaderRenderer = new TableHeaderRendererDashboard();

    Border border = BorderFactory.createMatteBorder(0,0,1,0, CSHelp.tableDividerColor);

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        //Performance und so ...
        if( row == 0 && column == 0) {
            table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            table.getTableHeader().setDefaultRenderer(tableHeaderRenderer);
            table.getTableHeader().setReorderingAllowed(false);
            table.setBorder(new EmptyBorder(0,0,0,0));
            table.setRowHeight(50);
        }

        if( value == null ) {
            JLabel labelNull = new JLabel("");
            labelNull.setFont(CSHelp.lato.deriveFont(12f));
            labelNull.setForeground(CSHelp.tableCellText);
            labelNull.setBorder( border );
            return labelNull;
        }



        Component guiComp = new JLabel( value.toString() );
        guiComp.setBackground(CSHelp.tableCellBackground);

        Class<?> clazz = value.getClass();

        if( clazz ==  String.class ){
            ((JLabel)guiComp).setOpaque(true);
            guiComp.setFont(CSHelp.lato.deriveFont(14f));
            guiComp.setForeground(Color.black);
            ((JLabel)guiComp).setBorder( border );
        } else if(clazz == LocalDateTime.class) {
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd.MM.YYYY");
            DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
            String datumString = dateFormat.format((LocalDateTime)value);
            String timeString = timeFormat.format((LocalDateTime)value) + " Uhr";
            ((JLabel)guiComp).setText("<html><body>" + datumString + "<br>" + timeString + "</body></html>");
            ((JLabel)guiComp).setOpaque(true);
            guiComp.setFont(CSHelp.lato.deriveFont(9f));
            ((JLabel)guiComp).setForeground(CSHelp.tableCellText);
            ((JLabel)guiComp).setBorder( border );
        }

        return guiComp;
    }

}
