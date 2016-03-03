package de.mhus.lib.core.lang;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * <p>InjectorList class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class InjectorList implements Injector,Iterable<Injector> {

	protected LinkedList<Injector> list = new LinkedList<Injector>();
	
	/**
	 * <p>size.</p>
	 *
	 * @return a int.
	 */
	public int size() {
		return list.size();
	}

	/**
	 * <p>add.</p>
	 *
	 * @param e a {@link de.mhus.lib.core.lang.Injector} object.
	 * @return a boolean.
	 */
	public boolean add(Injector e) {
		return list.add(e);
	}

	/** {@inheritDoc} */
	@Override
	public Iterator<Injector> iterator() {
		return list.iterator();
	}

	/**
	 * <p>clear.</p>
	 */
	public void clear() {
		list.clear();
	}

	/**
	 * <p>get.</p>
	 *
	 * @param index a int.
	 * @return a {@link de.mhus.lib.core.lang.Injector} object.
	 */
	public Injector get(int index) {
		return list.get(index);
	}

	/** {@inheritDoc} */
	@Override
	public void doInject(Object obj) throws Exception {
		for (Injector i : list)
			i.doInject(obj);
	}

}
