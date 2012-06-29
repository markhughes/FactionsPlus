package markehme.factionsplus.config;

import markehme.factionsplus.extras.*;


public class WYComment extends WYItem {
	
	private String fullLine;
	
	public WYComment( String _entireLine , WYSection parent, WYItem prev) {
		super(parent,prev);
		fullLine=_entireLine;
		assert Q.nn( fullLine );
//		System.out.println("Comment "+this+" parent="+parent+" prev="+prev);
	}

	public String getFullLine() {
		return fullLine;
	}
	
	@Override
	public String toString() {
		return getFullLine();
	}
}
