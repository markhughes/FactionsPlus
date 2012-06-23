package markehme.factionsplus;

import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class FactionsPlusUpdate {
	static public void checkUpdates() {
		String content = null;
		URLConnection connection = null;
		String v = FactionsPlus.version;
		
		if(FactionsPlus.config.getBoolean("disableUpdateCheck")) {
			return;
		}
		
		FactionsPlus.info("Checking for updates ... ");
		
		try {
			connection =  new URL("http://www.markeh.me/factionsplus.php?v=" + v).openConnection();
			Scanner scanner = new Scanner(connection.getInputStream());
			scanner.useDelimiter("\\Z");
			content = scanner.next();
		} catch ( Exception ex ) {
		    ex.printStackTrace();
		}
		
		if(!content.trim().equalsIgnoreCase(v.trim())) {
			FactionsPlus.log.warning("! -=====================================- !");
			FactionsPlus.log.warning("FactionsPlus has an update, you");
			FactionsPlus.log.warning("can upgrade to version " + content.trim() + " via");
			FactionsPlus.log.warning("http://dev.bukkit.org/server-mods/factionsplus/");
			FactionsPlus.log.warning("! -=====================================- !");
		} else {
			FactionsPlus.info("Up to date!");
		}
	}
}
