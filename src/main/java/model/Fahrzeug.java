package model;

import de.dhbwka.swe.utils.model.Attribute;
import de.dhbwka.swe.utils.model.IDepictable;

public class Fahrzeug implements IDepictable {

	public enum FahrzeugTyp {
		MINI, KLEIN, MITTEL, GROSS, LUXUS, TRANSPORTER
	}

	private int nummer; // ID
	private String modellName;
	private String markenName;
	private int baujahr;
	private String color; // kann hier als String verwendet werden, da die Farben mit Namen benannt werden
	private FahrzeugTyp fahrzeugTyp = FahrzeugTyp.KLEIN;

	public Fahrzeug() {
	}

	public Fahrzeug(int nummer, String modellName, String markenName, int baujahr, String color,
			FahrzeugTyp fahrzeugTyp) {
		super();
		this.nummer = nummer;
		this.modellName = modellName;
		this.markenName = markenName;
		this.baujahr = baujahr;
		this.color = color;
		this.fahrzeugTyp = fahrzeugTyp;
	}

	@Override
	public Attribute[] getAttributeArray() {
		return null;
	}

	@Override
	public String getElementID() {
		return "" + nummer;
	}

	public int getNummer() {
		return nummer;
	}

	public void setNummer(int nummer) {
		this.nummer = nummer;
	}

	public String getModellName() {
		return modellName;
	}

	public void setModellName(String modellName) {
		this.modellName = modellName;
	}

	public String getMarkenName() {
		return markenName;
	}

	public void setMarkenName(String markenName) {
		this.markenName = markenName;
	}

	public int getBaujahr() {
		return baujahr;
	}

	public void setBaujahr(int baujahr) {
		this.baujahr = baujahr;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public FahrzeugTyp getFahrzeugTyp() {
		return fahrzeugTyp;
	}

	public void setFahrzeugTyp(FahrzeugTyp fahrzeugTyp) {
		this.fahrzeugTyp = fahrzeugTyp;
	}

	@Override
	public String toString() {
		return "(" + nummer + "),  " + this.modellName + "  " + this.markenName + " Bj. " + this.baujahr
				+ "  " + this.color + "  " + this.fahrzeugTyp ;
	}

}
