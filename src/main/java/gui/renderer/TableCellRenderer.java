package gui.renderer;

import gui.customComponents.CustomTableComponent;
import model.Buchungsstatus;
import model.Fahrzeug;
import model.Fahrzeugkategorie;
import model.Kunde;
import util.CSHelp;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Enumeration;

public class TableCellRenderer implements javax.swing.table.TableCellRenderer {

    TableHeaderRenderer tableHeaderRenderer = new TableHeaderRenderer();

    Border border = BorderFactory.createMatteBorder(0,0,1,0, CSHelp.tableDividerColor);

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        //Performance und so ...
        if( row == 0 && column == 0) {
            table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            table.setShowGrid(false);
            table.getTableHeader().setDefaultRenderer(tableHeaderRenderer);
            table.getTableHeader().setReorderingAllowed(false);
            table.setBorder(BorderFactory.createEmptyBorder());
            table.setRowHeight(50);
            table.getColumn("Edit").setIdentifier("Edit");
            table.getColumn("Edit").setHeaderValue("");
            table.getColumn("Delete").setIdentifier("Delete");
            table.getColumn("Delete").setHeaderValue("");
            table.getColumn("Edit").setCellEditor(new JButtonEditor());
            table.getColumn("Delete").setCellEditor(new JButtonEditor());
            for (int index = 0; index < table.getColumnCount(); index++) {
                if(table.getColumnName(index) == "Google Maps") {
                    table.getColumn("Google Maps").setCellEditor(new JButtonEditor());
                    break;
                }
            }
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
            guiComp.setFont(CSHelp.lato.deriveFont(12f));
            guiComp.setForeground(CSHelp.tableCellText);
            ((JLabel)guiComp).setBorder( border );
        } else if ( clazz ==  Integer.class ) {
            ((JLabel)guiComp).setOpaque(true);
            guiComp.setFont(CSHelp.lato.deriveFont(12f));
            guiComp.setForeground(CSHelp.tableCellText);
            ((JLabel)guiComp).setBorder( border );
        } else if( clazz ==  URL.class ) {
            guiComp = new JButton(value.toString());
            guiComp.setFont(CSHelp.lato.deriveFont(10f));
            guiComp.setForeground(CSHelp.tableCellText);
            ((JButton)guiComp).setBorder( border );
            guiComp.setForeground(Color.BLUE.darker());
            guiComp.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        } else if(clazz == CustomTableComponent.EditButton.class || clazz == CustomTableComponent.DeleteButton.class || clazz == JButton.class) {
            guiComp = (JButton)value;
            ((JButton)guiComp).setBorder( border );
        }else if(clazz == LocalDateTime.class) {
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd.MM.YYYY");
            DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
            String datumString = dateFormat.format((LocalDateTime)value);
            String timeString = timeFormat.format((LocalDateTime)value) + " Uhr";
            ((JLabel)guiComp).setText("<html><body>" + datumString + "<br>" + timeString + "</body></html>");
            ((JLabel)guiComp).setOpaque(true);
            guiComp.setFont(CSHelp.lato.deriveFont(9f));
            ((JLabel)guiComp).setForeground(CSHelp.tableCellText);
            ((JLabel)guiComp).setBorder( border );
        }else if(clazz == LocalDate.class) {
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd.MM.YYYY");
            String datumString = dateFormat.format((LocalDate)value);
            ((JLabel)guiComp).setText(datumString);
            ((JLabel)guiComp).setOpaque(true);
            guiComp.setFont(CSHelp.lato.deriveFont(12f));
            ((JLabel)guiComp).setForeground(CSHelp.tableCellText);
            ((JLabel)guiComp).setBorder( border );
        }else if (clazz == File.class) {
                guiComp = new JLabel(CSHelp.imageList.get("icon_person.png"));
                ((JLabel)guiComp).setBorder( border );
        }else if (clazz == Fahrzeugkategorie.class ) {
            ((JLabel)guiComp).setOpaque(true);
            ((JLabel)guiComp).setText(((Fahrzeugkategorie) value).getBezeichner());
            guiComp.setFont(CSHelp.lato.deriveFont(12f));
            ((JLabel)guiComp).setBorder( border );
        }else if (clazz == Buchungsstatus.class ) {
            ((JLabel)guiComp).setOpaque(true);
            ((JLabel)guiComp).setText(((Buchungsstatus) value).getBezeichner());
            if (value == Buchungsstatus.INVALIDE) {
                ((JLabel)guiComp).setForeground(Color.red);
            }
            guiComp.setFont(CSHelp.lato.deriveFont(12f));
            ((JLabel)guiComp).setBorder( border );
        }else if (clazz == Kunde.class) {
            guiComp = new JLabel(((Kunde)value).getTableCellText());
            guiComp.setFont(CSHelp.lato.deriveFont(12f));
            guiComp.setForeground(Color.BLUE.darker());
            ((JLabel)guiComp).setBorder( border );
        }else if (clazz == Fahrzeug.class) {
            guiComp = new JLabel(((Fahrzeug)value).getTableCellText());
            guiComp.setFont(CSHelp.lato.deriveFont(12f));
            guiComp.setForeground(Color.BLUE.darker());
            ((JLabel)guiComp).setBorder( border );
        }

        return guiComp;
    }

}
