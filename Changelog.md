# FactionsPlus version 0.4.7

* fixed situations in which, while using Factions 1.7, checking if a faction was or not SafeZone would yield true if
it had explosions disabled

* properly closing any resources which were previously leaking

* new command: `/f reloadfp [all|config|templates]` causes reloading of the specified config(s)
just in case you manually edited them, you now thus don't have to issue a bukkit 'reload' or start/stop server
permission used is the same as the one for Factions, namely `factions.reload` (for both v 1.7 and 1.6)  
Note that if you delete `config.yml` before issuing `/f reloadfp` then the created one will have the same settings as
the previous one except the comments (because those are not yet implemented: namely each option having it's own comment)

* FactionsPlus commands are shown in /f help in both Factions 1.6 and 1.7

* New config option Teleports.`disallowTeleportingToEnemyLandViaHomeCommand` which will deny all teleports caused by
"/home" command only if they would've landed you in an enemy faction (enemy is a relation between you and that faction)  
It makes sure that you cannot exploit this by having home set outside enemy land and obstructing it to get you inside.

* added new config options to deny/report teleports via /home command or ender pearls that would land inside 
ally/neutral/enemy territory or into safezone/warzone. Here they are with their defaults:  
  - `Teleports.intoTerritory.Ally.deny.ViaHomeCommand`: false
  - `Teleports.intoTerritory.Ally.deny.ViaEnderPeals`: false
  - `Teleports.intoTerritory.Ally.reportOnConsole.ifTeleportCauseIs_Command`: false
  - `Teleports.intoTerritory.Ally.reportOnConsole.ViaEnderPeals`: false
  - `Teleports.intoTerritory.Enemy.deny.ViaHomeCommand`: true
  - `Teleports.intoTerritory.Enemy.deny.ViaEnderPeals`: true
  - `Teleports.intoTerritory.Enemy.reportOnConsole.ifTeleportCauseIs_Command`: true
  - `Teleports.intoTerritory.Enemy.reportOnConsole.ViaEnderPeals`: true
  - `Teleports.intoTerritory.Neutral.deny.ViaHomeCommand`: false
  - `Teleports.intoTerritory.Neutral.deny.ViaEnderPeals`: false
  - `Teleports.intoTerritory.Neutral.reportOnConsole.ifTeleportCauseIs_Command`: false
  - `Teleports.intoTerritory.Neutral.reportOnConsole.ViaEnderPeals`: false
  - `Teleports.intoTerritory.SafeZone.denyIfViaEnderPeals`: false
  - `Teleports.intoTerritory.WarZone.denyIfViaEnderPeals`: false  
Note that these are automatically added into your config, you don't have to add them manually but if you do, you'll have
realize that each "." actually represents a section ie. Teleports: then next line 2 spaces then intoYTerritory: and so on 4 spaces...
Reporting is done on console only. Denying will be instant, regardless of any warm-up delays other plugins may have.
The expected console message upon report would look similar to this:  
> 19:12:52 [INFO] [FactionsPlus] Player 's2' teleported into enemy land faction 'fac' using command: '/home my1'.
You may test this by making yourself op and using /home to tp into enemy territory. Which is denied by default, but
allowed for OPs.  
The used pearl is wasted and a message will show.

* new config options: Teleports.`disallowTeleportingToWarZoneViaEnderPeals` 
  and Teleports.`disallowTeleportingToSafeZoneViaEnderPeals`
  they prevent teleporting via ender pearls if they land into SafeZone/WarZone
  the used pearl is wasted and a message will show.

* new config option: powerboost.'respectFactionsWarZonePowerLossRules': Applies to both the wildernessPowerLoss and warZonePowerLoss settings and integrates a check for these in the powerboost listener

* new config option: powerboost.'extraPowerWhenKillMonster': self-explanatory
 
* fixed /f gc and /f cf commands to not error on console
  they won't work for Factions 1.7 due to it counting on other plugins like HeroChat to provide faction/global chat
  (this might be fixed in the future so that you can use these commands in 1.7 too)
  
* minimized overhead & memory-consumption when accessing the user defined config options at runtime

* LWC Locks Reset Fixing extras.Protection.LWC.`removeAllLocksOnClaim`
this will remove the locks in the claimed chunk, unless the locks are owned by anyone in the same faction you are in
in other words, locks owned by people in your faction(including you) won't be removed

* New command: /f clearlocks: faction admin accessible only, basically performs a clean of all non-faction owned protections in that chunk. Permission 'factionsplus.clearlwclocks'

* /f unban

* Bridge between 1.6 and 1.7, you only need one FactionsPlus.jar regardless of what Factions.jar you're using

* warpTeleportAllowedEnemyDistance

* blockCPublicAccessOnNonOwnFactionTerritory - will allow you to make a door truly public

* General bugfixes, including a bunch of NPEs, and other annoyances 

* - fixed /f togglestate to only charge you if we're hooked into economy  
  and to not cause internal error if inexistent faction tag was specified. 
  - If you have `factionsplus.togglestate.others` permission (or you are Op) you can toggle other factions
   only if you are (Op or)admin/officer/member in your faction if those were set in the config.
  - Prevent changing the state of WarZone, SafeZone or Wilderness, unless you are Op.
  - Even as Op, you are still charged the money (it's not like you can't afford it ;) ).
  - /f toggle yourfactiontag  is equivalent with /f toggle
  - Now tells you the faction tag which was affected by the change

* fixed NPE when joined while being previously in faction jail

* fixed spazzing out while in faction jail and tried to move once, you may now look around

* disallowed teleporting out while in faction jail either via commands like /home or via ender peals etc.

### config.yml features

* New Configuration setup

* obsolete config options are automatically upgraded/transformed into the newly named ones.  
  This implies that the old value is kept. You are also notified on console about this.

* invalid config options are automatically commented out.  You are also notified on console about this. 

* using obsolete/old config options combined with the new ones will auto comment the old ones with "# OVERRIDDEN..."
  so that you know that they are ignored. You are also notified on console about this.

* all subsequent (detected as)duplicate config options are automatically ignored and transformed into comments with
  a warning showing on console, the commented options are preceded by "# DUPLICATE #"
  You are also notified on console about this.
  
* comments `#` are kept on each save, you may add more or delete them.  
Comments are any lines whose first non-whitespace character is `#`.

### Known issues:
* `/f reloadfp` refreshes only the economy state, ie. if you disabled some options which were previously enabled they 
may still be in effect after reload (except economy that will auto enabled/disable on reload)

# FactionsPlus version 0.4.6
* this Changelog file did not exist in this version, changes are unknown.

