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

public class Fahrzeug implements IDepictable, IPersistable {

    public enum CSVPositions{
        ID,
        BEZEICHNUNG,
        MARKE,
        MOTOR,
        TÜREN,
        SITZE,
        KOFFERRAUMVOLUMEN,
        GEWICHT,
        FAHRZEUGKATEGORIE,
        FÜHRERSCHEINKLASSE,
        NUMMERNSCHILD,
        TÜV_BIS,
        FARBE,
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
        BEZEICHNUNG( "Bezeichnung", String.class, true, false, false ),
        MARKE( "Marke", String.class, true, false, false ),
        MOTOR( "Motor", String.class, true, false, false ),
        TÜREN("Türen", Integer.class, false, false, false),
        SITZE("Sitze", Integer.class, true, false, false),
        KOFFERRAUMVOLUMEN( "Kofferraumvolumen", Integer.class, false, false, false ),
        GEWICHT( "Gewicht", String.class, true, false, false ),
        FAHRZEUGKATEGORIE( "Kategorie", Fahrzeugkategorie.class, false, false, false ),
        FÜHRERSCHEINKLASSE( "Führerscheinklasse", String.class, false, false, false ),
        NUMMERNSCHILD( "Nummernschild", String.class, true, false, false ),
        TÜV_BIS( "TÜV bis", LocalDate.class, false, false, false ),
        FARBE( "Farbe", String.class, true, false, false ),
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
        Fahrzeug.Attributes[] pA = Fahrzeug.Attributes.values();

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

    private static String csvFileName = "Fahrzeuge.csv";

    public static String getCsvFileName() {
        return csvFileName;
    }

    private final Attribute[] attArr = new Attribute[ Fahrzeug.Attributes.values().length ];


    /**
     * Default-Konstruktor
     */
    public Fahrzeug() {
        this( null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    }

    public Fahrzeug(String bezeichnung, String marke, String motor, Integer türen, Integer sitze, Integer kofferraumvolumen, String gewicht, Fahrzeugkategorie kategorie, String führerscheinklasse, String nummernschild, LocalDate tüv_bis, String color, LocalDateTime last_edit) {
        this(null, bezeichnung, marke, motor, türen, sitze, kofferraumvolumen, gewicht, kategorie, führerscheinklasse, nummernschild, tüv_bis, color, last_edit);
    }

    public Fahrzeug(String iD, String bezeichnung, String marke, String motor, Integer türen, Integer sitze, Integer kofferraumvolumen, String gewicht, Fahrzeugkategorie kategorie, String führerscheinklasse, String nummernschild, LocalDate tüv_bis, String farbe, LocalDateTime last_edit ) {
        super();

        boolean modifiable = true;

        String randID = UUID.randomUUID().toString();
        this.attArr[ Attributes.ID.ordinal() ] = Fahrzeug.Attributes.ID.createAttribute( this, ( iD == null || iD.isEmpty() ? randID : iD ), randID );
        this.attArr[ Attributes.BEZEICHNUNG.ordinal() ] = Fahrzeug.Attributes.BEZEICHNUNG.createAttribute( this, bezeichnung, "--" );
        this.attArr[ Attributes.MARKE.ordinal() ] = Fahrzeug.Attributes.MARKE.createAttribute( this, marke, "--" );
        this.attArr[ Attributes.MOTOR.ordinal() ] = Fahrzeug.Attributes.MOTOR.createAttribute( this, motor, "" );
        this.attArr[ Attributes.TÜREN.ordinal() ] = Fahrzeug.Attributes.TÜREN.createAttribute( this, türen, "" );
        this.attArr[ Attributes.SITZE.ordinal() ] = Fahrzeug.Attributes.SITZE.createAttribute( this, sitze, "" );
        this.attArr[ Attributes.KOFFERRAUMVOLUMEN.ordinal() ] = Fahrzeug.Attributes.KOFFERRAUMVOLUMEN.createAttribute(this, kofferraumvolumen, "" );
        this.attArr[ Attributes.GEWICHT.ordinal() ] = Fahrzeug.Attributes.GEWICHT.createAttribute(this, gewicht, "" );
        this.attArr[ Attributes.FAHRZEUGKATEGORIE.ordinal() ] = Fahrzeug.Attributes.FAHRZEUGKATEGORIE.createAttribute(this, kategorie, "" );
        this.attArr[ Attributes.FÜHRERSCHEINKLASSE.ordinal() ] = Fahrzeug.Attributes.FÜHRERSCHEINKLASSE.createAttribute(this, führerscheinklasse, "" );
        this.attArr[ Attributes.NUMMERNSCHILD.ordinal() ] = Fahrzeug.Attributes.NUMMERNSCHILD.createAttribute(this, nummernschild, "" );
        this.attArr[ Attributes.TÜV_BIS.ordinal() ] = Fahrzeug.Attributes.TÜV_BIS.createAttribute(this, tüv_bis, "" );
        this.attArr[ Attributes.FARBE.ordinal() ] = Fahrzeug.Attributes.FARBE.createAttribute(this, farbe, "" );
        this.attArr[ Attributes.LAST_EDIT.ordinal() ] = Fahrzeug.Attributes.LAST_EDIT.createAttribute(this, last_edit, "" );
    }

    public <T> T getAttributeValueOf( Fahrzeug.Attributes attribute ) {
        return (T)this.attArr[ attribute.ordinal() ].getValue();
    }

    public <T> void setAttributeValueOf(Fahrzeug.Attributes attribute, T value ) throws Exception {
        if( ! attribute.modifiable )
            throw new IllegalArgumentException( "attribute '" + attribute.name() + "' is not modifiable!" );
        if( value.getClass() == this.attArr[ attribute.ordinal() ].getClass() )
            this.attArr[ attribute.ordinal() ].setValue( value );
        else
            throw new IllegalArgumentException( "wrong class type for attribute '" + attribute.name() + "'!" );
    }

    public String getTableCellText() {
        return "<html><body>"
                + this.attArr[ Attributes.BEZEICHNUNG.ordinal() ].getValue()
                + "</body></html>";
    }

    @Override
    public String getVisibleText() {
        return this.toString();
    }

    @Override
    public String toString() {
        return this.attArr[ Attributes.MARKE.ordinal() ].getValue() + ", "
                + this.attArr[ Attributes.BEZEICHNUNG.ordinal() ].getValue();
    }

    @Override
    public Class<?> getElementClass() {
        return this.getClass();
    }

    @Override
    public String getElementID() {
        return this.attArr[ Fahrzeug.Attributes.ID.ordinal() ].getValue().toString();
    }


    @Override
    public Attribute[] getAttributeArray() {
        return this.attArr;
    }

    // von IPersistable:
    @Override
    public Object getPrimaryKey() {
        return this.attArr[ Fahrzeug.Attributes.ID.ordinal() ].getValue();
    }

}
