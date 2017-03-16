package de.mhus.lib.test;

import java.util.LinkedList;

import de.mhus.lib.core.MApi;
import de.mhus.lib.core.base.AnnotationInjectStrategy;
import de.mhus.lib.core.base.BaseByThreadStrategy;
import junit.framework.TestCase;

public class MBaseTest extends TestCase {
	
	public void testInject() {
		MApi.get().getBaseControl().setFindStrategy(new BaseByThreadStrategy());
		MApi.get().getBaseControl().setInjectStrategy(new AnnotationInjectStrategy());
		@SuppressWarnings("unchecked")
		LinkedList<String> list = MApi.get().getBaseControl().base().lookup(LinkedList.class);
		list.add("TestString");
		new MBaseTestInjectionObject().test();
		new MBaseTestInjectionObjectExtended().test();
	}

}
