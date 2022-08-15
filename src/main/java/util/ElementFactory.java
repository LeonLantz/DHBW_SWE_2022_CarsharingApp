package util;

import de.dhbwka.swe.utils.model.Gruppe;
import de.dhbwka.swe.utils.model.IPersistable;
import de.dhbwka.swe.utils.model.Person;
import de.dhbwka.swe.utils.util.CommonEntityManager;
import model.*;
import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ElementFactory {

//  now in Class Person:
//	public enum CSVPosPerson{
//		ID,
//		NACHNAME,
//		VORNAME,
//		GEBURTSJAHR,
//		GRUPPEN,
//		BESCHREIBUNG
//	}
	
//  now in Class Gruppe:
//	public enum CSVPosGruppe{
//		ID,
//		NAME,
//		MITGLIEDER,
//		BESCHREIBUNG
//	}

	private CommonEntityManager entityManager = null;
	
	private IPersistable persistableElement = null;

	public ElementFactory(CommonEntityManager em) {
		this.entityManager = em;
	};

	// In case of any errors, this HashMap gets extended (with all references)
	private HashMap<String, String> mapOfUnreferencedElements = new HashMap<>();

	/**
	 * Die createElement-Methode anpassen, Kunde ist als Beispiel realisiert, nun die Factory für die
	 * anderen Klassen genau wie Person (s.u.) anpassen
	 */
	/**
	 * 
	 * @param c the type of the class to be created
	 * @param csvData the data read from a CSV file
	 * @return the instance of the element just created
	 * @throws Exception
	 */
	public IPersistable createElement(Class<?> c, String[] csvData) throws Exception {

		if (c == null) {
			throw new IllegalArgumentException("Klasse muss angegeben werden ( Klasse ist null )!");
		}
		else if( c == Kunde.class ) {
			String id = csvData[ Kunde.CSVPositions.ID.ordinal() ];
			String vorName = csvData[ Kunde.CSVPositions.VORNAME.ordinal() ];
			String nachName = csvData[ Kunde.CSVPositions.NACHNAME.ordinal() ];
			String email = csvData[ Kunde.CSVPositions.EMAIL.ordinal() ];
			String phone = csvData[ Kunde.CSVPositions.PHONE.ordinal() ];
			String iban = csvData[ Kunde.CSVPositions.IBAN.ordinal() ];
			String dateOfBirth = csvData[ Kunde.CSVPositions.DATEOFBIRTH.ordinal() ];
			String last_edit = csvData[ Kunde.CSVPositions.LAST_EDITED.ordinal() ];

			persistableElement = new Kunde( id, vorName, nachName, email, phone, iban, LocalDate.parse(dateOfBirth), LocalDateTime.parse(last_edit));
			/**
			 * hier kämen dann die N:M-Beziehungen hin, s.u. bei Person und Gruppe
			 */
		}
		else if( c == Fahrzeug.class) {
			String id = csvData[ Fahrzeug.CSVPositions.ID.ordinal() ];
			String bezeichnung = csvData[ Fahrzeug.CSVPositions.BEZEICHNUNG.ordinal() ];
			String marke = csvData[ Fahrzeug.CSVPositions.MARKE.ordinal() ];
			String motor = csvData[ Fahrzeug.CSVPositions.MOTOR.ordinal() ];
			String türen = csvData[ Fahrzeug.CSVPositions.TÜREN.ordinal() ];
			String sitze = csvData[ Fahrzeug.CSVPositions.SITZE.ordinal() ];
			String kofferraumvolumen = csvData[ Fahrzeug.CSVPositions.KOFFERRAUMVOLUMEN.ordinal() ];
			String gewicht = csvData[ Fahrzeug.CSVPositions.GEWICHT.ordinal() ];
			String fahrzeugkategorie = csvData[ Fahrzeug.CSVPositions.FAHRZEUGKATEGORIE.ordinal() ];
			String führerscheinklasse = csvData[ Fahrzeug.CSVPositions.FÜHRERSCHEINKLASSE.ordinal() ];
			String nummernschild = csvData[ Fahrzeug.CSVPositions.NUMMERNSCHILD.ordinal() ];
			String tüv_bis = csvData[ Fahrzeug.CSVPositions.TÜV_BIS.ordinal() ];
			String farbe = csvData[ Fahrzeug.CSVPositions.FARBE.ordinal() ];
			String last_edit = csvData[ Fahrzeug.CSVPositions.LAST_EDIT.ordinal() ];

			persistableElement = new Fahrzeug(id, bezeichnung, marke, motor, Integer.decode(türen), Integer.decode(sitze), Integer.decode(kofferraumvolumen), gewicht, Fahrzeugkategorie.fromString(fahrzeugkategorie), führerscheinklasse, nummernschild, LocalDate.parse(tüv_bis), farbe, LocalDateTime.parse(last_edit));
		}
		else if ( c == Bild.class ) {
			String id = csvData[ Bild.CSVPositions.ID.ordinal() ];
			String title = csvData[ Bild.CSVPositions.TITLE.ordinal() ];
			String filePath = csvData[ Bild.CSVPositions.FILEPATH.ordinal() ];
			ImageIcon imageIcon = new ImageIcon(filePath);
			String key = csvData[ Bild.CSVPositions.KEY.ordinal() ];

			persistableElement = new Bild(id, title, filePath, imageIcon, key);
		}
		else if ( c == Standort.class ) {
			String id = csvData[ Standort.CSVPositions.ID.ordinal() ];
			String strasse = csvData[ Standort.CSVPositions.STRASSE.ordinal() ];
			String plz = csvData[ Standort.CSVPositions.PLZ.ordinal() ];
			String ort = csvData[ Standort.CSVPositions.ORT.ordinal() ];
			String land = csvData[ Standort.CSVPositions.LAND.ordinal() ];
			String koordinaten = csvData[ Standort.CSVPositions.KOORDINATEN.ordinal() ];
			String kapazität = csvData[ Standort.CSVPositions.KAPAZITÄT.ordinal() ];
			String last_edit = csvData[ Standort.CSVPositions.LAST_EDIT.ordinal() ];

			persistableElement = new Standort(id, strasse, plz, ort, land, koordinaten, kapazität, LocalDateTime.parse(last_edit));
		}
//		else if( c == Person.class ) {
//			String id = csvData[ Person.CSVPositions.ID.ordinal() ];
//			String nachName = csvData[ Person.CSVPositions.NACHNAME.ordinal() ];
//			String vorName = csvData[ Person.CSVPositions.VORNAME.ordinal() ];
//			String beschreibung = csvData[ Person.CSVPositions.BESCHREIBUNG.ordinal() ];
//
//			persistableElement = new Person( id, vorName, nachName, beschreibung );
//			String gJahr = csvData[ Person.CSVPositions.GEBURTSJAHR.ordinal() ];
//			if (gJahr != null  &&  ! gJahr.isEmpty() ) {
//				int gebJahr  = Integer.parseInt( gJahr );
//				((Person)persistableElement).setGeburtsjahr( gebJahr );
//			}
//
//			String listOfGruppen = csvData[ Person.CSVPositions.GRUPPEN.ordinal() ];
//
//			try{
//				List<IPersistable> realRefs = getReferences(Gruppe.class, listOfGruppen);
//				for( IPersistable iP: realRefs ){
//					((Person)persistableElement).addGroup( (Gruppe)iP );
//				}
//			}
//			catch( Exception e ){
//				mapOfUnreferencedElements.put( id, listOfGruppen );
//			}
//		}
//		else if (c == Gruppe.class) {
//			String id = csvData[ Gruppe.CSVPositions.ID.ordinal() ];
//			String name = csvData[ Gruppe.CSVPositions.NAME.ordinal() ];
//			String beschreibung = csvData[ Gruppe.CSVPositions.BESCHREIBUNG.ordinal() ];
//
//			persistableElement = new Gruppe( id, name, beschreibung );
//
//			String listOfMitglieder = csvData[ Gruppe.CSVPositions.MITGLIEDER.ordinal() ];
//
//			try{
//				List<IPersistable> realRefs = getReferences(Person.class, listOfMitglieder );
//				for( IPersistable iP: realRefs ){
//					((Gruppe)persistableElement).addMitglied( (Person)iP );
//				}
//			}
//			catch( Exception e ){
//				mapOfUnreferencedElements.put( id, listOfMitglieder );
//			}
//		}
		entityManager.persist( persistableElement );

		return persistableElement;
	}

	/**
	 * Ab hier alles so lassen ***********************************************************************
	 */
	
	/**
	 * 
	 * @param c
	 * @param stringIDs
	 * @return
	 * @throws Exception
	 */
	private List<IPersistable> getReferences(Class<?> c, String stringIDs) throws Exception {
		List<IPersistable> refs = new ArrayList<>();
		if( stringIDs == null  ||  stringIDs.isEmpty() ) throw new RuntimeException( "List of refs is empty or null" );
		
		String[] arrIDs = stringIDs.split(",");
		
		for( String sId: arrIDs ){
			if( sId.isEmpty() || sId.indexOf( ' ' ) >= 0 ) throw new RuntimeException( "a reference is empty" );
//			List<IPersistable> ae = entityManager.find(c, "getPrimaryKey", sId );
			IPersistable ae = entityManager.find(c, sId );
			if( ae == null )
				throw new RuntimeException( "Reference '" + sId + "' not found!" );
			refs.add( ae );
		}
		
		return refs;
	}
	
	/**
	 * for compatibility reasons, this method simply delegates to {@link #resolveUnreferencedReferences()}
	 * @throws Exception
	 */
	public void resolveUnresolvedReferences() throws Exception {
		this.resolveUnreferencedReferences();
	}
	
	/**
	 * resolve the unresolved references stored in the local HashMap
	 * @throws Exception
	 */
	public void resolveUnreferencedReferences() throws Exception {
		for( String key : this.mapOfUnreferencedElements.keySet() ){
			IPersistable ip = this.entityManager.find( key );
			String refs = this.mapOfUnreferencedElements.get( key );
			
			if( ip instanceof Person ) {
				List<IPersistable> refList = getReferences(Gruppe.class, refs);
				refList.forEach( e -> ((Person)ip).addGroup( (Gruppe)e ) );
			}
			else if( ip instanceof Gruppe ) {
				List<IPersistable> refList = getReferences(Person.class, refs);
				refList.forEach( e -> ((Gruppe)ip).addMitglied( (Person)e ) );
			}
		}
	}

}
