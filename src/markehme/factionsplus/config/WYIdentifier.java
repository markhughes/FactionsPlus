package markehme.factionsplus.config;

import markehme.factionsplus.extras.*;



/**
 * two of these are .equals() if their ids .equals(), regardless of their value; used for HashMap<br>
 */
public class WYIdentifier extends WY_IDBased {
	
	private String	value;
	
	
	public WYIdentifier( int lineNumber, String identifier, String strValue){//, WYSection parent, WYItem prev ) {
		super( lineNumber, identifier);//, parent, prev );
		value = strValue;
		assert Q.nn( value );
		// System.out.println("ID "+this+" parent="+parent+" prev="+prev);
	}
	
	
	public String getValue() {
		return value;
	}
	
	
	@Override
	public String toString() {
		return super.toString() + " " + getValue();
	}
	
	
	@Override
	public boolean equals( Object obj ) {
		if ( ( null == obj ) || ( !( obj instanceof WYIdentifier ) ) ) {// which means can be subclass of WYIdentifier too
			return false;
		}
		return ( (WYIdentifier)obj ).getId().equals( this.getId() );
	}
	
	
	@Override
	public int hashCode() {
		return this.getId().hashCode();
	}
	
	
	/**
	 * this will completely remove `this` from chain, while it is replaced by an instance of WYComment which has the entire Id:
	 * Value line of
	 * WYIdentifier commented out
	 * 
	 * @return WYComment
	 */
	public WYComment replaceAndTransformSelfInto_WYComment() {
		WYItem oldNext = getNext();
		WYItem oldPrev = getPrev();
		WYItem oldParent = getParent();// just for checks
		
		WYComment asComment =
			new WYComment( getLineNumber(), WannabeYaml.commentChar + WannabeYaml.space + getId()
				+ WannabeYaml.IDVALUE_SEPARATOR + WannabeYaml.space + getValue());
1
		if ( null != oldNext ) {// had a next, must update its prev
			WYItem nextsPrev = oldNext.getPrev();
			assert nextsPrev == this;
			
			oldNext.setPrev( asComment );
			
		}// else `this` had no next
		
		// must still update comment's prev, if `this` had any, NO, actually this is done by constructor
		if ( null != oldPrev ) {
//			assert null == asComment.getPrev();// yeah should've been null by default, due to constructor call for WYComment
//			asComment.setPrev( oldPrev );
			assert asComment.getPrev() == oldPrev;
			// just making sure of assumptions
			assert oldPrev.getNext() == asComment;// already done by constructor
		}// else `this` has no prev
		
		assert asComment.getParent() == oldParent;
		assert this.getParent() == null;// should have no parent anymore
		
		// attempt to destroy or mark this id so that we can catch if we're still using it in other outside lists
		setNext( null );
		setPrev( null );
		setId( "###destroyed by replaceAndTransformSelfInto_WYComment()###" );
		setValue( "###destroyed by replaceAndTransformSelfInto_WYComment()###" );
		// that's a mark that tells you, when you ever see it, that you're not supposed to be using this, if you do, you'll know
		// you probably missed removing it from some list/hashmap after you called this method we're in
		return asComment;
	}
	
	
	private void setValue( String string ) {
		value = string;
	}
	
}
