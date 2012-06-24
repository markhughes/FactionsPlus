package markehme.factionsplus;

import java.io.*;

import org.bukkit.World;
import org.bukkit.entity.Player;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;


public class Utilities {
	/* ********** FILE RELATED ********** */
	
	public static String readFileAsString(File filePath) {
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
		File jailDataFile = new File(FactionsPlus.folderJails,"jaildata." + fplayer.getFactionId() + "." + thePlayer.getName());

		if(!jailDataFile.exists()) {
			return false;
		}

		String JailData = Utilities.readFileAsString(jailDataFile);

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
		File currentWarpFile = new File(FactionsPlus.folderWarps, faction.getId());

		int c = 0;
		if (currentWarpFile.exists()) {	
			FileInputStream fstream=null;
			DataInputStream in=null;
			BufferedReader br =null;
			try {
				fstream = new FileInputStream(currentWarpFile);
				in = new DataInputStream(fstream);
				br= new BufferedReader(new InputStreamReader(in));
				String strLine;

				while ((strLine = br.readLine()) != null) {
					if(strLine.contains(":")) {
						c++;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();			
			} finally {
				if ( null != br ) {
					try {
						br.close();
					} catch ( IOException e ) {
						e.printStackTrace();
					}
				}
				if ( null != in ) {
					try {
						in.close();
					} catch ( IOException e ) {
						e.printStackTrace();
					}
				}
				if ( null != fstream ) {
					try {
						fstream.close();
					} catch ( IOException e ) {
						e.printStackTrace();
					}
				}
			}
		}
		return c;
	}
	
	public static File getCurrentFolder() {
		return new File("");
	}
	
}
