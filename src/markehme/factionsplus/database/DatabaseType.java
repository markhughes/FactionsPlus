package markehme.factionsplus.database;

public enum DatabaseType {
	
	FlatFile (1, true, false, false),
	Redis (2, false, true, false),
	MySQL (3, false, false, true),
	;

	private final int _id;
	private final boolean iFlatFile, iRedis, iMySQL;
	
	public int getID() { return this._id; }
	public boolean isFlatFile() { return this.iFlatFile; }
	public boolean isRedis() { return this.iRedis; }
	public boolean isMySQLi() { return this.iMySQL; }
	
	/**
	 * FType value setter, not public. Does not need to be used outside of this enum. 
	 * @param id
	 * @param isFaction
	 * @param isSafezone
	 * @param isWarZone
	 * @param isWilderness
	 */
	private DatabaseType( final int id, final boolean isFlatFile, final boolean isRedis, final boolean isMySQL ) {
		this._id = id;
		this.iFlatFile = isFlatFile;
		this.iRedis = isRedis;
		this.iMySQL = isMySQL;

	}
	
	public static DatabaseType parse(String s) {
		if(s.equals("flatfile")) {
			return DatabaseType.FlatFile;
		} else if(s.equals("redis")) {
			return DatabaseType.Redis;
		} else if(s.equals("mysql")) {
			return DatabaseType.MySQL;
		}
		
		return null;
	}
}
