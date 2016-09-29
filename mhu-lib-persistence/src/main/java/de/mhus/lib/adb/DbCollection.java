package de.mhus.lib.adb;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import de.mhus.lib.adb.model.Field;
import de.mhus.lib.core.lang.MObject;
import de.mhus.lib.core.util.Table;
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
	private O next;
	private boolean recycle = false;
	private boolean hasNext = true;
	private boolean ownConnection;
	private O current;

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

	@SuppressWarnings("unchecked")
	private void nextObject() {
		next = null;
		if (!hasNext) return;
		try {
			while(true) {
				try {
					
					hasNext = res.next();
					if (hasNext) {
						O out = object;
						if (!recycle) {
							try {
								out = (O)manager.getSchema().createObject(object instanceof Class<?> ? (Class<?>)object : object.getClass(),registryName,res,manager, true);
							} catch (Throwable t) {
								close();
								throw new MException(con,t);
							}
						}
						manager.fillObject(registryName, out, con, res);
						next = out;
					} else {
						next = null;
					}
					break;
					
				} catch (AccessDeniedException ade) {
					// next one
				} catch (Throwable ade) {
					close();
					throw ade;
				}
			}
		} catch (Exception e) {
			log().w(e);
			hasNext = false;
		}
		if (!hasNext)
			close();
	}

	public void close() {
		if (res != null) {
			try {
				res.close();
			} catch (Exception e) {
				log().w(e);
			}
			res = null;
			next = null;
			hasNext = false;
			object = null;
		}
		if (con != null) {
			if (ownConnection)
				manager.getSchema().closeConnection(con);
			con = null;
		}
	}

	/**
	 * If recycle is on the original container object will be used for every iteration. If it's off then every time a new object will be created.
	 * Default is off.
	 * @param on
	 * @return x
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

	public O current() throws MException {
		return current;
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
		current = next;
		nextObject();
		return current;
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
		return list.toArray(dummy);
	}
	
	/**
	 * Transfer Objects to a table view.
	 * 
	 * @param maxSize More the zero, zero or less will disable the parameter
	 * @return x
	 */
	public Table toTableAndClose(int maxSize) {
		Table out = new Table();
		
		de.mhus.lib.adb.model.Table dbt = manager.getTable(registryName);
		for (Field dbf : dbt.getFields())
			out.addHeader(dbf.getName(), dbf.getType().getCanonicalName());

		Object[] row = new Object[out.getColumnSize()];
		for (O o : this) {
			int cnt = 0;
			try {
				for (Field dbf : dbt.getFields()) {
					row[cnt] = dbf.get(o);
					cnt++;
				}
				out.addRow(row);
				if (maxSize > 0 && out.getRowSize() >= maxSize) break;
			} catch (Throwable t) {
				log().d(t,cnt);
			}
		}
		close();
		
		return out;
	}

	public O getNextAndClose() {
		try {
			return hasNext() ? next() : null;
		} finally {
			close();
		}
	}

	public boolean skip(int cnt) { // TODO optimize, do not fully load objects
		for (int i = 0; i < cnt && hasNext(); i++)
			next();
		return hasNext();
	}
	
}
