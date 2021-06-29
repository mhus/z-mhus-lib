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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import de.mhus.lib.annotations.cmd.CmdArgument;
import de.mhus.lib.annotations.cmd.CmdOption;
import de.mhus.lib.core.MArgs;
import de.mhus.lib.tests.TestCase;

public class MArgsTest extends TestCase {

    @Test
    public void testHelp1() {
        String[] args = new String[] {"--help"};
        MArgs ma = new MArgs(args,
                MArgs.arg("1", false, "nr 1"),
                MArgs.arg("2", false, "nr 2")
//                MArgs.arg("3", true, "nr 3")
                );
        System.out.println(ma);
        ma.printUsage();
        assertTrue(ma.isPrintUsage());
        assertTrue(ma.isValid());
    }

    @Test
    public void testHelp2() {
        String[] args = new String[] {"--help"};
        MArgs ma = new MArgs(args,
                MArgs.arg("1", true, "nr 1"),
                MArgs.arg("2", false, "nr 2")
//                MArgs.arg("3", true, "nr 3")
                );
        System.out.println(ma);
        ma.printUsage();
        assertTrue(ma.isPrintUsage());
        assertFalse(ma.isValid());
    }
    
    @Test
    public void testHelp3() {
        String[] args = new String[] {};
        MArgs ma = new MArgs(args,
                MArgs.arg("1", true, "nr 1"),
                MArgs.arg("2", false, "nr 2")
//                MArgs.arg("3", true, "nr 3")
                );
        System.out.println(ma);
        ma.printUsage();
        assertTrue(ma.isPrintUsage());
        assertFalse(ma.isValid());
    }
   
    @Test
    public void testUnknownOptions() {
        String[] args = new String[] {"-a", "aa", "-b", "bb", "-c", "-d", "dd"};
        MArgs ma = new MArgs(args,
                MArgs.opt('a', null, 1, false, "opt a"),
                MArgs.opt('x', null, 1, false, "opt x is not used"),
                MArgs.allowOtherOptions()
                );
        System.out.println(ma);
        ma.printUsage();
        assertFalse(ma.isPrintUsage());
        assertTrue(ma.isValid());
        
        assertEquals(5, ma.getOptions().size());
        
        assertTrue(ma.hasOption("a"));
        assertEquals(1, ma.getOption("a").getValues().size());
        assertEquals(ma.getOption("a").getValue(), "aa");
        
        assertTrue(ma.hasOption("b"));
        assertEquals(1, ma.getOption("b").getValues().size());
        assertEquals(ma.getOption("b").getValue(), "bb");
        
        assertTrue(ma.hasOption("c"));
        assertEquals(0, ma.getOption("c").getValues().size());
        assertNull(ma.getOption("c").getValue());
        
        assertTrue(ma.hasOption("d"));
        assertEquals(1, ma.getOption("d").getValues().size());
        assertEquals(ma.getOption("d").getValue(), "dd");

        assertFalse(ma.hasOption("e"));
        assertEquals(0, ma.getOption("e").getValues().size());
        assertNull(ma.getOption("e").getValue());
        
        assertFalse(ma.hasOption("x"));
        assertEquals(0, ma.getOption("x").getValues().size());
        assertNull(ma.getOption("x").getValue());
        
    }

    @Test
    public void testPojoParsing() {
        String[] args = new String[] {"default1", "-a", "aa", "default2", "-a", "bb", "-b", "cc"};
        Container1 cont = new Container1();
    	MArgs ma = new MArgs(cont, args);
        System.out.println(ma);
        assertFalse(ma.isPrintUsage());
        assertTrue(ma.isValid());
    	assertEquals("default1", cont.arg1);
    	assertEquals("default2", cont.arg2);
    	assertEquals(2, cont.optA.length);
    	assertEquals("aa", cont.optA[0]);
    	assertEquals("bb", cont.optA[1]);
    	assertEquals("cc", cont.optB);

    }
    
