package util;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class CSHelp {
    public static Font lato, lato_bold;
    public static Color main, tableHeaderText, tableCellText, tableHeaderBackground, tableDividerColor, tableCellBackground, navBar, inputFieldBorder, inputFieldPlaceholder, inputFieldText, inputFieldBackground;
    public static ImageIcon button_add_kunde, button_add_fahrzeug, button_edit_row, button_delete_row, table_cell_image;
    //public static Map<String, ImageIcon> images;

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
    }

    public static void registerImages() {

        imageFiles = new File("src/main/resources/Images").listFiles();
        imageList = new HashMap<>();
        for(File file :  imageFiles) {
            if (!file.isHidden()) {
                BufferedImage bufferedImage = null;
                try {
                    bufferedImage = ImageIO.read(file);
                    imageList.put(file.getName(), new ImageIcon(bufferedImage));
                    System.out.println("Image:" + file.getName() + " successfully registered!");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean isNumber(String input) {
        String regex = "[0-9]+[\\.]?[0-9]*";
        return Pattern.matches(regex, input);
    }

    public static boolean isEmail(String input) {
        String regex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        return Pattern.matches(regex, input);
    }
}
