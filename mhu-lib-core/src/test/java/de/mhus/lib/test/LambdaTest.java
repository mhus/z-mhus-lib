package de.mhus.lib.test;

import org.junit.Test;

import de.mhus.lib.core.MProperties;
import de.mhus.lib.core.util.lambda.LambdaUtil;
import de.mhus.lib.errors.NotFoundException;
import de.mhus.lib.form.PojoDataSource;
import junit.framework.TestCase;

public class LambdaTest extends TestCase {

	@Test
	public void testGetMethodName() throws NotFoundException {
		LambdaUtil.debugOut = true;
		{
			String name = LambdaUtil.getMethodName(Object::getClass);
			System.out.println(name);
			assertEquals("getClass", name);
		}
		{
			String name = LambdaUtil.getMethodName(PojoDataSource::getNext);
			System.out.println(name);
			assertEquals("getNext", name);
		}
		{
			String name = LambdaUtil.getMethodName(PojoDataSource::log);
			System.out.println(name);
			assertEquals("log", name);
		}
		{
			String name = LambdaUtil.getMethodName(PojoExample::getMyDouble);
			System.out.println(name);
			assertEquals("getMyDouble", name);
		}
		{
			String name = LambdaUtil.getMethodName(PojoExample::getMyInt);
			System.out.println(name);
			assertEquals("getMyInt", name);
		}
		{
			String name = LambdaUtil.getMethodName(PojoExample::getMyString);
			System.out.println(name);
			assertEquals("getMyString", name);
		}
		{
			String name = LambdaUtil.getMethodName(PojoExample::toString);
			System.out.println(name);
			assertEquals("toString", name);
		}
		{
			String name = LambdaUtil.getMethodName(System.out::println);
			System.out.println(name);
			assertEquals("println", name);
		}
		{
			String name = LambdaUtil.getMethodName(PojoExample::setMyString);
			System.out.println(name);
			assertEquals("setMyString", name);
		}
		{
			String name = LambdaUtil.getMethodName(PojoExample::setMyDouble);
			System.out.println(name);
			assertEquals("setMyDouble", name);
		}
		{
			String name = LambdaUtil.getMethodName(PojoExample::setMyInt);
			System.out.println(name);
			assertEquals("setMyInt", name);
		}
		{
			String name = LambdaUtil.getMethodName(PojoExample::setMyBoolean);
			System.out.println(name);
			assertEquals("setMyBoolean", name);
		}
		{
			String name = LambdaUtil.getMethodName(PojoExample::isMyBoolean);
			System.out.println(name);
			assertEquals("isMyBoolean", name);
		}
		{
			String name = LambdaUtil.getMethodName(PojoExample::doClean);
			System.out.println(name);
			assertEquals("doClean", name);
		}
		{
			String name = LambdaUtil.getMethodName(MProperties::size);
			System.out.println(name);
			assertEquals("size", name);
		}
		
		{
			String name = LambdaUtil.getMethodName(PojoExample::setMyChar);
			System.out.println(name);
			assertEquals("setMyChar", name);
		}
		{
			String name = LambdaUtil.getMethodName(PojoExample::getMyChar);
			System.out.println(name);
			assertEquals("getMyChar", name);
		}

		{
			String name = LambdaUtil.getMethodName(PojoExample::setMyShort);
			System.out.println(name);
			assertEquals("setMyShort", name);
		}
		{
			String name = LambdaUtil.getMethodName(PojoExample::getMyShort);
			System.out.println(name);
			assertEquals("getMyShort", name);
		}

		{
			String name = LambdaUtil.getMethodName(PojoExample::setMyByte);
			System.out.println(name);
			assertEquals("setMyByte", name);
		}
		{
			String name = LambdaUtil.getMethodName(PojoExample::getMyByte);
			System.out.println(name);
			assertEquals("getMyByte", name);
		}

		{
			String name = LambdaUtil.getMethodName(PojoExample::setMyByteArray);
			System.out.println(name);
		}
		{
			String name = LambdaUtil.getMethodName(PojoExample::getMyByteArray);
			System.out.println(name);
			assertEquals("getMyByteArray", name);
		}

	}
}
