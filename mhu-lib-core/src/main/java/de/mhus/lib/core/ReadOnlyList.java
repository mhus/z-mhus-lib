package de.mhus.lib.core;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class ReadOnlyList<E> implements List<E> {

	private List<? extends E> instance;

	public ReadOnlyList(List<? extends E> instance) {
		this.instance = instance;
	}

	@Override
	public void forEach(Consumer<? super E> action) {
	}

	@Override
	public int size() {
		return instance.size();
	}

	@Override
	public boolean isEmpty() {
		return instance.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return instance.contains(o);
	}

	@Override
	public Iterator<E> iterator() {
		return (Iterator<E>) instance.iterator();
	}

	@Override
	public Object[] toArray() {
		return instance.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return instance.toArray(a);
	}

	@Override
	public boolean add(E e) {
		return false;
	}

	@Override
	public boolean remove(Object o) {
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return instance.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		return false;
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return false;
	}

	@Override
	public void replaceAll(UnaryOperator<E> operator) {
	}

	@Override
	public boolean removeIf(Predicate<? super E> filter) {
		return false;
	}

	@Override
	public void sort(Comparator<? super E> c) {
	}

	@Override
	public void clear() {
	}

	@Override
	public boolean equals(Object o) {
		return instance.equals(o);
	}

	@Override
	public int hashCode() {
		return instance.hashCode();
	}

	@Override
	public E get(int index) {
		return instance.get(index);
	}

	@Override
	public E set(int index, E element) {
		return null;
	}

	@Override
	public void add(int index, E element) {
	}

	@Override
	public Stream<E> stream() {
		return (Stream<E>) instance.stream();
	}

	@Override
	public E remove(int index) {
		return null;
	}

	@Override
	public Stream<E> parallelStream() {
		return (Stream<E>) instance.parallelStream();
	}

	@Override
	public int indexOf(Object o) {
		return instance.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return instance.lastIndexOf(o);
	}

	@Override
	public ListIterator<E> listIterator() {
		return (ListIterator<E>) instance.listIterator();
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		return (ListIterator<E>) instance.listIterator(index);
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		return (List<E>) instance.subList(fromIndex, toIndex);
	}

	@Override
	public Spliterator<E> spliterator() {
		return (Spliterator<E>) instance.spliterator();
	}
}
