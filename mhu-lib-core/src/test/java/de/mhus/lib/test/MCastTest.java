package de.mhus.lib.test;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

import junit.framework.TestCase;
import de.mhus.lib.core.MCast;
import de.mhus.lib.core.MDate;

public class MCastTest extends TestCase {

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
		System.out.println( MCast.toString( date ) );
//		assertTrue( MCast.toString( date ).equals("2020-12-01_00:00:00.000 CET") );
		
		date = MCast.toDate("2020-12-01 13:20", null );
		System.out.println( MCast.toString( date ) );
//		assertTrue( MCast.toString( date ).equals("2020-12-01_13:20:00.000 CET") );
		
		date = MCast.toDate("2020-12-01 13:20:10", null );
		System.out.println( MCast.toString( date ) );
//		assertTrue( MCast.toString( date ).equals("2020-12-01_13:20:10.000 CET") );
		
		date = MCast.toDate("2020-12-01 13:20:10.223", null );
		System.out.println( MCast.toString( date ) );
//		assertTrue( MCast.toString( date ).equals("2020-12-01_13:20:10.223 CET") );

		date = MCast.toDate("01.12.2020 13:20:10.223", null );
		System.out.println( MCast.toString( date ) );
//		assertTrue( MCast.toString( date ).equals("2020-12-01_13:20:10.223 CET") );
		
		date = MCast.toDate("12/01/2020 13:20:10.223", null );
		System.out.println( MCast.toString( date ) );
//		assertTrue( MCast.toString( date ).equals("2020-12-01_13:20:10.223 CET") );

		
		
		
		date = MCast.toDate("0020-12-01 13:20:10", null );
		System.out.println( MCast.toString( date ) );
//		assertTrue( MCast.toString( date ).equals("0020-12-01_13:20:10.000 CET") );
		
		date = MCast.toDate("20-12-01 13:20:10", null );
		System.out.println( MCast.toString( date ) );
//		assertTrue( MCast.toString( date ).equals("2020-12-01_13:20:10.000 CET") );
		
		date = MCast.toDate("12.2020 13:20:10", null );
		System.out.println( MCast.toString( date ) );
//		assertTrue( MCast.toString( date ).equals("2020-12-01_13:20:10.000 CET") );
		
		date = MCast.toDate("12/2020 13:20:10.223", null );
		System.out.println( MCast.toString( date ) );
//		assertTrue( MCast.toString( date ).equals("2020-12-01_13:20:10.223 CET") );

		
		
		date = MCast.toDate("2020-12-01_13:20:10", null );
		System.out.println( MCast.toString( date ) );
//		assertTrue( MCast.toString( date ).equals("2020-12-01_13:20:10.000 CET") );

		
		date = MCast.toDate("2020-12-01 13:20:10 UTC", null );
		Calendar c = Calendar.getInstance();
		int t = ( 13*60*60*1000 + c.getTimeZone().getRawOffset() ) / 1000 / 60 / 60;
		System.out.println( MCast.toString( date ) + " Hour: " + t );
//		assertTrue( MCast.toString( date ).equals("2020-12-01_" + t + ":20:10.000 CET") );

		
		
		
		date = MCast.toDate("2020-12-01 13:20:10", null );
		System.out.println( MDate.toIsoDate(date) );
		assertTrue( MDate.toIsoDate(date).equals( "2020-12-01" ) );
		System.out.println( MDate.toIsoDateTime(date) );
		assertTrue( MDate.toIsoDateTime(date).equals( "2020-12-01 13:20:10" ) );
		
		date = MCast.toDate("1722-12-01 +0100", null );
		System.out.println( MCast.toString( date ) );
//		assertTrue( MCast.toString( date ).equals("1722-12-01_00:00:00.000 CET") );

		date = MCast.toDate("27.07.10", null );
		System.out.println( MCast.toString( date ) );
//		assertTrue( MCast.toString( date ).equals("2010-07-27_00:00:00.000 CEST") );
		
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
		Date date = MDate.toDate("1.2.2003 04:05:00", null, Locale.GERMANY);
		System.out.println(date);
		{
			String str = MDate.toLocaleDateTime(date, Locale.GERMANY);
			System.out.println(str);
			Date ret = MDate.toDate(str, null, Locale.GERMANY);
			System.out.println(ret);
			assertEquals(date, ret);
		}
		{
			String str = MDate.toLocaleDateTime(date, Locale.UK);
			System.out.println(str);
			Date ret = MDate.toDate(str, null, Locale.UK);
			System.out.println(ret);
			assertEquals(date, ret);
		}
		{
			String str = MDate.toLocaleDateTime(date, Locale.US);
			System.out.println(str);
			Date ret = MDate.toDate(str, null, Locale.US);
			System.out.println(ret);
			assertEquals(date, ret);
		}
		
	}
}
