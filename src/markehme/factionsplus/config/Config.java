package markehme.factionsplus.config;

import java.io.*;
import java.lang.annotation.*;
import java.lang.reflect.*;
import java.nio.*;
import java.util.*;
import java.util.Map.Entry;

import markehme.factionsplus.*;
import java.util.Map.Entry;
import markehme.factionsplus.*;
import markehme.factionsplus.FactionsBridge.*;
import markehme.factionsplus.config.sections.*;
import markehme.factionsplus.config.yaml.*;
import markehme.factionsplus.events.*;
import markehme.factionsplus.extras.*;
import markehme.factionsplus.util.*;

import org.bukkit.*;
import org.bukkit.configuration.*;
import org.bukkit.configuration.file.*;
import org.bukkit.event.*;
import org.bukkit.plugin.*;
import org.yaml.snakeyaml.*;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;

import com.avaje.ebean.enhance.agent.*;



public abstract class Config {// not named Conf so to avoid conflicts with com.massivecraft.factions.Conf

	// do not use Plugin.getDataFolder() here, it will NPE, unless you call it in/after onEnable()
	public static final File				folderBase				= new File( "plugins" + File.separator + "FactionsPlus" );
	public static final File				folderWarps				= new File( folderBase, "warps" );
	public static final File				folderJails				= new File( folderBase, "jails" );
	public static final File				folderAnnouncements		= new File( folderBase, "announcements" );
	public static final File				folderFRules			= new File( folderBase, "frules" );
	public static final File				folderFBans				= new File( folderBase, "fbans" );
	public static final File				fileDisableInWarzone	= new File( folderBase, "disabled_in_warzone.txt" );
	
	public static File						templatesFile			= new File( folderBase, "templates.yml" );
	public static FileConfiguration			templates;
	
	// and it contains the defaults, so that they are no longer hardcoded in java code
	public final static File						fileConfig				= new File( Config.folderBase, "config.yml" );
	
	// never change this, it's yaml compatible:
	public static final char				DOT						= '.';
	
	// Begin Config Pointers
	
	/**
	 * Caveats: YOU CAN rename all the fields, it won't affect config.yml because it uses the name inside the annotation above
	 * the field
	 * if you rename the realAlias inside the annotation then you'll have to be adding oldaliases to each of the section's fields
	 * (children) and to their child @Section 's fields and so on; unless it's not a @Section
	 * cantfix: adding old aliases for (sub)sections should not be doable, because I cannot decide which parent's oldAlias would
	 * apply to the child's alias when computing the dotted format of the child
	 * 
	 * you may change order of these fields (or section's fields) but this won't have any effect if config.yml already existed,
	 * only if new one is about to be created<br>
	 * <br>
	 * fields could be named _something so that when you type Config._ and code completion with Ctrl+Space you can see only the
	 * relevant fields<br>
	 */
	@Section(
			realAlias_neverDotted = "jails" )
	public static final Section_Jails		_jails					= new Section_Jails();
	
	
	@Section(
			realAlias_neverDotted = "warps" )
	public static final Section_Warps		_warps					= new Section_Warps();
	
	@Section(
			realAlias_neverDotted = "banning" )
	public static final Section_Banning		_banning				= new Section_Banning();
	
	
	@Section(
			realAlias_neverDotted = "rules" )
	public static final Section_Rules		_rules					= new Section_Rules();
	
	@Section(
			realAlias_neverDotted = "peaceful" )
	public static final Section_Peaceful	_peaceful				= new Section_Peaceful();
	
	@Section(
			realAlias_neverDotted = "powerboosts" )
	public static final Section_PowerBoosts	_powerboosts			= new Section_PowerBoosts();
	
	@Section(
			realAlias_neverDotted = "announce" )
	public static final Section_Announce	_announce				= new Section_Announce();
	
	
	@Section(
			realAlias_neverDotted = "economy" )
	public static final Section_Economy		_economy				= new Section_Economy();
	
	
	@Section(
			comments = {
		"some comment here, if any", "second line of comment"
			}, realAlias_neverDotted = "Teleports" )
	public static final Section_Teleports	_teleports				= new Section_Teleports();
	
	@Section(
			realAlias_neverDotted = "extras" )
	public final static Section_Extras		_extras					= new Section_Extras();
	
	@Option(
			realAlias_inNonDottedFormat = "DoNotChangeMe" )
	// this is now useless, FIXME: remove this field, OR rename and increment it every time something changes in the config ie.
	// coder adds new options or removes or changes/renames config options but not when just changes their values (id: value)
	public static final _int				doNotChangeMe			= new _int( 11 );
	
	// the root class that contains the @Section and @Options to scan for
	private static final Class				configClass				= Config.class;
	// End Config
	
	
	
