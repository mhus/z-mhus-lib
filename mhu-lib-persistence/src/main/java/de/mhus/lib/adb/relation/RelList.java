package de.mhus.lib.adb.relation;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import de.mhus.lib.adb.DbComfortableObject;
import de.mhus.lib.adb.DbManager;
import de.mhus.lib.annotations.adb.DbRelation;
import de.mhus.lib.sql.DbConnection;

public class RelList<T> implements List<T> {

	List<T> current;
	List<T> add;
	List<T> remove;
	List<T> org;
	List<T> changed;
	private DbRelation config;
	
	@SuppressWarnings("unchecked")
	public RelList(List<?> list, DbRelation config) {
		org = (List<T>) list;
		current = (List<T>) list;
		this.config = config;
	}
	
	public int size() {
		return current.size();
	}
	public boolean isEmpty() {
		return current.isEmpty();
	}
	public boolean contains(Object o) {
		return current.contains(o);
	}
	public Iterator<T> iterator() {
		return new LinkedList<T>(current).iterator(); // iterate over a copy
	}
	public Object[] toArray() {
		return current.toArray();
	}
	public <T> T[] toArray(T[] a) {
		return current.toArray(a);
	}
	public boolean add(T e) {
		init();
		if (remove.contains(e)) remove.remove(e);
		if (!org.contains(e) && !add.contains(e)) add.add(e);
		if (!current.contains(e)) current.add(e);
		return true;
	}
	public boolean remove(Object o) {
		init();
		if (!remove.contains(o) && org.contains(o)) remove.add((T)o);
		if (add.contains(o)) add.remove(o);
		return current.remove(o);
	}
	
	private void init() {
		if (add != null) return;
		current = new LinkedList<T>(org);
		add = new LinkedList<T>();
		remove = new LinkedList<T>();
		changed = new LinkedList<T>();
	}

	public boolean containsAll(Collection<?> c) {
		return current.containsAll(c);
	}
	public boolean addAll(Collection<? extends T> c) {
		for (T t : c) add(t);
		return true;
	}
	public boolean addAll(int index, Collection<? extends T> c) {
		for (T t : c) add(t);
		return true;
	}
	public boolean removeAll(Collection<?> c) {
		for (Object t : c) remove(t);
		return true;

	}
	public boolean retainAll(Collection<?> c) {
		return current.retainAll(c);
	}
	public void clear() {
		for (T t : current) remove(t);
	}
	public boolean equals(Object o) {
		return current.equals(o);
	}
	public int hashCode() {
		return current.hashCode();
	}
	public T get(int index) {
		return current.get(index);
	}
	public T set(int index, T element) {
		// return current.set(index, element);
		return null;
	}
	public void add(int index, T element) {
		add(element);
	}
	public T remove(int index) {
		remove(get(index));
		return null;
	}
	public int indexOf(Object o) {
		return current.indexOf(o);
	}
	public int lastIndexOf(Object o) {
		return current.lastIndexOf(o);
	}
	public ListIterator<T> listIterator() {
		return current.listIterator();
	}
	public ListIterator<T> listIterator(int index) {
		return current.listIterator(index);
	}
	public List<T> subList(int fromIndex, int toIndex) {
		return current.subList(fromIndex, toIndex);
	}
	
	public boolean changed() {
		return add != null && (add.size() > 0 || remove.size() > 0 || changed.size() > 0);
	}
	
	public void save(DbManager manager, DbConnection con, String fName, Object value) throws Exception {
		if (remove != null) {
			for (T t : remove) {
				try {
					if (config.remove()) {
						manager.deleteObject(con, t);
					} else {
						manager.getTable(manager.getRegistryName(t)).getField(fName).set(t, null);
						manager.saveObject(con, t);
					}
				} catch (Throwable te) {
					te.printStackTrace(); //XXX
				}
			}
		}
		if (add != null) {
			for (T t : add) {
				try {
					manager.getTable(manager.getRegistryName(t)).getField(fName).set(t, value);
					if ((t instanceof DbComfortableObject) && !((DbComfortableObject)t).isAdbPersistent() )
						manager.createObject(con, t);
					else
						manager.saveObject(con, t);
				} catch (Throwable te) {
					te.printStackTrace(); //XXX
				}	
			}
		}
		if (changed != null) {
			for (T t : changed) {
				try {
					if (current.contains(t)) {
						manager.getTable(manager.getRegistryName(t)).getField(fName).set(t, value);
	//					if (t instanceof DbComfortableObject && !((DbComfortableObject)t).isAdbManaged() )
							manager.saveObject(con, t);
	//					else
	//						manager.saveObject(con, t);
					}
				} catch (Throwable te) {
					te.printStackTrace(); //XXX
				}
			}
		}
		
		org = current;
		current = null;
		add = null;
		remove = null;
		changed = null;
	}
	
	public void setChanged(T obj) {
		init();
		changed.add(obj);
	}
	
	public void unsetChanged(T obj) {
		if (changed == null) return;
		changed.remove(obj);
	}
	
}
