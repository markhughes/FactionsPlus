package markehme.factionsplus.config;

import java.lang.reflect.Field;

import markehme.factionsplus.config.yaml.WYIdentifier;



public class InvalidConfigValueTypeException extends RuntimeException {
	
	public InvalidConfigValueTypeException( WYIdentifier<COMetadata> wID, Field field, Throwable t ) {
		super( "----------\nThe config option `" + wID.getID_InAbsoluteDottedForm( Config.virtualRoot )
			+ "` at line `"+wID.getLineNumber()+"` had an unexpected type for the value `" +
				wID.getValue() + "` the expected type was: `"
			+ field.getType().getSimpleName() + "`", t );
	}
	
}
