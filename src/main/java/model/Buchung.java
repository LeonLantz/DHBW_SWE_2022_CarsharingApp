package model;

import de.dhbwka.swe.utils.model.Attribute;
import de.dhbwka.swe.utils.model.IDepictable;
import de.dhbwka.swe.utils.model.IPersistable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Buchung implements IDepictable, IPersistable {

    public enum CSVPositions{

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
        ID( "   ID", String.class, false, false, false ),
        BUCHUNGSNUMMER( "Buchungsnummer", String.class, true, false, false ),
        KUNDE( "Kunde", Kunde.class, true, false, false ),
        FAHRZEUG( "Fahrzeug", Fahrzeug.class, true, false, false ),
        START_DATE( "Start", Date.class, true, false, false ),
        END_DATE( "Ende", Date.class, true, false, false ),
        STATUS( "Status", String.class, true, false, false ),
        LAST_EDIT( "zul.", Date.class, true, false, false );

        private final String name;
        private final boolean visible;
        private final boolean modifiable;
        private final boolean editable;
        private final Class<?> clazz;

        Attributes(String name, Class<?> clazz, boolean visible, boolean modifyable, boolean editable) {
            this.name = name;
            this.visible = visible;
            this.clazz = clazz;
            this.modifiable = modifyable;
            this.editable = editable;
        }

        private Attribute createAttribute(Object dedicatedInstance, Object value, Object defaultValue ) {
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
        Buchung.Attributes[] pA = Buchung.Attributes.values();

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

    private final Attribute[] attArr = new Attribute[ Buchung.Attributes.values().length ];

    /**
     * Default-Konstruktor
     */
    public Buchung() {
        this( null, null, null, null, null, null, null, null);
    }

    public Buchung(String buchungsnummer, Kunde kunde, Fahrzeug fahrzeug, Date start_date, Date end_date, Buchungsstatus status, Date last_edit) {
        this(null, buchungsnummer, kunde, fahrzeug, start_date, end_date, status, last_edit);
    }

    public Buchung(String iD, String buchungsnummer, Kunde kunde, Fahrzeug fahrzeug, Date start_date, Date end_date, Buchungsstatus status, Date last_edit ) {
        super();

        String randID = UUID.randomUUID().toString();
        this.attArr[ Buchung.Attributes.ID.ordinal() ] = Buchung.Attributes.ID.createAttribute( this, ( iD == null || iD.isEmpty() ? randID : iD ), randID );
        this.attArr[ Attributes.BUCHUNGSNUMMER.ordinal() ] = Buchung.Attributes.BUCHUNGSNUMMER.createAttribute( this, buchungsnummer, null );
        this.attArr[ Attributes.KUNDE.ordinal() ] = Buchung.Attributes.KUNDE.createAttribute( this, kunde, null );
        this.attArr[ Attributes.FAHRZEUG.ordinal() ] = Buchung.Attributes.FAHRZEUG.createAttribute( this, fahrzeug, null );
        this.attArr[ Attributes.START_DATE.ordinal() ] = Buchung.Attributes.START_DATE.createAttribute( this, start_date, null );
        this.attArr[ Attributes.END_DATE.ordinal() ] = Buchung.Attributes.END_DATE.createAttribute( this, end_date, null );
        this.attArr[ Attributes.STATUS.ordinal() ] = Buchung.Attributes.STATUS.createAttribute( this, status, null );
        this.attArr[ Attributes.LAST_EDIT.ordinal() ] = Buchung.Attributes.LAST_EDIT.createAttribute(this, last_edit, "" );
    }

    public <T> T getAttributeValueOf( Buchung.Attributes attribute ) {
        return (T)this.attArr[ attribute.ordinal() ].getValue();
    }

    public <T> void setAttributeValueOf(Buchung.Attributes attribute, T value ) throws Exception {
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
        return this.attArr[ Attributes.BUCHUNGSNUMMER.ordinal() ].getValue() + ", "
                + this.attArr[ Attributes.KUNDE.ordinal() ].getValue() + ", "
                + this.attArr[ Attributes.FAHRZEUG.ordinal() ].getValue();
    }

    @Override
    public Class<?> getElementClass() {
        return this.getClass();
    }

    @Override
    public String getElementID() {
        return this.attArr[ Buchung.Attributes.ID.ordinal() ].getValue().toString();
    }


    @Override
    public Attribute[] getAttributeArray() {
        return this.attArr;
    }

    // von IPersistable:
    @Override
    public Object getPrimaryKey() {
        return this.attArr[ Buchung.Attributes.ID.ordinal() ].getValue();
    }

}
