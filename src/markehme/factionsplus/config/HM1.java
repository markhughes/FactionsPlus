package markehme.factionsplus.config;

import java.lang.reflect.*;
import java.util.*;

import markehme.factionsplus.config.yaml.*;
import markehme.factionsplus.util.*;



/**
 * one to many
 */
public class HM1 extends HashMap<Field, SetOfIDs>{

	/**
	 *  If this set already contains the element, the call leaves the set unchanged and returns false.
	 * @param dottedWIDToAdd 
	 * @param widToAdd
	 * @param forField
	 * @return if non-null then the existing one is returned and the real one is not added
	 */
	public final WYIdentifier<COMetadata> shyAddWIDToSet(String dottedWIDToAdd, WYIdentifier<COMetadata> widToAdd, Field forField) {
		assert Typeo.isValidAliasFormat( dottedWIDToAdd );
		assert Q.nn( forField );
		assert null != widToAdd;
		
		SetOfIDs setOfWIDs = this.get( forField );
		if (null == setOfWIDs) {
			setOfWIDs=new SetOfIDs();
			SetOfIDs existed = this.put( forField, setOfWIDs );
			assert null == existed:"just not possible "+existed+" vs "+setOfWIDs;
		}
		WYIdentifier<COMetadata> prev = setOfWIDs.get( dottedWIDToAdd );
		if (null == prev) {
			WYIdentifier<COMetadata> x = setOfWIDs.put(dottedWIDToAdd, widToAdd);
			assert x == prev;
		}//else it just existed
		return prev;
	}
	
	@Override
	public void clear() {
		for ( java.util.Map.Entry<Field, SetOfIDs> iterable_element : this.entrySet() ) {
			iterable_element.getValue().clear();
		}
		super.clear();
	}
}
