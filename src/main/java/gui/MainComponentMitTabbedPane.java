package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import control.CSControllerReinerObserverUndSender;
import de.dhbwka.swe.utils.event.EventCommand;
import de.dhbwka.swe.utils.event.GUIEvent;
import de.dhbwka.swe.utils.event.IGUIEventListener;
import de.dhbwka.swe.utils.event.IObservable;
import de.dhbwka.swe.utils.event.IUpdateEventListener;
import de.dhbwka.swe.utils.event.UpdateEvent;
import de.dhbwka.swe.utils.gui.AttributeComponent;
import de.dhbwka.swe.utils.gui.AttributeElement;
import de.dhbwka.swe.utils.gui.ButtonComponent;
import de.dhbwka.swe.utils.gui.ButtonElement;
import de.dhbwka.swe.utils.gui.ObservableComponent;
import de.dhbwka.swe.utils.gui.SimpleListComponent;
import de.dhbwka.swe.utils.model.Attribute;
import de.dhbwka.swe.utils.model.IAttributed;
import de.dhbwka.swe.utils.model.IDepictable;
import de.dhbwka.swe.utils.util.AppLogger;
import de.dhbwka.swe.utils.util.IAppLogger;
import de.dhbwka.swe.utils.util.IPropertyManager;
import model.Kunde;
import model.Standort;

