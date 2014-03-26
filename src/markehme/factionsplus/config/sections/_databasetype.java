package markehme.factionsplus.config.sections;

import markehme.factionsplus.config.BooleanFormatException;
import markehme.factionsplus.config._Base;
import markehme.factionsplus.database.DatabaseType;

public class _databasetype extends _Base {

	public DatabaseType _;
	public DatabaseType _defaultValue;
	
	
	public _databasetype(DatabaseType defaultValue) {
		_defaultValue = defaultValue;
		_=defaultValue;
	}
	
	@Override
	public void setValue( String value ) {
		
		_ = DatabaseType.parse(value.trim().toLowerCase());
		
	}

	@Override
	public String getValue() {
		return _.toString();
	}

	@Override
	public String getDefaultValue() {
		return "flatfile";
	}
}
