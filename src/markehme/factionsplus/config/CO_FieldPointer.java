package markehme.factionsplus.config;

import java.lang.reflect.*;

import markehme.factionsplus.config.yaml.*;
import markehme.factionsplus.util.*;


public class CO_FieldPointer extends COMetadata {
	private final Field field;
	private final WYIdentifier<COMetadata> wid;
	
	public CO_FieldPointer( Field _field, WYIdentifier<COMetadata> _wid ) {
		field=_field;
		wid=_wid;
		assert Q.nn( field );
		assert Q.nn(wid);
	}

	@Override
	protected void override_apply() {
		Exception error=null;
		try {
			Typeo.setFieldValue(field,wid.getValue());
		} catch ( IllegalArgumentException e ) {
			error=e;
		} catch ( IllegalAccessException e ) {
			error=e;
		}finally{
			if (null != error) {
				throw new RuntimeException("This should not happen",error);
			}
		}
	}
	
	Field getField(){
		return field;
	}
}
