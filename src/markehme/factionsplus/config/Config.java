package markehme.factionsplus.config;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import markehme.factionsplus.FactionsPlusTemplates;
import markehme.factionsplus.config.sections.Section_Announce;
import markehme.factionsplus.config.sections.Section_Banning;
import markehme.factionsplus.config.sections.Section_Economy;
import markehme.factionsplus.config.sections.Section_Extras;
import markehme.factionsplus.config.sections.Section_Factions;
import markehme.factionsplus.config.sections.Section_Jails;
import markehme.factionsplus.config.sections.Section_Peaceful;
import markehme.factionsplus.config.sections.Section_PowerBoosts;
import markehme.factionsplus.config.sections.Section_Rules;
import markehme.factionsplus.config.sections.Section_Teleports;
import markehme.factionsplus.config.sections.Section_Templates;
import markehme.factionsplus.config.sections.Section_Warps;
import markehme.factionsplus.config.sections._boolean;
import markehme.factionsplus.config.yaml.WYComment;
import markehme.factionsplus.config.yaml.WYIdentifier;
import markehme.factionsplus.config.yaml.WYItem;
import markehme.factionsplus.config.yaml.WYRawButLeveledLine;
import markehme.factionsplus.config.yaml.WYSection;
import markehme.factionsplus.config.yaml.WY_IDBased;
import markehme.factionsplus.config.yaml.WannabeYaml;
import markehme.factionsplus.events.FPConfigLoadedEvent;
import markehme.factionsplus.references.FP;
import markehme.factionsplus.references.FPP;
import markehme.factionsplus.util.Q;
import markehme.factionsplus.util.RethrownException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event;



public abstract class Config {

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
	@Section(comments = {
		"this has no effect @Section for now", "so don't even try to fill in @Section comments"
	},
			realAlias_neverDotted = "jails" )
	public static final Section_Jails		_jails					= new Section_Jails();
	
	@Section(
			realAlias_neverDotted = "warps" )
	public static final Section_Warps		_warps					= new Section_Warps();
	
