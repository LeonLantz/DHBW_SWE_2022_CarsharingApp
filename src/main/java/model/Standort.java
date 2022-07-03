package model;

import de.dhbwka.swe.utils.model.Attribute;
import de.dhbwka.swe.utils.model.IDepictable;

public class Standort implements IDepictable {

	private String id = "not set";
	private String name = "not set";
	private int anzahlPlaetze = 0;
	
	
	public Standort(String id, String name, int anzahlPlaetze) {
		super();
		this.id = id;
		this.name = name;
		this.anzahlPlaetze = anzahlPlaetze;
	}

	@Override
	public Attribute[] getAttributeArray() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getElementID() {
		return id;
	}

	public String getId() {
		return id;
	}

	public int getAnzahlPlaetze() {
		return anzahlPlaetze;
	}

	public void setAnzahlPlaetze(int anzahlPlaetze) {
		this.anzahlPlaetze = anzahlPlaetze;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "(" + this.id + ")  " + this.name + "  #Plaetze: " + this.anzahlPlaetze;
	}
}
