package de.mhus.lib.test;

import java.util.LinkedList;
import java.util.List;

import de.mhus.lib.core.MCollection;
import junit.framework.TestCase;

public class MCollectionTest extends TestCase {

	public void testListSort() {
		LinkedList<String> l = new LinkedList<>();
		l.add("z");
		l.add("a");
		l.add("m");
		List<String> s = MCollection.sorted(l);
		assertEquals("a", s.get(0));
		assertEquals("m", s.get(1));
		assertEquals("z", s.get(2));
	}
	
	public void testStringList() {
		String list = null;
		char S = ',';
		list = MCollection.append(list, S, "a");
		assertEquals("a", list);
		list = MCollection.append(list, S, "b");
		assertEquals("a,b", list);
		list = MCollection.append(list, S, "c");
		assertEquals("a,b,c", list);
		
		list = MCollection.set(list, S, "a");
		assertEquals("a,b,c", list);
		list = MCollection.set(list, S, "b");
		assertEquals("a,b,c", list);
		list = MCollection.set(list, S, "c");
		assertEquals("a,b,c", list);
		
		list = MCollection.remove(list, S, "b");
		assertEquals("a,c", list);
		list = MCollection.remove(list, S, "a");
		assertEquals("c", list);
		list = MCollection.remove(list, S, "c");
		assertEquals("", list);
		
	}
	
}
