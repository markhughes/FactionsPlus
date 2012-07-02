package markehme.factionsplus.config.yaml;

import markehme.factionsplus.extras.*;


/**
 * the comment should contain the comment char, but not the leading spaces before it<br>
 */
public class WYComment<METADATA_TYPE> extends WYRawButLeveledLine<METADATA_TYPE> {

	public WYComment(int lineNumber, String theCommentWithoutLeadingSpaces){//, WYSection parent, WYItem prev ) {
		super(lineNumber, theCommentWithoutLeadingSpaces);//, parent, prev );
	}
	
}
