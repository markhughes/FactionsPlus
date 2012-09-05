package markehme.factionsplus.config.yaml;

import markehme.factionsplus.util.Q;



public class WYSection<METADATA_TYPE> extends WY_IDBased<METADATA_TYPE> {
	
	private WYItem	firstChild, lastChild;
	private int	tmp_currentLine;
	
	
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
	
	
	public void append( WYItem child ) {
		if ( lastChild != null ) {// means first is also non-null
			assert lastChild.getNext() == null;
			lastChild.setNext( child );
			child.setPrev( lastChild );
			lastChild = child;
			assert child.getNext() == null;
		} else {
			// was previously empty
			firstChild = lastChild = child;
			assert null == child.getPrev();
			assert null == child.getNext();
		}
		
		assert child.getParent() == null;
		child.setParent( this );
	}
	
	
	public void prependWithCloneAndOrphaning( WYItem childToBeClonedAndOrphaned ) {
		try {
			WYItem clone = childToBeClonedAndOrphaned.clone();
			clone.setNext( null );
			clone.setPrev( null );
			clone.setParent( null );
			prepend(clone);
		} catch ( CloneNotSupportedException e ) {
			e.printStackTrace();
		}
	}
	
	public void prepend( WYItem orphanedChild ) {
		if ( firstChild != null ) {// means first is also non-null
			assert firstChild.getPrev() == null;
			firstChild.setPrev( orphanedChild );
			orphanedChild.setNext( firstChild );
			firstChild = orphanedChild;
			assert orphanedChild.getPrev() == null;
		} else {
			// was previously empty
			firstChild = lastChild = orphanedChild;
			assert null == orphanedChild.getPrev();
			assert null == orphanedChild.getNext();
		}
		
		assert orphanedChild.getParent() == null;
		orphanedChild.setParent( this );
	}
	
	
	public WYItem getLast() {
		return lastChild;
	}
	
	
	
	public void remove(WYItem<METADATA_TYPE> child) {
		assert child.getParent() == this : "bad parameter passed";
		
		if (firstChild == child) {
			firstChild=child.getNext();//can be null;
		}
		
		if (lastChild==child) {
			lastChild=child.getPrev();//can be null
		}
		
		WYItem oldPrev = child.getPrev();
		if ( null != oldPrev ) {// need to set its next
			assert oldPrev.getNext() == child : "bug somewhere else";
			oldPrev.setNext( child.getNext() );//which can be null
		}
		
		WYItem oldNext = child.getNext();
		if ( null != oldNext ) {
			assert oldNext.getPrev() == child : "bug somewhere else";
			oldNext.setPrev( child.getPrev() );
		}
		
		child.setParent( null );
		child.setPrev( null );
		child.setNext( null );
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
		//XXX: best to not destroy it, else we might have to cache it
//		wid.setId( "###destroyed by WYSection.replaceAndTransformSelfInto_WYComment()###" + wid.getId() + "###" );
//		wid.setValue( "###destroyed by WYSection.replaceAndTransformSelfInto_WYComment()###" + wid.getValue() + "###" );
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
	private void private_unsynchronized_recalculateLineNumbers( WYSection root ) {
		assert Q.nn( root );
		WYItem currentItem = root.getFirst();
		
		while ( null != currentItem ) {
			
			tmp_currentLine++;//next line

			currentItem.setLineNumber(tmp_currentLine);
			

			if ( WYSection.class == currentItem.getClass() ) {
				WYSection cs = (WYSection)currentItem;
				private_unsynchronized_recalculateLineNumbers( cs );// recurse
			}
			
			currentItem = currentItem.getNext();
		}
	}
	
	public void recalculateLineNumbers( WYSection root, int whatThePreviousLineNumber ) {
		synchronized (this) {
			tmp_currentLine=whatThePreviousLineNumber;
			private_unsynchronized_recalculateLineNumbers(this);
		}
	}
	public void recalculateLineNumbers() {
		
		recalculateLineNumbers( this, 0 );
	}


	public void insertBefore( WYItem<METADATA_TYPE> insertThis, WYItem<METADATA_TYPE> beforeThis ) {
		assert null != insertThis;
		assert null != beforeThis; 
		assert beforeThis.getParent() == this;
		
		assert insertThis.getParent() == null;
		assert insertThis.getNext() == null;
		assert insertThis.getPrev() == null;
		
		if (beforeThis == firstChild) {
			firstChild=insertThis;
		}else {
			assert beforeThis.getPrev() != null;
		}
		
		WYItem oldPrev = beforeThis.getPrev();
		if (null != oldPrev) {
			oldPrev.setNext( insertThis );
		}
		insertThis.setPrev( oldPrev );
		beforeThis.setPrev( insertThis );
		
		
		insertThis.setNext( beforeThis );
		
		insertThis.setParent( this );
	}
	
}
