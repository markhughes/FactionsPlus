package markehme.factionsplus.config;

import markehme.factionsplus.extras.*;



public class WYSection extends WYItem {
	
	private String	identifier;
	
	
	public WYSection( String id , WYItem parent, WYItem prev) {
		super(parent, prev);
		identifier = id;
		assert Q.nn( identifier );
	}
	
	public String getSectionName(){
		return identifier;
	}
	
}
