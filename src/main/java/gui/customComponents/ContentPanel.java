package gui.customComponents;

import util.CSHelp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ContentPanel extends JPanel {

    private JPanel header, content, footer;

    //Config
    private String headline;
    private ImageIcon buttonImage;

    public ContentPanel(String headline, ImageIcon buttonImage) {
        this.buttonImage = buttonImage;
        this.headline = headline;

        this.setLayout(new BorderLayout(0, 0));
        this.setPreferredSize(new Dimension(900, 720));
        this.add(createHeader(), BorderLayout.NORTH);
        this.add(createContent(), BorderLayout.CENTER);
        this.add(createFooter(), BorderLayout.SOUTH);
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

        JLabel header_west_label = new JLabel(this.headline);
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
        content.setPreferredSize(new Dimension(900, 500));
        content.setBackground(CSHelp.main);
        content.setBorder(new EmptyBorder(20,20,20,20));
        //content.add();
        return this.content;
    }

    private JPanel createFooter() {
        //Fußzeile
        footer = new JPanel(new BorderLayout(20,20));
        footer.setPreferredSize(new Dimension(900, 80));
        footer.setBackground(CSHelp.main);

        //Fußzeile, Komponente rechts
        JPanel footer_east = new JPanel(new FlowLayout(FlowLayout.CENTER, 0,0));
        footer_east.setPreferredSize(new Dimension(338,50));
        footer_east.add(createFooterButton());
        footer.add(footer_east, BorderLayout.EAST);

        return this.footer;
    }

    private TableRowEditButton createFooterButton() {
        TableRowEditButton addButton = TableRowEditButton.builder("EBTN")
                .size(new Dimension(300,54))
                .image(this.buttonImage)
                .build();
        return addButton;
    }

    public JPanel getContent() {
        return content;
    }

    public JPanel getFooter() {
        return footer;
    }

    public JPanel getHeader() {
        return header;
    }
}
