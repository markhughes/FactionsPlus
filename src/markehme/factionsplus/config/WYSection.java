package markehme.factionsplus.config;

import markehme.factionsplus.extras.*;



public class WYSection extends WY_IDBased {
	
	private WYItem firstChild,lastChild;

	
	public WYSection( String id , WYSection parent, WYItem prev) {
		super(id, parent, prev);
		firstChild=null;
		lastChild=null;
//		System.out.println("Section "+this+" parent="+parent+" prev="+prev);
	}
	
	public String getSectionName(){
		return getId();
	}
	
	@Override
	public String toString() {
		return getSectionName()+":";
	}

	public WYItem getFirst() {
		return firstChild;
	}

	void append( WYItem child ) {
		if (lastChild != null) {
			lastChild.setNext( child );
			child.setPrev(lastChild);
			lastChild=child;
		}else {
			firstChild=lastChild=child;
		}
	}

}
