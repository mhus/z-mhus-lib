package de.mhus.lib.test;

import de.mhus.lib.core.MSystem;
import de.mhus.lib.test.util.StringValue;
import de.mhus.lib.test.util.Template;
import junit.framework.TestCase;

public class MSystemTest extends TestCase {

	public void testTemplateNames() {
		Class<?> testy = StringValue.class;
		
		assertEquals("java.lang.String", MSystem.getTemplateCanonicalName(testy, 0) );
		assertEquals("java.lang.Integer", MSystem.getTemplateCanonicalName( (new Template<Integer>() {}).getClass(), 0 ) );
		
		assertNull( MSystem.getTemplateCanonicalName(testy, 1) );
		assertNull( MSystem.getTemplateCanonicalName( new Template<Integer>().getClass(), 0 ) );
		assertNull( MSystem.getTemplateCanonicalName(String.class, 0) );

	}
	
}
