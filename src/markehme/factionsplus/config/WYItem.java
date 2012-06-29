package markehme.factionsplus.config;


public abstract class WYItem {

	private WYItem prev;
	private WYItem next;
	private WYItem parent;
	
	public WYItem(WYItem _parent, WYItem _prev) {
		//constructor
		prev=_prev;//can be null
		parent=_parent;//can be null
		next=null;
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
}
