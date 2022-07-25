package app;

import control.CSControllerReinerObserverUndSender;
import gui.MainComponentMitNavBar;
import util.CSHelp;

import javax.swing.*;
import java.io.File;
import java.util.Arrays;
import java.util.stream.Collectors;

public class StartApp {

    public static void main(String[] args) {
        new StartApp(args);
    }

    public StartApp(String[] args) {
        CSHelp.init();
        initWithObserver(getParameterArgument(args, "d"), getParameterArgument(args, "p"));
    }

    public void initWithObserver(String csvDirectory, String propDirectory) {
        MainComponentMitNavBar mainComp = new MainComponentMitNavBar(null);

        CSControllerReinerObserverUndSender controller = new CSControllerReinerObserverUndSender();
        controller.addObserver( mainComp );
        mainComp.addObserver( controller );
        controller.init(csvDirectory, propDirectory);

        JFrame frame = new JFrame("Carsharing Buchungssoftware");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1080, 720);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.add(mainComp);
        frame.setVisible(true);
        //IOUtilities.openInJFrame(mainComp, 600, 500, 800, 300, "CarsharingApp", null, true);
    }

    private static final String sp = File.separator;

    public String getParameterArgument(String[] args, String parameter) {
        int VMOptionPos = Arrays.stream(args).collect(Collectors.toList()).indexOf("-" + parameter);
        if (VMOptionPos < 0) {
            throw new IllegalArgumentException("No run parameter \"-"+parameter+"\" given.");
        }
        if (VMOptionPos+1 >= args.length || args[VMOptionPos+1].startsWith("-")) {
            throw new IllegalArgumentException("No argument for run parameter \"-"+parameter+"\" given.");
        }
        String arg = args[VMOptionPos+1];
        if (!arg.startsWith(sp)) arg = sp + arg;
        if (!arg.endsWith(sp)) arg = arg+sp;
        return arg;
    }
}
