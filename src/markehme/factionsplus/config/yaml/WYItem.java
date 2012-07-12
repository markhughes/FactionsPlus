package markehme.factionsplus.config.yaml;


/**
 * all data (of a line) that are stored in these (subclasses of this) are to be stored without their leading spaces for their
 * current level<br>
 * "  some"<br>
 * "    some2"<br>
 * if level was 2, we have:<br>
 * "some"<br>
 * "  some2"<br>
 * stored;<br>
 * 
 * @param <METADATA_TYPE>
 *            the base class for the accepted metadata objects, this type is only so it could help with compile time typing,
 *            it's erased to Object type at runtime
 */
public abstract class WYItem<METADATA_TYPE> implements Cloneable {
	
	private WYItem			prev;
	private WYItem			next;
	private WYSection		parent;
	private int				lineNum;
	private METADATA_TYPE	pointerToMetadata	= null;
	
	
	// FIXME: must store lines relative to how many items are inserted, based on whoever we consider top parent but everytime
	// we'd insert a new item all lines after it must be recomputed which may not be what we want ... i see
	// maybe allow a method to be called to recompute all lines, once, and not on every init
	
	@Override
	public WYItem clone() throws CloneNotSupportedException {
		return (WYItem)super.clone();
	}
	
	/**
	 * just a lonely WYItem, not part of any parents
	 * 
	 * @param lineNumber
	 *            keep the line number for later reference, ie. when later detecting duplicates so that we can inform of the
	 *            exact line number of the dup
	 */
	public WYItem( int lineNumber ) {// , WYSection _parent, WYItem _prev) {
		// constructor
		lineNum = lineNumber;
		prev = null;
		parent = null;
		next = null;
		// if (null != prev) {
		// // assert null == prev.getNext();well not always, in case of transform..() being called
		// prev.setNext( this );
		// }
		// if (null != parent) {
		// parent.append(this);
		// }
	}
	
	
	/**
	 * stores a POINTER to your metadata object for this {@link WYItem}, it is NOT CLONED<br>
	 * 
	 * @param metadata
	 *            an user defined object (can be null) which is a class or subclass of METADATA_TYPE passed to {@link WYItem}
	 * @return old metadata (can be null)
	 */
	public METADATA_TYPE setMetadata( METADATA_TYPE metadata ) {
		METADATA_TYPE oldMetadata = pointerToMetadata;
		pointerToMetadata = metadata;
		return oldMetadata;
	}
	
	
	public METADATA_TYPE getMetadata() {
		return pointerToMetadata;
	}
	
	
	protected void setParent( WYSection _par ) {
		parent = _par;
	}
	
	
	public int getLineNumber() {
		return lineNum;
	}
	
	
	public void setNext( WYItem nxt ) {
		next = nxt;// can be null
	}
	
	
	public WYItem getNext() {
		return next;
	}
	
	
	public WYItem getPrev() {
		return prev;
	}
	
	
	public WYSection getParent() {
		return parent;
	}
	
	
	public void setPrev( WYItem _prev ) {
		prev = _prev;
	}
	
	
	public void setLineNumber( int newLineNumber ) {
		lineNum = newLineNumber;
	}
	
	// public void addToChain(WYItem newItem, WYItem parent, WYItem prev, WYItem next) {
	//
	// }
}
