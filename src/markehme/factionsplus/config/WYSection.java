package markehme.factionsplus.config;

import markehme.factionsplus.extras.*;



public class WYSection implements WYItem {
	
	private String	identifier;
	
	
	public WYSection( String id ) {
		identifier = id;
		assert Q.nn( identifier );
	}
	
}
