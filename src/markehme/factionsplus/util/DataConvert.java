package markehme.factionsplus.util;

import org.bukkit.ChatColor;

import com.massivecraft.factions.Rel;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.MCore.FPUConf;
import markehme.factionsplus.config.OldConfig;

@SuppressWarnings("deprecation")
public class DataConvert {
	public static void updated(String x, String y) {
		FactionsPlus.info(ChatColor.WHITE + "          - "+x+" -> "+y+"");
	}
	
	public static void doConvert() {
		if(OldConfig._jails.enabled._) {
			FactionsPlus.info(			ChatColor.WHITE + "[Convert] - Jails");
			
			FPUConf.get("default").jailsEnabled = true;
			
			if(OldConfig._jails.leadersCanSetJails._) {
				updated("_jails.leadersCanSetJails._", "whoCanSetJails[Rel.LEADER]");
				
				FPUConf.get("default").whoCanSetJails.remove(Rel.LEADER);
				FPUConf.get("default").whoCanSetJails.put(Rel.LEADER, true);
			}

			if(OldConfig._jails.officersCanSetJails._) {
				updated("_jails.officersCanSetJails._", "whoCanSetJails[Rel.OFFICER]");
				
				FPUConf.get("default").whoCanSetJails.remove(Rel.OFFICER);
				FPUConf.get("default").whoCanSetJails.put(Rel.OFFICER, true);
			}

			if(OldConfig._jails.membersCanSetJails._) {
				updated("_jails.officersCanSetJails._", "whoCanSetJails[Rel.MEMBER]");
				
				FPUConf.get("default").whoCanSetJails.remove(Rel.MEMBER);
				FPUConf.get("default").whoCanSetJails.put(Rel.MEMBER, true);
			}
			
			if(OldConfig._jails.canJailOnlyIfIssuerIsInOwnTerritory._) {
				updated("_jails.canJailOnlyIfIssuerIsInOwnTerritory._", "mustBeInOwnTerritoryToJail");
				
				FPUConf.get("default").mustBeInOwnTerritoryToJail = true;
			}
			
			
		}
	}
}
