package de.mhus.lib.core;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

/**
 * <p>MCollection class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class MCollection {

	/**
	 * Returns true of array is not null and the value of item is included in
	 * the array. It compares with the equals() method of the array item. Also
	 * a item value of null will be compared.
	 *
	 * @param array an array of {@link java.lang.Object} objects.
	 * @param item a {@link java.lang.Object} object.
	 * @return a boolean.
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
	 *
	 * @param array an array of T objects.
	 * @param list a {@link java.util.Collection} object.
	 * @param <T> a T object.
	 */
	public static <T> void copyArray(T[] array, Collection<T> list) {
		if (array == null || list == null) return;
		for (T item : array)
			if (item != null) list.add(item);
	}
	
	//from http://stackoverflow.com/questions/203984/how-do-i-remove-repeated-elements-from-arraylist
	/**
	 * remove duplicated entries
	 *
	 * @param list a {@link java.util.List} object.
	 * @param <T> a T object.
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
	 *
	 * @param list a {@link java.util.List} object.
	 * @param comparator a {@link java.util.Comparator} object.
	 * @param <T> a T object.
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

	/**
	 * <p>append.</p>
	 *
	 * @param array an array of T objects.
	 * @param newElements a T object.
	 * @param <T> a T object.
	 * @return an array of T objects.
	 */
	@SafeVarargs
	public static <T> T[] append(T[] array,T ... newElements) {
		
		if (newElements == null || newElements.length == 0) return array;
		if (array == null) return newElements;
		
		@SuppressWarnings("unchecked")
		T[] newArray = (T[]) Array.newInstance(array.getClass().getComponentType(), array.length + newElements.length);
		System.arraycopy(array, 0, newArray, 0, array.length);
		System.arraycopy(newElements, 0, newArray, array.length, newElements.length);
		
		return newArray;
	}

	/**
	 * <p>order.</p>
	 *
	 * @param array an array of int.
	 * @param unique a boolean.
	 * @return an array of int.
	 */
	public static int[] order(int[] array, boolean unique) {
		if (unique) {
			HashSet<Integer> set = new HashSet<>();
			for (int i : array)
				set.add(i);
			int[] out = new int[set.size()];
			Iterator<Integer> iter = set.iterator();
			for (int i = 0; i < out.length; i++)
				out[i] = iter.next();
			return out;
		}
		
		LinkedList<Integer> list = new LinkedList<>();
		for (int i : array)
			list.add(i);
		Collections.sort(list);
		int[] out = new int[list.size()];
		Iterator<Integer> iter = list.iterator();
		for (int i = 0; i < out.length; i++)
			out[i] = iter.next();
		return out;
	}

	/**
	 * <p>order.</p>
	 *
	 * @param array an array of long.
	 * @param unique a boolean.
	 * @return an array of long.
	 */
	public static long[] order(long[] array, boolean unique) {
		if (unique) {
			HashSet<Long> set = new HashSet<>();
			for (long i : array)
				set.add(i);
			long[] out = new long[set.size()];
			Iterator<Long> iter = set.iterator();
			for (int i = 0; i < out.length; i++)
				out[i] = iter.next();
			return out;
		}
		
		LinkedList<Long> list = new LinkedList<>();
		for (long i : array)
			list.add(i);
		Collections.sort(list);
		long[] out = new long[list.size()];
		Iterator<Long> iter = list.iterator();
		for (int i = 0; i < out.length; i++)
			out[i] = iter.next();
		return out;
	}

	/**
	 * <p>fillIntArray.</p>
	 *
	 * @param from a int.
	 * @param to a int.
	 * @return an array of int.
	 */
	public static int[] fillIntArray(int from, int to) {
		int[] out = new int[to-from];
		for (int l = 0; l < out.length; l++)
			out[l] = l+from;
		return out;
	}
	
	/**
	 * <p>toStringMap.</p>
	 *
	 * @param in a {@link java.util.Map} object.
	 * @param ignoreNull a boolean.
	 * @return a {@link java.util.Map} object.
	 */
	public static Map<String,String> toStringMap(Map<Object,Object> in, boolean ignoreNull) {
		HashMap<String, String> out = new HashMap<String,String>();
		for (Entry<Object, Object> e : in.entrySet()) {
			if (e.getValue() == null) {
				if (!ignoreNull)
					out.put(e.getKey().toString(), "");
			} else {
				out.put(e.getKey().toString(), e.getValue().toString());
			}
		}
		return out;
	}
	
	/**
	 * <p>toStringMap.</p>
	 *
	 * @param in a {@link de.mhus.lib.core.IProperties} object.
	 * @param ignoreNull a boolean.
	 * @return a {@link java.util.Map} object.
	 */
	public static Map<String,String> toStringMap(IProperties in, boolean ignoreNull) {
		HashMap<String, String> out = new HashMap<String,String>();
		for (Map.Entry<String,Object> e : in) {
			if (e.getValue() == null) {
				if (!ignoreNull)
					out.put(e.getKey(), "");
			} else {
				out.put(e.getKey(), e.getValue().toString());
			}
		}
		return out;
	}
	
	/**
	 * <p>toList.</p>
	 *
	 * @param array an array of T objects.
	 * @param <T> a T object.
	 * @return a {@link java.util.List} object.
	 */
	public static <T> List<T> toList(T[] array) {
		LinkedList<T> out = new LinkedList<>();
		for (T item : array)
			out.add(item);
		return out;
	}

	/**
	 * <p>toTreeSet.</p>
	 *
	 * @param items an array of T objects.
	 * @param <T> a T object.
	 * @return a {@link java.util.TreeSet} object.
	 */
	public static <T> TreeSet<T> toTreeSet(T[] items) {
		TreeSet<T> ret = new TreeSet<T>();
		for (T item : items)
			if (item != null) ret.add(item);
		return ret;
	}

	/**
	 * <p>toHashSet.</p>
	 *
	 * @param items an array of T objects.
	 * @param <T> a T object.
	 * @return a {@link java.util.HashSet} object.
	 */
	public static <T> HashSet<T> toHashSet(T[] items) {
		HashSet<T> ret = new HashSet<T>();
		for (T item : items)
			if (item != null) ret.add(item);
		return ret;
	}

	/**
	 * <p>addAll.</p>
	 *
	 * @param list a {@link java.util.List} object.
	 * @param items an array of T objects.
	 * @param <T> a T object.
	 */
	public static <T> void addAll(List<T> list, T[] items) {
		for (T i : items)
			if (i != null) list.add(i);
	}
	
	/**
	 * <p>addAll.</p>
	 *
	 * @param list a {@link java.util.Set} object.
	 * @param items an array of T objects.
	 * @param <T> a T object.
	 */
	public static <T> void addAll(Set<T> list, T[] items) {
		for (T i : items)
			if (i != null) list.add(i);
	}
	
}
