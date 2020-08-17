/**
 * Copyright (C) 2020 Mike Hummel (mh@mhus.de)
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
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Test;

import de.mhus.lib.core.MCollection;
import de.mhus.lib.tests.TestCase;

public class MCollectionTest extends TestCase {

    @Test
    public void testArrayManipulation() {
        {
            String[] array = new String[] {"a", "b", "c"};
            array = MCollection.append(array, "d");
            equals(new String[] {"a", "b", "c", "d"}, array);
        }
        {
            String[] array = new String[] {};
            array = MCollection.append(array, "d");
            equals(new String[] {"d"}, array);
        }
        // insert
        {
            String[] array = new String[] {"a", "b", "c"};
            array = MCollection.insert(array, 0, "d");
            equals(new String[] {"d", "a", "b", "c"}, array);
        }
        {
            String[] array = new String[] {"a", "b", "c"};
            array = MCollection.insert(array, 3, "d");
            equals(new String[] {"a", "b", "c", "d"}, array);
        }
        {
            String[] array = new String[] {"a", "b", "c"};
            array = MCollection.insert(array, 1, "d");
            equals(new String[] {"a", "d", "b", "c"}, array);
        }
        // remove
        {
            String[] array = new String[] {"a", "b", "c"};
            array = MCollection.remove(array, 0, 1);
            equals(new String[] {"b", "c"}, array);
        }
        {
            String[] array = new String[] {"a", "b", "c"};
            array = MCollection.remove(array, 0, 3);
            equals(new String[] {}, array);
        }
        {
            String[] array = new String[] {"a", "b", "c"};
            array = MCollection.remove(array, 1, 1);
            equals(new String[] {"a", "c"}, array);
        }
        {
            String[] array = new String[] {"a", "b", "c"};
            array = MCollection.remove(array, 2, 1);
            equals(new String[] {"a", "b"}, array);
        }
    }

    private void equals(String[] expected, String[] actual) {
        if (expected.length != actual.length) {
            System.err.println("Expected: " + Arrays.toString(expected));
            System.err.println("Actual  : " + Arrays.toString(actual));
            System.err.println("Not the same size: " + expected.length + " != " + actual.length);
            fail();
        }
        for (int i = 0; i < expected.length; i++)
            if (!expected[i].equals(actual[i])) {
                System.err.println("Expected: " + Arrays.toString(expected));
                System.err.println("Actual  : " + Arrays.toString(actual));
                System.err.println("Different at index " + i);
                fail();
            }
    }

    @Test
    public void testListSort() {
        LinkedList<String> l = new LinkedList<>();
        l.add("z");
        l.add("a");
        l.add("m");
        List<String> s = MCollection.sorted(l);
        assertEquals("a", s.get(0));
        assertEquals("m", s.get(1));
        assertEquals("z", s.get(2));
    }

    @Test
    public void testStringList() {
        String list = null;
        char S = ',';
        list = MCollection.append(list, S, "a");
        assertEquals("a", list);
        list = MCollection.append(list, S, "b");
        assertEquals("a,b", list);
        list = MCollection.append(list, S, "c");
        assertEquals("a,b,c", list);

        list = MCollection.set(list, S, "a");
        assertEquals("a,b,c", list);
        list = MCollection.set(list, S, "b");
        assertEquals("a,b,c", list);
        list = MCollection.set(list, S, "c");
        assertEquals("a,b,c", list);

        list = MCollection.remove(list, S, "b");
        assertEquals("a,c", list);
        list = MCollection.remove(list, S, "a");
        assertEquals("c", list);
        list = MCollection.remove(list, S, "c");
        assertEquals("", list);
    }
}
