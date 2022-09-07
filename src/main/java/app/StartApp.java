package app;

import control.CSControllerReinerObserverUndSender;
import de.dhbwka.swe.utils.model.IPersistable;
import de.dhbwka.swe.utils.util.CommonEntityManager;
import gui.MainComponentMitNavBar;
import model.Fahrzeug;
import model.Kunde;
import util.CSHelp;
import util.CSVHelper;
import util.PManager;
import util.WorkingCSVWriter;

import javax.swing.*;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StartApp {

    public static void main(String[] args) throws Exception {
        new StartApp(args);
    }

    public StartApp(String[] args) throws Exception {
        CSHelp.init();
        initWithObserver(getParameterArgument(args, "d"), getParameterArgument(args, "p"));
    }

    private static final String sp = File.separator;

    public void initWithObserver(String csvDirectory, String propFile) throws Exception {
        if (!csvDirectory.startsWith(sp)) csvDirectory = sp + csvDirectory;
        if (!csvDirectory.endsWith(sp)) csvDirectory = csvDirectory+sp;
        csvDirectory = csvDirectory.replaceAll("\\\\","/");
        propFile = propFile.replaceAll("\\\\","/");

        MainComponentMitNavBar mainComp = new MainComponentMitNavBar(new PManager(propFile).getPropertyManager(), csvDirectory);

        CSControllerReinerObserverUndSender controller = new CSControllerReinerObserverUndSender();
        controller.addObserver( mainComp );
        mainComp.addObserver( controller );
        controller.init(csvDirectory, propFile);

        //UIManager.put("Button.font", CSHelp.lato.deriveFont(14f));

        JFrame frame = new JFrame("Carsharing Buchungssoftware");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setSize(1080, 720);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.add(mainComp);
        frame.setVisible(true);
        //IOUtilities.openInJFrame(mainComp, 600, 500, 800, 300, "CarsharingApp", null, true);

        String finalCsvDirectory = csvDirectory;
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (JOptionPane.showConfirmDialog(frame,
                        "Möchten Sie die Anwendung wirklich schließen\nund alle Änderungen speichern?", "Carsharing Buchungssoftware",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, CSHelp.imageList.get("icon_closeandsave.png")) == JOptionPane.YES_OPTION) {
                    System.out.println("Writing all persisted entities to CSVs...");
                    try {
                        controller.writeAllCSVData(finalCsvDirectory);
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                    System.exit(0);
                }
            }
        });




    }

    /**
     * This method can be used to dynamically retrieve argument values from given run parameters during a JAR run.
     * @param args Standard arguments passed by a Main method
     * @param parameter Choose which parameter should be selected (e.g. "d")
     * @return String containing the argument
     */
    public String getParameterArgument(String[] args, String parameter) {
        int VMOptionPos = Arrays.stream(args).collect(Collectors.toList()).indexOf("-" + parameter);
        if (VMOptionPos < 0) {
            throw new IllegalArgumentException("No run parameter \"-"+parameter+"\" given.");
        }
        if (VMOptionPos+1 >= args.length || args[VMOptionPos+1].startsWith("-")) {
            throw new IllegalArgumentException("No argument for run parameter \"-"+parameter+"\" given.");
        }
        return args[VMOptionPos+1];
    }

    public static void setUIFont (javax.swing.plaf.FontUIResource f){
        java.util.Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get (key);
            if (value instanceof javax.swing.plaf.FontUIResource)
                UIManager.put (key, f);
        }
    }


}
