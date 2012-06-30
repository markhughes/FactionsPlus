package markehme.factionsplus.config;

import markehme.factionsplus.extras.*;


/**
 * the comment should contain the comment char, but not the leading spaces before it<br>
 */
public class WYComment extends WYRawButLeveledLine {

	public WYComment(int lineNumber, String theCommentWithoutLeadingSpaces){//, WYSection parent, WYItem prev ) {
		super(lineNumber, theCommentWithoutLeadingSpaces);//, parent, prev );
	}
	
}
