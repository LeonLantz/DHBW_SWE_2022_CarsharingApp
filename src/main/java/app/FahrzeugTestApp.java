package app;

import java.util.ArrayList;
import java.util.List;

import de.dhbwka.swe.utils.event.GUIEvent;
import de.dhbwka.swe.utils.event.IGUIEventListener;
import de.dhbwka.swe.utils.gui.ButtonComponent;
import de.dhbwka.swe.utils.gui.ButtonElement;
import de.dhbwka.swe.utils.gui.SimpleListComponent;
import de.dhbwka.swe.utils.util.IOUtilities;
import model.Fahrzeug;
import model.Fahrzeug.FahrzeugTyp;

public class FahrzeugTestApp implements IGUIEventListener {

	public static void main( String[] args ) {
		new FahrzeugTestApp();
	}
	
	public final static String BE_ADD = "BE-ADD";
	public final static String BE_REMOVE = "BE-REMOVE";
	
	public FahrzeugTestApp() {
		
		List<Fahrzeug> fahrzeuge = new ArrayList<>();
		Fahrzeug fz = new Fahrzeug( 1, "Opel", "Astra", 2021, "gruen", FahrzeugTyp.MITTEL );
		fahrzeuge.add(fz);
		fz = new Fahrzeug( 2, "BMW", "ID3", 2022, "rot", FahrzeugTyp.KLEIN );
		fahrzeuge.add(fz);
		fz = new Fahrzeug( 3, "Mercedes", "Sprinter", 2020, "gelb", FahrzeugTyp.TRANSPORTER );
		fahrzeuge.add(fz);
		
		SimpleListComponent slc = SimpleListComponent.builder( "SLC-1" ).build();
		ButtonElement[] btns = new ButtonElement[] { 
					ButtonElement.builder( BE_ADD ).type( ButtonElement.Type.BUTTON )
					.buttonText( "add" ).build(),
					ButtonElement.builder( BE_ADD ).type( ButtonElement.Type.BUTTON )
					.buttonText( "remove" ).build()
		};
		ButtonComponent btnCmp = ButtonComponent.builder( "BC-1" )
				.embeddedComponent( slc )
				.buttonElements( btns ).build();
		btnCmp.addObserver( this );
		slc.addObserver( this );

		slc.setListElements( fahrzeuge );
		
		IOUtilities.openInJFrame( btnCmp , 300, 400, 1000, 400, "titel", null, true ) ;
	}

	public void processGUIEvent( GUIEvent ge ) {
		System.out.println( "Event gefangen, Command Text: " + ge.getCmdText() );
		System.out.println( "                      Source: " + ge.getSource() );
		System.out.println( "                     Command: " + ge.getCmd() );
		System.out.println( "Event-data                  : " + ge.getData());
//		if( )
//		if( ge.getCmd().)
	}

}
