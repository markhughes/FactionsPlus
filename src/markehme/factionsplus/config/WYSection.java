package markehme.factionsplus.config;

import markehme.factionsplus.extras.*;



public class WYSection extends WYItem {
	
	private String	identifier;
	private WYItem first,last;

	
	public WYSection( String id , WYSection parent, WYItem prev) {
		super(parent, prev);
		identifier = id;
		assert Q.nn( identifier );
		first=null;
		last=null;
	}
	
	public String getSectionName(){
		return identifier;
	}
	
	@Override
	public String toString() {
		return getSectionName()+":";
	}

//	public void setChild( WYItem wyItem ) {
//		child=wyItem;//can be null
//	}
	public WYItem getFirst() {
		return first;
	}

	public void append( WYItem child ) {
		if (last != null) {
			last.setNext( child );
		}else {
			first=last=child;
		}
	}
}
