package app;

import control.CSControllerReinerObserverUndSender;
import gui.MainComponentMitNavBar;
import util.CSHelp;

import javax.swing.*;

public class StartApp {

    public static void main(String[] args) {
        new StartApp();
    }

    public StartApp() {
        CSHelp.init();
        initWithObserver();
    }

    public void initWithObserver() {
        MainComponentMitNavBar mainComp = new MainComponentMitNavBar(null);

        CSControllerReinerObserverUndSender controller = new CSControllerReinerObserverUndSender();
        controller.addObserver( mainComp );
        mainComp.addObserver( controller );
        controller.init();

        JFrame frame = new JFrame("Carsharing Buchungssoftware");
        frame.setSize(1080, 720);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.add(mainComp);
        frame.setVisible(true);
        //IOUtilities.openInJFrame(mainComp, 600, 500, 800, 300, "CarsharingApp", null, true);
    }
}
