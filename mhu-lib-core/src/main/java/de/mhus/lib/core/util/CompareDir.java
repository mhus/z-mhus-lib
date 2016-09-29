package de.mhus.lib.core.util;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import de.mhus.lib.core.MStopWatch;
import de.mhus.lib.core.lang.MObject;

/**
 * <p>CompareDir class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class CompareDir extends MObject {
		
	private boolean fullRefresh;
	private String[] pathes;
	private String[] pathesSlash;
	private int totalSize = 100;
	private int totalCnt;
	private int currentCnt = 0;
	private boolean killed;
	private int insertCnt = 0;
	private int updateCnt = 0;
	private int deleteCnt = 0;
	private boolean commitAfterEveryEvent;
	private boolean commitAfterFinish;
	private boolean needAllFolders;

	/**
	 * <p>compare.</p>
	 *
	 * @param current a {@link java.util.TreeMap} object.
	 * @param last a {@link java.util.TreeMap} object.
	 * @param listener a {@link de.mhus.lib.core.util.CompareDir.Listener} object.
	 */
	public void compare( TreeMap<String,CompareDirEntry> current, TreeMap<String,CompareDirEntry> last, Listener listener ) {
		
		MStopWatch tk = new MStopWatch();	
		tk.start();
		
		log().t( "START");
		listener.start( current, last );
		
		totalSize = current.size() + last.size();
		totalCnt  = 0;
		
		Iterator<Map.Entry<String,CompareDirEntry>> cur = current.entrySet().iterator();
		Iterator<Map.Entry<String,CompareDirEntry>> old = last.entrySet().iterator();
		
		Map.Entry<String,CompareDirEntry> curEntry = null;
		Map.Entry<String,CompareDirEntry> oldEntry = null;
		
		String      curKey = null;
		CompareDirEntry curVal = null;
		
		String        oldKey = null;
		CompareDirEntry oldVal = null;
		
		if ( cur.hasNext() ) {
			curEntry = cur.next();
			curKey   = curEntry.getKey();
			curVal   = curEntry.getValue();
			log().t( "CUR",curKey );
			totalCnt++;
			currentCnt++;
		}
		
		if ( old.hasNext() ) {
			oldEntry = old.next();
			oldKey   = (String)oldEntry.getKey();
			oldVal   = (CompareDirEntry)oldEntry.getValue();
			log().t( "OLD",oldKey );
			totalCnt++;
		}
		
		while ( true ) {
			
			if ( curEntry == null && oldEntry == null || killed )
				break;
			
			
			
			int comp = 0;
			
			if ( curEntry != null && oldEntry != null )
				comp = curKey.compareTo( oldKey );
			else
			if ( curEntry == null )
				comp = 1;
			else
				comp = -1;
				
			if ( comp == 0 ) {
				
				// found same check id and vstamp
				if ( fullRefresh || ! curVal.compareWithEntry(oldVal) ) {
					
					if ( isPath( curKey ) ) {
						// check by listener
						log().t( "UPDATE");
						updateCnt++;
						boolean ret = listener.updateObject( curKey, curVal, oldVal );
						if ( commitAfterEveryEvent ) {
							if ( ret )
								doCommit();
							else
								doRollback();
						}
					}					
				}
				
				
				if ( cur != null && cur.hasNext() ) {
					curEntry = cur.next();
					curKey   = curEntry.getKey();
					curVal   = curEntry.getValue();
					log().t( "CUR,",curKey );
					totalCnt++;
					currentCnt++;
				} else
					curEntry = null;
				
				if ( old != null && old.hasNext() ) {
					oldEntry = old.next();
					oldKey   = oldEntry.getKey();
					oldVal   = oldEntry.getValue();
					log().t( "OLD",oldKey );
					totalCnt++;
				} else
					oldEntry = null;
				
				continue;
				
			} else
			
			if ( comp > 0 ) {
				
				// old has new one (deleted in cur), check by listener
				if ( isPath( oldKey ) ) {
					log().t( "DELETE");
					deleteCnt++;
					boolean ret = listener.deleteObject( oldKey, oldVal );
					if ( commitAfterEveryEvent ) {
						if ( ret )
							doCommit();
						else
							doRollback();
					}
				}
				
				if ( old != null && old.hasNext() ) {
					oldEntry = old.next();
					oldKey   = oldEntry.getKey();
					oldVal   = oldEntry.getValue();
					log().t( "OLD",oldKey );
					totalCnt++;
				} else
					oldEntry = null;
				
				
			} else {
				// comp < 0
				
				// cur has new one, check by listener
				if ( isPath( curKey ) ) {
					log().t( "CREATE");
					insertCnt++;
					boolean ret = listener.createObject( curKey, curVal );
					if ( commitAfterEveryEvent ) {
						if ( ret )
							doCommit();
						else
							doRollback();
					}
				}
				 
				if ( cur != null && cur.hasNext() ) {
					curEntry = cur.next();
					curKey   = curEntry.getKey();
					curVal   = curEntry.getValue();
					log().t( "CUR",curKey );
					totalCnt++;
					currentCnt++;
				} else
					curEntry = null;
				
			}
			

			
		}
		
		log().t( "FINISH");
		boolean ret = listener.finish( current, last );
		if ( commitAfterFinish ) {
			if ( ret )
				doCommit();
			else
				doRollback();
		}
		
		tk.stop();
		log().d( "Time",tk.getCurrentTimeAsString() );

	}
	
	private boolean isPath( String path ) {
		if ( pathes == null ) return true;
		path = path.substring( 0, path.lastIndexOf( ',' ) );
		if ( needAllFolders ) {
			for ( int i = 0; i < pathes.length; i++ )
				if ( path.startsWith( pathesSlash[i] ) || path.equals( pathes[i] ) ) return true;
		} else {
			int pos = path.lastIndexOf( '/' );
			String p = null;
			if ( pos > 0 )
				p = path.substring( 0, pos );
			for ( int i = 0; i < pathes.length; i++ )
				if ( ( p != null && p.equals( pathes[i] )) || path.equals( pathes[i] ) ) return true;
			
		}
		return false;
	}
	
	/**
	 * <p>getProgress.</p>
	 *
	 * @return a int.
	 */
	public int getProgress() {
		if ( totalSize == 0 ) return 100;
		return totalCnt * 100 / totalSize;
	}

	/**
	 * <p>getProgressCount.</p>
	 *
	 * @return a long.
	 */
	public long getProgressCount() {
		return currentCnt;
	}

	/**
	 * <p>kill.</p>
	 */
	public void kill() {
		killed = true;
	}

	/**
	 * <p>getDeletedCnt.</p>
	 *
	 * @return a int.
	 */
	public int getDeletedCnt() {
		return deleteCnt;
	}

	/**
	 * <p>Getter for the field <code>insertCnt</code>.</p>
	 *
	 * @return a int.
	 */
	public int getInsertCnt() {
		return insertCnt;
	}

	/**
	 * <p>getUpdatedCnt.</p>
	 *
	 * @return a int.
	 */
	public int getUpdatedCnt() {
		return updateCnt;
	}

	/**
	 * <p>doCommit.</p>
	 */
	public void doCommit() {
	}

	/**
	 * <p>doRollback.</p>
	 */
	public void doRollback() {
	}
	
	/**
	 * <p>isFullRefresh.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isFullRefresh() {
		return fullRefresh;
	}

	/**
	 * <p>Setter for the field <code>fullRefresh</code>.</p>
	 *
	 * @param fullRefresh a boolean.
	 */
	public void setFullRefresh(boolean fullRefresh) {
		this.fullRefresh = fullRefresh;
	}

	/**
	 * <p>Getter for the field <code>pathes</code>.</p>
	 *
	 * @return an array of {@link java.lang.String} objects.
	 */
	public String[] getPathes() {
		return pathes;
	}

	/**
	 * <p>Setter for the field <code>pathes</code>.</p>
	 *
	 * @param pathes an array of {@link java.lang.String} objects.
	 */
	public void setPathes(String[] pathes) {
		this.pathes = pathes;
		pathesSlash = new String[ pathes.length ];
		for ( int i = 0; i < pathes.length; i++ )
			pathesSlash[i] = pathes[i] + '/';
	}

	/**
	 * <p>isCommitAfterEveryEvent.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isCommitAfterEveryEvent() {
		return commitAfterEveryEvent;
	}

	/**
	 * <p>Setter for the field <code>commitAfterEveryEvent</code>.</p>
	 *
	 * @param commitAfterEveryEvent a boolean.
	 */
	public void setCommitAfterEveryEvent(boolean commitAfterEveryEvent) {
		this.commitAfterEveryEvent = commitAfterEveryEvent;
	}

	/**
	 * <p>isCommitAfterFinish.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isCommitAfterFinish() {
		return commitAfterFinish;
	}

	/**
	 * <p>Setter for the field <code>commitAfterFinish</code>.</p>
	 *
	 * @param commitAfterFinish a boolean.
	 */
	public void setCommitAfterFinish(boolean commitAfterFinish) {
		this.commitAfterFinish = commitAfterFinish;
	}

	/**
	 * <p>isNeedAllFolders.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isNeedAllFolders() {
		return needAllFolders;
	}

	/**
	 * <p>Setter for the field <code>needAllFolders</code>.</p>
	 *
	 * @param needAllFolders a boolean.
	 */
	public void setNeedAllFolders(boolean needAllFolders) {
		this.needAllFolders = needAllFolders;
	}

	public static interface Listener {
		
		public void start( TreeMap<String,CompareDirEntry> pCurrent, TreeMap<String,CompareDirEntry> pLast );
		
		public boolean finish( TreeMap<String,CompareDirEntry> pCurrent, TreeMap<String,CompareDirEntry> pLast );
		
		public boolean updateObject( String path, CompareDirEntry curVal, CompareDirEntry lastVal );

		public boolean createObject( String path, CompareDirEntry curVal);

		public boolean deleteObject( String path, CompareDirEntry lastVal);
		
	}

}
