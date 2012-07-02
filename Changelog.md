# FactionsPlus version 0.4.7

* added new config option Teleports.`disallowTeleportingToEnemyLandViaEnderPeals`
if set(to true) this will prevent ender pearl teleports that land in enemy territory
(you can still teleport outside of enemy land as long as ender pearl does land outside it)
  
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

* new config options: Teleports.`disallowTeleportingToWarZoneViaEnderPeals` 
  and Teleports.`disallowTeleportingToSafeZoneViaEnderPeals`
  they prevent teleporting via ender pearls if they land into SafeZone/WarZone
  the used pearl is wasted and a message will show.

* fixed /f togglestate to only charge you if economy.`enabled` is set
  and to not cause internal error if inexistent faction tag was specified 
  if you have `factionsplus.togglestate.others` permission (or you are Op) you can toggle other factions
   only if you are admin/officer/member in your faction (or you are Op) if those were set in the config
  

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
  
* comments "#" are kept on each save, you may delete them (any line starting with a "#" )

### Known bugs:
* if you're using groupmanager once you gave a permission like `factionsplus.togglestate.others` to your default group
if you comment it out (remove)it and then `reload` the permission is still seen as active (even if you stop and 
restart bukkit). Not sure if this is Vault+Groupmanager and the way we use Vault's permissions or something else.

# FactionsPlus version 0.4.6
* nothing specified here

