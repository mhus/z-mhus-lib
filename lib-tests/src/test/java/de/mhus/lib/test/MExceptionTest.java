/**
 * Copyright (C) 2002 Mike Hummel (mh@mhus.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import de.mhus.lib.basics.IResult;
import de.mhus.lib.basics.RC.CAUSE;
import de.mhus.lib.errors.ErrorException;
import de.mhus.lib.errors.ErrorRuntimeException;
import de.mhus.lib.errors.NotFoundException;
import de.mhus.lib.tests.TestCase;

public class MExceptionTest extends TestCase {


    @Test
    public void testErrorException() {
        try {
            throw new ErrorException("test");
        } catch (Throwable t) {
            System.out.println(t);
            assertTrue(t instanceof ErrorException);
            ErrorException e = (ErrorException)t;
            assertEquals(400, e.getReturnCode());
            assertEquals("[400,\"test\"]", e.getMessage());
        }
        try {
            throw new ErrorException("test","nr1");
        } catch (Throwable t) {
            System.out.println(t);
            assertTrue(t instanceof ErrorException);
            ErrorException e = (ErrorException)t;
            assertEquals(400, e.getReturnCode());
            assertEquals("[400,\"test\",\"nr1\"]", e.getMessage());
        }
        try {
            throw new ErrorException("test","special \"\\");
        } catch (Throwable t) {
            System.out.println(t);
            assertTrue(t instanceof ErrorException);
            ErrorException e = (ErrorException)t;
            assertEquals(400, e.getReturnCode());
            assertEquals("[400,\"test\",\"special \\\"\\\\\"]", e.getMessage());
        }
        try {
            throw new ErrorException("test", "nr1", "nr2", "nr3");
        } catch (Throwable t) {
            System.out.println(t);
            assertTrue(t instanceof ErrorException);
            ErrorException e = (ErrorException)t;
            assertEquals(400, e.getReturnCode());
            assertEquals("[400,\"test\",\"nr1\",\"nr2\",\"nr3\"]", e.getMessage());
        }
        try {
            throw new ErrorException("test", "nr1", new Object[] {"nr2", "nr3"});
        } catch (Throwable t) {
            System.out.println(t);
            assertTrue(t instanceof ErrorException);
            ErrorException e = (ErrorException)t;
            assertEquals(400, e.getReturnCode());
            assertEquals("[400,\"test\",\"nr1\",\"[nr2, nr3]\"]", e.getMessage());
        }
        try {
            throw new ErrorException("test", "nr1", new int[] {2, 3});
        } catch (Throwable t) {
            System.out.println(t);
            assertTrue(t instanceof ErrorException);
            ErrorException e = (ErrorException)t;
            assertEquals(400, e.getReturnCode());
            assertEquals("[400,\"test\",\"nr1\",\"[2, 3]\"]", e.getMessage());
        }
        try {
            throw new ErrorException((IResult)new NotFoundException("test", "nr1"));
        } catch (Throwable t) {
            System.out.println(t);
            assertTrue(t instanceof ErrorException);
            ErrorException e = (ErrorException)t;
            assertEquals(404, e.getReturnCode());
            assertEquals("[404,\"test\",\"nr1\"]", e.getMessage());
        }
        try {
            throw new ErrorException("error1", new NotFoundException("test", "nr1"));
        } catch (Throwable t) {
            System.out.println(t);
            assertTrue(t instanceof ErrorException);
            ErrorException e = (ErrorException)t;
            assertEquals(400, e.getReturnCode());
            assertEquals("[400,\"error1\"]", e.getMessage());
        }
    }
    
    @Test
    public void testErrorExceptionCauseAdaption() {

        try {
            throw new ErrorException(CAUSE.ADAPT, "error1",new NotFoundException("test", "nr1"));
        } catch (Throwable t) {
            System.out.println(t);
            assertTrue(t instanceof ErrorException);
            ErrorException e = (ErrorException)t;
            assertEquals(404, e.getReturnCode());
            assertEquals("[404,\"test\",\"nr1\"]", e.getMessage());
            assertNotNull(t.getCause());
            assertEquals("[404,\"test\",\"nr1\"]", t.getCause().getMessage());
        }
        try {
            throw new ErrorException(CAUSE.APPEND, "error1", new NotFoundException("test", "nr1"));
        } catch (Throwable t) {
            System.out.println(t);
            assertTrue(t instanceof ErrorException);
            ErrorException e = (ErrorException)t;
            assertEquals(404, e.getReturnCode()); // APPEND will ADAPT the Error Code (!)
            assertEquals("[400,\"error1\",[404,\"test\",\"nr1\"]]", e.getMessage());
            assertNotNull(t.getCause());
            assertEquals("[404,\"test\",\"nr1\"]", t.getCause().getMessage());
        }
        try {
            throw new ErrorException(CAUSE.ENCAPSULATE, "error1",new NotFoundException("test", "nr1"));
        } catch (Throwable t) {
            System.out.println(t);
            assertTrue(t instanceof ErrorException);
            ErrorException e = (ErrorException)t;
            assertEquals(400, e.getReturnCode());
            assertEquals("[400,\"error1\"]", e.getMessage());
            assertNotNull(t.getCause());
            assertEquals("[404,\"test\",\"nr1\"]", t.getCause().getMessage());
        }
        try {
            throw new ErrorException(CAUSE.HIDE, "error1",new NotFoundException("test", "nr1"));
        } catch (Throwable t) {
            System.out.println(t);
            assertTrue(t instanceof ErrorException);
            ErrorException e = (ErrorException)t;
            assertEquals(400, e.getReturnCode());
            assertEquals("[400,\"error1\"]", e.getMessage());
            assertNull(t.getCause());
        }
        try {
            throw new ErrorException(CAUSE.IGNORE, "error1",new NotFoundException("test", "nr1"));
        } catch (Throwable t) {
            System.out.println(t);
            assertTrue(t instanceof ErrorException);
            ErrorException e = (ErrorException)t;
            assertEquals(400, e.getReturnCode());
            assertEquals("[400,\"error1\",\"404 de.mhus.lib.errors.NotFoundException: [404,\\\"test\\\",\\\"nr1\\\"]\"]", e.getMessage());
            assertNotNull(t.getCause());
            assertEquals("[404,\"test\",\"nr1\"]", t.getCause().getMessage());
        }
    }
    
    @Test
    public void testErrorRuntimeException() {
        try {
            throw new ErrorRuntimeException("test");
        } catch (Throwable t) {
            System.out.println(t);
            assertTrue(t instanceof ErrorRuntimeException);
            ErrorRuntimeException e = (ErrorRuntimeException)t;
            assertEquals(400, e.getReturnCode());
            assertEquals("[400,\"test\"]", e.getMessage());
        }
        try {
            throw new ErrorRuntimeException("test","nr1");
        } catch (Throwable t) {
            System.out.println(t);
            assertTrue(t instanceof ErrorRuntimeException);
            ErrorRuntimeException e = (ErrorRuntimeException)t;
            assertEquals(400, e.getReturnCode());
            assertEquals("[400,\"test\",\"nr1\"]", e.getMessage());
        }
        try {
            throw new ErrorRuntimeException("test","special \"\\");
        } catch (Throwable t) {
            System.out.println(t);
            assertTrue(t instanceof ErrorRuntimeException);
            ErrorRuntimeException e = (ErrorRuntimeException)t;
            assertEquals(400, e.getReturnCode());
            assertEquals("[400,\"test\",\"special \\\"\\\\\"]", e.getMessage());
        }
        try {
            throw new ErrorRuntimeException("test", "nr1", "nr2", "nr3");
        } catch (Throwable t) {
            System.out.println(t);
            assertTrue(t instanceof ErrorRuntimeException);
            ErrorRuntimeException e = (ErrorRuntimeException)t;
            assertEquals(400, e.getReturnCode());
            assertEquals("[400,\"test\",\"nr1\",\"nr2\",\"nr3\"]", e.getMessage());
        }
        try {
            throw new ErrorRuntimeException("test", "nr1", new Object[] {"nr2", "nr3"});
        } catch (Throwable t) {
            System.out.println(t);
            assertTrue(t instanceof ErrorRuntimeException);
            ErrorRuntimeException e = (ErrorRuntimeException)t;
            assertEquals(400, e.getReturnCode());
            assertEquals("[400,\"test\",\"nr1\",\"[nr2, nr3]\"]", e.getMessage());
        }
        try {
            throw new ErrorRuntimeException("test", "nr1", new int[] {2, 3});
        } catch (Throwable t) {
            System.out.println(t);
            assertTrue(t instanceof ErrorRuntimeException);
            ErrorRuntimeException e = (ErrorRuntimeException)t;
            assertEquals(400, e.getReturnCode());
            assertEquals("[400,\"test\",\"nr1\",\"[2, 3]\"]", e.getMessage());
        }
        try {
            throw new ErrorRuntimeException((IResult)new NotFoundException("test", "nr1"));
        } catch (Throwable t) {
            System.out.println(t);
            assertTrue(t instanceof ErrorRuntimeException);
            ErrorRuntimeException e = (ErrorRuntimeException)t;
            assertEquals(404, e.getReturnCode());
            assertEquals("[404,\"test\",\"nr1\"]", e.getMessage());
        }
        try {
            throw new ErrorRuntimeException("error1", new NotFoundException("test", "nr1"));
        } catch (Throwable t) {
            System.out.println(t);
            assertTrue(t instanceof ErrorRuntimeException);
            ErrorRuntimeException e = (ErrorRuntimeException)t;
            assertEquals(400, e.getReturnCode());
            assertEquals("[400,\"error1\"]", e.getMessage());
        }
        try {
            throw new ErrorRuntimeException(CAUSE.ADAPT, "error1",new NotFoundException("test", "nr1"));
        } catch (Throwable t) {
            System.out.println(t);
            assertTrue(t instanceof ErrorRuntimeException);
            ErrorRuntimeException e = (ErrorRuntimeException)t;
            assertEquals(404, e.getReturnCode());
            assertEquals("[404,\"test\",\"nr1\"]", e.getMessage());
        }
        try {
            throw new ErrorRuntimeException(CAUSE.APPEND, "error1", new NotFoundException("test", "nr1"));
        } catch (Throwable t) {
            System.out.println(t);
            assertTrue(t instanceof ErrorRuntimeException);
            ErrorRuntimeException e = (ErrorRuntimeException)t;
            assertEquals(404, e.getReturnCode()); // APPEND will ADAPT the Error Code (!)
            assertEquals("[400,\"error1\",[404,\"test\",\"nr1\"]]", e.getMessage());
        }
    }
    
}
