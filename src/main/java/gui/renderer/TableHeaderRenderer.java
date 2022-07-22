package gui.renderer;

import util.CSHelp;

import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class TableHeaderRenderer extends DefaultTableCellRenderer {

    public TableHeaderRenderer() {
        //setSize(new Dimension(0, 45));
        setPreferredSize(new Dimension(0, 50));
        setFont(CSHelp.lato_bold.deriveFont(14f));
        setBackground(CSHelp.tableHeaderBackground);
        setForeground(CSHelp.tableHeaderText);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(CSHelp.tableDividerColor);
        g.drawLine(0, getHeight()-1, getWidth(), getHeight()-1);
    }
}