	private static File						currentFolder_OnPluginClassInit;
	private static File						currentFolder_OnEnable	= null;
	
	
	private static boolean					inited					= false;
	private static boolean					loaded= false;
	
	
	/**
	 * call this one time onEnable() never on onLoad() due to its evilness;)<br>
	 */
	public final static void init() {
		assert !isLoaded();
		setInited( false );
		boolean failed = false;
		// try {
		if ( Q.isInconsistencyFileBug() ) {
			throw FactionsPlusPlugin.bailOut( "Please do not have `user.dir` property set, it will mess up so many things"
				+ "(or did you use native functions to change current folder from the one that was on jvm startup?!)" );
		}
		
		if ( hasFileFieldsTrap() ) {
			throw FactionsPlusPlugin.bailOut( "there is a coding trap which will likely cause unexpected behaviour "
				+ "in places that use files, tell plugin author to fix" );
		}
		
		// first make sure the (hard)coded options are valid while at the same time build a list of all obsolete+new
		// option
		// names
		
		Typeo.sanitize_AndUpdateClassMapping( configClass );
		setInited( true);
	}
	
	/**
	 * make sure all the File fields in this class that are likely used somewhere else in constructors like new File(field,
	 * myfile);
	 * are non-empty to avoid 'myfile' being in root of drive instead of just current folder as expected<br>
	 * this would cause some evil inconsistencies if any of those fields would resolve to empty paths<br>
	 */
	private static boolean hasFileFieldsTrap() {
		Class classToCheckFor_FileFields = Config.class;
		Field[] allFields = classToCheckFor_FileFields.getFields();
		for ( Field field : allFields ) {
			if ( File.class.equals( field.getType() ) ) {
				// got one File field to check
				try {
					File instance = (File)field.get( classToCheckFor_FileFields );
					if ( instance.getPath().isEmpty() ) {
						// oops, found one, to avoid traps where you expect new File( instance, yourfile);
						// to have 'yourfile' in root folder of that drive ie. '\yourfile' instead of what you might
						// expect "yourfile" to be just in current folder just like a new File(yourfile) would do
						return true;
					}
				} catch ( IllegalArgumentException e ) {
					Q.rethrow( e );
				} catch ( IllegalAccessException e ) {
					Q.rethrow( e );
				}
			}
		}
		return false;
	}
	
	
	/**
	 * called on plugin.onEnable() and every time you want the config to reload<br>
	 * aka reload all (well, config and templates)<br>
	 */
	public synchronized final static void reload() {
		
		Config.setLoaded( false );// must be here to cause config to reload on every plugin(s) reload from console
		Config.templates = null;
		boolean failed = false;
		try {
			
			Config.ensureFoldersExist();
			
			reloadConfig();
			
			reloadTemplates();
			
			// last:
			Config.setLoaded( true );
			// Create the event here
			Event event = new FPConfigLoadedEvent();
			// Call the event
//			throw null;//this will be caught
			Bukkit.getServer().getPluginManager().callEvent(event);
			//exceptions from inside the hooked event cannot be caught here for they are not let thru
		} catch ( Throwable t ) {
//			FactionsPlus.warn("caught");
			Q.rethrow( t );
		} finally {
			if ( failed ) {
				FactionsPlus.instance.disableSelf();// must make sure we're disabled if something failed if not /plugins would
													// show us green
				// but mostly, for consistency's sake and stuff we couldn't think of/anticipate now
			}
		}
	}
	
	

	
	protected static void ensureFoldersExist() {
		File dataF = FactionsPlus.instance.getDataFolder();
		if ( !dataF.equals( folderBase ) ) {
			throw FactionsPlusPlugin
				.bailOut( "Base folder and dataFolder differ, this may not be intended and it may just be a possible bug in the code;"
					+ "folderBase=" + folderBase + " dataFolder=" + dataF );
		}
		
		try {
			addDir( Config.folderBase );
			addDir( Config.folderWarps );
			addDir( Config.folderJails );
			addDir( Config.folderAnnouncements );
			addDir( Config.folderFRules );
			addDir( Config.folderFBans );
			
			if ( !Config.fileDisableInWarzone.exists() ) {
				Config.fileDisableInWarzone.createNewFile();
				FactionsPlusPlugin.info( "Created file: " + Config.fileDisableInWarzone );
			}
			
			if ( !Config.templatesFile.exists() ) {
				
				FactionsPlusTemplates.createTemplatesFile();
				FactionsPlusPlugin.info( "Created file: " + Config.templatesFile );
			}
			
		} catch ( Exception e ) {
			e.printStackTrace();
			throw FactionsPlusPlugin.bailOut( "something failed when ensuring the folders exist" );
		}
	}
	
	
	private static final void addDir( File dir ) {
		if ( !dir.exists() ) {
			if ( dir.getPath().isEmpty() ) {
				throw FactionsPlusPlugin.bailOut( "bad coding, this should usually not trigger here, but earlier" );
			}
			FactionsPlusPlugin.info( "Added directory: " + dir );
			dir.mkdirs();
		}
	}
	
	
	
