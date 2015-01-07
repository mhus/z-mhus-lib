package de.mhus.lib.core;

import java.util.Collection;

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
	 */
	public static <T> void copyArray(T[] array, Collection<T> list) {
		if (array == null || list == null) return;
		for (T item : array)
			if (item != null) list.add(item);
	}
}
