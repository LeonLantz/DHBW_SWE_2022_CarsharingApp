package control;

import de.dhbwka.swe.utils.event.EventCommand;
import de.dhbwka.swe.utils.event.GUIEvent;
import de.dhbwka.swe.utils.event.IGUIEventListener;
import de.dhbwka.swe.utils.event.IUpdateEventListener;
import de.dhbwka.swe.utils.event.IUpdateEventSender;
import de.dhbwka.swe.utils.event.UpdateEvent;
import de.dhbwka.swe.utils.gui.SimpleListComponent;
import de.dhbwka.swe.utils.model.IDepictable;
import de.dhbwka.swe.utils.model.IPersistable;
import de.dhbwka.swe.utils.util.AppLogger;
import de.dhbwka.swe.utils.util.CSVReader;
import de.dhbwka.swe.utils.util.CSVWriter;
import de.dhbwka.swe.utils.util.CommonEntityManager;
import de.dhbwka.swe.utils.util.IAppLogger;

import gui.EditIDepicatableDialog;
import gui.GUIKundeAnlegen;
import gui.MainComponentMitNavBar;
import gui.customComponents.CustomTableComponent;
import gui.GUIFahrzeugAnlegen;
import model.Bild;
import model.Fahrzeug;
import model.Kunde;
import util.ElementFactory;
import util.WorkingCSVReader;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

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
		modelClasses.put("Bilder.csv", Bild.class);

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
		System.out.println(ge.getCmdText());

		if (ge.getCmd().equals(MainComponentMitNavBar.Commands.BUTTON_PRESSED)) {
			EditIDepicatableDialog editIDepicatableDialog;

			if (ge.getData() == Kunde.class) {
				GUIKundeAnlegen guiKundeAnlegen = new GUIKundeAnlegen(this);
				editIDepicatableDialog = new EditIDepicatableDialog(guiKundeAnlegen, new Dimension(500, 400));
				editIDepicatableDialog.setVisible(true);
			}else if (ge.getData() == Fahrzeug.class) {
				GUIFahrzeugAnlegen guiFahrzeugAnlegen = new GUIFahrzeugAnlegen(this);
				editIDepicatableDialog = new EditIDepicatableDialog(guiFahrzeugAnlegen, new Dimension(500, 700));
				editIDepicatableDialog.setVisible(true);
			}

		}else if (ge.getCmdText().equals(GUIFahrzeugAnlegen.Commands.ADD_FAHRZEUG.cmdText)) {
			logger.debug( ge.getData().toString() );
			String[] fahrzeugAtts = (String[])ge.getData();
			try {
				// element wird erzeugt und in ElementManager gespeichert
				elementFactory.createElement(Fahrzeug.class, fahrzeugAtts);
				fireUpdateEvent( new UpdateEvent(this, Commands.SET_FAHRZEUGE, entityManager.findAll(Fahrzeug.class) ) );
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if (ge.getCmdText().equals(CustomTableComponent.Commands.DELETE_ROW.cmdText)) {
			System.out.println("TODO: Objekt löschen");
			//entityManager.remove((IPersistable)ge.getData());
			//fireUpdateEvent( new UpdateEvent(this, Commands.SET_KUNDEN, entityManager.findAll(Kunde.class) ) );
		}else if (ge.getCmdText().equals(CustomTableComponent.Commands.EDIT_ROW.cmdText)) {
			System.out.println(ge.getData());

			IDepictable iDepictable = (IDepictable)ge.getData();
			String iD = iDepictable.getElementID().toString();

			List<IPersistable> allImages = entityManager.findAll(Bild.class);
			List<Bild> bildList = new ArrayList<>();
			for (IPersistable iPersistable : allImages) {
				bildList.add((Bild) iPersistable);
			}

			bildList = bildList.stream()
					.filter(b -> b.getSecondaryKey().equals(iD))
					.collect(Collectors.toList());

			EditIDepicatableDialog editIDepicatableDialog;

			if (iDepictable.getClass() == Kunde.class) {
				GUIKundeAnlegen guiKundeAnlegen = new GUIKundeAnlegen(this, iDepictable, bildList);
				editIDepicatableDialog = new EditIDepicatableDialog(guiKundeAnlegen, new Dimension(500, 700));
				editIDepicatableDialog.setVisible(true);
			}else if (iDepictable.getClass() == Fahrzeug.class) {
				GUIFahrzeugAnlegen guiFahrzeugAnlegen = new GUIFahrzeugAnlegen(this, iDepictable, bildList);
				editIDepicatableDialog = new EditIDepicatableDialog(guiFahrzeugAnlegen, new Dimension(500, 700));
				editIDepicatableDialog.setVisible(true);
			}


		}else if (ge.getCmdText().equals(SimpleListComponent.Commands.ELEMENT_SELECTED.cmdText)) {
			if (ge.getData().getClass() == Bild.class) {
				Bild bild = (Bild) ge.getData();

				ImageIcon imageIcon = bild.getImage();


				JOptionPane.showMessageDialog(null, bild.getAttributeValueOf(Bild.Attributes.TITLE), bild.getAttributeValueOf(Bild.Attributes.FILEPATH), JOptionPane.INFORMATION_MESSAGE, imageIcon);

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