	private final static String	bucketOfSpaces	= new String( new char[WannabeYaml.maxLevelSpaces] ).replace( '\0', ' ' );
	
	
	/**
	 * this works for yaml's .getValues(deep)
	 * 
	 * @param level
	 * @param start
	 * @throws IOException
	 */
	private final static void parseWrite( int level, Map<String, Object> start ) throws IOException {
		for ( Map.Entry<String, Object> entry : start.entrySet() ) {
			Object val = entry.getValue();
			String key = entry.getKey();
			if ( level > 0 ) {
				bw.write( bucketOfSpaces, 0, WannabeYaml.spacesPerLevel * level );
			}
			bw.write( key );
			bw.write( WannabeYaml.IDVALUE_SEPARATOR );
			if ( !( val instanceof MemorySection ) ) {
				bw.write( " " + val );
				bw.newLine();
			} else {
				bw.newLine();
				parseWrite( level + 1, ( (MemorySection)val ).getValues( false ) );
			}
		}
	}
	
	
	
	private final static void appendSection( int level, WYSection root ) throws IOException {
		assert Q.nn( root );
		WYItem currentItem = root.getFirst();
		
		while ( null != currentItem ) {
			
			Class<? extends WYItem> cls = currentItem.getClass();
			// System.out.println(currentItem+"!");
			
			if ( level > 0 ) {
				bw.write( bucketOfSpaces, 0, WannabeYaml.spacesPerLevel * level );
			}
			
			if ( currentItem instanceof WYRawButLeveledLine ) {
				bw.write( ( (WYRawButLeveledLine)currentItem ).getRawButLeveledLine() );
				bw.newLine();
			} else {
				
				if ( !( currentItem instanceof WY_IDBased ) ) {
					throw FactionsPlus.bailOut( "impossible, coding bug detected" );
				}
				
				
				if ( WYIdentifier.class == cls ) {
					WYIdentifier wid = ( (WYIdentifier)currentItem );
					// System.out.println(wid.getInAbsoluteDottedForm(virtualRoot));
					bw.write( wid.getId() );
					bw.write( WannabeYaml.IDVALUE_SEPARATOR );
					bw.write( WannabeYaml.space + wid.getValue() );
					bw.newLine();
				} else {
					if ( WYSection.class == cls ) {
						WYSection cs = (WYSection)currentItem;
						bw.write( ( cs ).getId() + WannabeYaml.IDVALUE_SEPARATOR );
						bw.newLine();
						appendSection( level + 1, cs );// recurse
					} else {
						throw FactionsPlus.bailOut( "impossible, coding bug detected" );
					}
				}
			}
			currentItem = currentItem.getNext();
		}
	}
	
	
	/**
	 * must be inside: synchronized ( mapField_to_ListOfWYIdentifier )<br>
	 * this parses the entire representation of the config.yml and marks duplicates and invalid configs<br>
	 * 
	 * @param root
	 */
	private final static void parseOneTime_and_CheckForValids( WYSection root, String dottedParentSection ) {
		assert Q.nn( root );
		WYItem<COMetadata> currentItem = root.getFirst();
		// WYSection parent = root;
		// int level=0;
		// while ( null != parent ) {
		boolean isTopLevelSection = ( null == dottedParentSection ) || dottedParentSection.isEmpty();
		
		while ( null != currentItem ) {
			
			Class<? extends WYItem> cls = currentItem.getClass();
			
			
			if ( WYSection.class == cls ) {
				WYSection cs = (WYSection)currentItem;
				// sections are not checked for having oldaliases mainly since they are part of the dotted form of a config
				// options and thus
				// are indirectly checked when config options(aka ids) are checked
				String dotted = ( isTopLevelSection ? cs.getId() : dottedParentSection + Config.DOT + cs.getId() );
				
				parseOneTime_and_CheckForValids( cs, dotted );// recurse
				// parent = cs;
				// currentItem = cs.getFirst();
			} else {
				if ( WYIdentifier.class == cls ) {
					WYIdentifier<COMetadata> wid = ( (WYIdentifier)currentItem );
					String dotted = ( isTopLevelSection ? wid.getId() : dottedParentSection + Config.DOT + wid.getId() );
					// String dotted = wid.getID_InAbsoluteDottedForm( virtualRoot );
					// System.out.println( dotted );
					Field foundAsField = Typeo.getField_correspondingTo_DottedFormat( dotted );
					if ( null == foundAsField ) {
//						System.out.println("noadd: "+dotted+" "+wid.getID_InAbsoluteDottedForm( virtualRoot ));
						// done: invalid config option encountered in config.yml transforms into comment
						// WYSection widsParent = wid.getParent();
						// assert null ! it just wouldn't ever be null, else bad coding else where heh
						COMetadata oldmd = wid.setMetadata( new CO_Invalid( wid, dotted ) );
						assert null == oldmd : "should not already have metadata, else we failed somewhere else";
					} else {
//						System.out.println("add: "+dotted+" "+wid.getID_InAbsoluteDottedForm( virtualRoot ));
						// keep track of this dotted+wid in a set for this field ie. field-> {dotted->wid}
						WYIdentifier<COMetadata> prevWID = mapFieldToID.shyAddWIDToSet( dotted, wid, foundAsField );
						if ( null != prevWID ) {// not added because there was this prev which already existed
							// existed? then we found a duplicate between all of those that we parsed from .yml
							// ie. same id occurred twice in the .yml
							// Typeo.getDottedRealAliasOfField( foundAsField );
							int activeLine = prevWID.getLineNumber();
							
							COMetadata oldmd = wid.setMetadata( new CO_Duplicate( wid, dotted, prevWID ) );
							assert null == oldmd : "should not already have metadata, else we failed somewhere else";
						} else {
							// this wid was new so we associate it with the field, the field was already associated with it
							// above
							wid.setMetadata( new CO_FieldPointer( foundAsField, wid ) );
						}
						// System.out.println( "!!!" + dotted );
						// TODO: must check if config.yml has the same id twice or more, if yes then what? last overrides?
						// or throw
						// or move extras into file?
						
						// : we can let the HashMap check if one already exists even though they will != but they will
						// .equals()
						// if we define that
						// so if two differed(subsequent) dotted forms map to the same Field, then we found duplicate
						// options in
						// .yml file
						// well actually no, the above is false premising in the current context
						
						// HashMap<WYIdentifier, String> existingWYIdList =
						// mapField_to_WYIdDotted.get( foundAsField );
						// if ( null == existingWYIdList ) {
						// // first time creating the list for this Field 'found'
						// // which also means there should be no duplicate checks in this {} block
						// existingWYIdList = new HashMap<WYIdentifier, String>();
						// HashMap<WYIdentifier, String> impossible =
						// mapField_to_WYIdDotted.put( foundAsField, existingWYIdList );
						// assert null == impossible :
						// "this just cannot freaking happen, but still, can never really `know when you're missing something` aka `be sure`";
						// assert existingWYIdList == mapField_to_WYIdDotted.get( foundAsField );
						//
						//
						// existingWYIdList.put(wid, dotted);
						// // new DualPack( dotted, wid ) );// add all config options one by one in
						// // the
						// // order of occurrence
						// // in
						// // config.yml
						//
						// assert existingWYIdList.contains( new DualPack( dotted, wid ) );
						//
						// } else {
						// // check only if the list wasn't empty, if we're here it wasn't, thus it may already have at
						// // least 1
						// // element which we
						// // must check against and see if wid isn't already existing there (as different instance though)
						// // does id already exist, ie. duplicate encountered in .yml ?
						//
						// // FIXME: this compares wid regardless of parents, but we must compare their dotted form instead
						// // so either store hashmap or make sure equals compares dotted forms
						// // hashmap will be faster, a hashmap of dotted -> wid
						// // or a wid.setEqualsComparesIncludingParentsUpTo(virtualRoot) - naah this one is too much
						// // overhead,
						// // hashmap ftw!
						//
						//
						// int index = existingWYIdList.indexOf( new DualPack( dotted, WYIdentifier.NULL ) );
						// // seeks dotted format 'wid' in list by doing .equals() on each of
						// // them // inside // the list
						// if ( index >= 0 ) {// exists already ?
						// WYIdentifier activeCfgOption = existingWYIdList.get( index ).getSecond();
						// int activeLine = activeCfgOption.getLineNumber();
						//
						// COMetadata oldmd = wid.setMetadata( new CO_Duplicate( wid, dotted, activeCfgOption ) );
						// assert null == oldmd : "should not already have metadata, else we failed somewhere else";
						//
						// // WYSection widsParent = wid.getParent();
						// // TODO: also check if it is in any other lists, it probably isn't at this time.
						// // currentItem = widsParent.replaceAndTransformInto_WYComment( wid, commentPrefixForDUPs );
						// // wid.replaceAndTransformSelfInto_WYComment();
						// // so we still have a getNext() to go to, after wid is basically destroyed(at
						// // least its getNext will be null after this)
						// // let's not forget to remove this from list,
						// // existingWYIdList.remove( index );// a MUST
						// // assert existingWYIdList.contains( wid );
						// // System.out.println(existingWYIdList.get( index ));
						// // existingWYIdList.add( index, currentItem );
						// // assert !existingWYIdList.contains( wid );
						// // assert existingWYIdList.contains( currentItem);
						//
						// // this means, it will compare id without considering values (as per WYIdentifier's
						// // .equals()
						//
						// // if we're here this will work:
						//
						//
						// // TODO: what to do when same config is encountered twice, does it override the prev one? do
						// // we
						// // stop? do
						// // we move it to some file for reviewal? or do we comment it out?
						// } else {
						// // doesn't exit, we add it to list
						// existingWYIdList.addLast( new DualPack( dotted, wid ) );
						// }
						// }
						// assert null != existingWYIdList;// obviously
						
						
					}// end of else found
					
					// Object rtcid = getRuntimeConfigIdFor( wid );// pinpoint an annotated field in {@Link Config.class}
					// if ( null == rtcid ) {
					// // there isn't a runtime option for the encountered id(=config option name)
					// // therefore we check if it's an old alias
					// Object newId = getNewIdForTheOldAlias( wid );// if any
					// if ( null != newId ) {
					// // not old alias either
					// // thus it's an invalid config option encountered
					// String failMsg = "invalid config option encountered: " + wid;// TODO: also show the in config
					// line/pos
					// // for it
					// // first make sure it won't be written on next config save, by removing it from chain
					// // wid.removeSelf();
					// throw new RuntimeException( failMsg );//it won't be written to config if we abort
					// }
					//
					// // we then found an old alias for this id, since we're here
					// if ( newId.encounteredAliasesCount() < 1 ) {
					// // update the newID's value with the old alias' value
					// newId.setValue( wid.getValue() );
					// newId.addEncounteredOldAlias( wid );
					// } else {
					// // we already encountered an alias for this id
					// // how would we know which is the right value
					// // for now we consider the last encountered alias as the overriding value
					// newId.setValue( wid.getValue() );
					// newId.addEncounteredOldAlias( wid );
					// FactionsPlus
					// .warn( " Config option " + newId.getInAbsoluteDottedForm()
					// + " was overwritten by old alias found for it "
					// + wid.getInAbsoluteDottedForm( virtualRoot ) );
					// }
					// } else {
					// if ( rtcid.wasAlreadyLinked() ) {// was linked to new id, meaning it was already set
					//
					// } else {
					// rtcid.linkTo( wid );
					// rtcid.setValue( wid.getValue() );
					// }
					// }
					
					
					
				} else {// non id
					assert ( currentItem instanceof WYRawButLeveledLine );
					// ignore raw lines like comments or empty lines, for now
					// currentItem = currentItem.getNext();
				}
			}// else
			
			// if (null == currentItem) {
			// WYSection par = currentItem.getParent();
			// if (null != par);
			// }
			currentItem = currentItem.getNext();
		}// inner while
			// parent = parent.getParent();
		// }// outer while
		
		// }// sync
	}
	