	@Section(
			realAlias_neverDotted = "factions" )
	public static final Section_Factions	_factions				= new Section_Factions();
	
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
			 realAlias_neverDotted = "Teleports" )
	public static final Section_Teleports	_teleports				= new Section_Teleports();
	
	@Section(
			realAlias_neverDotted = "extras" )
	public final static Section_Extras		_extras					= new Section_Extras();
	
	@Section(
			realAlias_neverDotted = "template" )
	public final static Section_Templates		_templates					= new Section_Templates();
	
	@Option(
		autoComment={
			"if true it will remove all auto comments which explain what each option does in the config file."
			,"The config file header is not removed."
			,"This option is here only for those that want to increase readability in the config file."
		},
		realAlias_inNonDottedFormat = "disableAutoCommentsInConfig" )
	public static final _boolean disableAutoCommentsInConfig=new _boolean(false);
	
	// the root class that contains the @Section and @Options to scan for
	private static final Class				configClass				= Config.class;
	// End Config
	
	
	
	private static File						currentFolder_OnPluginClassInit;
	private static File						currentFolder_OnEnable	= null;
	
	
	private static boolean					inited					= false;
	private static boolean					loaded= false;
	private static String[]	fpConfigHeaderArray;
	
	/**
	 * call this one time onEnable() never on onLoad() due to its evilness;)<br>
	 */
	public final static void init() {
		assert !isLoaded();
		setInited( false );
		boolean failed = false;
		// try {
		if ( Q.isInconsistencyFileBug() ) {
			throw FPP.bailOut( "Please do not have `user.dir` property set, it will mess up so many things"
				+ "(or did you use native functions to change current folder from the one that was on jvm startup?!)" );
		}
		
		if ( hasFileFieldsTrap() ) {
			throw FPP.bailOut( "there is a coding trap which will likely cause unexpected behaviour "
				+ "in places that use files, tell plugin author to fix" );
		}
		
		// first make sure the (hard)coded options are valid while at the same time build a list of all obsolete+new
		// option
		// names
		Typeo.sanitize_AndUpdateClassMapping( configClass );
		
		//this header will be included at the top of the config.yml
		fpConfigHeaderArray	= new String[]{
			FP.instance.getDescription().getFullName()+" configuration file"
			,""
			,""
			,"All comments starting with `### ` (3 # and a space)(aka auto comments) will be automatically updated, " 
			," please refrain from making any changes inside those lines because they will be lost upon save"
			,"Comments with just one `#` will remain, though they will be moved before/after the auto comments"
			,""
			,"You may not use `.` aka dot in config options names even though we are referring to them like that"
			,"  ie. jails.enabled becomes `jails:<hit enter and 2 spaces>enabled`"
			," Each level is indented by exactly 2 spaces"
			,""
			,"You may use `/f debug configdiff` to see the config options that you've changed from their default (OPs/console only)"
			,""
			,""
			,""
			,""
		};
		
		for ( int i = 0; i < fpConfigHeaderArray.length; i++ ) {
			if (fpConfigHeaderArray[i].contains("\n") || fpConfigHeaderArray[i].contains( "\r" ) ) {
				throw FP.bailOut( "Do not use newlines inside the header, " +
						"but instead add a new element line in the array. Problematic line #"+i+"\n`"+fpConfigHeaderArray[i]+"`" );
			}
		}
		
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
				FP.instance.disableSelf();// must make sure we're disabled if something failed if not /plugins would
													// show us green
				// but mostly, for consistency's sake and stuff we couldn't think of/anticipate now
			}
		}
	}
	
	

	
	protected static void ensureFoldersExist() {
		File dataF = FP.instance.getDataFolder();
		if ( !dataF.equals( folderBase ) ) {
			throw FPP
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
				FPP.info( "Created file: " + Config.fileDisableInWarzone );
			}
			
			if(Config.templatesFile.exists()) {
				FPP.info( "Templates file is no longer used, removing. See Config > Template" );
				
				Config.templatesFile.delete();
			}
			
		} catch ( Exception e ) {
			throw FPP.bailOut(e, "something failed when ensuring the folders exist" );
		}
	}
	
	
	private static final void addDir( File dir ) {
		if ( !dir.exists() ) {
			if ( dir.getPath().isEmpty() ) {
				throw FPP.bailOut( "bad coding, this should usually not trigger here, but earlier" );
			}
			FPP.info( "Added directory: " + dir );
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
					throw FPP.bailOut( "impossible, coding bug detected" );
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
						throw FP.bailOut( "impossible, coding bug detected" );
					}
				}
			}
			currentItem = currentItem.getNext();
		}
	}
	
	
	/**
	 * must be inside: synchronized ( mapFieldToID )<br>
	 * this parses the entire representation of the config.yml and marks duplicates and invalid configs<br>
	 * 
	 * @param root
	 */
	private final static void parseOneTime_and_CheckForValids( WYSection root, String dottedParentSection ) {
		assert Q.nn( root );
		WYItem<COMetadata> currentItem = root.getFirst();
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
							wid.setMetadata( new CO_FieldPointer( foundAsField, wid, true ) );
						}
						
					}// end of else found
					
				} else {// non id
					assert ( currentItem instanceof WYRawButLeveledLine );
					// ignore raw lines like comments or empty lines, for now
				}
			}// else
			
			currentItem = currentItem.getNext();
		}// inner while
	}
	
	private static BufferedWriter	bw;
	
	
	public final static void saveConfig() {
		try {
			// done earlier: actually meld the class options/values into the WYIdentifiers here in the represented yml file
			// before you write virtualRoot
			
			FileOutputStream fos = null;
			OutputStreamWriter osw = null;
			bw = null;
			try {
				//for tests:
//				fos=new FileOutputStream( new File( Config.fileConfig.getParent(), "config2-test-only.yml" ) );
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
			throw FPP.bailOut( "could not save config file: " + Config.fileConfig.getAbsolutePath() );
		}
	}
	
	protected static WYSection	virtualRoot		= null;
	
	// one to many
	private static final HM1	mapFieldToID	= new HM1();//store all except newly added fields
	private static final String	AUTOCOMMENTS_PREFIX	= "### ";
	private static final String	BEGINNING_OF_NEXT_CFG_OPTION	= "###########################################################";
//	private static boolean firstTime=true;
	
	public synchronized final static boolean reloadConfig() {
		
		if ( Config.fileConfig.exists() ) {
			if ( !Config.fileConfig.isFile() ) {
				throw FPP.bailOut( "While '" + Config.fileConfig.getAbsolutePath()
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
				FP.bailOut(e, "Cannot create config file "+Config.fileConfig.getAbsolutePath() );
			}
			
//			virtualRoot=createWYRootFromFields();
		}
		
		
		// config file exists
		try {
			
//			if (firstTime) {
//				firstTime=false;
//				//first time they are already set to defaults, no need to set them again
//			}else {
//				//not first time? ie. /f reloadfp  instead of bukkit 'reload'
//				//we restore defaults just in case ie. a config option was deleted or the entire file was deleted
//				//in which case we'll need to restore the default value for that(those) option(s) rather than the previous value 
//				//we already had set
////				Typeo.resetConfigToDefaults();//done: this may not be needed anymore now, due to any new options getting default values
//			}
			
			// now read the existing config
			virtualRoot = WannabeYaml.read( fileConfig );
			cleanAutoComments( virtualRoot, null );// without updating the line numbers
			
			
			// now check to see if we have any old config options or invalid ones in the config
			// remove invalids (move them to config_invalids.yml and carry over the old config values to the new ones, then
			// remove old
			// but only if new values are not already set
			synchronized ( mapFieldToID ) {
				synchronized ( Typeo.lock1 ) {
					mapFieldToID.clear();
					
					parseOneTime_and_CheckForValids( virtualRoot, null );
					
					// done: still have to make sure the fields have their comments above them!!!!!!!!!!!!!!!!!!
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
			// e.printStackTrace();
			throw FPP.bailOut( e, "failed to load existing config file '" + Config.fileConfig.getAbsolutePath()
				+ "'" );
		}
			
		prependHeader(virtualRoot);
		
		
		//TODO:
		//1.setvalues for all fields
		//all config options should be already in the virtualRoot (even those upgraded or new non-existing ones)
		setFieldValuesToThoseFromConfig();
		
		//2.put or not autocomments
		if (!Config.disableAutoCommentsInConfig._){
			addAutoCommentsInConfig();
		}
		
		//3.apply changes like commenting out dups/invalids/overridden ones (which implies line recalc happens at this point)
		applyChangesInsideConfig();//comment out dups etc. & set fields value to the specified values inside config;
		
		saveConfig();
		
		
		//bechmarked(0.4.7 code or so): 274339 lines in an 11.6MB config.yml file at under 5 seconds with 300 info/warns shown on console
		//that yielded a 15.6 MB file which on f reloadfp was parsed w/o info/warns in under 570ms
		// 2.3 seconds if all info/warn are suppressed
		
		//last:
		if (encountered>SKIP_AFTER_ENCOUNTERED) {
			FP.warn( "Skipped "+ChatColor.RED+(encountered-SKIP_AFTER_ENCOUNTERED)+ChatColor.RESET
				+" more messages due to being over the limit of "+SKIP_AFTER_ENCOUNTERED );
		}
		encountered=0;
		virtualRoot=null;//to allow gc to reclaim this memory, whenever
		return true;
	}
	
	
	private static void addAutoCommentsInConfig() {
		synchronized ( Typeo.lock1 ) {
			howManyWeSet=0;
			
			parseAndAddAutoComments(virtualRoot,null);
			
			assert Typeo.orderedListOfFields.size() == howManyWeSet:"not all/or more fields were set: "+
					howManyWeSet+" / "+Typeo.orderedListOfFields.size();
			
		}
	}

	private static void parseAndAddAutoComments( WYSection root, String dottedParentSection) {
		assert Q.nn( root );
		WYItem<COMetadata> currentItem = root.getFirst();
		boolean isTopLevelSection = ( null == dottedParentSection ) || dottedParentSection.isEmpty();
		
		while ( null != currentItem ) {
			WYItem nextItem = currentItem.getNext();//this is because currentItem will change and have it's next not point to old next:)
			
			Class<? extends WYItem> cls = currentItem.getClass();
			
			
			if ( WYSection.class == cls ) {
				WYSection cs = (WYSection)currentItem;
				String dotted = ( isTopLevelSection ? cs.getId() : dottedParentSection + Config.DOT + cs.getId() );
//				assert null == cs.getMetadata() : "this should not have metadata, unless we missed something";
				
				parseAndAddAutoComments( cs , dotted );// recurse
			} else {
				if ( WYIdentifier.class == cls ) {
					WYIdentifier<COMetadata> wid = ( (WYIdentifier)currentItem );
					COMetadata meta = wid.getMetadata();
					if ( ( null != meta ) && ( meta.getClass().equals(CO_FieldPointer.class) ) ) {
						howManyWeSet++;
						//then it's a valid field ie. not dup/invalid/overridden/upgraded but rather real
						String dotted = ( isTopLevelSection ? wid.getId() : dottedParentSection + Config.DOT + wid.getId() );
						Field field = Typeo.getField_correspondingTo_DottedFormat( dotted );
						assert null != field:"something went wrong somewhere else field not found for: "+dotted;
						String[] comments = Typeo.getComments( field );
						prependComments( root, wid, dotted, comments, field );
					}
				} else {// non id
					assert ( currentItem instanceof WYRawButLeveledLine );
					// ignore raw lines like comments or empty lines, for now
				}
			}// else
			
			currentItem = nextItem;
		}// while
	}


	private static int howManyWeSet=0;//temporary, for tests
	private static void setFieldValuesToThoseFromConfig() {
		synchronized ( Typeo.lock1 ) {
			howManyWeSet=0;
		
			parseAndSetFields(virtualRoot);
		
			assert Typeo.orderedListOfFields.size() == howManyWeSet:"not all/or more fields were set: "+
					howManyWeSet+" / "+Typeo.orderedListOfFields.size();
		}
	}
	
	private static void parseAndSetFields( WYSection root ) {// , String dottedParentSection) {
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
				parseAndSetFields( cs );// , dotted );// recurse
			} else {
				if ( WYIdentifier.class == cls ) {
					WYIdentifier<COMetadata> wid = ( (WYIdentifier)currentItem );
					// String dotted = ( isTopLevelSection ? wid.getId() : dottedParentSection + Config.DOT + wid.getId() );
					COMetadata meta = wid.getMetadata();
					if ( null != meta ) {
						// ok this one has meta, ie. it's one of duplicate/invalid/overridden
//						if (meta.getClass().equals(CO_Upgraded.class)) {
//							FactionsPlus.warn(((CO_FieldPointer)meta).wid+" vs "+wid+" and "+((CO_FieldPointer)meta).field);
//						}
						if ( meta.getClass().equals( CO_FieldPointer.class)  ) {
							//this doesn't include the CO_Upgraded which is a subclass and the old config value, 
							//because the field it was upgraded to was already added and it's a CO_FieldPointer
							CO_FieldPointer fpmeta = (CO_FieldPointer)meta;
							try {
								howManyWeSet++;
//								assert wid == fpmeta.wid;
								assert fpmeta.wid.getValue().equals(wid.getValue());
//								if (fpmeta.getClass().equals(CO_Upgraded.class)) {
//									FactionsPlus.warn(fpmeta.wid+" vs "+wid+" and "+fpmeta.field);
//								}
								Typeo.setFieldValue( fpmeta.field, wid.getValue() );
							} catch ( Throwable t ) {
								//TODO: maybe collect these and display after instead of quitting on the first bad one, just in case 
								// there are plenty it would require running or /f reloadfp multiple times after each fix
								if ( t.getClass().equals( NumberFormatException.class )
									|| t.getClass().equals( BooleanFormatException.class ) )
								{
									Q.rethrow( new InvalidConfigValueTypeException( wid, fpmeta.field, t ) );
								} else {
									Q.rethrow( new FailedToSetConfigValueException( wid, fpmeta.field, t ) );
								}
							}
						}
					}
				} else {// non id
					assert ( currentItem instanceof WYRawButLeveledLine );
					// ignore raw lines like comments or empty lines, for now
				}
			}// else
			
			currentItem = nextItem;
		}// while
	}
	

	private static void prependHeader( WYSection root ) {
		for ( int j = fpConfigHeaderArray.length-1; j >= 0; j-- ) {
			String line = fpConfigHeaderArray[j];
			//line numbers are irrelevant because they will be recalculated later
			root.prepend( getAsAutoComment(line) );
		}
	}
	
	private static final WYComment<COMetadata> getAsAutoComment(String line) {
		return new WYComment<COMetadata>( 0, AUTOCOMMENTS_PREFIX+line );
	}
	
	private static final boolean isAutoComment(String line) {
		if ( (null == line) || (line.isEmpty()) ) {
			return false;
		}
		return line.startsWith( AUTOCOMMENTS_PREFIX );
	}

	private final static void cleanAutoComments( WYSection root, String dottedParentSection ) {
		assert Q.nn( root );
		WYItem<COMetadata> currentItem = root.getFirst();
		boolean isTopLevelSection = ( null == dottedParentSection ) || dottedParentSection.isEmpty();
		
		WYItem nextItem;
		while ( null != currentItem ) {
			nextItem=currentItem.getNext();
			Class<? extends WYItem> cls = currentItem.getClass();
			
			
			if ( WYSection.class == cls ) {
				WYSection cs = (WYSection)currentItem;
				String dotted = ( isTopLevelSection ? cs.getId() : dottedParentSection + Config.DOT + cs.getId() );
				
				cleanAutoComments( cs, dotted );// recurse
				
				//insert empty line between sections (well at the end of each)
//				root.append( new WYEmptyLine<COMetadata>( 0 ) );ok bad idea
			} else {
				if (currentItem instanceof WYRawButLeveledLine) {
					if (cls == WYComment.class) {
						WYComment comment=(WYComment)currentItem;
						if (isAutoComment( comment.getRawButLeveledLine() ) ){
//							System.out.println(comment.getRawButLeveledLine());
							root.remove( comment );
						}
					}
				}//else whatever
			}// else
			
			currentItem =nextItem;
		}// inner while
	}

	private static WYSection createWYRootFromFields() {
		Q.ni();
		return virtualRoot;
		//
	}


	private static void applyChangesInsideConfig() {
		virtualRoot.recalculateLineNumbers();
		parseAndApplyChanges( virtualRoot );
		// that will set lines as comments due to duplicates/invalid/overridden
		// but will not apply the values from the config into the Fields, this is done earlier before this call
	}
	
	
	@SuppressWarnings( "boxing" )
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
						while (true) {
							Class<? extends COMetadata> metaClass = meta.getClass();
							if ( metaClass.equals( CO_Duplicate.class ) ) {
								CO_Duplicate metaDup = (CO_Duplicate)meta;
								metaDup.appliesToWID.getParent().replaceAndTransformInto_WYComment( metaDup.appliesToWID,
									metaDup.commentPrefixForDUPs );
								
								Config
									.warn( "Duplicate config option encountered at line "
										+ metaDup.colorLineNumOnDuplicate
										+ metaDup.appliesToWID.getLineNumber()
										+ ChatColor.RESET
										+ " and this was transformed into comment so that you can review it & know that it was ignored.\n"
										// + "This is how the line looks now(without leading spaces):\n"
										+ metaDup.colorOnDuplicate + metaDup.appliesToWID.toString() + "\n" + ChatColor.RESET
										+ "the option at line " + ChatColor.AQUA + metaDup.theActiveFirstWID.getLineNumber()
										+ ChatColor.RESET + " overriddes this duplicate with value " + ChatColor.AQUA
										+ metaDup.theActiveFirstWID.getValue() );
								
								break;
							}
							
							if ( metaClass.equals( CO_Invalid.class ) ) {
								CO_Invalid metaInvalid = (CO_Invalid)meta;
								metaInvalid.appliesToWID.getParent().replaceAndTransformInto_WYComment(
									metaInvalid.appliesToWID, metaInvalid.commentPrefixForINVALIDs );
								Config.warn( "Invalid config option\n" + metaInvalid.colorOnINVALID
									+ metaInvalid.thePassedDottedFormatForThisWID + ChatColor.RESET
									+ " was auto commented at line "
									// // + fileConfig
									// + " at line "
									+ metaInvalid.colorOnINVALID + metaInvalid.appliesToWID.getLineNumber() + '\n'// +ChatColor.RESET
									// +
									// " and this was transformed into comment so that you can review it & know that it was ignored.\n"
									// + "This is how the line looks now(without leading spaces):\n"
									+ metaInvalid.colorOnINVALID + metaInvalid.appliesToWID.toString() );
								
								break;
							}
							
							if ( metaClass.equals( CO_Overridden.class ) ) {
								CO_Overridden metaOverridden = (CO_Overridden)meta;
								metaOverridden.lostOne.getParent()
									.replaceAndTransformInto_WYComment(
										metaOverridden.lostOne,
										String.format( metaOverridden.commentPrefixForOVERRIDDENones,
											metaOverridden.dottedOverriddenByThis,
											metaOverridden.overriddenByThis.getLineNumber() ) );
								
								Config.warn( "Config option " + ChatColor.AQUA + metaOverridden.dottedOverriddenByThis
									+ ChatColor.RESET + " at line " + ChatColor.AQUA
									+ metaOverridden.overriddenByThis.getLineNumber() + ChatColor.RESET
									+ " overrides the old alias for it `" + ChatColor.DARK_AQUA + metaOverridden.dottedLostOne
									+ ChatColor.RESET + "` which is at line " + ChatColor.DARK_AQUA
									+ metaOverridden.lostOne.getLineNumber() + ChatColor.RESET
									+ " which was also transformed into comment to show it's ignored." );
								
								break;
							}
							
							if ( metaClass.equals( CO_Upgraded.class ) ) {
								CO_Upgraded metaUpgraded = (CO_Upgraded)meta;
								metaUpgraded.upgradedWID.getParent().replaceAndTransformInto_WYComment(
									metaUpgraded.upgradedWID,
									String.format( metaUpgraded.commentPrefixForUPGRADEDones, metaUpgraded.theNewUpgradeDotted,
										metaUpgraded.wid.getLineNumber() ) );
								
								Config.info( "Upgraded `" + ChatColor.DARK_AQUA + metaUpgraded.upgradedDotted + ChatColor.RESET
									+ "` of line `" + ChatColor.DARK_AQUA + metaUpgraded.upgradedWID.getLineNumber()
									+ ChatColor.RESET + "` to the new config name of `"
									+ metaUpgraded.COLOR_FOR_NEW_OPTIONS_ADDED + metaUpgraded.theNewUpgradeDotted
									+ ChatColor.RESET + "` of line `" + metaUpgraded.COLOR_FOR_NEW_OPTIONS_ADDED
									+ metaUpgraded.wid.getLineNumber() + "`" );
								
								break;
							}
							
							//last:
							break;//just in case
						}//hacky while
//						meta.apply();
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
	
	
	@SuppressWarnings("null")
	private static void parseSecondTime_and_sortOverrides( WYSection vroot ) {
		synchronized ( mapFieldToID ) {
			synchronized ( Typeo.lock1 ) {
				// parse all found config options in .yml , only those found! and sort the list for their overrides
				// which means, we'll now know what option overrides which one if more than 1 was found in .yml for a specific
				// config field
				// realAlias if found in .yml always overrides any old aliases found, else if no realAlias found, then
				// the top oldAliases override the bottom ones when looking at the @Option annotation
				Field field = null;
//				mapFieldToID.get( Typeo.orderedListOfFields. )
				for ( Iterator iterator = Typeo.orderedListOfFields.iterator(); iterator.hasNext(); ) {
					field = (Field)iterator.next();
					// Option anno = field.getAnnotation( Option.class );
//					String[] comments=Typeo.getComments(field);
					String[] orderOfAliases = Typeo.getListOfOldAliases( field );
					String dottedRealAlias = Typeo.getDottedRealAliasOfField( field );// anno.realAlias_inNonDottedFormat();
					assert null != orderOfAliases;// due to the way annotation works

					SetOfIDs aSet = mapFieldToID.get( field );
					if ( null == aSet ) {
						// this config option was not yet defined in .yml so it means we have to add it
						// previousField
						// done: make sure this field added has the comment above them, and if it's already there don't prepend
						// it again
						//true: this will use default value ie. on /f reloadfp  due to all fields getting their values reset if not first time config load
						//tho maybe we should use getFieldDefaultValue just in case
						WYIdentifier<COMetadata> x = putFieldValueInTheRightWYPlace( vroot, Typeo.getFieldDefaultValue( field ), dottedRealAlias);
						assert null != x;
						COMetadata previousMD = x.setMetadata( new CO_FieldPointer( field, x, false) );//we still have to set the field to its default value
						assert null == previousMD:previousMD;
//						for ( int i = 0; i < comments.length; i++ ) {
//							//System.out.println(field.getName()+" "+comments[i]);
//							x.getParent().insertBefore( getAsAutoComment( comments[i] ), x );
//						}
						//these should always be shown, mainly because they cannot be THAT many to be requiring skipping
						FP.info( "Adding new config option\n`" +COMetadata.COLOR_FOR_NEW_OPTIONS_ADDED+ dottedRealAlias + ChatColor.RESET+"`" );
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
						
						//and we can add comments now; case 2 of 3: when realAlias already existed in config
//						if (!Config.disableAutoCommentsInConfig._){this won't work here
							//prependComments(overriderWID.getParent(), overriderWID, dottedOverrider, comments, field);
//						}
//						for ( int i = 0; i < comments.length; i++ ) {
////							System.out.println(field.getName()+" 2 "+comments[i]);
//							overriderWID.getParent().insertBefore( getAsAutoComment( comments[i] ), overriderWID );
//						}
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
							Field pfield = fp.field;
							assert null != pfield;
							assert pfield.equals( field ) : "should've been the same field, else code logic failed";
							// boolean contained = iter.remove( entry );this won't work, ConcurrentModificationException
							// assert contained;
							// }
							
						}
					}
					assert !aSet.isEmpty();
					aSet.clear();// cleaning some ram
					
					assert null != overriderWID;
					
					if (!dottedOverrider.equals(dottedRealAlias)) {
						//IF the overrider is not the realAlias then we transform it to the real alias
						
						String valueToCarry = overriderWID.getValue();
						
						WYIdentifier<COMetadata> old = overriderWID;
						//this is gonna be tricky to replace and removing it's parentSections if they're empty
						
						overriderWID=putFieldValueInTheRightWYPlace( vroot, valueToCarry, dottedRealAlias );
						assert null != overriderWID;
						COMetadata pre = overriderWID.setMetadata( new CO_FieldPointer( field, overriderWID, false) );//we still have to set the field to its default value
						assert null == pre:pre;
						
						COMetadata previousMD = old.setMetadata( new CO_Upgraded( old, dottedOverrider, field, overriderWID, dottedRealAlias ) );
						dottedOverrider=dottedRealAlias;
						
						// it has to have been associated with the field, if it has a prev metadata
						assert ( CO_FieldPointer.class.isAssignableFrom( previousMD.getClass() ) ) : "this should be the only way"
							+ "that the wid we got here had a previously associated metadata with it, aka it has to have the"
							+ "pointer to the Field";
						CO_FieldPointer fp = (CO_FieldPointer)previousMD;
						Field pfield = fp.field;
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
					//dup/invalid/overridden ones will be orphaned and reader would not know who the parent was
				}
			}// sync2
		}// sync1
	}
	
	
	
	
	/**
	 * called when either 1. cfgoption didn't exist in config file OR 2. when it was upgraded from oldalias to the new one,<br>
	 * the new one didn't exist and thus cfgoption was newly created (in both cases)
	 * @param vroot
	 * @param value
	 * @param dottedRealAlias
	 * @param comments
	 * @param field
	 * @return
	 */
	private static WYIdentifier putFieldValueInTheRightWYPlace( WYSection vroot, String value, String dottedRealAlias) {
		assert Q.nn( vroot );
		assert Q.nn( value );
		assert Typeo.isValidAliasFormat( dottedRealAlias );
//		FactionsPlus.info( "putFieldValueInTheRightWYPlace " + dottedRealAlias );
		WYSection foundParentSection = parseCreateAndReturnParentSectionFor( vroot,  dottedRealAlias );
		assert null != foundParentSection : "impossible, it should've created and returned a parent even if it didn't exist";
		int index = dottedRealAlias.lastIndexOf( Config.DOT );
		String lastPartOfRealAlias;
		if ( index >= 0 ) {// well not really 0
			lastPartOfRealAlias = dottedRealAlias.substring( 1 + index );
		}else{
			lastPartOfRealAlias=dottedRealAlias;
		}
		assert Typeo.isValidAliasFormat( lastPartOfRealAlias ) : lastPartOfRealAlias;
		WYIdentifier leaf = new WYIdentifier<COMetadata>( 0, lastPartOfRealAlias, value );
		// leaf.setMetadata( metadata )
		// XXX: new(unencountered) config options (in config.yml) are added as last in the subsection where the `id: value` is
		// for now, ideally TODO: add new config options in the right place in the Fields order
		foundParentSection.append( leaf );
		// System.out.println( foundParentSection.getInAbsoluteDottedForm() );
		
		//and we can add comments now; case 1&3 of 3: when newly added cfgoption in .yml OR when old alias existed only => newly added must happen
//		if (!Config.disableAutoCommentsInConfig._){won't work here
			//prependComments(foundParentSection, leaf, dottedRealAlias, comments, field);
//		}
		return leaf;
	}
	
	
	private static final void prependComments(WYSection parentOfChild, WYIdentifier<COMetadata> beforeThisChild,
		String dottedAlias, String[] comments, Field field) {
		assert null != parentOfChild;
		assert null != beforeThisChild;
		assert beforeThisChild.getParent() == parentOfChild;
		assert null != comments;
		
//		if (!dottedAlias.contains( "." )) {
//			System.out.println(dottedAlias);
//			new Exception().printStackTrace();
//		}
//		if (comments.length>0) {
//			parentOfChild.insertBefore( new WYRawButLeveledLine<COMetadata>( 0, "" ), beforeThisChild );
			// empty line before, so we can see clearly; actually we can't do empty lines because they are kept, but we can do
			// commented empty line
		parentOfChild.insertBefore( getAsAutoComment( ""/*BEGINNING_OF_NEXT_CFG_OPTION*/ ), beforeThisChild );
		parentOfChild.insertBefore( getAsAutoComment( "# "+dottedAlias ), beforeThisChild );
		parentOfChild.insertBefore( getAsAutoComment( "default value: "+Typeo.getFieldDefaultValue( field )), beforeThisChild );
//		}
		
		for ( int i = 0; i < comments.length; i++ ) {//auto skipped if no comments defined
			parentOfChild.insertBefore( getAsAutoComment( comments[i] ), beforeThisChild );
		}
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
	
	private static final int SKIP_AFTER_ENCOUNTERED=300;
	private static int encountered=0;
	//these are to be used only when reporting config options while parsing config.yml
	protected static void info(String msg) {
		encountered++;
		if ( encountered <= SKIP_AFTER_ENCOUNTERED ) {
			FP.info( msg );
		}
	}
	
	
	protected static void warn(String msg) {
		encountered++;
		if ( encountered <= SKIP_AFTER_ENCOUNTERED ) {
			FP.warn(msg);
		}
	}
}
