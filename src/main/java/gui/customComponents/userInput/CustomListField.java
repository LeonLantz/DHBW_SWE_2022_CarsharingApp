package gui.customComponents.userInput;

import de.dhbwka.swe.utils.event.GUIEvent;
import de.dhbwka.swe.utils.event.IGUIEventListener;
import de.dhbwka.swe.utils.gui.SimpleListComponent;
import de.dhbwka.swe.utils.model.IDepictable;
import de.dhbwka.swe.utils.model.IPersistable;
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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class CustomListField extends CustomInputField {

    private IGUIEventListener observer;

    private SimpleListComponent slc;
    private IDepictable iDepictable;

    // Für Kunde, Fahrzeug und Standort
    public CustomListField(String title, IGUIEventListener observer, List<IDepictable> list) {
        this.addObserver(observer);
        this.title = title;
        this.iDepictable = iDepictable;
        this.value = "";
        this.observer = observer;
        initUIEntity(list);
    }

    //Für Bild und Dokument (Kunde, Fahrzeug, Standort)
    public CustomListField(String title, IGUIEventListener observer, IDepictable iDepictable) {
        this.addObserver(observer);
        this.title = title;
        this.iDepictable = iDepictable;
        this.value = "";
        this.observer = observer;
        initUIBild();
    }

    private void initUIEntity(List<IDepictable> list) {
        this.setLayout(new BorderLayout(0, 0));
        this.setBorder(new EmptyBorder(10, 10, 0, 10));
        this.setBackground(Color.WHITE);

        Dimension dimension = null;
        if (this.title == "Kunde") {
            dimension = new Dimension(182,130);
        }else if (this.title == "Fahrzeug") {
            dimension = new Dimension(182,170);
        }

        slc = SimpleListComponent.builder("123456")
                .title(title)
                .observer(this.observer)
                .font(CSHelp.lato.deriveFont(12f))
                .componentSize(dimension)
                .build();
        slc.setListElements(list);

        this.add(slc, BorderLayout.CENTER);
    }

    private void initUIBild() {
        this.setLayout(new BorderLayout(0, 0));
        this.setBorder(new EmptyBorder(10, 10, 0, 10));
        this.setBackground(Color.WHITE);

        slc = SimpleListComponent.builder("12345")
                .title(title)
                .observer(this.observer)
                .font(CSHelp.lato.deriveFont(12f))
                .componentSize(new Dimension(180, 130))
                .build();

        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        southPanel.setBackground(Color.white);

        JPanel mainPanel = new JPanel(new BorderLayout(0,0));
        mainPanel.add(slc, BorderLayout.CENTER);
        mainPanel.add(southPanel, BorderLayout.EAST);

        JButton button = new JButton("+");
        button.setPreferredSize(new Dimension(15, 15));
        button.setBorder(new EmptyBorder(0,0,0,0));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setToolTipText("Neues Objekt hinzufügen");
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
                    String path = j.getSelectedFile().getAbsolutePath();
                    try {
                        BufferedImage image = ImageIO.read(new File(path));
                        String answer = JOptionPane.showInputDialog(null, "Bitte geben Sie den Bildnamen an", "Neues Bild", JOptionPane.INFORMATION_MESSAGE);
                        slc.clearSelection();

                        String imageID = UUID.randomUUID().toString();
                        String filePath = "src/main/resources/SystemImages/" + imageID + ".png";
                        ImageIO.write(image, "png", new File(filePath));
                        String[] imageValues = new String[]{imageID, answer, filePath, iDepictable.getElementID()};
                        fireGUIEvent(new GUIEvent(this, Commands.ADD_BILD, imageValues));

                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }

                }
            }
        });

        this.add(mainPanel, BorderLayout.CENTER);
    }

    public void setListElements(Object objectList) {
        List<IDepictable> iDepictableList = (List<IDepictable>) objectList;
        slc.setListElements(iDepictableList, true);
        slc.clearSelection();
    }

    public SimpleListComponent getSlc() {
        return slc;
    }

    @Override
    public void setValue(String value) {
    }

    @Override
    public void setEnabled(boolean enabled) {
        slc.setEnabled(enabled);
    }
}