	private static BufferedWriter	bw;
	
	
	public final static void saveConfig() {
		try {
			// FIXME: actually meld the class options/values into the WYIdentifiers here in the represented yml file
			// before you write virtualRoot
			
			FileOutputStream fos = null;
			OutputStreamWriter osw = null;
			bw = null;
			try {
				//for tests:new File( Config.fileConfig.getParent(), "config2.yml" ) );
				fos = new FileOutputStream( Config.fileConfig);
				osw = new OutputStreamWriter( fos, Q.UTF8 );
				bw = new BufferedWriter( osw );
				// parseWrite( 0, config.getValues( false ) );
				appendSection( 0, virtualRoot );
			} catch ( IOException e ) {
				Q.rethrow( e );
			} finally {
				if ( null != bw ) {
					try {
						bw.close();
					} catch ( IOException e ) {
						e.printStackTrace();
					}
				}
				if ( null != osw ) {
					try {
						osw.close();
					} catch ( IOException e ) {
						e.printStackTrace();
					}
				}
				if ( null != fos ) {
					try {
						fos.close();
					} catch ( IOException e ) {
						e.printStackTrace();
					}
				}
			}
			// for ( Map.Entry<String, Object> entry : config.getValues( true).entrySet() ) {
			// Object val = entry.getValue();
			// if ( !( val instanceof MemorySection ) ) {//ignore sections, parse only "var: value" tuples else it won't carry
			// over
			// String key = entry.getKey();
			// root.put(key,val);
			// }else {
			// MemorySection msVal=(MemorySection)val;
			// msVal.getValues( true );
			// }
			// }
			//
			// DumperOptions opt = new DumperOptions();
			// opt.setDefaultFlowStyle( DumperOptions.FlowStyle.BLOCK );
			// final Yaml yaml = new Yaml( opt );
			// FileOutputStream x = null;
			// OutputStreamWriter y=null;
			// try {
			// x=new FileOutputStream( Config.fileConfig );
			// y = new OutputStreamWriter( x, "UTF-8" );
			// yaml.dump(root,y );
			// } finally {
			// if ( null != x ) {
			// x.close();
			// }
			// }
			
			// getConfig().save( Config.fileConfig );
		} catch ( RethrownException e ) {
			e.printStackTrace();
			throw FactionsPlusPlugin.bailOut( "could not save config file: " + Config.fileConfig.getAbsolutePath() );
		}
	}
	
