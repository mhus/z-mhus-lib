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
		
		TimeZone.setDefault(TimeZone.getTimeZone("CET")); 
		
		Date date = MCast.toDate("2020-12-01 +0100", null );
		assertNotNull(date);
		System.out.println( MCast.toString( date ) );
//		assertTrue( MCast.toString( date ).equals("2020-12-01_00:00:00.000 CET") );
		
		date = MCast.toDate("2020-12-01 13:20", null );
		assertNotNull(date);
		System.out.println( MCast.toString( date ) );
//		assertTrue( MCast.toString( date ).equals("2020-12-01_13:20:00.000 CET") );
		
		date = MCast.toDate("2020-12-01 13:20:10", null );
		assertNotNull(date);
		System.out.println( MCast.toString( date ) );
//		assertTrue( MCast.toString( date ).equals("2020-12-01_13:20:10.000 CET") );
		
		date = MCast.toDate("2020-12-01 13:20:10.223", null );
		assertNotNull(date);
		System.out.println( MCast.toString( date ) );
//		assertTrue( MCast.toString( date ).equals("2020-12-01_13:20:10.223 CET") );

		date = MCast.toDate("01.12.2020 13:20:10.223", null );
		assertNotNull(date);
		System.out.println( MCast.toString( date ) );
//		assertTrue( MCast.toString( date ).equals("2020-12-01_13:20:10.223 CET") );
		
		date = MCast.toDate("12/01/2020 13:20:10.223", null );
		assertNotNull(date);
		System.out.println( MCast.toString( date ) );
//		assertTrue( MCast.toString( date ).equals("2020-12-01_13:20:10.223 CET") );

		
		
		
		date = MCast.toDate("0020-12-01 13:20:10", null );
		assertNotNull(date);
		System.out.println( MCast.toString( date ) );
//		assertTrue( MCast.toString( date ).equals("0020-12-01_13:20:10.000 CET") );
		
		date = MCast.toDate("20-12-01 13:20:10", null );
		assertNotNull(date);
		System.out.println( MCast.toString( date ) );
//		assertTrue( MCast.toString( date ).equals("2020-12-01_13:20:10.000 CET") );
		
		date = MCast.toDate("12.2020 13:20:10", null );
//TODO		assertNotNull(date);
		System.out.println( MCast.toString( date ) );
//		assertTrue( MCast.toString( date ).equals("2020-12-01_13:20:10.000 CET") );
		
		date = MCast.toDate("12/2020 13:20:10.223", null );
//TODO		assertNotNull(date);
		System.out.println( MCast.toString( date ) );
//		assertTrue( MCast.toString( date ).equals("2020-12-01_13:20:10.223 CET") );

		
		
		date = MCast.toDate("2020-12-01_13:20:10", null );
		System.out.println( MCast.toString( date ) );
//		assertTrue( MCast.toString( date ).equals("2020-12-01_13:20:10.000 CET") );

		
		date = MCast.toDate("2020-12-01 13:20:10 UTC", null );
		assertNotNull(date);
		Calendar c = Calendar.getInstance();
		int t = ( 13*60*60*1000 + c.getTimeZone().getRawOffset() ) / 1000 / 60 / 60;
		System.out.println( MCast.toString( date ) + " Hour: " + t );
//		assertTrue( MCast.toString( date ).equals("2020-12-01_" + t + ":20:10.000 CET") );

		
		
		
		date = MCast.toDate("2020-12-01 13:20:10", null );
		assertNotNull(date);
		System.out.println( MDate.toIsoDate(date) );
		assertTrue( MDate.toIsoDate(date).equals( "2020-12-01" ) );
		System.out.println( MDate.toIsoDateTime(date) );
		assertTrue( MDate.toIsoDateTime(date).equals( "2020-12-01 13:20:10" ) );
		
		date = MCast.toDate("1722-12-01 +0100", null );
		assertNotNull(date);
		System.out.println( MCast.toString( date ) );
//		assertTrue( MCast.toString( date ).equals("1722-12-01_00:00:00.000 CET") );

		date = MCast.toDate("27.07.10", null );
		assertNotNull(date);
		System.out.println( MCast.toString( date ) );
//		assertTrue( MCast.toString( date ).equals("2010-07-27_00:00:00.000 CEST") );
		
		date = MCast.toDate("2016-08-31T15:38:27", null);
//TODO		assertNotNull(date);
		System.out.println( MCast.toString( date ) );
		
		date = MCast.toDate("1968-12-13T00:00:00Z", null);
		assertNotNull(date);
		System.out.println( MCast.toString( date ) );
		
		System.out.println();
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
