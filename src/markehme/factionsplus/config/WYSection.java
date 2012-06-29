package markehme.factionsplus.config;

import markehme.factionsplus.extras.*;



public class WYSection extends WYItem {
	
	private String	identifier;
	
	
	public WYSection( String id , WYItem parent, WYItem next) {
		super(parent,next);
		identifier = id;
		assert Q.nn( identifier );
	}
	
}