	protected static WYSection	virtualRoot		= null;
	
	// one to many
	private static final HM1	mapFieldToID	= new HM1();
	
	
	public synchronized final static boolean reloadConfig() {
		
		if ( Config.fileConfig.exists() ) {
			if ( !Config.fileConfig.isFile() ) {
				throw FactionsPlusPlugin.bailOut( "While '" + Config.fileConfig.getAbsolutePath()
					+ "' exists, it is not a file!" );
			}
		}else {
			//FIXME: evil hack, creating empty file and allowing the following code(which is meant just for when file is not empty) to actually fill it up
			// x: what to do when config doesn't exit, probably just saveConfig() or fill up virtualRoot
			// will have to fill the root with the fields and their comments
			
//			throw FactionsPlus.bailOut( "inexistent config" );
			try {
				Config.fileConfig.createNewFile();
			} catch ( IOException e ) {
				FactionsPlus.bailOut(e, "Cannot create config file "+Config.fileConfig.getAbsolutePath() );
			}
			
//			virtualRoot=createWYRootFromFields();
		}
			// config file exists
			try {
				
				// now read the existing config
				virtualRoot = WannabeYaml.read( fileConfig );
				
				
				
				// now check to see if we have any old config options or invalid ones in the config
				// remove invalids (move them to config_invalids.yml and carry over the old config values to the new ones, then
				// remove old
				// but only if new values are not already set
				synchronized ( mapFieldToID ) {
				synchronized ( Typeo.lock1 ) {
					mapFieldToID.clear();
					
					parseOneTime_and_CheckForValids( virtualRoot, null );
					
					// FIXME: still have to make sure the fields have their comments above them!!!!!!!!!!!!!!!!!!
					parseSecondTime_and_sortOverrides( virtualRoot );// from mapField_to_ListOfWYIdentifier
					// now we need to use mapField_to_ListOfWYIdentifier to see which values (first in list) will have effect
					// and notify admin on console only if the below values which were overridden have had a different value
					// coalesceOverrides( virtualRoot );
					
					// addMissingFieldsToConfig( virtualRoot );
					// cleanram: when done:
					mapFieldToID.clear();// free up memory for gc
					
				}// sync
				}
				
			} catch ( IOException e ) {
//				e.printStackTrace();
				throw FactionsPlusPlugin.bailOut(e, "failed to load existing config file '" + Config.fileConfig.getAbsolutePath()
					+ "'" );
			}
			
		
		
		applyChanges();
		
		saveConfig();
		
		//last:
		virtualRoot=null;//to allow gc to reclaim this memory, whenever
		return true;
	}
	
	
	private static WYSection createWYRootFromFields() {
		Q.ni();
		return virtualRoot;
		//
	}


