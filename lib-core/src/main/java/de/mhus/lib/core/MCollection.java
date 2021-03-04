/**
 * Copyright (C) 2002 Mike Hummel (mh@mhus.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
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
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import de.mhus.lib.core.util.EmptyList;
import de.mhus.lib.core.util.ReadOnlyList;
import de.mhus.lib.errors.MRuntimeException;

public class MCollection {

    public static final List<?> EMPTY_LIST = new EmptyList<>();

    /**
     * Returns true of array is not null and the value of item is included in the array. It compares
     * with the equals() method of the array item. Also a item value of null will be compared.
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

    public static int indexOf(Object[] array, Object item) {
        if (array == null) return -1;
        int pos = -1;
        for (Object o : array) {
            pos++;
            if (item == null && o == null || o != null && o.equals(item)) return pos;
        }
        return -1;
    }

    /**
     * Fills a list at the end with the values of an array, ignoring null values.
     *
     * @param array
     * @param list
     */
    public static <T> void copyArray(T[] array, Collection<T> list) {
        if (array == null || list == null) return;
        for (T item : array) if (item != null) list.add(item);
    }

    // from
    // http://stackoverflow.com/questions/203984/how-do-i-remove-repeated-elements-from-arraylist
    /**
     * remove duplicated entries
     *
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
     * remove duplicated entries, Attention exponential runtime behavior !!! Running from beginning
     * to the end, the first element will be left, following removed.
     *
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
    public static <T> T[] append(T[] array, T... newElements) {

        if (newElements == null || newElements.length == 0) return array;
        if (array == null) return newElements;

        @SuppressWarnings("unchecked")
        T[] newArray =
                (T[])
                        Array.newInstance(
                                array.getClass().getComponentType(),
                                array.length + newElements.length);
        System.arraycopy(array, 0, newArray, 0, array.length);
        System.arraycopy(newElements, 0, newArray, array.length, newElements.length);

        return newArray;
    }

    @SafeVarargs
    public static <T> T[] insert(T[] array, int index, T... newElements) {

        if (newElements == null || newElements.length == 0) return array;
        if (array == null) return newElements;
        if (index < 0 || index > array.length)
            throw new IndexOutOfBoundsException(
                    "Array.length: " + array.length + " Index: " + index);

        @SuppressWarnings("unchecked")
        T[] newArray =
                (T[])
                        Array.newInstance(
                                array.getClass().getComponentType(),
                                array.length + newElements.length);
        System.arraycopy(array, 0, newArray, 0, index);
        System.arraycopy(newElements, 0, newArray, index, newElements.length);
        System.arraycopy(array, index, newArray, index + newElements.length, array.length - index);

        return newArray;
    }

    public static <T> T[] remove(T[] array, int offset, int len) {

        if (array == null) return null;
        if (offset < 0 || offset + len > array.length)
            throw new IndexOutOfBoundsException(
                    "Array.length: " + array.length + " Offset: " + offset + " Len: " + len);

        @SuppressWarnings("unchecked")
        T[] newArray =
                (T[]) Array.newInstance(array.getClass().getComponentType(), array.length - len);
        System.arraycopy(array, 0, newArray, 0, offset);
        System.arraycopy(array, offset + len, newArray, offset, array.length - len - offset);

        return newArray;
    }

    public static int[] order(int[] array, boolean unique) {
        if (unique) {
            HashSet<Integer> set = new HashSet<>();
            for (int i : array) set.add(i);
            int[] out = new int[set.size()];
            Iterator<Integer> iter = set.iterator();
            for (int i = 0; i < out.length; i++) out[i] = iter.next();
            return out;
        }

        LinkedList<Integer> list = new LinkedList<>();
        for (int i : array) list.add(i);
        Collections.sort(list);
        int[] out = new int[list.size()];
        Iterator<Integer> iter = list.iterator();
        for (int i = 0; i < out.length; i++) out[i] = iter.next();
        return out;
    }

    public static long[] order(long[] array, boolean unique) {
        if (unique) {
            HashSet<Long> set = new HashSet<>();
            for (long i : array) set.add(i);
            long[] out = new long[set.size()];
            Iterator<Long> iter = set.iterator();
            for (int i = 0; i < out.length; i++) out[i] = iter.next();
            return out;
        }

        LinkedList<Long> list = new LinkedList<>();
        for (long i : array) list.add(i);
        Collections.sort(list);
        long[] out = new long[list.size()];
        Iterator<Long> iter = list.iterator();
        for (int i = 0; i < out.length; i++) out[i] = iter.next();
        return out;
    }

    public static int[] fillIntArray(int from, int to) {
        int[] out = new int[to - from];
        for (int l = 0; l < out.length; l++) out[l] = l + from;
        return out;
    }

    public static Map<String, String> toStringMap(Map<Object, Object> in, boolean ignoreNull) {
        HashMap<String, String> out = new HashMap<String, String>();
        for (Entry<Object, Object> e : in.entrySet()) {
            if (e.getValue() == null) {
                if (!ignoreNull) out.put(e.getKey().toString(), "");
            } else {
                out.put(e.getKey().toString(), e.getValue().toString());
            }
        }
        return out;
    }

    public static Map<String, String> toStringMap(IProperties in, boolean ignoreNull) {
        HashMap<String, String> out = new HashMap<String, String>();
        for (Map.Entry<String, Object> e : in) {
            if (e.getValue() == null) {
                if (!ignoreNull) out.put(e.getKey(), "");
            } else {
                out.put(e.getKey(), e.getValue().toString());
            }
        }
        return out;
    }

    public static <T> List<T> toList(@SuppressWarnings("unchecked") T... array) {
        LinkedList<T> out = new LinkedList<>();
        for (T item : array) out.add(item);
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
        for (T item : items) if (item != null) ret.add(item);
        return ret;
    }

    public static <T> HashSet<T> toHashSet(T[] items) {
        HashSet<T> ret = new HashSet<T>();
        for (T item : items) if (item != null) ret.add(item);
        return ret;
    }

    public static <T> void addAll(List<T> list, T[] items) {
        for (T i : items) if (i != null) list.add(i);
    }

    public static <T> void addAll(Set<T> list, T[] items) {
        for (T i : items) if (i != null) list.add(i);
    }

    @SuppressWarnings("unchecked")
    public static <T extends List<?>> T getEmptyList() {
        return (T) EMPTY_LIST;
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
     * Returns true if the given item is part of the list. The list itself is a char separated list
     * of items. White spaces are not allowed! The search is case sensitive.
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
        return list.matches("(.*" + s + "|)" + Pattern.quote(item) + "(" + s + ".*|)");
    }

    /**
     * Append the item to the end of the list using the separator. If list is empty the item will be
     * the first element.
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
     * Append the item to the end of the list using the separator if not already exists. If list is
     * empty the item will be the first element.
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
        if (list.startsWith(item + separator))
            return list.substring(item.length() + 1); // first element
        if (list.endsWith(separator + item))
            return list.substring(0, list.length() - 1 - item.length()); // last element
        return list.replaceFirst(
                Pattern.quote(separator + item + separator),
                String.valueOf(separator)); // somewhere in the middle
    }

    /**
     * Returns a new instance of Map with sorted keys.
     *
     * @param in
     * @return a new sorted map
     */
    public static <K, V> Map<K, V> sorted(Map<K, V> in) {
        return new TreeMap<K, V>(in);
    }

    /**
     * If sort is possible (instance of Comparable) the function will create a new instance of list,
     * copy and sort all the entries from the source into the new list and returns the created list.
     *
     * <p>If the list could not be sorted the original list object will be returned.
     *
     * @param in
     * @return A new sorted list
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <K> List<K> sorted(List<K> in) {
        if (in != null && in.size() > 0 && in.get(0) instanceof Comparable) {
            LinkedList<Comparable> l = new LinkedList<>();
            for (K item : in) if (item instanceof Comparable) l.add((Comparable) item);
            Collections.sort(l);
            return (List<K>) l;
        } else if (in == null) return new LinkedList<>();
        else return in;
    }

    public static <K, V> Map<K, V> sorted(Map<K, V> in, Comparator<K> comp) {
        TreeMap<K, V> out = new TreeMap<K, V>(comp);
        out.putAll(in);
        return out;
    }

    /**
     * The function will create a new instance of list, copy and sort all the entries from the
     * source into the new list and returns the created list.
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
        } else return new LinkedList<>();
    }

    /**
     * Process for each entry in the array. Return the new value for each entry.
     *
     * @param array
     * @param manipulator
     */
    public static <T> void updateEach(T[] array, Function<T, T> manipulator) {
        if (array == null) return;
        for (int i = 0; i < array.length; i++) array[i] = manipulator.apply(array[i]);
    }

    /**
     * Execute the consumer for each entry of the array.
     *
     * @param array
     * @param consumer
     */
    public static <T> void forEach(T[] array, Consumer<T> consumer) {
        if (array == null) return;
        for (int i = 0; i < array.length; i++) consumer.accept(array[i]);
    }

    public static boolean isEmpty(Collection<?> col) {
        return col == null || col.size() == 0;
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.size() == 0;
    }

    public static boolean isSet(Collection<?> col) {
        return !isEmpty(col);
    }

    public static boolean isSet(Map<?, ?> map) {
        return !isEmpty(map);
    }

    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0 || isAllNull(array);
    }

    public static boolean isSet(Object[] array) {
        return !isEmpty(array);
    }

    public static boolean isAllNull(Object[] array) {
        for (Object o : array) if (o != null) return false;
        return true;
    }

    /**
     * Create a new map and convert all string keys to lower case.
     *
     * @param parameters
     * @return map with lower case keys
     */
    public static Map<String, Object> toLowerCaseKeys(Map<String, Object> parameters) {
        return parameters
                .entrySet()
                .parallelStream()
                .collect(
                        Collectors.toMap(
                                entry -> entry.getKey().toLowerCase(), Map.Entry::getValue));
    }

    public static boolean equals(Object[] nr1, Object[] nr2) {
        if (nr1 == null && nr2 == null) return true;
        if (nr1 == null || nr2 == null) return false;
        if (nr1.length != nr2.length) return false;
        for (int i = 0; i < nr1.length; i++) if (!MSystem.equals(nr1[i], nr2[i])) return false;
        return true;
    }

    public static boolean equals(byte[] nr1, byte[] nr2) {
        if (nr1 == null && nr2 == null) return true;
        if (nr1 == null || nr2 == null) return false;
        if (nr1.length != nr2.length) return false;
        for (int i = 0; i < nr1.length; i++) if (nr1[i] != nr2[i]) return false;
        return true;
    }

    public static boolean equals(int[] nr1, int[] nr2) {
        if (nr1 == null && nr2 == null) return true;
        if (nr1 == null || nr2 == null) return false;
        if (nr1.length != nr2.length) return false;
        for (int i = 0; i < nr1.length; i++) if (nr1[i] != nr2[i]) return false;
        return true;
    }

    public static boolean equals(double[] nr1, double[] nr2) {
        if (nr1 == null && nr2 == null) return true;
        if (nr1 == null || nr2 == null) return false;
        if (nr1.length != nr2.length) return false;
        for (int i = 0; i < nr1.length; i++) if (nr1[i] != nr2[i]) return false;
        return true;
    }

    public static boolean equals(char[] nr1, char[] nr2) {
        if (nr1 == null && nr2 == null) return true;
        if (nr1 == null || nr2 == null) return false;
        if (nr1.length != nr2.length) return false;
        for (int i = 0; i < nr1.length; i++) if (nr1[i] != nr2[i]) return false;
        return true;
    }

    public static <T> Iterable<T> iterate(final Iterator<T> iterator) {
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return iterator;
            }
        };
    }

    /**
     * Cut a part from an array and create a new array with the values.
     *
     * @param from
     * @param start
     * @param stop
     * @return new cropped array
     */
    public static <T> T[] cropArray(T[] from, int start, int stop) {
        int length = stop - start;
        if (length < 0) throw new MRuntimeException("malformed indexes", start, stop, length);
        @SuppressWarnings("unchecked")
        T[] out = (T[]) Array.newInstance(from.getClass().getComponentType(), length);
        System.arraycopy(from, start, out, 0, length);
        return out;
    }

    /**
     * @param from
     * @param left
     * @param right
     * @return new extended array
     */
    public static <T> T[] extendArray(T[] from, int left, int right) {
        if (left < 0 || right < 0) throw new MRuntimeException("malformed extensions", left, right);
        int length = from.length + left + right;
        @SuppressWarnings("unchecked")
        T[] out = (T[]) Array.newInstance(from.getClass().getComponentType(), length);
        System.arraycopy(from, 0, out, left, from.length);
        return out;
    }

    /**
     * Search for an entry and return it use the filter to find it. Will return the first entry or
     * null.
     *
     * @param iter The list or iterable object
     * @param filter The filter to find the entry
     * @return The entry or null
     */
    public static <T> T search(Iterable<T> iter, Predicate<? super T> filter) {
        for (T item : iter) if (filter.test(item)) return item;
        return null;
    }

    /**
     * Return an new map and add the attributes alternating key and value.
     *
     * @param <K>
     * @param <V>
     * @param keyValues
     * @return A new Map filed with values
     */
    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> asMap(Object... keyValues) {
        HashMap<K, V> out = new HashMap<>();
        for (int i = 0; i < keyValues.length - 1; i = i + 2)
            out.put((K) keyValues[i], (V) keyValues[i + 1]);
        return out;
    }

    /**
     * Transforms a map into a key - value pair string.
     *
     * @param map
     * @return a key-value list
     */
    public static String[] toPairs(Map<String, Object> map) {
        if (map == null) return null;
        String[] out = new String[map.size() * 2];
        int cnt = 0;
        for (Entry<String, Object> entry : map.entrySet()) {
            out[cnt] = entry.getKey();
            cnt++;
            out[cnt] = String.valueOf(entry.getValue());
            cnt++;
        }
        return out;
    }

    /**
     * Extract the keys starting with prefix in a new HashMap.
     * Will return an empty map if prefix or map is null.
     * 
     * @param <V> Type of the value
     * @param prefix Prefix of the key to extract
     * @param map Map of all entries
     * @return Extracted subset
     */
    public static <V> HashMap<String, V> subset(String prefix, Map<String, V> map) {
        HashMap<String, V> out = new HashMap<>();
        if (prefix == null || map == null) return out;
        map.forEach((k,v) -> {if (k.startsWith(prefix)) out.put(k, v); } );
        return out;
    }
    
    /**
     * Extract the keys starting with prefix in a new HashMap.
     * It removes the prefix from the keys.
     * Will return an empty map if prefix or map is null.
     * 
     * @param <V> Type of the value
     * @param prefix Prefix of the key to extract
     * @param map Map of all entries
     * @return Extracted subset
     */
    public static <V> HashMap<String, V> subsetCrop(String prefix, Map<String, V> map) {
        HashMap<String, V> out = new HashMap<>();
        if (prefix == null || map == null) return out;
        int l = prefix.length();
        map.forEach((k,v) -> {if (k.startsWith(prefix)) out.put(k.substring(l), v); } );
        return out;
    }

}
