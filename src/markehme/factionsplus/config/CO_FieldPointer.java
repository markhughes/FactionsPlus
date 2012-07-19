package markehme.factionsplus.config;

import java.lang.reflect.*;

import markehme.factionsplus.config.yaml.*;
import markehme.factionsplus.util.*;


public class CO_FieldPointer extends COMetadata {
	public final Field field;
	public final WYIdentifier<COMetadata> wid;
	public final boolean existing;//just for info purposes: if false it means this was newly added to the config either due to upgrading old or due to not existing
	
	public CO_FieldPointer( Field _field, WYIdentifier<COMetadata> _wid, boolean _existing ) {
		field=_field;
		wid=_wid;
		assert Q.nn( field );
		assert Q.nn(wid);
		existing=_existing;
	}
	

}
