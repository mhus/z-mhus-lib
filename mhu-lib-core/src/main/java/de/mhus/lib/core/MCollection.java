package de.mhus.lib.core;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MCollection {

	/**
	 * Returns true of array is not null and the value of item is included in
	 * the array. It compares with the equals() method of the array item. Also
	 * a item value of null will be compared.
	 * 
	 * @param array
	 * @param item
	 * @return
	 */
	public static boolean contains(Object[] array, Object item) {
		if (array == null) return false;
		for (Object o : array) {
			if (item == null && o == null || o != null && o.equals(item)) return true;
		}
		return false;
	}

	/**
	 * Fills a list at the end with the values of an array, ignoring null values.
	 * @param array 
	 * @param list 
	 */
	public static <T> void copyArray(T[] array, Collection<T> list) {
		if (array == null || list == null) return;
		for (T item : array)
			if (item != null) list.add(item);
	}
	
	//from http://stackoverflow.com/questions/203984/how-do-i-remove-repeated-elements-from-arraylist
	/**
	 * remove duplicated entries
	 * @param list
	 */
	public static <T> void removeDuplicates(List<T> list) {
	    final Set<T> encountered = new HashSet<T>();
	    for (Iterator<T> iter = list.iterator(); iter.hasNext(); ) {
	        final T t = iter.next();
	        final boolean first = encountered.add(t);
	        if (!first) {
	            iter.remove();
	        }
	    }
	}

	/**
	 * remove duplicated entries, Attention exponential runtime behavior !!!
	 * Running from beginning to the end, the first element will be left, following removed.
	 * @param list
	 * @param comparator 
	 */
	public static <T> void removeDuplicates(List<T> list, Comparator<T> comparator) {
	    final Set<T> encountered = new HashSet<>();
	    for (Iterator<T> iter = list.iterator(); iter.hasNext(); ) {
	        final T t = iter.next();
	        boolean removed = false;
	        for (Iterator<T> iter2 = encountered.iterator(); iter2.hasNext(); ) {
	        	final T e = iter2.next();
	        	if (comparator.compare(t, e) == 0) {
		            iter.remove();
		            removed = true;
		            break;
	        	}
	        }
	        if (!removed) {
	        	encountered.add(t);
	        }
	    }
	}
	
}
