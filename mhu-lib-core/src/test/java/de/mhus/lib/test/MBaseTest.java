package de.mhus.lib.test;

import java.util.LinkedList;

import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.base.AnnotationInjectStrategy;
import de.mhus.lib.core.base.BaseByThreadStrategy;
import junit.framework.TestCase;

public class MBaseTest extends TestCase {
	
	public void testInject() {
		MSingleton.get().getBaseControl().setFindStrategy(new BaseByThreadStrategy());
		MSingleton.get().getBaseControl().setInjectStrategy(new AnnotationInjectStrategy());
		@SuppressWarnings("unchecked")
		LinkedList<String> list = MSingleton.get().getBaseControl().base(null).lookup(LinkedList.class);
		list.add("TestString");
		new MBaseTestInjectionObject().test();
		new MBaseTestInjectionObjectExtended().test();
	}

}
