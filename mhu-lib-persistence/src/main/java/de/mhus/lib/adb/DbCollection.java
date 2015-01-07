package de.mhus.lib.adb;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import de.mhus.lib.core.lang.MObject;
import de.mhus.lib.errors.AccessDeniedException;
import de.mhus.lib.errors.MException;
import de.mhus.lib.sql.DbConnection;
import de.mhus.lib.sql.DbResult;

/**
 * Represents a collection of results.
 * 
 * @author mikehummel
 *
 * @param <O>
 */
public class DbCollection<O> extends MObject implements Iterable<O>, Iterator<O> {

	private DbManager manager;
	private DbResult res;
	private DbConnection con;
	private String registryName;
	private O object;
	private boolean recycle = false;
	private boolean hasNext = true;
	private boolean ownConnection;

	public DbCollection(DbManager manager, DbConnection con, boolean ownConnection, String registryName, O object, DbResult res) throws MException {
		
		if (registryName == null) {
			Class<?> clazz = manager.getSchema().findClassForObject(object,manager);
			if (clazz == null)
				throw new MException("class definition not found for object",object.getClass().getCanonicalName(),registryName);
			registryName = clazz.getCanonicalName();
		}
		
		this.manager = manager;
		this.res = res;
		this.con = con;
		this.registryName = registryName;
		this.object = object;
		this.ownConnection = ownConnection;
		
		nextObject();
	}
	
	private void nextObject() {
		if (!hasNext) return;
		try {
			while(true) {
				try {
					hasNext = res.next();
					if (hasNext)
						manager.checkFillObject(registryName, object, con, res);
					break;
				} catch (MException ade) {
					if (ade.getCause() == null || !(ade.getCause() instanceof AccessDeniedException) )
						throw ade;
				}
			}
			// TODO check read access and maybe get next row
		} catch (Exception e) {
			log().w(e);
			hasNext = false;
		}
		if (!hasNext)
			close();
	}

	public void close() {
		if (res == null) return;
		try {
			res.close();
		} catch (Exception e) {
			log().w(e);
		}
		if (ownConnection)
			con.close();
		res = null;
	}

	/**
	 * If recycle is on the original container object will be used for every iteration. If it's off then every time a new object will be created.
	 * Default is off.
	 * @param on
	 * @return 
	 */
	public DbCollection<O> setRecycle(boolean on) {
		recycle = on;
		return this;
	}
	
	public boolean isRecycle() {
		return recycle;
	}
	
	@Override
	public boolean hasNext() {
		return hasNext;
	}

	@SuppressWarnings("unchecked")
	public O getObject() throws MException {

		O out = object;
		if (!recycle) {
			try {
				out = (O)manager.getSchema().createObject(object instanceof Class<?> ? (Class<?>)object : object.getClass(),registryName,res,manager, true);
			} catch (Throwable t) {
				throw new MException(con,t);
			}
		}
		if (out == null) return null; // for secure
		manager.fillObject(registryName, out, con, res);
		return out;
	}

	@Override
	public Iterator<O> iterator() {
		return this;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	@Override
	public O next() {
		if (!hasNext) throw new NoSuchElementException();
		try {
			O out = getObject();
			nextObject();
			return out;
		} catch (MException e) {
			log().w(e);
		}
		throw new NoSuchElementException();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addToList(List list) {
		for (O o : this) {
			list.add(o);
		}
	}
	
	public List<O> toCacheAndClose() {
		List<O> list = new LinkedList<O>();
		addToList(list);
		close();
		return list;
	}
	
	public O[] toArrayAndClose(O[] dummy) {
		List<O> list = toCacheAndClose();
		return (O[]) list.toArray(dummy);
	}
	
	public O getNextAndClose() {
		try {
			return hasNext() ? next() : null;
		} finally {
			close();
		}
	}

}
