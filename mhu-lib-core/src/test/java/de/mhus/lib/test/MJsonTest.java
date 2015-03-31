package de.mhus.lib.test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import de.mhus.lib.core.MJson;
import junit.framework.TestCase;

public class MJsonTest extends TestCase {

	public void testTransformations() {
		{
			JsonNode j = MJson.pojoToJson( true );
			Object v = MJson.jsonToPojo( j );
			System.out.println(j);
			assertEquals(true, (boolean)v);
		}

		{
			JsonNode j = MJson.pojoToJson( 1 );
			Object v = MJson.jsonToPojo( j );
			System.out.println(j);
			assertEquals(1, (int)v);
		}

		{
			JsonNode j = MJson.pojoToJson( "aloa" );
			Object v = MJson.jsonToPojo( j );
			System.out.println(j);
			assertEquals("aloa", (String)v);
		}

		{
			JsonNode j = MJson.pojoToJson( null );
			Object v = MJson.jsonToPojo( j );
			System.out.println(j);
			assertNull(v);
		}
		
		{
			JsonNode j = MJson.pojoToJson( (byte)1 );
			Object v = MJson.jsonToPojo( j );
			System.out.println(j);
			assertEquals(1, (byte)v);
		}
		
		{
			JsonNode j = MJson.pojoToJson( (double)1 );
			Object v = MJson.jsonToPojo( j );
			System.out.println(j);
			assertEquals(1.0, (double)v);
		}
		
		{
			Map<String,Object> m = new HashMap<>();
			m.put("a", "b");
			JsonNode j = MJson.pojoToJson( m );
			Object v = MJson.jsonToPojo( j );
			assertEquals(m.getClass(), v.getClass());
			System.out.println(j);
			assertEquals(m.toString(), v.toString());
			
		}

		{
			Map<String,Object> m = new TreeMap<>();
			m.put("a", "b");
			m.put("b", 1);
			m.put("c", true);
			m.put("d", new MJsonObject("a"));
			JsonNode j = MJson.pojoToJson( m );
			Object v = MJson.jsonToPojo( j );
			assertEquals(m.getClass(), v.getClass());
			System.out.println(m);
			System.out.println(v);
			System.out.println(j);
			assertEquals(m.toString(), v.toString());
			
		}
		
		{
			Object u = new byte[] {0,1,2};
			JsonNode j = MJson.pojoToJson( u );
			System.out.println(j);
			Object v = MJson.jsonToPojo( j );
			System.out.println(Arrays.toString((byte[])u));
			System.out.println(Arrays.toString((byte[])v));
			assertEquals(Arrays.toString((byte[])u), Arrays.toString((byte[])v));
		}

		
	}
	
		
}
