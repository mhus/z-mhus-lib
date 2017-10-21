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
			String name = LambdaUtil.getFunctionName(Object::getClass);
			System.out.println(name);
			assertEquals("getClass", name);
		}
		{
			String name = LambdaUtil.getFunctionName(PojoDataSource::getNext);
			System.out.println(name);
			assertEquals("getNext", name);
		}
		{
			String name = LambdaUtil.getFunctionName(PojoDataSource::log);
			System.out.println(name);
			assertEquals("log", name);
		}

		{
			String name = LambdaUtil.getFunctionName(PojoExample::getMyDouble);
			System.out.println(name);
			assertEquals("getMyDouble", name);
		}
		{
			String name = LambdaUtil.getFunctionName(PojoExample::getMyInt);
			System.out.println(name);
			assertEquals("getMyInt", name);
		}
		{
			String name = LambdaUtil.getFunctionName(PojoExample::getMyString);
			System.out.println(name);
			assertEquals("getMyString", name);
		}
		{
			String name = LambdaUtil.getFunctionName(PojoExample::toString);
			System.out.println(name);
			assertEquals("toString", name);
		}
		{
			String name = LambdaUtil.getConsumerName(System.out::println);
			System.out.println(name);
			assertEquals("println", name);
		}
		{
			String name = LambdaUtil.getBiConsumerName(PojoExample::setMyString);
			System.out.println(name);
			assertEquals("setMyString", name);
		}
		{
			String name = LambdaUtil.getBiConsumerName(PojoExample::setMyDouble);
			System.out.println(name);
			assertEquals("setMyDouble", name);
		}
		{
			String name = LambdaUtil.getBiConsumerName(PojoExample::setMyInt);
			System.out.println(name);
			assertEquals("setMyInt", name);
		}
		{
			String name = LambdaUtil.getBiConsumerName(PojoExample::setMyBoolean);
			System.out.println(name);
			assertEquals("setMyBoolean", name);
		}
		{
			String name = LambdaUtil.getFunctionName(PojoExample::isMyBoolean);
			System.out.println(name);
			assertEquals("isMyBoolean", name);
		}
		{
			String name = LambdaUtil.getConsumerName(PojoExample::doClean);
			System.out.println(name);
			assertEquals("doClean", name);
		}
		
		{
			String name = LambdaUtil.getFunctionName(MProperties::size);
			System.out.println(name);
			assertEquals("size", name);
		}
/*		
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
*/
	}
}
