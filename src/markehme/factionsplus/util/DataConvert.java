package markehme.factionsplus.util;

import org.bukkit.ChatColor;

import com.massivecraft.factions.Rel;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.MCore.UConf;
import markehme.factionsplus.config.OldConfig;

public class DataConvert {
	public static void updated(String x, String y) {
		FactionsPlus.info(ChatColor.WHITE + "          - "+x+" -> "+y+"");
	}
	
	public static void doConvert() {
		if(OldConfig._jails.enabled._) {
			FactionsPlus.info(			ChatColor.WHITE + "[Convert] - Jails");
			
			UConf.get("default").jailsEnabled = true;
			
			if(OldConfig._jails.leadersCanSetJails._) {
				updated("_jails.leadersCanSetJails._", "whoCanSetJails[Rel.LEADER]");
				
				UConf.get("default").whoCanSetJails.remove(Rel.LEADER);
				UConf.get("default").whoCanSetJails.put(Rel.LEADER, true);
			}

			if(OldConfig._jails.officersCanSetJails._) {
				updated("_jails.officersCanSetJails._", "whoCanSetJails[Rel.OFFICER]");
				
				UConf.get("default").whoCanSetJails.remove(Rel.OFFICER);
				UConf.get("default").whoCanSetJails.put(Rel.OFFICER, true);
			}

			if(OldConfig._jails.membersCanSetJails._) {
				updated("_jails.officersCanSetJails._", "whoCanSetJails[Rel.MEMBER]");
				
				UConf.get("default").whoCanSetJails.remove(Rel.MEMBER);
				UConf.get("default").whoCanSetJails.put(Rel.MEMBER, true);
			}
			
			if(OldConfig._jails.canJailOnlyIfIssuerIsInOwnTerritory._) {
				updated("_jails.canJailOnlyIfIssuerIsInOwnTerritory._", "mustBeInOwnTerritoryToJail");
				
				UConf.get("default").mustBeInOwnTerritoryToJail = true;
			}
			
			
		}
	}
}
