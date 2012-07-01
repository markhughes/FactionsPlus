package markehme.factionsplus.config.yaml;


/**
 * all data (of a line) that are stored in these (subclasses of this) are to be stored without their leading spaces for their current level<br>
 * "  some"<br>
 * "    some2"<br>
 * if level was 2, we have:<br>
 * "some"<br>
 * "  some2"<br>
 * stored;<br>
 */
public abstract class WYItem {

	private WYItem prev;
	private WYItem next;
	private WYSection parent;
	private int	lineNum;
	
	
	/**
	 * just a lonely WYItem, not part of any parents
	 * @param lineNumber keep the line number for later reference, ie. when later detecting duplicates so that we can inform of the exact line number of the dup
	 */
	public WYItem(int lineNumber){//, WYSection _parent, WYItem _prev) {
		//constructor
		lineNum=lineNumber;
		prev=null;
		parent=null;
		next=null;
//		if (null != prev) {
////			assert null == prev.getNext();well not always, in case of transform..() being called
//			prev.setNext( this );
//		}
//		if (null != parent) {
//			parent.append(this);
//		}
	}
	
	protected void setParent(WYSection _par) {
		parent=_par;
	}
	
	public int getLineNumber(){
		return lineNum;
	}
	
	public void setNext(WYItem nxt) {
		next=nxt;//can be null
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
		prev=_prev;
	}

//	public void addToChain(WYItem newItem, WYItem parent, WYItem prev, WYItem next) {
//		
//	}
}
