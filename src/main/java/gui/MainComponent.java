package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JTabbedPane;

import de.dhbwka.swe.utils.event.GUIEvent;
import de.dhbwka.swe.utils.event.IGUIEventListener;
import de.dhbwka.swe.utils.event.IObservable;
import de.dhbwka.swe.utils.gui.AttributeComponent;
import de.dhbwka.swe.utils.gui.AttributeElement;
import de.dhbwka.swe.utils.gui.ButtonComponent;
import de.dhbwka.swe.utils.gui.ButtonElement;
import de.dhbwka.swe.utils.gui.ObservableComponent;
import de.dhbwka.swe.utils.gui.SimpleListComponent;
import de.dhbwka.swe.utils.model.Attribute;
import de.dhbwka.swe.utils.model.IAttributed;
import de.dhbwka.swe.utils.model.IDepictable;
import de.dhbwka.swe.utils.util.IPropertyManager;
import model.Kunde;
import model.Standort;

public class MainComponent extends ObservableComponent implements IGUIEventListener{

	private IPropertyManager propManager = null;
	
	private List<IDepictable> allElements = new ArrayList<>();
	private List<AttributeElement> allAttributeElements = new ArrayList<>();
	
	public final static String SLC = "SimpleListComponent-1";
	public final static String ATTC = "AttributeComponent-1";
	public final static String BTC = "ButtonComponent-1";
	
	private SimpleListComponent slc = null;
	private AttributeComponent attComp = null;
	private ButtonComponent btnComp = null;
	private JTabbedPane tabbedPane = new JTabbedPane();
	
	public MainComponent( IPropertyManager propManager ) {
//		if( propManager == null ) throw new IllegalArgumentException( "PropManager must not be null!");
		this.propManager = propManager;
		initUI();
	}

	private void initUI() {

		/**
		 * hier kann ein JTabbedPane verwendet werden. Dann werden alle Inhalte (Tabs)
		 * in einer eigenen Methode erzeugt und zugewiesen.
		 * Wenn einTab selbst ein JPanel oder eine JComponent ist, dann kann sie auch in eine
		 * eigene Klasse ausgelagert werden. In diesem Fall ist jedoch die Kommunikation 
		 * zusätzlich aufzubauen (z.B. Observer, was ja auch sinnvoll wäre).
		 */
		
		
		
		
		this.setLayout( new BorderLayout() );
		slc = SimpleListComponent.builder( SLC )
				.propManager( this.propManager )
				.title("ein erster Titel")
				.build();
		slc.setPreferredSize( new Dimension(200, 500) );
		
		btnComp = createButtonComponentForLeftList( slc );
		btnComp.addObserver(this);
		
		this.add( btnComp, BorderLayout.WEST );
		slc.addObserver( this );
		Kunde initKunde = new Kunde();
		initKunde.getAttributeArray();
		attComp = AttributeComponent.builder(ATTC)
				.attributeElements( createAttributeElementsFor( initKunde ) )
				.build();
		this.add( attComp, BorderLayout.EAST );
		attComp.setPreferredSize( new Dimension(350,500) );
		attComp.addObserver( this );
		
	}
	private final static String BTN_ADD_LL = "AddElement2LeftList";
	private final static String BTN_EMOVE_LL = "RemoveElementFromLeftList";
	
	private ButtonComponent createButtonComponentForLeftList( SimpleListComponent slc ) {
		ButtonElement[] beArr = new ButtonElement[] {
			ButtonElement.builder( BTN_ADD_LL )
			.buttonText( "add" )
			.build(),
			ButtonElement.builder( BTN_ADD_LL )
			.buttonText( "remove" )
			.build()
		};
		
		return ButtonComponent.builder(BTC).buttonElements(beArr)
				.embeddedComponent( slc )
				.build();
	}
	
	private AttributeElement[] createAttributeElementsFor( IAttributed attObj ) {
		AttributeElement[] aeArr = null;
		if( attObj != null ) {
			Attribute[] attArr = attObj.getAttributeArray();
			aeArr = new AttributeElement[ attArr.length ];
			for (int i = 0; i < attArr.length; i++) {
				aeArr[i] = AttributeElement.builder( attArr[i].getName() )
						.attribute( attArr[i] )
						.colon( true )
						.toolTip( attArr[i].getName() )
						.modificationType( attArr[i].isModifiable() 
								? AttributeElement.ModificationType.DIRECT 
								: AttributeElement.ModificationType.NONE )
						.actionType( AttributeElement.ActionType.NONE)
						.labelSize( new Dimension(100,40) )
						.labelAlignment( javax.swing.SwingConstants.RIGHT )
						.build();
			}
		}
		return aeArr;
	}
	
	public void setElements( List<IDepictable> allElems ) {
//		this.allElements.clear();
//		this.allElements.addAll(allElems);
		this.allElements = allElems;
		slc.setListElements(allElems);
	}

	@Override
	public void processGUIEvent(GUIEvent ge) {
		System.out.println("Event: " + ge);
		System.out.println("Event.Command: " + ge.getCmdText() );
		System.out.println("Event.Data: " + ge.getData() );
		/**
		 * hier wird direkt der Event in der GUI-Komponente weitergeleitet von der SimpleListComponent
		 * zur AttributeComponent, da der Controller mit dem Event nichts anderes machen würde, als ihn
		 * wieder zurückzusenden (processUpdateEvent()).
		 */
		if( ge.getCmdText().equals( SimpleListComponent.Commands.ELEMENT_SELECTED.cmdText )) {
			if( ge.getData() instanceof Kunde )
				System.out.println("es ist ein Kunde");
			Kunde knd = (Kunde)ge.getData();
			System.out.println("Kunde: " + knd);
			Attribute[] atsKunde = knd.getAttributeArray();
			this.attComp.setAttributeElementValues( atsKunde );
			System.out.println( atsKunde[0]);
//			this.attComp.setAttributeElements(ats);
		}
		/**
		 * wenn nichts gemacht wird: an den Controller weiterleiten ...
		 */
		fireGUIEvent(ge);
	}
	
}
