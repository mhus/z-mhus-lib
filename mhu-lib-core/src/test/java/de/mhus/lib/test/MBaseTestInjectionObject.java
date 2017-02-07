package de.mhus.lib.test;

import java.util.LinkedList;

import org.junit.Assert;

import de.mhus.lib.annotations.base.Bind;
import de.mhus.lib.core.lang.MBaseObject;

@Bind
public class MBaseTestInjectionObject extends MBaseObject {

	@Bind
	private LinkedList<String> list;

	public void test() {
		Assert.assertEquals(1,list.size());
	}
	
}
