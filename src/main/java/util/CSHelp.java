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

public class CSHelp {
    public static Font lato, lato_bold;
    public static Color main, tableHeaderText, tableCellText, tableHeaderBackground, tableDividerColor, tableCellBackground, navBar;
    public static ImageIcon button_add_kunde, button_edit_row, button_delete_row;
    //public static Map<String, ImageIcon> images;

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
    }

    public static void registerImages() {

        File file_add_kunde = new File("src/main/resources/Images/kunden.png");
        File file_edit_row = new File("src/main/resources/Images/edit.png");
        File file_delete_row = new File("src/main/resources/Images/delete.png");

        try {
            //Image "Kunde hinzufügen"
            BufferedImage b1 = ImageIO.read(file_add_kunde);
            Image i1 = new ImageIcon(b1).getImage().getScaledInstance(300, 54, Image.SCALE_SMOOTH);
            button_add_kunde = new ImageIcon(i1);

            //Image "Tabellenzeile bearbeiten"
            BufferedImage b2 = ImageIO.read(file_edit_row);
            Image i2 = new ImageIcon(b2).getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH);
            button_edit_row = new ImageIcon(i2);

            //Image "Tabellenzeile löschen"
            BufferedImage b3 = ImageIO.read(file_delete_row);
            Image i3 = new ImageIcon(b3).getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH);
            button_delete_row = new ImageIcon(i3);
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    public static ImageIcon createCircle(File file) throws IOException {
        BufferedImage master = ImageIO.read(file);
        int diameter = Math.min(master.getWidth(), master.getHeight());
        BufferedImage mask = new BufferedImage(master.getWidth(), master.getHeight(), BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = mask.createGraphics();
        applyQualityRenderingHints(g2d);
        g2d.fillOval(0, 0, diameter - 1, diameter - 1);
        g2d.dispose();

        BufferedImage masked = new BufferedImage(diameter, diameter, BufferedImage.TYPE_INT_ARGB);
        g2d = masked.createGraphics();
        applyQualityRenderingHints(g2d);
        int x = (diameter - master.getWidth()) / 2;
        int y = (diameter - master.getHeight()) / 2;
        g2d.drawImage(master, x, y, null);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_IN));
        g2d.drawImage(mask, 0, 0, null);
        g2d.dispose();

        ImageIcon imageIcon = new ImageIcon(masked);
        Image image = imageIcon.getImage();
        Image newimg = image.getScaledInstance(30, 30,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way

        return new ImageIcon(newimg);
    }

    private static void applyQualityRenderingHints(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
    }
}
