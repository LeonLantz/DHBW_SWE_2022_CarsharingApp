package util;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.Buffer;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class CSHelp {
    public static Font lato, lato_bold;
    public static Color main, tableHeaderText, tableCellText, tableHeaderBackground, tableDividerColor, tableCellBackground, navBar, inputFieldBorder, inputFieldPlaceholder, inputFieldText, inputFieldBorderColor, inputFieldBackground, navBarItemActive, navBarTextActive, tileCountColor;
    public static ImageIcon button_add_kunde, button_add_fahrzeug, button_edit_row, button_delete_row, table_cell_image;

    public static File[] imageFiles;
    public static HashMap<String, ImageIcon> imageList;

    public static void init() {
        registerFonts();
        registerColors();
        registerImages();
    }

    private static void registerFonts() {
        try {
            lato = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/Fonts/Lato-Regular.ttf"));
            lato_bold = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/Fonts/Lato-Bold.ttf"));
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(lato);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(lato_bold);
        } catch (IOException|FontFormatException e) {}
    }

    public static void registerColors() {
        main = new Color(0xEFEFEF);
        tableCellText = new Color(0x52575C);
        navBar = new Color(0xE5E5E5);
        tableHeaderText = new Color(0x25282B);
        tableHeaderBackground = new Color(0xE8E8E8);
        tableDividerColor = new Color(0xDBDDE0);
        tableCellBackground = new Color(0xFFFFFF);
        inputFieldBorder = new Color(0xDCDFE6);
        inputFieldPlaceholder = new Color(0xC0C4CC);
        inputFieldText = new Color(0x5F6377);
        inputFieldBackground = new Color(0xFCFCFD);
        navBarItemActive = new Color(0xE3F2FD);
        navBarTextActive = new Color(0x2196F3);
        tileCountColor = new Color(0x9E9E9E);
        inputFieldBorderColor = new Color(0xCFD0D7);
    }

    public static void registerImages() {

        imageFiles = new File("src/main/resources/SystemImages").listFiles();
        imageList = new HashMap<>();
        for(File file :  imageFiles) {
            if (!file.isHidden()) {
                BufferedImage bufferedImage = null;
                try {
                    bufferedImage = ImageIO.read(file);
                    imageList.put(file.getName(), new ImageIcon(bufferedImage));
                    //System.out.println("Image:" + file.getName() + " successfully registered!");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void createJDialog(Component component, Dimension dimension) {
        JDialog dialog = new JDialog();
        dialog.setLayout(new BorderLayout());
        dialog.setModal(true);
        dialog.setResizable(false);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setSize(dimension);
        dialog.add(component, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    public static boolean isNumber(String input) {
        String regex = "[0-9]+[\\.]?[0-9]*";
        return Pattern.matches(regex, input);
    }

    public static boolean isEmail(String input) {
        String regex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        return Pattern.matches(regex, input);
    }

    public static boolean isDate(String input) {
        String regex = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$";
        return Pattern.matches(regex, input);
    }


    private static final String sp = File.separator;
    public static String getAbsolutWorkingDirectory() {
        String jarPath = "";
        try {
            jarPath = URLDecoder.decode(CSHelp.class.getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        // Incase a maven jar is run
        if (jarPath.endsWith(".jar")) {
            return jarPath.substring(0, jarPath.lastIndexOf(sp)) + "/classes";
        }
        return jarPath.substring(0, jarPath.lastIndexOf(sp));
    }
}