	private static void applyChanges() {
		virtualRoot.recalculateLineNumbers();
		parseAndApplyChanges( virtualRoot );
		// that will set lines as comments due to duplicates/invalid/overridden
		// and most importantly will apply the values from the config into the Fields
	}
	
	
	private static void parseAndApplyChanges( WYSection root ) {// , String dottedParentSection) {
		assert Q.nn( root );
		WYItem<COMetadata> currentItem = root.getFirst();
		// boolean isTopLevelSection = ( null == dottedParentSection ) || dottedParentSection.isEmpty();
		
		while ( null != currentItem ) {
			WYItem nextItem = currentItem.getNext();//this is because currentItem will change and have it's next not point to old next:)
			
			Class<? extends WYItem> cls = currentItem.getClass();
			
			
			if ( WYSection.class == cls ) {
				WYSection cs = (WYSection)currentItem;
				// String dotted = ( isTopLevelSection ? cs.getId() : dottedParentSection + Config.DOT + cs.getId() );
				assert null == cs.getMetadata() : "this should not have metadata, unless we missed something";
				parseAndApplyChanges( cs );// , dotted );// recurse
			} else {
				if ( WYIdentifier.class == cls ) {
					WYIdentifier<COMetadata> wid = ( (WYIdentifier)currentItem );
					// String dotted = ( isTopLevelSection ? wid.getId() : dottedParentSection + Config.DOT + wid.getId() );
					COMetadata meta = wid.getMetadata();
					if ( null != meta ) {
						// ok this one has meta, ie. it's one of duplicate/invalid/overridden
						meta.apply();
						// if you need the applied/new item, the nextItem.getPrev() would be it
					}
				} else {// non id
					assert ( currentItem instanceof WYRawButLeveledLine );
					// ignore raw lines like comments or empty lines, for now
				}
			}// else
			
			currentItem = nextItem;
		}// while
	}
	
	
	private static void parseSecondTime_and_sortOverrides( WYSection vroot ) {
		synchronized ( mapFieldToID ) {
			synchronized ( Typeo.lock1 ) {
				// parse all found config options in .yml , only those found! and sort the list for their overrides
				// which means, we'll now know what option overrides which one if more than 1 was found in .yml for a specific
				// config field
				// realAlias if found in .yml always overrides any old aliases found, else if no realAlias found, then
				// the top oldAliases override the bottom ones when looking at the @Option annotation
				Field field = null;
				// SetOfIDs pointerToLastFoundSet=null;//the last field which had a WYIdentifier connection to .yml
				// Field pointerToLastFoundField=null;
				// String pointerToLastDottedRealAliasOfFoundField=null;
//				WYIdentifier<COMetadata> lastGoodOverriderWID = null;
				for ( Iterator iterator = Typeo.orderedListOfFields.iterator(); iterator.hasNext(); ) {
					field = (Field)iterator.next();
					// Option anno = field.getAnnotation( Option.class );
					String[] orderOfAliases = Typeo.getListOfOldAliases( field );
					String dottedRealAlias = Typeo.getDottedRealAliasOfField( field );// anno.realAlias_inNonDottedFormat();
					assert null != orderOfAliases;// due to the way annotation works

					SetOfIDs aSet = mapFieldToID.get( field );
					if ( null == aSet ) {
						// this config option was not yet defined in .yml so it means we have to add it
						// previousField
						// TODO: make sure this field added has the comment above them, and if it's already there don't prepend
						// it again
						WYIdentifier x = putFieldValueInTheRightWYPlace( vroot, Typeo.getFieldValue( field ), dottedRealAlias );
						assert null != x;
						
						FactionsPlus.info( "Adding new config option\n`" +COMetadata.COLOR_FOR_NEW_OPTIONS_ADDED+ dottedRealAlias + ChatColor.RESET+"`" );
						continue;
					}
					// else, the option was defined, so
					// multiple names for it may have been found in the config, and we need to decide which one overrides which
					// other one
					// where noting that the realAlias always overrides the oldAliases if both are found
					
					
					
					// find who is the overrider:
					
					// assume from start it's realAlias, the overrider
					String dottedOverrider = null;
					
					WYIdentifier<COMetadata> overriderWID = aSet.get( dottedRealAlias );
					if ( null == overriderWID ) {
						//so there were some options but none was the real Alias!
//						FactionsPlus.info( dottedRealAlias);
//						for ( Entry<String, WYIdentifier<COMetadata>> string : aSet.entrySet() ) {
//							System.out.println("!"+string.getKey()+" "+string.getValue().getID_InAbsoluteDottedForm(virtualRoot));
//						}
						// the realAlias is not the overriding one, so we parse oldAliases to find the topmost found one to be
						// as
						// our supreme overrider which means all others found are marked as overriden
						for ( int i = 0; i < orderOfAliases.length; i++ ) {
							overriderWID = aSet.get( orderOfAliases[i] );
							if ( null != overriderWID ) {
								dottedOverrider = orderOfAliases[i];
								break;// for, because the first one we find, overrides all the below ones
							}
						}
						assert ( null != overriderWID ) : "if the real alias wasn't in list, then at least "
							+ "one oldalias that is not .equals to realAlias would've been found and set";
					} else {
						// the real alias overrides any of the old aliases
						dottedOverrider = dottedRealAlias;
					}
					// so we have our overrider
					// now we have to parse the aSet and mark all that are not overriderWID as overridden by overriderWID
					
					assert ( null != overriderWID );
					assert ( null != dottedOverrider );
					// there is always 1 overrider
					
					Set<Entry<String, WYIdentifier<COMetadata>>> iter = aSet.entrySet();
					for ( Entry<String, WYIdentifier<COMetadata>> entry : iter ) {
						WYIdentifier<COMetadata> wid = entry.getValue();
						if ( overriderWID != wid ) {
							// mark as overridden
							
							String widDotted = entry.getKey();
							COMetadata previousMD =
								wid.setMetadata( new CO_Overridden( wid, widDotted, overriderWID, dottedOverrider ) );
								// it has to have been associated with the field, if it has a prev metadata
								assert ( CO_FieldPointer.class.isAssignableFrom( previousMD.getClass() ) ) : "this should be the only way"
									+ "that the wid we got here had a previously associated metadata with it, aka it has to have the"
									+ "pointer to the Field";
								CO_FieldPointer fp = (CO_FieldPointer)previousMD;
								Field pfield = fp.getField();
								assert null != pfield;
								assert pfield.equals( field ) : "should've been the same field, else code logic failed";
								// boolean contained = iter.remove( entry );this won't work, ConcurrentModificationException
								// assert contained;
//							}
							
						}
					}
					assert !aSet.isEmpty();
					aSet.clear();// cleaning some ram
					
					assert null != overriderWID;
					
					if (!dottedOverrider.equals(dottedRealAlias)) {
						//IF the overrider is not the realAlias then we transform it to the real alias
						@SuppressWarnings( "null" )
						String valueToCarry = overriderWID.getValue();
						
						WYIdentifier<COMetadata> old = overriderWID;
						//this is gonna be tricky to replace and removing it's parentSections if they're empty
						
						overriderWID=putFieldValueInTheRightWYPlace( vroot, valueToCarry, dottedRealAlias );
						COMetadata previousMD = old.setMetadata( new CO_Upgraded( old, dottedOverrider, field, overriderWID, dottedRealAlias ) );
						dottedOverrider=dottedRealAlias;
						
							// it has to have been associated with the field, if it has a prev metadata
							assert ( CO_FieldPointer.class.isAssignableFrom( previousMD.getClass() ) ) : "this should be the only way"
								+ "that the wid we got here had a previously associated metadata with it, aka it has to have the"
								+ "pointer to the Field";
							CO_FieldPointer fp = (CO_FieldPointer)previousMD;
							Field pfield = fp.getField();
							assert null != pfield;
							assert pfield.equals( field ) : "should've been the same field, else code logic failed";
							// boolean contained = iter.remove( entry );this won't work, ConcurrentModificationException
							// assert contained;
					}
					assert null != overriderWID;
					assert Typeo.isValidAliasFormat( dottedOverrider );
					aSet.put( dottedOverrider, overriderWID );
					assert aSet.size() == 1;
					//DONT: comment out any empty Sections before write, in #applyChanges(), because the commented 
					//dup/invalid/overriden ones will be orphaned and reader would not know who the parent was
				}
			}// sync2
		}// sync1
	}
	
	
	
	
	private static WYIdentifier putFieldValueInTheRightWYPlace( WYSection vroot, String value, String dottedRealAlias ) {
		assert Q.nn( vroot );
		assert Q.nn( value );
		assert Typeo.isValidAliasFormat( dottedRealAlias );
//		FactionsPlus.info( "putFieldValueInTheRightWYPlace " + dottedRealAlias );
		WYSection foundParentSection = parseCreateAndReturnParentSectionFor( vroot,  dottedRealAlias );
		assert null != foundParentSection : "impossible, it should've created and returned a parent even if it didn't exist";
		int index = dottedRealAlias.lastIndexOf( Config.DOT );
		if ( index >= 0 ) {// well not really 0
			dottedRealAlias = dottedRealAlias.substring( 1 + index );
		}
		assert Typeo.isValidAliasFormat( dottedRealAlias ) : dottedRealAlias;
		WYIdentifier leaf = new WYIdentifier<COMetadata>( 0, dottedRealAlias, value );
		// leaf.setMetadata( metadata )
		// XXX: new(unencountered) config options (in config.yml) are added as last in the subsection where the `id: value` is
		// for now, ideally TODO: add new config options in the right place in the Fields order
		foundParentSection.append( leaf );
		// System.out.println( foundParentSection.getInAbsoluteDottedForm() );
		return leaf;
	}
	
	
	/**
	 * attempts to create all parents for the passed ID
	 * 
	 * @param root
	 * @param field
	 * @param dottedID
	 *            ie. extras.lwc.disableSomething
	 * @return
	 */
	private static WYSection parseCreateAndReturnParentSectionFor( WYSection root, String dottedID ) {
		assert Q.nn( root );
		
		
		WYItem<COMetadata> currentItem = root.getFirst();
		int index = dottedID.indexOf( Config.DOT );
		if ( index < 0 ) {
			// we're just at the id
			return root;
		}
		// else not yet reached
		String findCurrent = dottedID.substring( 0, index );
		
		while ( null != currentItem ) {
			
			Class<? extends WYItem> cls = currentItem.getClass();
			
			
			if ( WYSection.class == cls ) {
				WYSection cs = (WYSection)currentItem;
				if ( findCurrent.equals( cs.getId() ) ) {
					return parseCreateAndReturnParentSectionFor( cs, dottedID.substring( 1 + index ) );// recurse
				}
			} else {
				if ( WYIdentifier.class == cls ) {
					WYIdentifier<COMetadata> wid = ( (WYIdentifier)currentItem );
					if ( findCurrent.equals( wid.getId() ) ) {
						throw new RuntimeException(
							"bad parameters for this method, because you searched for parent aka section, and we found it as an id" );
						// return root;
					}
				} else {// non id
					assert ( currentItem instanceof WYRawButLeveledLine );
					// ignore raw lines like comments or empty lines, for now
				}
			}// else
			
			currentItem = currentItem.getNext();
		}// inner while
		
		// if not found
		// make parent section, without completing the line number (which will be recalculated later anyway)
		WYSection<COMetadata> parent = new WYSection<COMetadata>( 0, findCurrent );
		root.append( parent );
		return parseCreateAndReturnParentSectionFor( parent,  dottedID.substring( 1 + index ) );
	}
	
	
	public static boolean isInited() {
		return inited;
	}
	
	private static void setInited( boolean nowState ) {
		inited = nowState;
	}
	
	private static void setLoaded( boolean nowState ) {
		loaded = nowState;
	}
	
	public static boolean isLoaded() {
		return loaded;
	}
	
	public final synchronized static boolean reloadTemplates() {
		if (!Config.isInited()) {
			return false;
		}else {
			Config.templates = YamlConfiguration.loadConfiguration( Config.templatesFile );
			return null != Config.templates;
		}
	}

	public static void deInit() {
		if (isInited()) {
			if (isLoaded()) {
				setLoaded( false );
			}
			setInited( false );
		}
	}
	
}