    static class Container1 {
    	@CmdArgument(index = 1)
    	String arg1;
    	@CmdArgument(index = 2)
    	String arg2;
    	@CmdOption(shortcut = 'a', multi = true)
    	String[] optA;
    	@CmdOption(shortcut = 'b', value = true)
    	String optB;
    	
    }
     
	
    @Test
    public void testUsageMultiParsing1() {
        String[] args = new String[] {"default", "-a", "aa", "default", "-a", "bb", "-b", "cc"};
        MArgs ma = new MArgs(args,
        		MArgs.help("Do something"),
        		MArgs.opt('a', null, -1, false, "opt a"),
        		MArgs.opt('b', null, 1, false, "opt a"),
        		MArgs.arg("1", "arg 1"),
        		MArgs.arg("2", "arg 2")
        		);
        System.out.println(ma);
        assertTrue(ma.isValid());
        assertEquals(2, ma.getArguments().size());
        assertEquals(2, ma.getOptions().size());
        assertEquals(2, ma.getOption("a").getValues().size());
        assertEquals(1, ma.getOption("b").getValues().size());
    }

    @Test
    public void testUsageMultiParsing2() {
        String[] args = new String[] {"default", "-a", "aa", "default", "-a", "bb", "-b", "cc"};
        MArgs ma = new MArgs(args,
        		MArgs.help("Do something"),
        		MArgs.opt('a', null, -1, false, "opt a"),
        		MArgs.opt('b', null, 1, false, "opt a"),
        		MArgs.arg("1", true, "arg 1"),
        		MArgs.arg("2", true, "arg 2")
        		);
        System.out.println(ma);
        assertTrue(ma.isValid());
        assertEquals(2, ma.getArguments().size());
        assertEquals(2, ma.getOptions().size());
        assertEquals(2, ma.getOption("a").getValues().size());
        assertEquals(1, ma.getOption("b").getValues().size());
    }
    
    @Test
    public void testUsageMultiParsing3() {
        String[] args = new String[] {"default", "-a", "aa", "-a", "bb", "-b", "cc"};
        MArgs ma = new MArgs(args,
        		MArgs.help("Do something"),
        		MArgs.opt('a', null, -1, false, "opt a"),
        		MArgs.opt('b', null, 1, false, "opt a"),
        		MArgs.arg("1", true, "arg 1"),
        		MArgs.arg("2", true, "arg 2")
        		);
        System.out.println(ma);
        assertEquals(1, ma.getArguments().size());
        assertEquals(2, ma.getOptions().size());
        assertEquals(2, ma.getOption("a").getValues().size());
        assertEquals(1, ma.getOption("b").getValues().size());
        assertFalse(ma.isValid());
        
    }
    @Test
    public void testUsageMultiParsing4() {
        String[] args = new String[] {"default", "-a", "aa", "default", "-a", "bb", "-b", "cc", "default"};
        MArgs ma = new MArgs(args,
        		MArgs.help("Do something"),
        		MArgs.opt('a', null, -1, false, "opt a"),
        		MArgs.opt('b', null, 1, false, "opt a"),
        		MArgs.arg("1", true, "arg 1"),
        		MArgs.argAll("2", "arg 2")
        		);
        System.out.println(ma);
        assertTrue(ma.isValid());
        assertEquals(2, ma.getArguments().size());
        assertEquals(2, ma.getOptions().size());
        assertEquals(2, ma.getOption("a").getValues().size());
        assertEquals(1, ma.getOption("b").getValues().size());
    }

    @Test
    public void testUsageKeyParsing1() {
        String[] args = new String[] {"-a", "aa", "-a", "bb", "-a", "cc"};
        MArgs ma = new MArgs(args,
        		MArgs.opt('a', null, 3, false, "opt a")
        		);
        System.out.println(ma);
        assertTrue(ma.isValid());
        assertEquals(1, ma.getOptions().size());
        assertEquals(3, ma.getOption("a").getValues().size());
        assertEquals(ma.getOption("a").getValue(), "aa");
    }

