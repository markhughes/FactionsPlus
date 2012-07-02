# FactionsPlus version 0.4.7

* added new config option Teleports.`disallowTeleportingToEnemyLandViaEnderPeals`
if set(to true) this will prevent ender pearl teleports that land in enemy territory
(you can still teleport outside of enemy land as long as ender pearl does land outside it)
  
* fixed /f gc and /f cf commands to not error on console
  they won't work for Factions 1.7 due to it counting on other plugins like HeroChat to provide faction/global chat
  (this might be fixed in the future so that you can use these commands in 1.7 too)
  
* minimized overhead & memory-consumption when accessing the user defined config options at runtime

### config.yml features
* all subsequent (detected as)duplicate config options are automatically ignored and transformed into comments with
  a warning showing on console, the commented options are preceded by "# DUPLICATE #"

* LWC Locks Reset Fixing (useLWCIntegrationFix)

* /f unban

* Bridge between 1.6 and 1.7

* New Configuration setup

* warpTeleportAllowedEnemyDistance

* blockCPublicAccessOnNonOwnFactionTerritory - will allow you to make a door truly public

* General bugfixes, including a bunch of npes, and other annoyances 



# FactionsPlus version 0.4.6
* nothing specified here

