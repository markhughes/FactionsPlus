package markehme.factionsplus.config;

import markehme.factionsplus.extras.*;


public class WYRawLine extends WYItem {
	private String fullLine;
	
	public WYRawLine( String _entireLine , WYSection parent, WYItem prev) {
		super(parent,prev);
		fullLine=_entireLine;
		assert Q.nn( fullLine );
	}

	public String getFullLine() {
		return fullLine;
	}
	
	@Override
	public String toString() {
		return getFullLine();
	}
}