    @Test
    public void testUsageKeyParsing2() {
        String[] args = new String[] {"-a", "aa", "-a", "bb", "-a", "cc"};
        MArgs ma = new MArgs(args,
        		MArgs.opt('a', null, 3, true, "opt a")
        		);
        System.out.println(ma);
        assertTrue(ma.isValid());
        assertEquals(1, ma.getOptions().size());
        assertEquals(3, ma.getOption("a").getValues().size());
        assertEquals(ma.getOption("a").getValue(), "aa");
    }
    
    @Test
    public void testUsageKeyParsing3() {
        String[] args = new String[] {"-a", "aa", "-a", "bb"};
        MArgs ma = new MArgs(args,
        		MArgs.opt('a', null, 3, false, "opt a")
        		);
        System.out.println(ma);
        assertTrue(ma.isValid());
        assertEquals(1, ma.getOptions().size());
        assertEquals(2, ma.getOption("a").getValues().size());
        assertEquals(ma.getOption("a").getValue(), "aa");
    }

    @Test
    public void testUsageKeyParsing4() {
        String[] args = new String[] {"-a", "aa", "-a", "bb"};
        MArgs ma = new MArgs(args,
        		MArgs.opt('a', null, 3, true, "opt a")
        		);
        System.out.println(ma);
        assertEquals(1, ma.getOptions().size());
        assertEquals(2, ma.getOption("a").getValues().size());
        assertEquals(ma.getOption("a").getValue(), "aa");
        assertFalse(ma.isValid());
    }

    @Test
    public void testUsageDefaultParsing() {
        String[] args = new String[] {"a", "b", "c"};
        MArgs ma = new MArgs(args,
        		MArgs.arg("1", "nr 1"),
        		MArgs.arg("2", "nr 2"),
        		MArgs.arg("3", "nr 3")
        		);
        System.out.println(ma);
        assertTrue(ma.isValid());
        assertEquals(3, ma.getArguments().size());
    }

    @Test
    public void testUsageFailDefaultParsing() {
        String[] args = new String[] {"a", "b"};
        MArgs ma = new MArgs(args,
        		MArgs.arg("1", true, "nr 1"),
        		MArgs.arg("2", true, "nr 2"),
        		MArgs.arg("3", true, "nr 3")
        		);
        System.out.println(ma);
        assertEquals(2, ma.getArguments().size());
        assertTrue(ma.isPrintUsage());
        assertFalse(ma.isValid());
    }

    @Test
    public void testDefaultParsing() {
        String[] args = new String[] {"a", "b", "c"};
        MArgs ma = new MArgs(args);
        System.out.println(ma);
        assertTrue(ma.isValid());
        assertEquals(3, ma.getArguments().size());
    }

    @Test
    public void testKeyParsing() {
        String[] args = new String[] {"-a", "aa", "-a", "bb", "-a", "cc"};
        MArgs ma = new MArgs(args);
        System.out.println(ma);
        assertTrue(ma.isValid());
        assertEquals(1, ma.getOptions().size());
        assertEquals(3, ma.getOption("a").getValues().size());
        assertEquals(ma.getOption("a").getValue(), "aa");
    }

    @Test
    public void testMultiParsing() {
        String[] args = new String[] {"default", "-a", "aa", "default", "-a", "bb", "-b", "cc"};
        MArgs ma = new MArgs(args);
        System.out.println(ma);
        assertEquals(2, ma.getArguments().size());
        assertEquals(2, ma.getOptions().size());
        assertEquals(2, ma.getOption("a").getValues().size());
        assertEquals(1, ma.getOption("b").getValues().size());
        assertTrue(ma.isValid());
    }

    @Test
    public void testOrder() {
        String[] args = new String[] {"-a", "zz", "-a", "bb", "-a", "aa"};
        MArgs ma = new MArgs(args);
        System.out.println(ma);
        assertTrue(ma.isValid());
        assertEquals(1, ma.getOptions().size());
        assertEquals(3, ma.getOption("a").getValues().size());
        assertEquals("zz", ma.getOption("a").getValues().get(0));
        assertEquals("bb", ma.getOption("a").getValues().get(1));
        assertEquals("aa", ma.getOption("a").getValues().get(2));
    }
}
