package markehme.factionsplus.config;

import markehme.factionsplus.extras.*;


public class WYComment implements WYItem {
	
	private String line;
	
	public WYComment( String _line ) {
		line=_line;
		Q.nn( line );
	}
	
}
