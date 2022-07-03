package app;

import de.dhbwka.swe.utils.app.ComponentTest;
import de.dhbwka.swe.utils.util.IAppLogger;

public class SWETestappClass {

	public static void main( String[] args ) {
		// TODO Auto-generated method stub
		new SWETestappClass();
	}

	public SWETestappClass() {
//		SimpleTextComponentApp.main( null );
//		TextComponentApp.main( null );
//		TextComponent tc = TextComponentApp.createTextComponent( "TC", null );
//		IOUtilities.openInJFrame( tc , 300, 400, 1000, 400, "tc", null, true ) ;
		try {
			new ComponentTest(null, IAppLogger.Severity.ERROR);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
