package gui.customComponents.userInput;

import de.dhbwka.swe.utils.event.GUIEvent;
import de.dhbwka.swe.utils.event.IGUIEventListener;
import de.dhbwka.swe.utils.gui.SimpleListComponent;
import de.dhbwka.swe.utils.model.IDepictable;
import gui.GUIFahrzeugAnlegen;
import model.Bild;
import util.CSHelp;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class CustomListField extends CustomInputField {

    private SimpleListComponent slc;
    private IDepictable iDepictable;

    public CustomListField(String title, String placeholder, IGUIEventListener observer, IDepictable iDepictable) {
        this.addObserver(observer);
        this.title = title;
        this.iDepictable = iDepictable;
        this.placeholder = placeholder;
        this.value = "";

        //InitUI
        this.setLayout(new BorderLayout(0,0));
        this.setPreferredSize(new Dimension(200,130));
        this.setBorder(new EmptyBorder(10,10,0,10));
        this.setBackground(Color.WHITE);

        slc = SimpleListComponent.builder("okayy")
                .title(title)
                .observer(observer)
                .font(CSHelp.lato.deriveFont(12f))
                .componentSize(new Dimension(200,130))
                .build();

        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        southPanel.setBackground(Color.white);

        JButton button = new JButton("+");
        button.setBorder(new LineBorder(Color.black, 1));
        button.setPreferredSize(new Dimension(20, 20));
        southPanel.add(button);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // create an object of JFileChooser class
                JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

                // restrict the user to select files of all types
                j.setAcceptAllFileFilterUsed(false);

                // set a title for the dialog
                j.setDialogTitle("Select a .txt file");

                // only allow files of .txt extension
                FileNameExtensionFilter restrict = new FileNameExtensionFilter("Only .png files", "png");
                j.addChoosableFileFilter(restrict);

                // invoke the showsOpenDialog function to show the save dialog
                int r = j.showOpenDialog(null);

                // if the user selects a file
                if (r == JFileChooser.APPROVE_OPTION) {
                    // set the label to the path of the selected file
                    System.out.println(j.getSelectedFile().getAbsolutePath());

                    String path = j.getSelectedFile().getAbsolutePath();
                    try {
                        BufferedImage image = ImageIO.read(new File(path));
                        String answer = JOptionPane.showInputDialog(null, "Bitte geben Sie den Bildnamen an", "Neues Bild", JOptionPane.INFORMATION_MESSAGE);
                        slc.clearSelection();
                        String imageID = UUID.randomUUID().toString();
                        String filePath = "src/main/resources/Images/" + imageID + ".png";
                        ImageIO.write(image, "png", new File(filePath));
                        String[] imageValues = new String[]{imageID, answer, filePath, iDepictable.getElementID()};
                        fireGUIEvent(new GUIEvent(this, Commands.ADD_BILD, imageValues ));

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        this.add(southPanel, BorderLayout.SOUTH);
        this.add(slc, BorderLayout.CENTER);
    }

    public void setListElements(List<Bild> objectList) {
        slc.setListElements(objectList, true);
        slc.clearSelection();
    }

    public SimpleListComponent getSlc() {
        return slc;
    }

    @Override
    public void setValue(String value) {}


}
