package markehme.factionsplus.config;


public abstract class WYItem {

	private WYItem prev;
	private WYItem next;
	private WYItem parent;
	
	/**
	 * this is the next aka appended item
	 * @param _parent
	 * @param _prev
	 */
	public WYItem(WYItem _parent, WYItem _prev) {
		//constructor
		prev=_prev;//can be null
		parent=_parent;//can be null
		next=null;
		if (null != prev) {
			assert null == prev.getNext();
			prev.setNext( this );
		}
		System.out.println(this+" "+parent+" "+prev);
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
	
	public WYItem getParent() {
		return parent;
	}
	
//	public void addToChain(WYItem newItem, WYItem parent, WYItem prev, WYItem next) {
//		
//	}
}
