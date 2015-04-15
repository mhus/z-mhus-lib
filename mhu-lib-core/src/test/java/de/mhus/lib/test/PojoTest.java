package de.mhus.lib.test;

import junit.framework.TestCase;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.pojo.AttributesStrategy;
import de.mhus.lib.core.pojo.DefaultFilter;
import de.mhus.lib.core.pojo.DefaultStrategy;
import de.mhus.lib.core.pojo.FunctionsStrategy;
import de.mhus.lib.core.pojo.PojoAction;
import de.mhus.lib.core.pojo.PojoAttribute;
import de.mhus.lib.core.pojo.PojoModel;
import de.mhus.lib.core.pojo.PojoParser;

public class PojoTest extends TestCase {

	@SuppressWarnings("unchecked")
	public void testFunctionStrategy() throws Exception {
		
		PojoExample example = new PojoExample();
		PojoModel model = new PojoParser().parse(example, new FunctionsStrategy()).filter(new DefaultFilter()).getModel();

		System.out.println("Attributes: " + MString.join(model.getAttributeNames(), ','));
		System.out.println("Actions: " + MString.join(model.getActionNames(), ','));
		
		PojoAttribute<String> myString = model.getAttribute("mystring");
		assertNotNull(myString);
		
		myString.set(example, "aloa");
		assertEquals("aloa", example.getMyString());
		assertEquals("aloa", myString.get(example));

		PojoAction doClean = model.getAction("doclean");
		assertNotNull(doClean);
		doClean.doExecute(example);
		
		assertNull(example.getMyString());
		assertNull(myString.get(example));
		
		// Embedded
		PojoAttribute<String> line1 = model.getAttribute("myembedded.line1");
		assertNotNull(line1);
		line1.set(example, "cleopatra");
		assertEquals("cleopatra",example.getMyEmbedded().getLine1());
		
		// check hidden
		PojoAttribute<String> myhidden = model.getAttribute("myhidden");
		assertNull(myhidden);
		
		// check no action function
		PojoAction toString = model.getAction("tostring");
		assertNull(toString);
		
		// check read only
		PojoAttribute<String> myReadOnly = model.getAttribute("myreadonly");
		assertNotNull(myReadOnly);
		assertTrue(myReadOnly.canRead());
		assertFalse(myReadOnly.canWrite());
		
		PojoAttribute<Integer> myInt = model.getAttribute("myint");
		assertNotNull(myInt);
		myInt.set(example, 1);
		assertEquals(1, example.getMyInt());
		myInt.set(example, null);
		assertEquals(0, example.getMyInt());

		
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void testFunctionUpper() {
		
		PojoExample example = new PojoExample();
		PojoModel model = new PojoParser().parse(example,new FunctionsStrategy(true, false, ".", false, (Class)null)).filter(new DefaultFilter()).getModel();
		
		System.out.println("Attributes: " + MString.join(model.getAttributeNames(), ','));
		System.out.println("Actions: " + MString.join(model.getActionNames(), ','));
		
		PojoAttribute<String> myString = model.getAttribute("MyString");
		assertNotNull(myString);
		
		PojoAction doClean = model.getAction("doClean");
		assertNotNull(doClean);

		PojoAttribute<String> line1 = model.getAttribute("MyEmbedded.Line1");
		assertNotNull(line1);

		PojoAttribute<String> myhidden = model.getAttribute("MyHidden");
		assertNull(myhidden);
		
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	public void testAttributeStrategy() throws Exception {
		
		PojoExample example = new PojoExample();
		PojoModel model = new PojoParser().parse(example,new AttributesStrategy()).filter(new DefaultFilter()).getModel();
		
		System.out.println("Attributes: " + MString.join(model.getAttributeNames(), ','));
		System.out.println("Actions: " + MString.join(model.getActionNames(), ','));

		PojoAttribute<String> myString = model.getAttribute("mystring");
		assertNotNull(myString);
		
		myString.set(example, "aloa");
		assertEquals("aloa", example.getMyString());
		assertEquals("aloa", myString.get(example));

		PojoAttribute<String> myAttributeOnly = model.getAttribute("myattributeonly");
		assertNotNull(myString);
				
		// Embedded
		PojoAttribute<String> line1 = model.getAttribute("myembedded.line1");
		assertNotNull(line1);
		line1.set(example, "cleopatra");
		assertEquals("cleopatra",example.getMyEmbedded().getLine1());
		
		// check hidden
		PojoAttribute<String> myhidden = model.getAttribute("myhidden");
		assertNull(myhidden);
		
		// check read only
		PojoAttribute<String> myReadOnly = model.getAttribute("myreadonly");
		assertNotNull(myReadOnly);
		assertTrue(myReadOnly.canRead());
		assertTrue(myReadOnly.canWrite());
		
		PojoAttribute<Integer> myInt = model.getAttribute("myint");
		assertNotNull(myInt);
		myInt.set(example, 1);
		assertEquals(1, example.getMyInt());
		myInt.set(example, null);
		assertEquals(0, example.getMyInt());
		
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	public void testDefaultStrategy() throws Exception {
		
		PojoExample example = new PojoExample();
		PojoModel model = new PojoParser().parse(example,new DefaultStrategy()).filter(new DefaultFilter()).getModel();
		
		System.out.println("Attributes: " + MString.join(model.getAttributeNames(), ','));
		System.out.println("Actions: " + MString.join(model.getActionNames(), ','));

		PojoAttribute<String> myString = model.getAttribute("mystring");
		assertNotNull(myString);
		
		myString.set(example, "aloa");
		assertEquals("aloa", example.getMyString());
		assertEquals("aloa", myString.get(example));

		PojoAttribute<String> myAttributeOnly = model.getAttribute("myattributeonly");
		assertNotNull(myString);
		
		PojoAction doClean = model.getAction("doclean");
		assertNotNull(doClean);
		doClean.doExecute(example);
		
		assertNull(example.getMyString());
		assertNull(myString.get(example));
		
		// Embedded
		PojoAttribute<String> line1 = model.getAttribute("myembedded.line1");
		assertNotNull(line1);
		line1.set(example, "cleopatra");
		assertEquals("cleopatra",example.getMyEmbedded().getLine1());
		
		// check hidden
		PojoAttribute<String> myhidden = model.getAttribute("myhidden");
		assertNull(myhidden);
		
		// check no action function
		PojoAction toString = model.getAction("tostring");
		assertNull(toString);
		
		// check read only
		PojoAttribute<String> myReadOnly = model.getAttribute("myreadonly");
		assertNotNull(myReadOnly);
		assertTrue(myReadOnly.canRead());
		assertFalse(myReadOnly.canWrite());

	}
	
}
