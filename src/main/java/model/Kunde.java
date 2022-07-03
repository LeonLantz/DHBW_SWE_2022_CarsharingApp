package model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.dhbwka.swe.utils.model.Attribute;
import de.dhbwka.swe.utils.model.IDepictable;
import de.dhbwka.swe.utils.model.IPersistable;
/**
 * Klasse Kunde als Beispiel einer Modellklasse mit Attributen
 * 
 * Alle Attributwerte der Klasse werden in de.dhbwka.swe.utils.model.Attribute-Instanzen
 * abgelegt.
 * Die Attribute 
 * 
 * - werden in der Enum Attributes definiert
 * - bzw. deren Werte im Konstruktor gesetzt
 * - können in Form der Attribute selbst geholt bzw. geändert werden.
 *
 */
public class Kunde implements IDepictable, IPersistable {

	public enum CSVPositions{
		ID,
		VORNAME,
		NACHNAME,
		ENGAGIERT,
		BESCHREIBUNG
	}

	
	/**
	 * Enum zur Definition der Attribut-Instanzen
	 * 
	 * Hier müssen lediglich die Attribute und deren Eigenschaften deklariert werden
	 *
	 */
	public enum Attributes {

		/**
		 * Attribute erzeugen, die folgende Einstellungen haben:
		 * Name, Klasse (Typ), sichtbar, aenderbar, editierbar
		 */
		ID( "ID", String.class, true, false, false ),
		VORNAME( "Vorname", String.class, true, true, true ),
		NACHNAME( "Nachname", String.class, true, true, true ),
		ENGAGIERT( "engagiert", Boolean.class, true, true, true ),
		BESCHREIBUNG( "Beschreibung", String.class, true, true, true );

		private String name;
		private boolean visible;
		private boolean modifiable, editable;
		private Class<?> clazz;

		private Attributes( String name, Class<?> clazz, boolean visible, boolean modifyable, boolean editable ) {
			this.name = name;
			this.visible = visible;
			this.clazz = clazz;
			this.modifiable = modifyable;
			this.editable = editable;
		}
		
		private Attribute createAttribute( Object dedicatedInstance, Object value, Object defaultValue ) {
			return new Attribute( this.name, dedicatedInstance, this.clazz, value, 
					defaultValue, this.visible, this.modifiable, this.editable, false );  // mandatory
		}
	}

	/**
	 * get all names of the attributes for the Person
	 * 
	 * @return the complete list of attribute names
	 */
	public final static String[] getAllAttributeNames() {
		return getAttributeNames( false );
	}

	/**
	 * get the names of all attributes for the Person
	 * 
	 * @param  filterVisibleAttributes if true, only names of visible attributes are
	 *                                     listed, if false, all attribute names are
	 *                                     listed.
	 * @return                         the list of attribute names
	 */
	public final static String[] getAttributeNames( boolean filterVisibleAttributes ) {
		List<String> names = new ArrayList<>();
		Attributes[] pA = Attributes.values();

		for( int i = 0 ; i < pA.length ; i++ ){

			if( !filterVisibleAttributes ){
				names.add( pA[ i ].name );
			}
			else if( pA[ i ].visible ){
				names.add( pA[ i ].name );
			}
		}
		return names.toArray( new String[ names.size() ] );
	}

	private Attribute[] attArr = new Attribute[ Attributes.values().length ];

	/**
	 * Default-Konstruktor
	 */
	public Kunde() {
		this( null, null, null, "--", false );
	}
	
	public Kunde( String vorName, String nachName ) {
		this( null, vorName, nachName, "--", false );
	}

	public Kunde( String vorName, String nachName, String description ) {
		this( null, vorName, nachName, description, false );
	}

	/**
	 * Hauptkonstruktor, wird von allen anderen Konstruktoren aufgerufen.
	 * Die Attribute werden in der Enum Attributes deklariert. Hier werden nur die Werte übergeben.
	 * @param iD 
	 * @param vorName
	 * @param nachName
	 * @param description
	 * @param engaged
	 */
	public Kunde( String iD, String vorName, String nachName, String description, boolean engaged ) {
		super();

		boolean modifiable = true;
		
		String randID = UUID.randomUUID().toString();
		this.attArr[ Attributes.ID.ordinal() ] = Attributes.ID.createAttribute( this, ( iD == null || iD.isEmpty() ? randID : iD ), randID );
		this.attArr[ Attributes.NACHNAME.ordinal() ] = Attributes.NACHNAME.createAttribute( this, nachName, "--" );
		this.attArr[ Attributes.VORNAME.ordinal() ] = Attributes.VORNAME.createAttribute( this, vorName, "--" );
		this.attArr[ Attributes.BESCHREIBUNG.ordinal() ] = Attributes.BESCHREIBUNG.createAttribute( this, description, "" );
		this.attArr[ Attributes.ENGAGIERT.ordinal() ] = Attributes.ENGAGIERT.createAttribute(this, engaged, Boolean.TRUE );
	}

	public <T> T getAttributeValueOf( Attributes attribute ) {
		return (T)this.attArr[ attribute.ordinal() ].getValue();
	}

	public <T> void setAttributeValueOf( Attributes attribute, T value ) throws Exception {
		if( ! attribute.modifiable )
			throw new IllegalArgumentException( "attribute '" + attribute.name() + "' is not modifiable!" );
		if( value.getClass() == this.attArr[ attribute.ordinal() ].getClass() )
			this.attArr[ attribute.ordinal() ].setValue( value );
		else
			throw new IllegalArgumentException( "wrong class type for attribute '" + attribute.name() + "'!" );
	}

	@Override
	public String getVisibleText() {
		return this.toString();
	}

	@Override
	public String toString() {
		return this.attArr[ Attributes.NACHNAME.ordinal() ].getValue() + ", "
					+ this.attArr[ Attributes.VORNAME.ordinal() ].getValue() + ", "
					+ this.attArr[ Attributes.BESCHREIBUNG.ordinal() ].getValue();
	}

	@Override
	public Class<?> getElementClass() {
		return this.getClass();
	}

	@Override
	public String getElementID() {
		return this.attArr[ Attributes.ID.ordinal() ].getValue().toString();
	}

	/**
	 * sets the description
	 *
	 * @param description the description that will be set
	 */
	public void setDescription( String description ) {
		try{
			this.attArr[ Attributes.BESCHREIBUNG.ordinal() ].setValue( description );
		}
		catch( Exception e ){
			e.printStackTrace();
		}
	}

	@Override
	public Attribute[] getAttributeArray() {
		return this.attArr;
	}

	// von IPersistable:
	@Override
	public Object getPrimaryKey() {
		return this.attArr[ Attributes.ID.ordinal() ].getValue();
	}

}
