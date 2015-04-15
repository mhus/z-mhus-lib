package de.mhus.lib.core.lang;

import java.util.Iterator;
import java.util.LinkedList;

public class InjectorList implements Injector,Iterable<Injector> {

	protected LinkedList<Injector> list = new LinkedList<Injector>();
	
	public int size() {
		return list.size();
	}

	public boolean add(Injector e) {
		return list.add(e);
	}

	@Override
	public Iterator<Injector> iterator() {
		return list.iterator();
	}

	public void clear() {
		list.clear();
	}

	public Injector get(int index) {
		return list.get(index);
	}

	@Override
	public void doInject(Object obj) throws Exception {
		for (Injector i : list)
			i.doInject(obj);
	}

}
