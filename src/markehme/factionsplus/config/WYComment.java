package markehme.factionsplus.config;

import markehme.factionsplus.extras.*;


public class WYComment extends WYItem {
	
	private String line;
	
	public WYComment( String _line , WYItem parent, WYItem next) {
		super(parent,next);
		line=_line;
		assert Q.nn( line );
	}
	
	
}
