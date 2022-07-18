package control;

import de.dhbwka.swe.utils.event.*;
import de.dhbwka.swe.utils.util.*;
import gui.MainComponentMitTabbedPane;
import model.Kunde;
import util.ElementFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class CSControllerTest implements IGUIEventListener, IUpdateEventSender {

    public enum Commands implements EventCommand {

        /**
         * Command:  ID + gelieferter Payload-Typ
         */
        SET_KUNDEN( "Controller.setKunden", List.class );

        public final Class<?> payloadType;
        public final String cmdText;

        private Commands( String cmdText, Class<?> payloadType ) {
            this.cmdText = cmdText;
            this.payloadType = payloadType;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getCmdText() {
            return this.cmdText;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Class<?> getPayloadType() {
            return this.payloadType;
        }
    }

    /**
     * alle Listener, die auf Updates warten
     */
    List<EventListener> allListeners = new ArrayList<>();

    /**
     * entityManager für die Elemente (können auch mehrere EntityManager sein ...
     */
    CommonEntityManager entityManager = new CommonEntityManager();

    /**
     * entityFactory für die Elemente
     */
    ElementFactory elementFactory = new ElementFactory( entityManager );

    /**
     * Lesen und schreiben der Elemente. Attribut kann (muss nicht)
     * zum Lesen/Schreiben wiederverwendet werden (csvReader = new CSVReader() ...)
     */
    CSVReader csvReader = null;
    CSVWriter csvWriter = null;

    IAppLogger logger = AppLogger.getInstance();


    public CSControllerTest() {
        logger.setSeverity(  IAppLogger.Severity.DEBUG_LOW );
    }

    /**
     * "start" the controller.
     * Das muss separat gemacht werden, da beim Verknüpfen der Observer (Controller+MainGUI)
     * das Laden der Daten bereits durchgeführt wurde und somit der UpdateEvent "ins Leere" ging
     */
    public void init() {
        //loadCSVData();
        //fireUpdateEvent( new UpdateEvent(this, CSControllerTest.Commands.SET_KUNDEN, entityManager.findAll( Kunde.class) ) );

    }

    private void loadCSVData() throws IOException {

        String filePath = this.getClass().getResource("/CSVFiles/Kunden.csv").getPath();
        CSVReader csvReader = new CSVReader( filePath );
        List<String[]> csvData = csvReader.readData();

        csvData.forEach( e -> {
            try {
                elementFactory.createElement(Kunde.class, e);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
    }








    // fuer alle GUI-Elemente, die aktualisiert werden sollen:
    @Override
    public boolean addObserver(EventListener eL) {
        return this.allListeners.add(eL);
    }

    @Override
    public boolean removeObserver(EventListener eL) {
        return this.allListeners.remove(eL);
    }

    @Override
    public void processGUIEvent(GUIEvent ge) {
        logger.debug("Hier ist der Controller!   Event: " + ge);

/*
        if( ge.getCmd() == MainComponentMitTabbedPane.Commands.ADD_KUNDE ) {
            logger.debug( ge.getData().toString() );
            String[] kundenAtts = (String[])ge.getData();
            try {
                // element wird erzeugt und in ElementManager gespeichert
                elementFactory.createElement(Kunde.class, kundenAtts);
                //fireUpdateEvent( new UpdateEvent(this, CSControllerReinerObserverUndSender.Commands.SET_KUNDEN, entityManager.findAll( Kunde.class) ) );

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
*/
    }

    /**
     * zum Senden von UpdateEvents an entsprechende Listener (IUpdateEventListener)
     * @param ue
     */
    private void fireUpdateEvent( UpdateEvent ue ) {
        for (EventListener eventListener : allListeners) {
            if( eventListener instanceof IUpdateEventListener ) {
                ((IUpdateEventListener)eventListener).processUpdateEvent(ue);
            }
        }
    }
}
