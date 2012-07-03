# FactionsPlus version 0.4.7

* new command: `/f reloadfp [all|config|templates]` causes reloading of the specified config(s)
just in case you manually edited them, you now thus don't have to issue a bukkit 'reload' or start/stop server
permission used is the same as the one for Factions, namely `factions.reload` (for both v 1.7 and 1.6)

* FactionsPlus commands are shown in /f help in both Factions 1.6 and 1.7

* added new config option Teleports.`disallowTeleportingToEnemyLandViaEnderPeals`
if set(to true) this will prevent ender pearl teleports that land in enemy territory
(you can still teleport outside of enemy land as long as ender pearl does land outside it)
  
* new config options: Teleports.`disallowTeleportingToWarZoneViaEnderPeals` 
  and Teleports.`disallowTeleportingToSafeZoneViaEnderPeals`
  they prevent teleporting via ender pearls if they land into SafeZone/WarZone
  the used pearl is wasted and a message will show.
  
* fixed /f gc and /f cf commands to not error on console
  they won't work for Factions 1.7 due to it counting on other plugins like HeroChat to provide faction/global chat
  (this might be fixed in the future so that you can use these commands in 1.7 too)
  
* minimized overhead & memory-consumption when accessing the user defined config options at runtime

* LWC Locks Reset Fixing extras.Protection.LWC.`removeAllLocksOnClaim`
this will remove the locks in the claimed chunk, unless the locks are owned by anyone in the same faction you are in
in other words, locks owned by people in your faction(including you) won't be removed

* /f unban

* Bridge between 1.6 and 1.7, you only need one FactionsPlus.jar regardless of what Factions.jar you're using

* warpTeleportAllowedEnemyDistance

* blockCPublicAccessOnNonOwnFactionTerritory - will allow you to make a door truly public

* General bugfixes, including a bunch of npes, and other annoyances 

* - fixed /f togglestate to only charge you if economy.`enabled` is set
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
  
* comments `#` are kept on each save, you may add more or delete them (any line starting with a `#` )



# FactionsPlus version 0.4.6
* this Changelog file did not exist in this version, changes are unknown.

