package de.mhus.lib.test;

import junit.framework.TestCase;

import java.util.HashMap;

import de.mhus.lib.core.matcher.Condition;
import de.mhus.lib.core.matcher.Matcher;
import de.mhus.lib.errors.MException;

public class MatcherTest extends TestCase {

	public void testSimple() throws MException {
		{
			Matcher filter = new Matcher(".*aaa.*");
			System.out.println(filter);
			assertEquals(true, filter.matches("aaa"));
			assertEquals(true, filter.matches("blablaaabla"));
			assertEquals(false, filter.matches("xyz"));
		}
		
		{
			Matcher filter = new Matcher(".*aaa.* or .*bbb.*");
			System.out.println(filter);
			assertEquals(true, filter.matches("aaa"));
			assertEquals(true, filter.matches("blablaaabla"));
			assertEquals(true, filter.matches("bbb"));
			assertEquals(true, filter.matches("blablabbbla"));
			assertEquals(false, filter.matches("xyz"));
		}

		{
			Matcher filter = new Matcher(".*aaa.* and .*bbb.*");
			System.out.println(filter);
			assertEquals(false, filter.matches("aaa"));
			assertEquals(false, filter.matches("blablaaabla"));
			assertEquals(false, filter.matches("bbb"));
			assertEquals(false, filter.matches("blablabbbla"));
			assertEquals(false, filter.matches("xyz"));
			assertEquals(true, filter.matches("blablaaabbbla"));
		}

		{
			Matcher filter = new Matcher(".*aaa.* and not .*bbb.*");
			System.out.println(filter);
			assertEquals(true, filter.matches("aaa"));
			assertEquals(true, filter.matches("blablaaabla"));
			assertEquals(false, filter.matches("bbb"));
			assertEquals(false, filter.matches("blablabbbla"));
			assertEquals(false, filter.matches("xyz"));
			assertEquals(false, filter.matches("blablaaabbbla"));
		}
		
		{
			Matcher filter = new Matcher("not .*aaa.* and .*bbb.*");
			System.out.println(filter);
			assertEquals(false, filter.matches("aaa"));
			assertEquals(false, filter.matches("blablaaabla"));
			assertEquals(true, filter.matches("bbb"));
			assertEquals(true, filter.matches("blablabbbla"));
			assertEquals(false, filter.matches("xyz"));
			assertEquals(false, filter.matches("blablaaabbbla"));
		}
		
		{
			Matcher filter = new Matcher("not .*aaa.* and not .*bbb.*");
			System.out.println(filter);
			assertEquals(false, filter.matches("aaa"));
			assertEquals(false, filter.matches("blablaaabla"));
			assertEquals(false, filter.matches("bbb"));
			assertEquals(false, filter.matches("blablabbbla"));
			assertEquals(true, filter.matches("xyz"));
			assertEquals(false, filter.matches("blablaaabbbla"));
		}

	}

	public void testBrackets() throws MException {
		{
			Matcher filter = new Matcher("not (.*aaa.* or .*bbb.*)");
			System.out.println(filter);
			assertEquals(false, filter.matches("aaa"));
			assertEquals(false, filter.matches("blablaaabla"));
			assertEquals(false, filter.matches("bbb"));
			assertEquals(false, filter.matches("blablabbbla"));
			assertEquals(true, filter.matches("xyz"));
			assertEquals(false, filter.matches("blablaaabbbla"));
		}
		
		{
			Matcher filter = new Matcher("not (.*aaa.* and .*bbb.*)");
			System.out.println(filter);
			assertEquals(true, filter.matches("aaa"));
			assertEquals(true, filter.matches("blablaaabla"));
			assertEquals(true, filter.matches("bbb"));
			assertEquals(true, filter.matches("blablabbbla"));
			assertEquals(true, filter.matches("xyz"));
			assertEquals(false, filter.matches("blablaaabbbla"));
		}
		{
			Matcher filter = new Matcher(".*xyz.* or (.*aaa.* and .*bbb.*)");
			System.out.println(filter);
			assertEquals(false, filter.matches("aaa"));
			assertEquals(false, filter.matches("blablaaabla"));
			assertEquals(false, filter.matches("bbb"));
			assertEquals(false, filter.matches("blablabbbla"));
			assertEquals(true, filter.matches("xyz"));
			assertEquals(true, filter.matches("blablaaabbbla"));
		}
	}
	
	public void testPatterns() throws MException {
		{
			Matcher filter = new Matcher("fs *aaa*");
			System.out.println(filter);
			assertEquals(true, filter.matches("aaa"));
			assertEquals(true, filter.matches("blablaaabla"));
			assertEquals(false, filter.matches("xyz"));
		}
		{
			Matcher filter = new Matcher("sql %aaa%");
			System.out.println(filter);
			assertEquals(true, filter.matches("aaa"));
			assertEquals(true, filter.matches("blablaaabla"));
			assertEquals(false, filter.matches("xyz"));
		}

	}
	
	public void testCondition() throws MException {
		HashMap<String, Object> val = new HashMap<String,Object>();
		val.put("param1", "aloa");
		val.put("param2", "nix");
		
		{
			Condition cond = new Condition("$param1 aloa");
			System.out.println(cond);
			assertEquals(true, cond.matches(val));
		}
		{
			Condition cond = new Condition("$param1 aloax");
			System.out.println(cond);
			assertEquals(false, cond.matches(val));
		}
		{
			Condition cond = new Condition("($param1 bla or $param1 aloa)");
			System.out.println(cond);
			assertEquals(true, cond.matches(val));
		}
		{
			Condition cond = new Condition("($param1 bla or $param1 blub)");
			System.out.println(cond);
			assertEquals(false, cond.matches(val));
		}
		{
			Condition cond = new Condition("($param1 aloa and $param2 nix)");
			System.out.println(cond);
			assertEquals(true, cond.matches(val));
		}
		
		
	}
	
}
