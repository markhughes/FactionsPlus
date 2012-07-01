package markehme.factionsplus.config.yaml;

import markehme.factionsplus.extras.*;
import markehme.factionsplus.util.*;



public class WYSection<METADATA_TYPE> extends WY_IDBased<METADATA_TYPE> {
	
	private WYItem	firstChild, lastChild;
	
	
	public WYSection( int lineNumber, String id ) {// , WYSection parent, WYItem prev) {
		super( lineNumber, id );// , parent, prev);
		firstChild = null;
		lastChild = null;
		// System.out.println("Section "+this+" parent="+parent+" prev="+prev);
	}
	
	
	// public String getSectionName(){
	// return getId();
	// }
	
	public WYItem getFirst() {
		return firstChild;
	}
	
	
	public boolean isEmpty() {
		return null == firstChild;
	}
	
	
	void append( WYItem child ) {
		if ( lastChild != null ) {// means first is also non-null
			assert lastChild.getNext() == null;
			lastChild.setNext( child );
			child.setPrev( lastChild );
			lastChild = child;
		} else {
			// was previously empty
			firstChild = lastChild = child;
			assert null == child.getPrev();
			assert null == child.getNext();
		}
		
		child.setParent( this );
	}
	
	
	public WYItem getLast() {
		return lastChild;
	}
	
	
	/**
	 * @param wid
	 *            identifier (aka "id: value")
	 * @param commentPrefix
	 *            added right after the "# " and before the identifier,with always 1 space before and after it
	 * @return the commented identifier (aka "# prefix id: value")
	 */
	public WYComment replaceAndTransformInto_WYComment( WYIdentifier wid, String commentPrefix ) {
		assert wid.getParent() == this : "bad parameter passed";
		
		WYComment asComment =
			new WYComment( wid.getLineNumber(), "" + WannabeYaml.commentChar + WannabeYaml.space + commentPrefix
				+ WannabeYaml.space + wid.getId() + WannabeYaml.IDVALUE_SEPARATOR + WannabeYaml.space + wid.getValue() );
		// stating the obvious:
		assert asComment.getNext() == null;
		assert asComment.getPrev() == null;
		assert asComment.getParent() == null;
		
		// making comment part of this parent
		// let's not forget head/tail !!
		if ( wid == firstChild ) {
			firstChild = asComment;
		}
		if ( wid == lastChild ) {
			lastChild = asComment;
		}
		WYItem oldPrev = wid.getPrev();
		if ( null != oldPrev ) {// need to set its next
			asComment.setPrev( oldPrev );
			assert oldPrev.getNext() == wid : "bug somewhere else";
			oldPrev.setNext( asComment );
		}
		
		WYItem oldNext = wid.getNext();
		if ( null != oldNext ) {
			asComment.setNext( oldNext );
			assert oldNext.getPrev() == wid : "bug somewhere else";
			oldNext.setPrev( asComment );
		}
		
		asComment.setParent( this );
		// done
		
		// orphaning wid:
		// attempt to destroy or mark this id so that we can catch if we're still using it in other outside lists
		wid.setParent( null );
		wid.setPrev( null );
		wid.setNext( null );
		wid.setId( "###destroyed by WYSection.replaceAndTransformSelfInto_WYComment()###" + wid.getId() + "###" );
		wid.setValue( "###destroyed by WYSection.replaceAndTransformSelfInto_WYComment()###" + wid.getValue() + "###" );
		// we leave only setLine()
		// that's a mark that tells you, when you ever see it, that you're not supposed to be using this, if you do, you'll know
		// you probably missed removing it from some list/hashmap after you called this method we're in
		
		return asComment;
	}
	
	
	/**
	 * @param root
	 *            the root section which to parse, only lines inside it will be counted with first item from it being first line
	 *            and last item in it being last line<br>
	 *            each item (aka subclass of WYItem is considered to be 1 line)
	 * @param whatThePreviousLineNumber
	 *            the number of the first line, ie. should be 0 if you want the first line to be 1
	 */
	public void recalculateLineNumbers( WYSection root, int whatThePreviousLineNumber ) {
		assert Q.nn( root );
		WYItem currentItem = root.getFirst();
		int currentLine=whatThePreviousLineNumber;//pointing to previous line, atm
		
		while ( null != currentItem ) {
			
			currentLine++;//next line

			currentItem.setLineNumber(currentLine);
			

			if ( WYSection.class == currentItem.getClass() ) {
				WYSection cs = (WYSection)currentItem;
				recalculateLineNumbers( cs, currentLine );// recurse
			}
			
			currentItem = currentItem.getNext();
		}
	}
	
}
