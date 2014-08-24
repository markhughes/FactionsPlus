package markehme.factionsplus.config.yaml;

import markehme.factionsplus.util.Q;


public class WYRawButLeveledLine<METADATA_TYPE> extends WYItem<METADATA_TYPE> {
	private String fullLine;
	
	/**
	 * if input was:<br>
	 * "     something"<br>
	 * and our level is<br>
	 * "  ^"<br>
	 * then the line would be:<br>
	 * __"   something"  (without the __  that's used for the level)<br>
	 * in other words: leading spaces representing level are stripped<br> 
	 * @param lineNumber 
	 * @param alreadyLeveledLine with all leading spaces, that are part of the level, removed
	 */
	public WYRawButLeveledLine(int lineNumber, String alreadyLeveledLine){// , WYSection parent, WYItem prev) {
		super(lineNumber);//,parent,prev);
		fullLine=alreadyLeveledLine;
		assert Q.nn( fullLine );
	}

	public String getRawButLeveledLine() {
		return fullLine;
	}
	
	@Override
	public String toString() {
		return getRawButLeveledLine();
	}
}
