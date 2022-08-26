package gui.customComponents.userInput;

import de.dhbwka.swe.utils.event.GUIEvent;
import de.dhbwka.swe.utils.event.IGUIEventListener;
import de.dhbwka.swe.utils.gui.SimpleListComponent;
import de.dhbwka.swe.utils.model.IDepictable;
import util.CSHelp;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLDecoder;
import java.util.List;
import java.util.UUID;

public class CustomListField extends CustomInputField {

    private IGUIEventListener observer;
    private SimpleListComponent _slc;
    private IDepictable _iDepictable;
    private JButton _button;

    private static final String SP = File.separator;

    //For Kunde, Fahrzeug und Standort
    public CustomListField(String title, IGUIEventListener observer, List<IDepictable> list) {
        this.addObserver(observer);
        this.title = title;
        this._iDepictable = _iDepictable;
        this.value = "";
        this.observer = observer;
        initUIEntity(list);
    }

    //For Bild (Kunde, Fahrzeug, Standort) and Dokument (Buchung, Fahrzeug, Standort)
    public CustomListField(String title, IGUIEventListener observer, IDepictable _iDepictable) {
        this.addObserver(observer);
        this.title = title;
        this._iDepictable = _iDepictable;
        this.value = "";
        this.observer = observer;
        initUIFile();
    }

    private void initUIEntity(List<IDepictable> list) {
        this.setLayout(new BorderLayout(0, 0));
        this.setBorder(new EmptyBorder(10, 10, 0, 10));
        this.setBackground(Color.WHITE);

        Dimension dimension = null;
        if (this.title == "Kunde") {
            dimension = new Dimension(182, 130);
        } else if (this.title == "Fahrzeug") {
            dimension = new Dimension(182, 170);
        }

        _slc = SimpleListComponent.builder("123456")
                .title(title)
                .observer(this.observer)
                .font(CSHelp.lato.deriveFont(12f))
                .componentSize(dimension)
                .build();
        _slc.setListElements(list);

        this.add(_slc, BorderLayout.CENTER);
    }

    private void initUIFile() {
        this.setLayout(new BorderLayout(0, 0));
        this.setBorder(new EmptyBorder(10, 10, 0, 10));
        this.setBackground(Color.WHITE);

        ImageIcon imageIconButtonInfo = CSHelp.imageList.get("button_info.png");

        _slc = SimpleListComponent.builder("12345")
                .title(title)
                .observer(this.observer)
                .font(CSHelp.lato.deriveFont(12f))
                .componentSize(new Dimension(180, 130))
                .build();
        _slc.setFont(CSHelp.lato);

        JPanel eastPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        eastPanel.setBackground(Color.WHITE);
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.add(_slc, BorderLayout.CENTER);
        mainPanel.add(eastPanel, BorderLayout.EAST);

        _button = new JButton("+");
        _button.setPreferredSize(new Dimension(15, 15));
        _button.setBorder(new EmptyBorder(0, 0, 0, 0));
        _button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        _button.requestFocusInWindow();
        _button.setToolTipText("Neues Objekt hinzufügen");
        eastPanel.add(_button);
        if (title == "Bilder") {
            if (this._iDepictable == null) {
                _button.addActionListener(e -> JOptionPane.showMessageDialog(this.getParent(), "Bilder können erst nach dem Erstellen des Objektes hinzugefügt werden!", "Information", JOptionPane.INFORMATION_MESSAGE, imageIconButtonInfo));
            } else {
                attachBildListener(_button);
            }
        } else if (title == "Dokumente") {
            if (_iDepictable == null) {
                _button.addActionListener(e -> JOptionPane.showMessageDialog(this.getParent(), "Dokumente können erst nach dem Erstellen des Objektes hinzugefügt werden!", "Information", JOptionPane.INFORMATION_MESSAGE, imageIconButtonInfo));
            } else {
                attachDokumentListener(_button);
            }
        }
        this.add(mainPanel, BorderLayout.CENTER);
    }

    private JButton attachBildListener(JButton button) {
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
                        String answer = JOptionPane.showInputDialog(CustomListField.this.getParent(), "Bitte geben Sie den Bildnamen an", "Neues Bild", JOptionPane.INFORMATION_MESSAGE);
                        if (answer == null || (answer != null && ("".equals(answer)))) {
                            //TODO: System.out.println("Error");
                        } else {
                            String imageID = UUID.randomUUID().toString();
                            String filePath = "/UserImages/" + imageID + ".png";
                            ImageIO.write(image, "png", new File(getAbsolutWorkingDirectory() + filePath));
                            String[] imageValues = new String[]{imageID, answer, filePath, _iDepictable.getElementID()};
                            fireGUIEvent(new GUIEvent(this, Commands.ADD_BILD, imageValues));
                        }
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
                _slc.clearSelection();
            }
        });
        return button;
    }

    private JButton attachDokumentListener(JButton button) {
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // create an object of JFileChooser class
                JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

                // restrict the user to select files of all types
                j.setAcceptAllFileFilterUsed(false);

                // set a title for the dialog
                j.setDialogTitle("Select a .pdf file");

                // only allow files of .txt extension
                FileNameExtensionFilter restrict = new FileNameExtensionFilter("Only .pdf files", "pdf");
                j.addChoosableFileFilter(restrict);

                // invoke the showsOpenDialog function to show the save dialog
                int r = j.showOpenDialog(CustomListField.this);

                // if the user selects a file
                if (r == JFileChooser.APPROVE_OPTION) {
                    // set the label to the path of the selected file
                    String path = j.getSelectedFile().getAbsolutePath();
                    try {
                        File source = new File(path);
                        ImageIcon icon = CSHelp.imageList.get("icon_typing.png");
                        String answer = (String) JOptionPane.showInputDialog(j, "Bitte geben Sie den Dokumentennamen an", "Neues Dokument", JOptionPane.INFORMATION_MESSAGE, icon, null, null);
                        if (answer != null) {
                            _slc.clearSelection();

                            String dokumentID = UUID.randomUUID().toString();
                            String filePath = "/Dokumente/" + dokumentID + ".pdf";

                            copy(source, new File(getAbsolutWorkingDirectory() + filePath));

                            String[] dokumentValues = new String[]{dokumentID, answer, filePath, _iDepictable.getElementID()};
                            fireGUIEvent(new GUIEvent(this, Commands.ADD_DOKUMENT, dokumentValues));
                        }
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }
        });
        return button;
    }

    private String getAbsolutWorkingDirectory() {
        String jarPath = "";
        try {
            jarPath = URLDecoder.decode(getClass().getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        return jarPath.substring(0, jarPath.lastIndexOf(SP));
    }

    private static void copy(File src, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(src);
            os = new FileOutputStream(dest);

            byte[] buf = new byte[1024];

            int bytesRead;
            while ((bytesRead = is.read(buf)) > 0) {
                os.write(buf, 0, bytesRead);
            }
        } finally {
            is.close();
            os.close();
        }
    }

    public void setListElements(Object objectList) {
        List<IDepictable> iDepictableList = (List<IDepictable>) objectList;
        _slc.setListElements(iDepictableList, true);
        _slc.clearSelection();
    }

    public SimpleListComponent get_slc() {
        return _slc;
    }

    public JButton get_button() {
        return _button;
    }

    @Override
    public void setValue(String value) {
    }

    @Override
    public void setEnabled(boolean enabled) {
        _slc.setEnabled(enabled);
    }
}


