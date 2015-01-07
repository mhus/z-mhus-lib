package de.mhus.lib.test;

import java.awt.List;
import java.util.LinkedList;

import org.junit.Assert;

import de.mhus.lib.core.activator.MutableActivator;
import de.mhus.lib.core.lang.MObject;
import de.mhus.lib.core.system.DefaultBase;

public class MBaseTestClass extends MObject {

	@SuppressWarnings({ "unchecked" })
	public void override() {
		LinkedList<String> list1 = base(LinkedList.class);
		list1.clear();
		list1.add("test");
		Assert.assertEquals(1,list1.size());
		check(1);
		
		new MBaseTestClass().check(1); // on this conf. one entry

		try {
			createBase();
			check(1);
			
			base().setLocal(LinkedList.class);
			((MutableActivator)((DefaultBase)base()).getActivator()).addMap(List.class, LinkedList.class);
			
			check(0);
			new MBaseTestClass().check(0); // on this configuration zero entries
			
		} finally {
			leaveBase();
		}
		check(1);
		
		new MBaseTestClass().check(1); // on this conf. one entry

	}

	@SuppressWarnings("unchecked")
	private void check(int cnt) {
		LinkedList<String> list2 = base(LinkedList.class);
		Assert.assertEquals(cnt,list2.size());
	}

	@SuppressWarnings("unchecked")
	public void fork() {
		
		LinkedList<String> list1 = base(LinkedList.class);
		list1.clear();
		list1.add("test");
		Assert.assertEquals(1,list1.size());

		new MBaseTestClass().check(1);

		forkBase();
		base().setLocal(LinkedList.class);
		// only local scope
		check(0);
		// not for children
		new MBaseTestClass().check(1);

		try {
			installBase();
			// now scope for children also
			new MBaseTestClass().check(0);
		
		} finally {
			leaveBase();
		}
		
		// and back again
		new MBaseTestClass().check(1);
		check(0);
	}
	
}
