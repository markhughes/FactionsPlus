package markehme.factionsplus.config.sections;

import markehme.factionsplus.config.Option;
import markehme.factionsplus.database.DatabaseType;

public class Section_Database {
	
	@Option(realAlias_inNonDottedFormat = "type"  )
	public  final DatabaseType	type		=  DatabaseType.FlatFile;
	
	@Option(realAlias_inNonDottedFormat = "username"  )
	public  final String		username	=  new String("factionsplus");
	
	@Option(realAlias_inNonDottedFormat = "password"  )
	public  final String		password	=  new String("h3llo.w0rld");
	
	@Option(realAlias_inNonDottedFormat = "database"  )
	public  final String		database	=  new String("FactionsPlusDB");
	
}
