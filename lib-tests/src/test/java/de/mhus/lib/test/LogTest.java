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

// import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import de.mhus.lib.basics.IResult;
import de.mhus.lib.basics.RC;
import de.mhus.lib.basics.RC.CAUSE;
import de.mhus.lib.errors.MException;
import de.mhus.lib.tests.TestCase;

public class LogTest extends TestCase {
    
    @Test
    public void testRC() throws Exception {
        {
            String msg = RC.toMessage(1,(IResult)null, "test", null , 0);
            System.out.println(msg);
            assertEquals("[1,\"test\"]", msg);
        }
        {
            String msg = RC.toMessage(1,(IResult)null, "test", new Object[] {"nr1"} , 0);
            System.out.println(msg);
            assertEquals("[1,\"test\",\"nr1\"]", msg);
        }
        {
            String msg = RC.toMessage(1,(IResult)null, "test", new Object[] {"nr1",null} , 0);
            System.out.println(msg);
            assertEquals("[1,\"test\",\"nr1\",null]", msg);
        }
        {
            String msg = RC.toMessage(1,(IResult)null, "test", new Object[] {"nr1",null, new String[] {"a","b"}, "last" } , 0);
            System.out.println(msg);
            assertEquals("[1,\"test\",\"nr1\",null,\"[a, b]\",\"last\"]", msg);
        }
        {
            String msg = RC.toMessage(1,CAUSE.APPEND, "test", new Object[] {"nr1", new Exception("exception"), "nr2"} , 0);
            System.out.println(msg);
            assertEquals("[1,\"test\",\"nr1\",\"nr2\"]", msg);
        }
        {
            MException cause = new MException(1,"cause");
            String msg = RC.toMessage(1,cause, "test", new Object[] {"nr1", "nr2"} , 0);
            System.out.println(msg);
            assertEquals("[1,\"test\",\"nr1\",\"nr2\",[1,\"cause\"]]", msg);
        }
        {
            MException cause = new MException(1,"cause", "c1");
            String msg = RC.toMessage(1,cause, "test", new Object[] {"nr1", "nr2"} , 0);
            System.out.println(msg);
            assertEquals("[1,\"test\",\"nr1\",\"nr2\",[1,\"cause\",\"c1\"]]", msg);
        }
    }
    
}
