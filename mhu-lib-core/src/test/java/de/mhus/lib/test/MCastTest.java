/**
 * Copyright 2018 Mike Hummel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.test;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.MDate;
import junit.framework.TestCase;

public class MCastTest extends TestCase {

	@Override
	protected void setUp() throws Exception {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}
	
	public void testCurrencyString() {
		
		String out = null;
		
		out = MCast.toCurrencyString( 10.2 );
		// System.out.println(out);
		assertTrue("10,20".equals(out));
		
		out = MCast.toCurrencyString( 10.202 );
		assertTrue("10,20".equals(out));
		
		out = MCast.toCurrencyString( 10.206 );
		assertTrue("10,21".equals(out));
		
		out = MCast.toCurrencyString( -10.20 );
		assertTrue("-10,20".equals(out));

	}

	public void testToDate() {
		
		TimeZone.setDefault(TimeZone.getTimeZone("GMT")); 
		Locale defaultLocale = Locale.getDefault();
		Locale.setDefault(Locale.GERMANY);
		
		System.out.print("1: ");
		Date date = MCast.toDate("2020-12-01 +0100", null );
		assertNotNull(date);
		System.out.println( MDate.toIso8601( date ) );
		assertTrue( MDate.toIso8601( date ).equals("2020-12-01T00:00:00") );
		
		System.out.print("2: ");
		date = MCast.toDate("2020-12-01 13:20", null );
		assertNotNull(date);
		System.out.println( MDate.toIso8601( date ) );
		assertTrue( MDate.toIso8601( date ).equals("2020-12-01T13:20:00") );
		
		System.out.print("3: ");
		date = MCast.toDate("2020-12-01 13:20:10", null );
		assertNotNull(date);
		System.out.println( MDate.toIso8601( date ) );
		assertTrue( MDate.toIso8601( date ).equals("2020-12-01T13:20:10") );
		
		System.out.print("4: ");
		date = MCast.toDate("2020-12-01 13:20:10.223", null );
		assertNotNull(date);
		System.out.println( MDate.toIso8601( date ) );
		assertTrue( MDate.toIso8601( date ).equals("2020-12-01T13:20:10") );

		System.out.print("5: ");
		date = MCast.toDate("01.12.2020 13:20:10.223", null );
		assertNotNull(date);
		System.out.println( MDate.toIso8601( date ) );
		assertTrue( MDate.toIso8601( date ).equals("2020-12-01T13:20:10") );
		
		Locale.setDefault(Locale.GERMANY);
		System.out.print("6.1: ");
		date = MCast.toDate("12/01/2020 13:20:10.223", null );
		assertNotNull(date);
		System.out.println( MDate.toIso8601( date ) );
		assertTrue( MDate.toIso8601( date ).equals("2020-01-12T13:20:10") );
		
		Locale.setDefault(Locale.US);
		System.out.print("6.2: ");
		date = MCast.toDate("12/01/2020 13:20:10.223", null );
		assertNotNull(date);
		System.out.println( MDate.toIso8601( date ) );
		assertTrue( MDate.toIso8601( date ).equals("2020-12-01T13:20:10") );

		Locale.setDefault(Locale.GERMANY);
		
		System.out.print("7: ");
		date = MCast.toDate("0020-12-01 13:20:10", null );
		assertNotNull(date);
		System.out.println( MDate.toIso8601( date ) );
		assertTrue( MDate.toIso8601( date ).equals("0020-12-01T13:20:10") );
		
		System.out.print("8: ");
		date = MCast.toDate("20-12-01 13:20:10", null );
		assertNotNull(date);
		System.out.println( MDate.toIso8601( date ) );
		assertTrue( MDate.toIso8601( date ).equals("2020-12-01T13:20:10") );
		
		System.out.print("9: ");
		date = MCast.toDate("12.2020 13:20:10", null );
		assertNotNull(date);
		System.out.println( MDate.toIso8601( date ) );
		assertTrue( MDate.toIso8601( date ).equals("2020-12-01T13:20:10") );
		
		System.out.print("10: ");
		date = MCast.toDate("12/2020 13:20:10.223", null );
		assertNotNull(date);
		System.out.println( MDate.toIso8601( date ) );
		assertTrue( MDate.toIso8601( date ).equals("2020-12-01T13:20:10") );
		
		System.out.print("11: ");
		date = MCast.toDate("2020-12-01_13:20:10", null );
		System.out.println( MDate.toIso8601( date ) );
		assertTrue( MDate.toIso8601( date ).equals("2020-12-01T13:20:10") );
		
		System.out.print("12: ");
		date = MCast.toDate("2020-12-01 13:20:10 +3:00", null );
		assertNotNull(date);
		Calendar c = Calendar.getInstance();
		int t = ( 13*60*60*1000 + c.getTimeZone().getRawOffset() ) / 1000 / 60 / 60;
		System.out.println( MDate.toIso8601( date ) );
		System.out.println( "   Hour: " + t );
		assertTrue( MDate.toIso8601( date ).equals("2020-12-01T10:20:10") ); // 13:00 in +3 means 10:00 here in GMT
		
		System.out.print("13: ");
		date = MCast.toDate("2020-12-01 13:20:10", null );
		assertNotNull(date);
		System.out.println( MDate.toIso8601( date ) );
		assertTrue( MDate.toIsoDate(date).equals( "2020-12-01" ) );
		assertTrue( MDate.toIso8601(date).equals( "2020-12-01T13:20:10" ) );
		
		System.out.print("14: ");
		date = MCast.toDate("1722-12-01 +0100", null );
		assertNotNull(date);
		System.out.println( MDate.toIso8601( date ) );
		assertTrue( MDate.toIso8601( date ).equals("1722-12-01T00:00:00") );

		System.out.print("15: ");
		date = MCast.toDate("27.07.10", null );
		assertNotNull(date);
		System.out.println( MDate.toIso8601( date ) );
		assertTrue( MDate.toIso8601( date ).equals("2010-07-27T00:00:00") );
		
		System.out.print("16: ");
		date = MCast.toDate("2016-08-31T15:38:27", null);
		assertNotNull(date);
		System.out.println( MDate.toIso8601( date ) );
		assertTrue( MDate.toIso8601( date ).equals("2016-08-31T15:38:27") );
		
		System.out.print("17: ");
		date = MCast.toDate("1968-12-13T00:00:00Z", null);
		assertNotNull(date);
		System.out.println( MDate.toIso8601( date ) );
		assertTrue( MDate.toIso8601( date ).equals("1968-12-13T00:00:00") );
		
		System.out.print("18: ");
		date = MCast.toDate("Jan 1, 2000 1:00 am", null);
		assertNotNull(date);
		System.out.println( MDate.toIso8601( date ) );
		assertTrue( MDate.toIso8601( date ).equals("2000-01-01T01:00:00") );
		
		System.out.print("19: ");
		date = MCast.toDate("Jan 1, 2000 1:00 pm", null);
		assertNotNull(date);
		System.out.println( MDate.toIso8601( date ) );
		assertTrue( MDate.toIso8601( date ).equals("2000-01-01T13:00:00") );
		
		System.out.print("20: ");
		date = MCast.toDate("Jan 1, 2000 12:10 pm", null);
		assertNotNull(date);
		System.out.println( MDate.toIso8601( date ) );
		assertTrue( MDate.toIso8601( date ).equals("2000-01-01T12:10:00") );
		
		System.out.print("21: ");
		date = MCast.toDate("Jan 1, 2000 12:10 am", null);
		assertNotNull(date);
		System.out.println( MDate.toIso8601( date ) );
		assertTrue( MDate.toIso8601( date ).equals("2000-01-01T00:10:00") );
		
		System.out.println();
		
		TimeZone.setDefault(null); 
		Locale.setDefault(defaultLocale);

	}
	
	
	public void testBinary() throws UnsupportedEncodingException {
		
		byte[] b = new byte[256];
		for ( int i = -127; i < 128; i++ ) 
			b[i+127] = (byte)i;
		String str = MCast.toBinaryString(b);
		System.out.println( str );
		b = MCast.fromBinaryString(str);
		for ( int i = -127; i < 128; i++ )
			assertTrue( b[i+127] == (byte)i);
		
	}
	
	public void testGenericTypeCast() {
		boolean b = (boolean) MCast.toType("true", boolean.class, null);
		assertTrue(b);
		
		b = (boolean) MCast.toType("true", Boolean.class, null);
		assertTrue(b);

		UUID uo = UUID.randomUUID();
		UUID ud = (UUID)MCast.toType(uo.toString(), UUID.class, null);
		assertEquals(uo, ud);
		
		ud = (UUID)MCast.toType("a", UUID.class, null);
		assertNull(ud);
		
	}
	
	public void testLocalFormating() {

		System.out.println(TimeZone.getDefault());
		Date date = MDate.toDate("1.2.2003 04:05:00", null, Locale.GERMANY);
		System.out.println(date);
		{
			String str = MDate.toDateTimeString(date, Locale.GERMANY);
			System.out.println(str);
			Date ret = MDate.toDate(str, null, Locale.GERMANY);
			System.out.println(ret);
			assertEquals(date, ret);
		}
		{
			String str = MDate.toDateTimeString(date, Locale.UK);
			System.out.println(str);
			Date ret = MDate.toDate(str, null, Locale.UK);
			System.out.println(ret);
			assertEquals(date, ret);
		}
		{
			String str = MDate.toDateTimeString(date, Locale.US);
			System.out.println(str);
			Date ret = MDate.toDate(str, null, Locale.US);
			System.out.println(ret);
			assertEquals(date, ret);
		}
		
	}
	
	public void testDateTransform() {
		String str = MDate.transform("dd.MM.yyyy", "2016-07-20", null );
//		String str = new java.text.SimpleDateFormat("dd.MM.yyyy").format( de.mhus.lib.core.MDate.toDate("2016-07-20", null) );
		System.out.println(str);
		assertEquals("20.07.2016", str);
	}
	
	public void testDouble() {
		Locale def = Locale.getDefault();
		Locale.setDefault(Locale.GERMANY);
		assertEquals(10.01, MCast.todouble("10.01", 0));
		Locale.setDefault(Locale.US);
		assertEquals(10.01, MCast.todouble("10.01", 0));
		Locale.setDefault(Locale.FRANCE);
		assertEquals(10.01, MCast.todouble("10.01", 0));
		Locale.setDefault(def);
		assertEquals(10.01, MCast.todouble("10.01", 0));

		assertEquals(4294967295.01, MCast.todoubleUS("4,294,967,295.01", 0));
		assertEquals(4294967295.01, MCast.todoubleUS("4 294 967 295.01", 0));
		assertEquals(4294967295.01, MCast.todoubleEuropean("4 294 967.295,01", 0));
		assertEquals(4294967295.01, MCast.todoubleEuropean("4.294.967.295,01", 0));
		
	}
	
	public void testLongToBytes() {
		System.out.println(">>> testLongToBytes");
		{
			long org = 0;
			byte[] b = MCast.longToBytes(org);
			long copy = MCast.bytesToLong(b);
			assertEquals(org, copy);
		}
		{
			long org = 1;
			byte[] b = MCast.longToBytes(org);
			long copy = MCast.bytesToLong(b);
			assertEquals(org, copy);
		}
		{
			long org = Long.MIN_VALUE;
			byte[] b = MCast.longToBytes(org);
			long copy = MCast.bytesToLong(b);
			assertEquals(org, copy);
		}
		{
			long org = Long.MAX_VALUE;
			byte[] b = MCast.longToBytes(org);
			long copy = MCast.bytesToLong(b);
			assertEquals(org, copy);
		}
	}

	public void testDateToString() {
		{
			Date date = MDate.toDate("01.02.2003", null);
			String res = MDate.toIso8601(date);
			System.out.println(res);
		}
		{
			Calendar date = Calendar.getInstance();
			date.setTime(MDate.toDate("01.02.2003", null));
			String res = MDate.toIso8601(date);
			System.out.println(res);
		}
	}
}
