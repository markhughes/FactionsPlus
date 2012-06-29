package markehme.factionsplus.config;


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
	
	
	/**
	 * this is the next aka appended item
	 * @param _parent
	 * @param _prev
	 */
	public WYItem(WYSection _parent, WYItem _prev) {
		//constructor
		prev=_prev;//can be null
		parent=_parent;//can be null
		next=null;
		if (null != prev) {
			assert null == prev.getNext();
			prev.setNext( this );
		}
		if (null != parent) {
			parent.append(this);
		}
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
