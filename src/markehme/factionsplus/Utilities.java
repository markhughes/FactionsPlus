package markehme.factionsplus;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

import org.bukkit.World;
import org.bukkit.entity.Player;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;


public class Utilities {
	/* ********** FILE RELATED ********** */

	public static String readFileAsString(String filePath) {
		try {
			FileInputStream fstream = new FileInputStream(filePath);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			String fullThing = "";

			while ((strLine = br.readLine()) != null)   {
				fullThing = fullThing + strLine + "\r\n";
			}

			in.close();

			return fullThing;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}


	public static void writeToFile(String fileN, String T) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(fileN), true));
			bw.write(T);
			bw.newLine();
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* ********** JAIL RELATED ********** */

	public static boolean isJailed(Player thePlayer) {
		FPlayer fplayer = FPlayers.i.get(thePlayer.getName());
		File jailDataFile = new File(FactionsPlus.BASE_FOLDER + "jails" + File.separator + "jaildata." + fplayer.getFactionId() + "." + thePlayer.getName());

		if(!jailDataFile.exists()) {
			return false;
		}

		String JailData = null;

		JailData = Utilities.readFileAsString(FactionsPlus.BASE_FOLDER + "jails" + File.separator + "jaildata." + fplayer.getFactionId() + "." + thePlayer.getName());

		if(JailData == "0") {
			return false;
		} else {
			return true;
		}
	}

	/* ********** FACTIONS RELATED ********** */

	public static boolean isOfficer(FPlayer fplayer) {
		String role = fplayer.getRole().toString().toLowerCase().trim();
		if(role.contains("officer") || role.contains("moderator")) {
			return true;
		}
		return false;
	}

	public static boolean isLeader(FPlayer fplayer) {
		String role = fplayer.getRole().toString().toLowerCase().trim();
		if(role.contains("leader") || role.contains("admin")) {
			return true;
		}
		return false;
	}

	public static boolean checkGroupPerm(World world, String group, String permission) {
		if(FactionsPlus.config.getBoolean("enablePermissionGroups")) {
			return(FactionsPlus.permission.groupHas(world, group, permission));
		} else {
			return true;
		}
	}

	public static void addPower(Player player, double amount) {
		FPlayer fplayer = FPlayers.i.get(player);
		fplayer.setPowerBoost(fplayer.getPowerBoost() + amount);
	}

	public static void addPower(FPlayer player, double amount) {
		player.setPowerBoost(player.getPowerBoost() + amount);
	}

	public static void addPower(String player, double amount) {
		FPlayer fplayer = FPlayers.i.get(player);
		fplayer.setPowerBoost(fplayer.getPowerBoost() + amount);
	}

	public static void removePower(Player player, double amount) {
		FPlayer fplayer = FPlayers.i.get(player);
		fplayer.setPowerBoost(fplayer.getPowerBoost() - amount);
	}

	public static void removePower(FPlayer player, double amount) {
		player.setPowerBoost(player.getPowerBoost() - amount);
	}

	public static void removePower(String player, double amount) {
		FPlayer fplayer = FPlayers.i.get(player);
		fplayer.setPowerBoost(fplayer.getPowerBoost() - amount);
	}

	public static int getCountOfWarps(Faction faction) {
		File currentWarpFile = new File(FactionsPlus.BASE_FOLDER + "warps" + File.separator + faction.getId());

		int c = 0;
		if (currentWarpFile.exists()) {	
			try {
				FileInputStream fstream = new FileInputStream(currentWarpFile);
				DataInputStream in = new DataInputStream(fstream);
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
				String strLine;

				while ((strLine = br.readLine()) != null) {
					if(strLine.contains(":")) {
						c++;
					}
				}
				in.close();
			} catch (Exception e) {
				e.printStackTrace();			
			}
		}
		return c;
	}
}
