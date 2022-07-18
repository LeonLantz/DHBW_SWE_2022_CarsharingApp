package model;

import de.dhbwka.swe.utils.model.Attribute;
import de.dhbwka.swe.utils.model.IDepictable;
import de.dhbwka.swe.utils.model.IPersistable;

public class Anschrift implements IDepictable, IPersistable {

    private String kundenID;
    private String straße;
    private String hausnummer;
    private String adresszusatz;
    private String ort;
    private String plz;
    private String land;

    public Anschrift(String kundenID, String straße, String hausnummer, String adresszusatz, String ort, String plz, String land) {
        this.kundenID = kundenID;
        this.straße = straße;
        this.hausnummer = hausnummer;
        this.adresszusatz = adresszusatz;
        this.ort = ort;
        this.plz = plz;
        this.land = land;
    }

    public String getOrt() {
        return ort;
    }

    public String getPlz() {
        return plz;
    }

    @Override
    public String getElementID() {
        return "" + kundenID;
    }

    @Override
    public Attribute[] getAttributeArray() {
        return null;
    }

    @Override
    public Object getPrimaryKey() {
        return null;
    }
}
