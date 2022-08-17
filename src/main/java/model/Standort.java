package model;

import de.dhbwka.swe.utils.model.Attribute;
import de.dhbwka.swe.utils.model.IDepictable;
import de.dhbwka.swe.utils.model.IPersistable;

import java.io.File;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Standort implements IDepictable, IPersistable {
    public enum CSVPositions{
        ID,
        STRASSE,
        PLZ,
        ORT,
        LAND,
        KOORDINATEN,
        KAPAZITÄT,
        LAST_EDIT

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
        ID( "ID", String.class, false, false, false ),
        PLZ( "PLZ", String.class, true, false, false ),
        ORT( "Ort", String.class, true, false, false ),
        STRASSE( "Straße", String.class, true, false, false ),
        LAND( "Land", String.class, false, false, false ),
        KOORDINATEN( "Koordinaten", String.class, true, false, false ),
        KAPAZITÄT( "Kapazität", String.class, true, false, false ),
        LAST_EDIT( "Zeitstempel", LocalDateTime.class, true, false, false );

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

    private final Attribute[] attArr = new Attribute[ Standort.Attributes.values().length ];

    /**
     * Default-Konstruktor
     */
    public Standort() {
        this( null, null, null, null, null, null, null, null);
    }

    public Standort(String strasse, String plz, String ort, String land, String koordinaten, String kapazität, LocalDateTime last_edit) {
        this(null, strasse, plz, ort, land, koordinaten, kapazität, last_edit);
    }

    public Standort(String iD, String strasse, String plz, String ort, String land, String koordinaten, String kapazität, LocalDateTime last_edit ) {
        super();

        String randID = UUID.randomUUID().toString();
        this.attArr[ Attributes.ID.ordinal() ] = Standort.Attributes.ID.createAttribute( this, ( iD == null || iD.isEmpty() ? randID : iD ), randID );
        this.attArr[ Attributes.STRASSE.ordinal() ] = Attributes.STRASSE.createAttribute( this, strasse, null );
        this.attArr[ Attributes.PLZ.ordinal() ] = Attributes.PLZ.createAttribute( this, plz, null );
        this.attArr[ Attributes.ORT.ordinal() ] = Attributes.ORT.createAttribute( this, ort, null );
        this.attArr[ Attributes.LAND.ordinal() ] = Attributes.LAND.createAttribute( this, land, null );
        this.attArr[ Attributes.KOORDINATEN.ordinal() ] = Attributes.KOORDINATEN.createAttribute( this, koordinaten, null );
        this.attArr[ Attributes.KAPAZITÄT.ordinal() ] = Attributes.KAPAZITÄT.createAttribute( this, kapazität, null );
        this.attArr[ Attributes.LAST_EDIT.ordinal() ] = Attributes.LAST_EDIT.createAttribute(this, last_edit, "" );
    }

    public <T> T getAttributeValueOf( Standort.Attributes attribute ) {
        return (T)this.attArr[ attribute.ordinal() ].getValue();
    }

    public <T> void setAttributeValueOf(Standort.Attributes attribute, T value ) throws Exception {
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
        return this.attArr[ Attributes.ORT.ordinal() ].getValue() + ", "
                + this.attArr[ Attributes.STRASSE.ordinal() ].getValue() + ", "
                + this.attArr[ Attributes.KAPAZITÄT.ordinal() ].getValue();
    }

    @Override
    public Class<?> getElementClass() {
        return this.getClass();
    }

    @Override
    public String getElementID() {
        return this.attArr[ Standort.Attributes.ID.ordinal() ].getValue().toString();
    }


    @Override
    public Attribute[] getAttributeArray() {
        return this.attArr;
    }

    // von IPersistable:
    @Override
    public Object getPrimaryKey() {
        return this.attArr[ Standort.Attributes.ID.ordinal() ].getValue();
    }

}
