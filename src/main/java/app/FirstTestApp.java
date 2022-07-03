package app;

import de.dhbwka.swe.utils.event.GUIEvent;
import de.dhbwka.swe.utils.event.IGUIEventListener;
import de.dhbwka.swe.utils.gui.ButtonComponent;
import de.dhbwka.swe.utils.gui.ButtonElement;
import de.dhbwka.swe.utils.gui.SimpleListComponent;
import de.dhbwka.swe.utils.util.IOUtilities;

public class FirstTestApp implements IGUIEventListener {

	public static void main( String[] args ) {
		new FirstTestApp();
	}
	
	public FirstTestApp() {
		SimpleListComponent slc = SimpleListComponent.builder( "SLC-1" ).build();
		ButtonElement[] btns = new ButtonElement[] { 
					ButtonElement.builder( "Btn-1" ).type( ButtonElement.Type.BUTTON )
					.buttonText( "show" ).build(),
					ButtonElement.builder( "Btn-2" ).type( ButtonElement.Type.BUTTON )
					.buttonText( "hide" ).build()
		};
		ButtonComponent btnCmp = ButtonComponent.builder( "BC-1" )
				.embeddedComponent( slc )
				.buttonElements( btns ).build();
		btnCmp.addObserver( this );
		
		IOUtilities.openInJFrame( btnCmp , 300, 400, 1000, 400, "titel", null, true ) ;
	}

	public void processGUIEvent( GUIEvent ge ) {
		System.out.println( "Event gefangen: " + ge.getCmdText() );
		System.out.println( "Event-data:" + ge.getData());
	}

}
