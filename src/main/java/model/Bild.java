package model;

import de.dhbwka.swe.utils.model.Attribute;
import de.dhbwka.swe.utils.model.IDepictable;
import de.dhbwka.swe.utils.model.IPersistable;

import javax.swing.*;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Bild implements IDepictable, IPersistable {

    public enum CSVPositions{
        ID,
        TITLE,
        FILEPATH,
        KEY
    }

    public enum Attributes {

        ID( "   ID", String.class, false, false, false ),
        TITLE( "Title", String.class, true, false, false ),
        FILEPATH( "FilePath", String.class, true, false, false ),
        IMAGE( "Image", ImageIcon.class, true, false, false ),
        KEY( "Key", ImageIcon.class, true, false, false );

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

    private static String csvFileName = "Bilder.csv";

    public static String getCsvFileName() {
        return csvFileName;
    }

    private final Attribute[] attArr = new Attribute[ Attributes.values().length ];

    /**
     * Default-Konstruktor
     */
    public Bild() {
        this( null, null, null, null, null);
    }

    public Bild(String title, String filePath) {
        this(null, title, filePath, null, null);
    }

    public Bild(String iD, String title, String filePath, ImageIcon imageIcon, String key) {
        super();

        boolean modifiable = true;

        String randID = UUID.randomUUID().toString();
        this.attArr[ Attributes.ID.ordinal() ] = Attributes.ID.createAttribute( this, ( iD == null || iD.isEmpty() ? randID : iD ), randID );
        this.attArr[ Attributes.TITLE.ordinal() ] = Attributes.TITLE.createAttribute( this, title, null );
        this.attArr[ Attributes.FILEPATH.ordinal() ] = Attributes.FILEPATH.createAttribute( this, filePath, null );
        this.attArr[ Attributes.IMAGE.ordinal() ] = Attributes.IMAGE.createAttribute( this, imageIcon, null );
        this.attArr[ Attributes.KEY.ordinal() ] = Attributes.KEY.createAttribute( this, key, null );
    }

    public <T> T getAttributeValueOf( Attributes attribute ) {
        return (T)this.attArr[ attribute.ordinal() ].getValue();
    }

    public <T> void setAttributeValueOf(Attributes attribute, T value ) throws Exception {
        if( ! attribute.modifiable )
            throw new IllegalArgumentException( "attribute '" + attribute.name() + "' is not modifiable!" );
        if( value.getClass() == this.attArr[ attribute.ordinal() ].getClass() )
            this.attArr[ attribute.ordinal() ].setValue( value );
        else
            throw new IllegalArgumentException( "wrong class type for attribute '" + attribute.name() + "'!" );
    }

    public String getSecondaryKey() { return this.attArr[ Attributes.KEY.ordinal()].getValue().toString(); }
    public ImageIcon getImage() { return (ImageIcon) this.attArr[ Attributes.IMAGE.ordinal()].getValue(); }

    @Override
    public String getVisibleText() {
        return this.toString();
    }

    @Override
    public String toString() {
        return this.attArr[ Attributes.TITLE.ordinal() ].getValue() + ", "
                + this.attArr[ Attributes.FILEPATH.ordinal() ].getValue();
    }

    @Override
    public Class<?> getElementClass() {
        return this.getClass();
    }

    @Override
    public String getElementID() {
        return this.attArr[ Bild.Attributes.ID.ordinal() ].getValue().toString();
    }


    @Override
    public Attribute[] getAttributeArray() {
        return this.attArr;
    }

    // von IPersistable:
    @Override
    public Object getPrimaryKey() {
        return this.attArr[ Bild.Attributes.ID.ordinal() ].getValue();
    }
}
