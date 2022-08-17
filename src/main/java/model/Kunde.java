package model;


import de.dhbwka.swe.utils.model.Attribute;
import de.dhbwka.swe.utils.model.IDepictable;
import de.dhbwka.swe.utils.model.IPersistable;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
        EMAIL,
        PHONE,
        IBAN,
        DATEOFBIRTH,
        LAST_EDITED
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
        VORNAME( "Vorname", String.class, true, false, false ),
        NACHNAME( "Nachname", String.class, true, false, false ),
        EMAIL( "Email", String.class, true, false, false ),
        PHONE("Telefon", String.class, true, false, false),
        IBAN("Kontonummer", String.class, false, false, false),
        DATEOFBIRTH( "Geburtsdatum", LocalDate.class, true, false, false ),
        LAST_EDITED( "Zeitstempel", LocalDateTime.class, true, false, false );

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
        public String getName() {
            return this.name;
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

    private static String csvFileName = "Kunden.csv";

    public static String getCsvFileName() {
        return csvFileName;
    }

    private final Attribute[] attArr = new Attribute[ Attributes.values().length ];

    /**
     * Default-Konstruktor
     */
    public Kunde() {
        this( null, null, null, null, null, null, null);
    }

    public Kunde( String vorName, String nachName ) {
        this( null, vorName, nachName, "--", null, null, null, null);
    }

    public Kunde(String vorName, String nachName, String email, String phone, String iban, LocalDate dateOfBirth, LocalDateTime last_edit) {
        this(null, vorName, nachName, email, phone, iban, dateOfBirth, last_edit);
    }

    /**
     * Hauptkonstruktor, wird von allen anderen Konstruktoren aufgerufen.
     * Die Attribute werden in der Enum Attributes deklariert. Hier werden nur die Werte übergeben.
     * @param iD
     * @param vorName
     * @param nachName
     * @param email
     * @param dateOfBirth
     */
    public Kunde(String iD, String vorName, String nachName, String email, String phone, String iban, LocalDate dateOfBirth, LocalDateTime last_edit ) {
        super();

        boolean modifiable = true;

        String randID = UUID.randomUUID().toString();
        this.attArr[ Attributes.ID.ordinal() ] = Attributes.ID.createAttribute( this, ( iD == null || iD.isEmpty() ? randID : iD ), randID );
        this.attArr[ Attributes.NACHNAME.ordinal() ] = Attributes.NACHNAME.createAttribute( this, nachName, "--" );
        this.attArr[ Attributes.VORNAME.ordinal() ] = Attributes.VORNAME.createAttribute( this, vorName, "--" );
        this.attArr[ Attributes.EMAIL.ordinal() ] = Attributes.EMAIL.createAttribute( this, email, "" );
        this.attArr[ Attributes.PHONE.ordinal() ] = Attributes.PHONE.createAttribute( this, phone, "" );
        this.attArr[ Attributes.IBAN.ordinal() ] = Attributes.IBAN.createAttribute( this, iban, "" );
        this.attArr[ Attributes.DATEOFBIRTH.ordinal() ] = Attributes.DATEOFBIRTH.createAttribute(this, dateOfBirth, "" );
        this.attArr[ Attributes.LAST_EDITED.ordinal() ] = Attributes.LAST_EDITED.createAttribute(this, last_edit, "" );
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
    public String getTableCellText() {
        return "<html><body>"
                + this.attArr[ Attributes.NACHNAME.ordinal() ].getValue()
                + ", " + this.attArr[ Attributes.VORNAME.ordinal() ].getValue()
                + "<br>" + this.attArr[ Attributes.PHONE.ordinal() ].getValue()
                + "</body></html>";
    }

    @Override
    public String getVisibleText() {
        return this.toString();
    }

    @Override
    public String toString() {
        return this.attArr[ Attributes.NACHNAME.ordinal() ].getValue() + ", "
                + this.attArr[ Attributes.VORNAME.ordinal() ].getValue();
    }

    @Override
    public Class<?> getElementClass() {
        return this.getClass();
    }

    @Override
    public String getElementID() {
        return this.attArr[ Attributes.ID.ordinal() ].getValue().toString();
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
