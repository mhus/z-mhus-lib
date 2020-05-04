/**
 * Copyright 2018 Mike Hummel
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.Test;

import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.util.AlreadyBoundException;
import de.mhus.lib.core.util.LocalClassLoader;
import de.mhus.lib.errors.NotFoundException;
import de.mhus.lib.test.util.TransferIfc;
import de.mhus.lib.test.util.TransferImpl;
import static org.junit.jupiter.api.Assertions.*;

public class BytecodeTest {

    @Test
    public void testGetMethodName() throws NotFoundException {
        //		LambdaUtil.debugOut = true;
        //		{
        //			String name = LambdaUtil.getFunctionName(Object::getClass);
        //			System.out.println(name);
        //			assertEquals("getClass", name);
        //		}
        //		{
        //			String name = LambdaUtil.getFunctionName(PojoDataSource::getNext);
        //			System.out.println(name);
        //			assertEquals("getNext", name);
        //		}
        //		{
        //			String name = LambdaUtil.getFunctionName(PojoDataSource::log);
        //			System.out.println(name);
        //			assertEquals("log", name);
        //		}
        //
        //		{
        //			String name = LambdaUtil.getFunctionName(PojoExample::getMyDouble);
        //			System.out.println(name);
        //			assertEquals("getMyDouble", name);
        //		}
        //		{
        //			String name = LambdaUtil.getFunctionName(PojoExample::getMyInt);
        //			System.out.println(name);
        //			assertEquals("getMyInt", name);
        //		}
        //		{
        //			String name = LambdaUtil.getFunctionName(PojoExample::getMyString);
        //			System.out.println(name);
        //			assertEquals("getMyString", name);
        //		}
        //		{
        //			String name = LambdaUtil.getFunctionName(PojoExample::toString);
        //			System.out.println(name);
        //			assertEquals("toString", name);
        //		}
        //		{
        //			String name = LambdaUtil.getConsumerName(System.out::println);
        //			System.out.println(name);
        //			assertEquals("println", name);
        //		}
        //		{
        //			String name = LambdaUtil.getBiConsumerName(PojoExample::setMyString);
        //			System.out.println(name);
        //			assertEquals("setMyString", name);
        //		}
        //		{
        //			String name = LambdaUtil.getBiConsumerName(PojoExample::setMyDouble);
        //			System.out.println(name);
        //			assertEquals("setMyDouble", name);
        //		}
        //		{
        //			String name = LambdaUtil.getBiConsumerName(PojoExample::setMyInt);
        //			System.out.println(name);
        //			assertEquals("setMyInt", name);
        //		}
        //		{
        //			String name = LambdaUtil.getBiConsumerName(PojoExample::setMyBoolean);
        //			System.out.println(name);
        //			assertEquals("setMyBoolean", name);
        //		}
        //		{
        //			String name = LambdaUtil.getFunctionName(PojoExample::isMyBoolean);
        //			System.out.println(name);
        //			assertEquals("isMyBoolean", name);
        //		}
        //		{
        //			String name = LambdaUtil.getConsumerName(PojoExample::doClean);
        //			System.out.println(name);
        //			assertEquals("doClean", name);
        //		}
        //
        //		{
        //			String name = LambdaUtil.getFunctionName(MProperties::size);
        //			System.out.println(name);
        //			assertEquals("size", name);
        //		}
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

    @Test
    public void testClassTransfer()
            throws IOException, AlreadyBoundException, ClassNotFoundException,
                    InstantiationException, IllegalAccessException, IllegalArgumentException,
                    InvocationTargetException, NoSuchMethodException, SecurityException {

        String name = TransferImpl.class.getCanonicalName();
        byte[] code = MSystem.getBytes(TransferImpl.class);
        LocalClassLoader cl = new LocalClassLoader();
        cl.addClassCode(name, code);

        Class<?> clazz = cl.loadClass(name);
        assertNotSame(TransferImpl.class, clazz);

        TransferIfc obj = (TransferIfc) clazz.getDeclaredConstructor().newInstance();
        int res = obj.hello();
        assertEquals(1, res);
    }
}
