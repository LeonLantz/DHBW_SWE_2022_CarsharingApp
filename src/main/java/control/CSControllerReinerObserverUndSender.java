package control;

import de.dhbwka.swe.utils.event.EventCommand;
import de.dhbwka.swe.utils.event.GUIEvent;
import de.dhbwka.swe.utils.event.IGUIEventListener;
import de.dhbwka.swe.utils.event.IUpdateEventListener;
import de.dhbwka.swe.utils.event.IUpdateEventSender;
import de.dhbwka.swe.utils.event.UpdateEvent;
import de.dhbwka.swe.utils.util.AppLogger;
import de.dhbwka.swe.utils.util.CSVReader;
import de.dhbwka.swe.utils.util.CSVWriter;
import de.dhbwka.swe.utils.util.CommonEntityManager;
import de.dhbwka.swe.utils.util.IAppLogger;

import gui.CreateKundeView;
import gui.MainComponentMitNavBar;
import gui.customComponents.ObjectCreationPanel;
import model.Fahrzeug;
import model.Fahrzeugkategorie;
import model.Kunde;
import util.ElementFactory;
import util.WorkingCSVReader;
import util.WorkingCSVWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class CSControllerReinerObserverUndSender implements IGUIEventListener, IUpdateEventSender {
	
	/**
	 * Folgende Commands deklarieren! Es sind die Commands, welche der Controller 
	 * z.B. an die MainGUI als UpdateEvent sendet
	 * Die Commands müssen nur oben deklariert werden, der Rest des enums ist copy/paste (s. SWE-utils-GUIs)
	 */
	public enum Commands implements EventCommand {

		/**
		 * Command:  ID + gelieferter Payload-Typ
		 */
		SET_BUCHUNGEN( "Controller.setBuchungen", List.class ),
		SET_FAHRZEUGE( "Controller.setFahrzeuge", List.class ),
		SET_KUNDEN( "Controller.setKunden", List.class ),
		SET_STANDORTE( "Controller.setStandorte", List.class );

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

	public CSControllerReinerObserverUndSender() {
//		logger.setSeverity(  IAppLogger.Severity.DEBUG_LOW );
	}
	
	/**
	 * "start" the controller.
	 * Das muss separat gemacht werden, da beim Verknüpfen der Observer (Controller+MainGUI)
	 * das Laden der Daten bereits durchgeführt wurde und somit der UpdateEvent "ins Leere" ging
	 */
	public void init(String csvDirectory, String propDirectory) {
		try {
			loadCSVData(csvDirectory);
			fireUpdateEvent( new UpdateEvent(this, Commands.SET_KUNDEN, entityManager.findAll( Kunde.class) ) );
			fireUpdateEvent( new UpdateEvent(this, Commands.SET_FAHRZEUGE, entityManager.findAll( Fahrzeug.class) ) );
			System.out.println(entityManager.findAll(Fahrzeug.class));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadCSVData(String csvDirectory) throws IOException {
		// Fixed CSV Reading and Writing (works even with built Maven JARs)

		Map<String, Class> modelClasses = new HashMap<>();
		modelClasses.put("Kunden.csv", Kunde.class);
		modelClasses.put("Fahrzeuge.csv", Fahrzeug.class);

		for (String fileName : modelClasses.keySet()) {
			System.out.println(fileName);

			WorkingCSVReader workingCSVReader = new WorkingCSVReader(csvDirectory+fileName, ";", true);
			List<String[]> csvData = workingCSVReader.readData();

			csvData.forEach( e -> {
				try {
					elementFactory.createElement(modelClasses.get(fileName), e);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			});
		}



//		WorkingCSVReader workingCSVReader = new WorkingCSVReader(csvDirectory+"Fahrzeuge.csv", ";", true);
//		List<String[]> csvData = workingCSVReader.readData();
//
//		//TODO: DELETE these two lines
//		// since those are only demo code to show csv writing functionality
//		WorkingCSVWriter workingCSVWriter = new WorkingCSVWriter(csvDirectory+"writetest.csv", ";", "#ID;Name;Vorname;engagiert;Beschreibung");
//		workingCSVWriter.writeData(csvData);
//
//		csvData.forEach( e -> {
//			try {
//				elementFactory.createElement(Fahrzeug.class, e);
//			} catch (Exception e1) {
//				e1.printStackTrace();
//			}
//		});
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

	// die GUI-Events verarbeiten, die von den GUIs kommen:
	@Override
	public void processGUIEvent(GUIEvent ge) {

		//logger.debug("Hier ist der Controller!   Event: " + ge);

		//System.out.println(ge.getCmdText());

		if (ge.getCmd().equals(MainComponentMitNavBar.Commands.BUTTON_PRESSED)) {
			System.out.println(ge.getData());

			CreateKundeView createKundeView = new CreateKundeView();

			ObjectCreationPanel ocp = ObjectCreationPanel.builder("Kunden")
					.observer(this)
					.build();



			//TODO: create CustomComponent for value input of model classes
//			JPanel content = new JPanel();
//			content.setPreferredSize(new Dimension(500,700));
//			JButton button = new JButton("Hallo");
//			button.addActionListener(new ActionListener() {
//				@Override
//				public void actionPerformed(ActionEvent e) {
//					System.out.println("Neuer Kunde erfolgreich angelegt");
//				}
//			});
//			content.add(button);
//			content.add(new JLabel("Tschüss"));
			//----------

			createKundeView.setContent(ocp);
			createKundeView.setVisible(true);
		}else if (ge.getCmdText().equals(ObjectCreationPanel.Commands.ADD_KUNDE.cmdText)) {
			logger.debug( ge.getData().toString() );
			String[] kundenAtts = (String[])ge.getData();
			try {
				// element wird erzeugt und in ElementManager gespeichert
				elementFactory.createElement(Kunde.class, kundenAtts);
				fireUpdateEvent( new UpdateEvent(this, Commands.SET_KUNDEN, entityManager.findAll( Kunde.class) ) );

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
/*
		if( ge.getCmd() == MainComponentMitTabbedPane.Commands.ADD_KUNDE ) {
			logger.debug( ge.getData().toString() );
			String[] kundenAtts = (String[])ge.getData();
			try {
				// element wird erzeugt und in ElementManager gespeichert
				elementFactory.createElement(Kunde.class, kundenAtts);
				fireUpdateEvent( new UpdateEvent(this, Commands.SET_KUNDEN, entityManager.findAll( Kunde.class) ) );

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