public class MainComponentMitTabbedPane extends ObservableComponent 
		implements IGUIEventListener, IUpdateEventListener{

	/**
	 * Folgende Commands deklarieren! Es sind die Commands, welche z.B. die MainGUI als 
	 * GUIEvent sendet
	 * Die Commands müssen nur oben deklariert werden, der Rest des enums ist copy/paste (s. SWE-utils-GUIs)
	 */
	public enum Commands implements EventCommand {

		/**
		 * Command:  ID + gelieferter Payload-Typ
		 */
		SAVE_KUNDEN( "MainComponent.saveAllKunden", null ), // reiner Befehl ohne Payload 
		ADD_KUNDE( "MainComponent.addKunde", String[].class ),
		REMOVE_KUNDE( "MainComponent.removeKunde", IDepictable.class );

		public final Class<?> payloadType;
		public final String cmdText;

		private Commands( String cmdText, Class<?> payloadType ) {
			this.cmdText = cmdText;
			this.payloadType = payloadType;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String getCmdText() {
			return this.cmdText;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Class<?> getPayloadType() {
			return this.payloadType;
		}
	}

	private IPropertyManager propManager = null;
	
	private IAppLogger logger = AppLogger.getInstance();
	
	private List<IDepictable> allElements = new ArrayList<>();
	private List<AttributeElement> allAttributeElements = new ArrayList<>();
	
	private final static String BTN_ADD_LL = "AddElement2LeftList";
	private final static String BTN_REMOVE_LL = "RemoveElementFromLeftList";
	
	public final static String SLC = "SimpleListComponent-1";
	public final static String ATTC = "AttributeComponent-1";
	public final static String BTC = "ButtonComponent-1";
	
	public final static String LBL_ATTC_KUNDE = "Attribute des Kunden";
	public final static String LBL_SLC_KUNDE = "Alle Kunden";
	
	public final static String TAB_KUNDE = "Kunden";
	public final static String TAB_BUCHUNG = "Buchungen";
	
	private final static Dimension attCompSize =new Dimension(350,500);

	private SimpleListComponent slc = null;
	private AttributeComponent attComp = null;
	private ButtonComponent btnComp = null;
	private JTabbedPane tabbedPane = new JTabbedPane();
	
	public MainComponentMitTabbedPane( IPropertyManager propManager ) {
//		if( propManager == null ) throw new IllegalArgumentException( "PropManager must not be null!");
		this.propManager = propManager;
		initUI();
	}

	private void initUI() {

		/**
		 * hier wird ein JTabbedPane verwendet. Dann werden alle Inhalte (Tabs)
		 * in einer eigenen Methode erzeugt und zugewiesen.
		 * Wenn einTab selbst ein JPanel oder eine JComponent ist, dann kann sie auch in eine
		 * eigene Klasse ausgelagert werden. In diesem Fall ist jedoch die Kommunikation 
		 * zusätzlich aufzubauen (z.B. Observer, was ja auch sinnvoll wäre).
		 */

		// wir nehmen trotzdem ein Borderlayout, da am flexibelsten
		this.setLayout( new BorderLayout() );
		
		this.tabbedPane.add( TAB_KUNDE, createKundenTab() );
		this.tabbedPane.add( TAB_BUCHUNG, createBuchungTab() );
		
		this.add(tabbedPane);
		
	}
	
	
	private JPanel createKundenTab() {
		// Basispanel mit BorderLayout
		JPanel pnlKunde = new JPanel(new BorderLayout());
		
		slc = SimpleListComponent.builder( SLC )
				.propManager( this.propManager )
				.title( LBL_SLC_KUNDE )
				.build();
		slc.setPreferredSize( new Dimension(200, 500) );
		
		btnComp = createButtonComponentForLeftList( slc );
		
		pnlKunde.add( btnComp, BorderLayout.WEST );
		slc.addObserver( this );
		Kunde initKunde = new Kunde();
		initKunde.getAttributeArray();
		attComp = createAttributeComponent( ATTC, LBL_ATTC_KUNDE, createAttributeElementsFor( initKunde ) ); 
		pnlKunde.add( attComp, BorderLayout.EAST );
		attComp.setPreferredSize( attCompSize );
		attComp.addObserver( this );
		
		return pnlKunde;
	}
	
	private JPanel createBuchungTab() {
		// Basispanel mit BorderLayout
		JPanel pnlBuchung = new JPanel(new BorderLayout());
		pnlBuchung.add(new JButton("@Todo"));
		
		return pnlBuchung;
	}
	
	private AttributeComponent createAttributeComponent( String id, String title, AttributeElement[] attElements) {
		return AttributeComponent.builder( id )
				.attributeElements( attElements )
				.title( title )
				.build();
	}
	
	private ButtonComponent createButtonComponentForLeftList( SimpleListComponent slc ) {
		ButtonElement[] beArr = new ButtonElement[] {
			ButtonElement.builder( BTN_ADD_LL )
			.buttonText( "add" )
			.build(),
			ButtonElement.builder( BTN_REMOVE_LL )
			.buttonText( "remove" )
			.build()
		};
		
		return ButtonComponent.builder(BTC).buttonElements(beArr)
				.embeddedComponent( slc )
				.observer( this )
				.componentSize( null )
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
			return;
		}
		if( ge.getSource() == this.btnComp ) {
			ButtonElement be = (ButtonElement)ge.getData();
			/**
			 * Kunde hinzufügen:
			 * AttComp zur Eingabe mit Dummy-Kunde bauen
			 * Daten eingeben lassen und holen
			 * Daten an Controller zum Erzeugen des neuen Kunden weitergeben
			 * Der Controller schickt dann den neuen Kunden wieder per UpdateEvent an die GUI 
			 */
			if( be.getID().equals( BTN_ADD_LL ) ) {
				// build and show an AtributeComponent for input
				Kunde initKunde = new Kunde();
				initKunde.getAttributeArray();
				AttributeComponent attC = createAttributeComponent( "input", LBL_ATTC_KUNDE, createAttributeElementsFor( initKunde ) ); 
				attC.setPreferredSize(attCompSize);
				if( JOptionPane.showConfirmDialog(this, attC, "bitte die Kundendaten eintragen", JOptionPane.OK_CANCEL_OPTION )
						== JOptionPane.OK_OPTION ) 
				{
					String[] attVals = attC.getAttributeValuesAsArray();
					Arrays.asList(attVals).forEach( e -> logger.debug(e) );
					fireGUIEvent( new GUIEvent(this, Commands.ADD_KUNDE, attVals ));
				}
			}
		}
		/**
		 * wenn nichts gemacht wird: an den Controller weiterleiten ...
		 */
		fireGUIEvent(ge);
	}

	@Override
	public void processUpdateEvent(UpdateEvent ue) {
		if( ue.getCmd() == CSControllerReinerObserverUndSender.Commands.SET_KUNDEN ) {
			List<Kunde> lstKunde = (List<Kunde>)ue.getData();
			this.slc.setListElements(lstKunde, true);
			if( lstKunde.size() > 0 ) {
				// wenn mind. 1 Element -> in AttComp darstellen (da sonst auto-generierte ID verwendet wird) 
				this.attComp.setAttributeElementValues( lstKunde.get(0).getAttributeArray() );
			}
		}
	}
	
}
