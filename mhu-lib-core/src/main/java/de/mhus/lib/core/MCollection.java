/**
 * Copyright 2018 Mike Hummel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Pattern;

import de.mhus.lib.core.util.EmptyList;
import de.mhus.lib.core.util.ReadOnlyList;

public class MCollection {

	public static final List<?> EMPTY_LIST = new EmptyList<>();
	
	/**
	 * Returns true of array is not null and the value of item is included in
	 * the array. It compares with the equals() method of the array item. Also
	 * a item value of null will be compared.
	 * 
	 * @param array
	 * @param item
	 * @return true if contains the item
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

	public static int[] fillIntArray(int from, int to) {
		int[] out = new int[to-from];
		for (int l = 0; l < out.length; l++)
			out[l] = l+from;
		return out;
	}
	
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
	
	public static <T> List<T> toList(T[] array) {
		LinkedList<T> out = new LinkedList<>();
		for (T item : array)
			out.add(item);
		return out;
	}

//	public static <T> Set<T> toSet(T[] array) {
//		HashSet<T> out = new HashSet<>();
//		for (T item : array)
//			out.add(item);
//		return out;
//	}
	
	public static <T> TreeSet<T> toTreeSet(T[] items) {
		TreeSet<T> ret = new TreeSet<T>();
		for (T item : items)
			if (item != null) ret.add(item);
		return ret;
	}

	public static <T> HashSet<T> toHashSet(T[] items) {
		HashSet<T> ret = new HashSet<T>();
		for (T item : items)
			if (item != null) ret.add(item);
		return ret;
	}

	public static <T> void addAll(List<T> list, T[] items) {
		for (T i : items)
			if (i != null) list.add(i);
	}
	
	public static <T> void addAll(Set<T> list, T[] items) {
		for (T i : items)
			if (i != null) list.add(i);
	}

	@SuppressWarnings("unchecked")
	public static <T extends List<?>> T getEmptyList() {
		return (T)EMPTY_LIST;
	}

	public static <T> List<T> toReadOnlyList(List<? extends T> in) {
		return new ReadOnlyList<T>(in);
	}

	public static <T> List<T> toList(Collection<? extends T> set) {
		LinkedList<T> out = new LinkedList<>();
		out.addAll(set);
		return out;
	}

	public static <T> Set<T> toSet(Collection<? extends T> list) {
		HashSet<T> set = new HashSet<>();
		set.addAll(list);
		return set;
	}
	
	/**
	 * Returns true if the given item is part of the list. The list itself is a
	 * char separated list of items. White spaces are not allowed! The search is
	 * case sensitive.
	 * 
	 * @param list The list of items
	 * @param separator The separator between the list items
	 * @param item The searched item
	 * @return true if the item is part of the list
	 */
	public static boolean contains(String list, char separator, String item) {
		if (list == null || item == null) return false;
		// (.*,|)test(,.*|)
		String s = Pattern.quote(String.valueOf(separator));
		return list.matches( "(.*" + s + "|)" + Pattern.quote(item) + "(" + s + ".*|)" );
	}
	
	/**
	 * Append the item to the end of the list using the separator.
	 * If list is empty the item will be the first element.
	 * 
	 * @param list The list of items
	 * @param separator The item separator
	 * @param item New item to append
	 * @return The new list
	 */
	public static String append(String list, char separator, String item) {
		if (item == null) return list;
		if (MString.isEmpty(list)) return item;
		return list + separator + item;
	}

	/**
	 * Append the item to the end of the list using the separator if not already exists.
	 * If list is empty the item will be the first element.
	 * 
	 * @param list The list of items
	 * @param separator The item separator
	 * @param item New item to append
	 * @return The new list
	 */
	public static String set(String list, char separator, String item) {
		if (item == null) return list;
		if (MString.isEmpty(list)) return item;
		if (contains(list, separator, item)) return list;
		return list + separator + item;
	}

	/**
	 * Remove the given item from the list once.
	 * 
	 * @param list List of items
	 * @param separator Separator between the items
	 * @param item The item to remove
	 * @return New list with removed item
	 */
	public static String remove(String list, char separator, String item) {
		if (list == null || item == null || list.length() == 0) return list;
		if (list.equals(item)) return ""; // last element
		if (list.startsWith(item + separator)) return list.substring(item.length()+1); // first element
		if (list.endsWith(separator + item)) return list.substring(0, list.length() - 1 - item.length()); // last element
		return list.replaceFirst(Pattern.quote(separator + item + separator), String.valueOf(separator)); // somewhere in the middle
	}
	
	/**
	 * Returns a new instance of Map with sorted keys.
	 * 
	 * @param in
	 * @return a new sorted map
	 */
	public static <K,V> Map<K,V> sorted(Map<K,V> in) {
		return new TreeMap<K,V>(in);
	}
	
	/**
	 * If sort is possible (instance of Comparable) the function will create a new
	 * instance of list, copy and sort all the entries from the source into the new list and
	 * returns the created list.
	 * 
	 * If the list could not be sorted the original list object will be returned.
	 * 
	 * @param in
	 * @return A new sorted list
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <K> List<K> sorted(List<K> in) {
		if (in != null && in.size() > 0 && in.get(0) instanceof Comparable) {
			LinkedList<Comparable> l = new LinkedList<>();
			for (K item : in)
				if (item instanceof Comparable)
					l.add((Comparable)item);
			Collections.sort(l);
			return (List<K>) l;
		} else
		if (in == null)
			return new LinkedList<>();
		else
			return in;
	}

	public static <K,V> Map<K,V> sorted(Map<K,V> in, Comparator<K> comp) {
		TreeMap<K, V> out = new TreeMap<K,V>(comp);
		out.putAll(in);
		return out;
	}
	
	/**
	 * The function will create a new instance of list, copy and sort all the entries from 
	 * the source into the new list and returns the created list.
	 * 
	 * @param in
	 * @param comp 
	 * @return A new sorted list
	 */
	public static <K> List<K> sorted(List<K> in, Comparator<K> comp) {
		if (in != null && in.size() > 0) {
			LinkedList<K> l = new LinkedList<>(in);
			Collections.sort(l, comp);
			return (List<K>) l;
		} else
			return new LinkedList<>();
	}
	
}
