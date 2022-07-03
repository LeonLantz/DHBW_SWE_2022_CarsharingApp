package control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.net.URL;

import de.dhbwka.swe.utils.event.GUIEvent;
import de.dhbwka.swe.utils.event.IGUIEventListener;
import de.dhbwka.swe.utils.event.IGUIEventSender;
import de.dhbwka.swe.utils.event.IUpdateEventSender;
import de.dhbwka.swe.utils.model.IDepictable;
import de.dhbwka.swe.utils.util.AppLogger;
import de.dhbwka.swe.utils.util.CSVReader;
import de.dhbwka.swe.utils.util.CSVWriter;
import de.dhbwka.swe.utils.util.CommonEntityManager;
import de.dhbwka.swe.utils.util.GenericEntityManager;
import de.dhbwka.swe.utils.util.IAppLogger;
import gui.MainComponent;
import model.Standort;
import util.ElementFactory;

public class CSController implements IGUIEventListener, IUpdateEventSender {
	
	/**
	 * Referenz zur Main-GUI, ist nicht erforderlich, wenn alles 
	 * vollständig mit Observer realisiert wird
	 */
	MainComponent mainComponent = null;
	
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

	public CSController(MainComponent mainComponent) {
		this.mainComponent = mainComponent;
		mainComponent.addObserver(this);
		logger.setSeverity(  IAppLogger.Severity.DEBUG_LOW );
		
		try {
			loadCSVData();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private void loadCSVData() throws IOException {
		/**
		 * Hier sollen alle CSV-Daten gelesen werden 
		 */
		/**
		 * exemplarisch für Kunden
		 */
		String filePath = this.getClass().getResource("/CSVFiles/Kunden.csv").getPath();  // ohne "file:" am Anfang
		CSVReader csvReader = new CSVReader( filePath );
		List<String[]> csvData = csvReader.readData();
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

		logger.debug("Hier ist der Controller!   Event: " + ge);
		
		if( ge.getData() instanceof Standort ) {
			logger.debug( ge.getData().toString() );
			
			
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	}

	public void setElements(List<IDepictable> testData) {
		mainComponent.setElements(testData);		
	}


}
