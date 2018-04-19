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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;

import de.mhus.lib.core.MBigMath;
import de.mhus.lib.core.MFile;
import de.mhus.lib.core.MMath;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.crypt.AsyncKey;
import de.mhus.lib.core.crypt.CipherBlockAdd;
import de.mhus.lib.core.crypt.CipherBlockRotate;
import de.mhus.lib.core.crypt.CipherInputStream;
import de.mhus.lib.core.crypt.CipherOutputStream;
import de.mhus.lib.core.crypt.MCrypt;
import junit.framework.TestCase;

public class CryptTest extends TestCase {

	final static String key2048 =
			"-----BEGIN RSA PRIVATE KEY-----\n"+
			"MIIEowIBAAKCAQEAoUyyHPciq+UhB8CEb/1YIeO7/hmbQL3kxaxHRFWZzZVjsD09\n"+
			"KPqwHwHJ9xpBGg7K+4K8nuEVR2SrCO8KNNqhqqLkIjO1v9kn65grflfeP0MdRmZu\n"+
			"58FpXiurb0yapwJVqCynTnXK6yUmgAMWRC3SvXnChsr0U8lLfkvsQ3cTAhrR0z1L\n"+
			"/R02d2geZnBj+mu1fMomjVccEbHnmOL2+/PkkhtUkQiClKPz63w7xd9fvF8cFnhV\n"+
			"E3FqUHEu6J2G2cnhN86C8U4eU1kb0eWCnSzzGVLtUsIf1tlt5TaNyWDy5RdYw49s\n"+
			"kqoqKvmXEhcc65oBphDLgFg1FfypqO1ojUXhBQIDAQABAoIBAAUDckHOOKipHYa1\n"+
			"KCim8jdTccNrHlU70cGHIkvwcTBfpVKUBLOiXxkHoDRq/30E2rBIlv5FNrkaWuqT\n"+
			"K3kLFp1MJNUfUFXfNQtwlmF97619s4o9otLXQyQnLVPvSJtKSklI4gZhSOZYKEMw\n"+
			"VV/XIMa84xv3cPKtvgf16ikKqW+WQy97IKGMLhbrEhbKDdmY5mcvpLQ0OM2Btmjp\n"+
			"rgs5SEavWoNrIzHqgQ6xNB13oJxPtDCfWo1GkHFpsGSp5jzvsQQHZMVMGxera6+l\n"+
			"kq5EhK2XA3r7zf3b9dGCYUNqU4HNd2WU3AbTOi1omaPkKITeJ78U2wmc/QDb3S3c\n"+
			"dYq7fF0CgYEAy6pINWueYqES4OBq4fv+g6D9zVD9eEE05z1yj7Bay+Fa/kvx5E4m\n"+
			"Y8nWJ/EDOBBOCJRq1Ebshr8UEuJIDbR8D0E8N63+L0nOqLppj6L/eEtajXVlqf5J\n"+
			"pb0+Oo/jW6sn4m4guvrPLn36r1bzXiFp9wKZjUVIqlGwIiz8XxhyjgcCgYEAyr96\n"+
			"CcpTAzRRV+7RBV446fYvJJjbNcHYYKvcHMg6BfgOOwbsUHnNvzwiJJZujntkFZp9\n"+
			"tYDZIGW85fCuVnrlxGzn52PgqZE0kZ9OaeLG74sGMJx6H2x8uEMXRQyM7s48OYBB\n"+
			"X9GCIWyNah6zFsJPomyYiDHB7E9P6u14tx16VZMCgYEAwJiD9niR6+U0bBHtIU1i\n"+
			"7ukUec+IEute8vnp1zXHdxviJ657zhGVPjKFYXoKOD86++QWbi2vyPDzM7RmvQcb\n"+
			"dnWTU3gncmKSmn7GCn3yprhjpngJLst4q9IdAdZGA88ERZ0tOISr3eRmZt+L/00L\n"+
			"3vnHaY/GWsIrFPaDpg4BbosCgYAz2wlhm6fjt+veK6y2TMUNwfOIzreyZiPrhclE\n"+
			"a0m74RfyrPCgHKcs9DpfVUJtms2cYOkqFQxzptHLleVhJQnDVX9yxS7e786cODyc\n"+
			"BG6RMeOhZ0Qs6Vh04GQBOxaItaLdqhoOYc2AsvzwWW3Asm4fwtq4atGImTh9g8NO\n"+
			"QnHZlQKBgGcp17bbPD43khFDRvcyHwtguMrKdGxMav+c+jEUtVjsY6meb+EWnUwM\n"+
			"VU2hmZcHNJacp5XQd/GyWqqGkzm9kMV4z5hfiIElhgRg2h3fkpUoVoytNlL4p5kU\n"+
			"btvEfCueGxAFk2OtpGn5akKIpITtF/lVxbTN0yFqhEuaL+pSubW5\n"+
			"-----END RSA PRIVATE KEY-----";
	
	final static String key256 =
			"-----BEGIN RSA PRIVATE KEY-----\n"+
			"MIGtAgEAAiEAxVq56rE81vq5AdHUW1A080fbJ9VMswMEQhq6eNZMeckCAwEAAQIh\n"+
			"AJPB8I5Zcm6WOuu02OQg8fKdgJTYP9r7BMLre6vaoJ5dAhEA85mCJpzJUAcM9t91\n"+
			"5QkVzwIRAM9mkZCsW9GtzhHmRiVIdOcCEQCm5WSjWcYfW0VJmt4mNmxHAhEAs9cA\n"+
			"yi5qv/qyAZtnn9SgaQIRAJNnH1i7zc7VZ4Zk0udBLLY=\n"+
			"-----END RSA PRIVATE KEY-----";
	
	public void testPrivateKeyLoad() throws IOException {
		AsyncKey pair256 = MCrypt.loadPrivateRsaKey(key256);
		System.out.println(pair256);
		AsyncKey pair2048 = MCrypt.loadPrivateRsaKey(key2048);
		System.out.println(pair2048);
		
	}
	
	public void testEnDeCode() throws IOException {
		System.out.println(">>> testEnDeCode");
		{
			AsyncKey pair256 = MCrypt.loadPrivateRsaKey(key256);
			byte[] org = MString.toBytes("Hello World! Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.");
			BigInteger[] enc = MCrypt.encodeBytes(pair256, org);
			byte[] copy = MCrypt.decodeBytes(pair256, enc);
			System.out.println("Enc Elements: " + enc.length);
			System.out.println(MString.toString(copy));
			assertEquals(MString.toString(org), MString.toString(copy));
		}
		{
			AsyncKey pair256 = MCrypt.loadPrivateRsaKey(key256);
			String org = "test";
			String enc = MCrypt.encode(pair256, org);
			System.out.println(enc);
			String copy = MCrypt.decode(pair256, enc);
			System.out.println(copy);
			assertEquals(org, copy);
		}
		{
			AsyncKey pair256 = MCrypt.loadPrivateRsaKey(key256);
			String org = "test";
			String enc = MCrypt.encodeWithSalt(pair256, org);
			System.out.println(enc);
			String copy = MCrypt.decodeWithSalt(pair256, enc);
			System.out.println(copy);
			assertEquals(org, copy);
		}
		{
			AsyncKey pair256 = MCrypt.loadPrivateRsaKey(key256);
			byte[] org = "Hello UTF8 äöüß".getBytes(MString.CHARSET_UTF_8);
			BigInteger[] enc = MCrypt.encodeBytes(pair256, org);
			byte[] copy = MCrypt.decodeBytes(pair256, enc);
			System.out.println(new String(copy,MString.CHARSET_UTF_8));
			assertEquals(new String(org, MString.CHARSET_UTF_8), new String(copy, MString.CHARSET_UTF_8));
		}
		{
			AsyncKey pair256 = MCrypt.loadPrivateRsaKey(key256);
			byte[] org = "Hello ISO 8859-1 äöüß".getBytes(MString.CHARSET_ISO_8859_1);
			BigInteger[] enc = MCrypt.encodeBytes(pair256, org);
			byte[] copy = MCrypt.decodeBytes(pair256, enc);
			System.out.println(new String(copy,MString.CHARSET_ISO_8859_1));
			assertEquals(new String(org, MString.CHARSET_ISO_8859_1), new String(copy, MString.CHARSET_ISO_8859_1));
		}
		{
			AsyncKey pair256 = MCrypt.loadPrivateRsaKey(key256);
			byte[] org = "Hello UTF16 äöüß".getBytes(MString.CHARSET_UTF_16);
			BigInteger[] enc = MCrypt.encodeBytes(pair256, org);
			byte[] copy = MCrypt.decodeBytes(pair256, enc);
			System.out.println(new String(copy,MString.CHARSET_UTF_16));
			assertEquals(new String(org, MString.CHARSET_UTF_16), new String(copy, MString.CHARSET_UTF_16));
		}
	}
	
	public void testEnDeCodeBinary() throws IOException {
		System.out.println(">>> testEnDeCodeBinary");
		AsyncKey pair256 = MCrypt.loadPrivateRsaKey(key256);
		byte[] org = new byte[256];
		for (int i=0; i < org.length; i++)
			org[i] = (byte)(i-128);
		BigInteger[] enc = MCrypt.encodeBytes(pair256, org);
		byte[] copy = MCrypt.decodeBytes(pair256, enc);
		//System.out.println(new String(copy));
		
		for (int i=0; i < org.length; i++)
			assertEquals(org[i], copy[i]);
		
	}
	
	public void testNegativeValue() throws Exception {
		System.out.println(">>> testNegativeValue");
		AsyncKey pair256 = MCrypt.loadPrivateRsaKey(key256);
		try {
			MCrypt.encode(pair256, new BigInteger("-1"));
			throw new Exception("Negative encode Test Failed");
		} catch (IOException e) {
			
		}
		try {
			MCrypt.decode(pair256, new BigInteger("-1"));
			throw new Exception("Negative decode Test Failed");
		} catch (IOException e) {
			
		}
	}
	
	public void testBase62Encode() throws IOException {
		System.out.println(">>> testBase62Encode");
		{
			System.out.println("--- 256 bit key");
			AsyncKey pair = MCrypt.loadPrivateRsaKey(key256);
			byte[] org = "Hello World!".getBytes();
			BigInteger[] enc = MCrypt.encodeBytes(pair, org);
			String b62 = MBigMath.toBase62(enc);
			b62 = MString.wrap(b62,100);
			System.out.println(b62);
			BigInteger[] b62enc = MBigMath.fromBase62Array(b62);
			byte[] copy = MCrypt.decodeBytes(pair, b62enc);
			System.out.println(new String(copy));
			System.out.println("62 Size: " + b62.length());
			assertEquals(new String(org), new String(copy));
		}
		{
			System.out.println("--- 2048 bit key");
			AsyncKey pair = MCrypt.loadPrivateRsaKey(key2048);
			byte[] org = "Hello World!".getBytes();
			BigInteger[] enc = MCrypt.encodeBytes(pair, org);
			String b62 = MBigMath.toBase62(enc);
			b62 = MString.wrap(b62,100);
			System.out.println(b62);
			BigInteger[] b62enc = MBigMath.fromBase62Array(b62);
			byte[] copy = MCrypt.decodeBytes(pair, b62enc);
			System.out.println(new String(copy));
			System.out.println("62 Size: " + b62.length());
			assertEquals(new String(org), new String(copy));
		}
	}
	
	public void testBase62() {
		System.out.println(">>> testBase62");
		{
			int m = Integer.MAX_VALUE;
			String b62 = MBigMath.toBase62(BigInteger.valueOf(m));
			int n = MBigMath.fromBase62(b62).intValue();
			System.out.println(m + " " + b62 + " " + n);
			assertEquals(m, n);
		}
		{
			int m = Integer.MIN_VALUE;
			String b62 = MBigMath.toBase62(BigInteger.valueOf(m));
			int n = MBigMath.fromBase62(b62).intValue();
			System.out.println(m + " " + b62 + " " + n);
			assertEquals(m, n);
		}
		{
			int m = 0;
			String b62 = MBigMath.toBase62(BigInteger.valueOf(m));
			int n = MBigMath.fromBase62(b62).intValue();
			System.out.println(m + " " + b62 + " " + n);
			assertEquals(m, n);
		}
		{
			String m = "20362187185641719740744354144518944735575949959856858868198444658825667466984270784929654204614580514689002872397300330656664561831814098832695775153878115027562330791292701943761254258386134072241633320460815291031714014496633155881788866223406018609362093078216332925891836454970304283868408313426082512669633657354469746229309116875207499950872538992343485180039215301661855418632786237406531379449599695768733862836388927907549255503769238544123030194101611094996705530387720100026063425333350613562296206296883503866883085316253987741357241342223657485340474646359731670857982325029246463452542949298851914572037";
			String b62 = MBigMath.toBase62(new BigInteger(m));
			String n = MBigMath.fromBase62(b62).toString();
			System.out.println(m + "\n" + b62 + "\n" + n);
			assertEquals(m, n);
			System.out.println("62 Size: " + m.length() + " -> " + b62.length());
		}
	}

	public void testBase91Encode() throws IOException {
		System.out.println(">>> testBase91Encode");
		{
			System.out.println("--- 256 bit key");
			AsyncKey pair = MCrypt.loadPrivateRsaKey(key256);
			byte[] org = "Hello World!".getBytes();
			BigInteger[] enc = MCrypt.encodeBytes(pair, org);
			String b = MBigMath.toBase91(enc);
			b = MString.wrap(b,100);
			System.out.println(b);
			BigInteger[] benc = MBigMath.fromBase91Array(b);
			byte[] copy = MCrypt.decodeBytes(pair, benc);
			System.out.println(new String(copy));
			System.out.println("91 Size: " + b.length());
			assertEquals(new String(org), new String(copy));
		}
		{
			System.out.println("--- 2048 bit key");
			AsyncKey pair = MCrypt.loadPrivateRsaKey(key2048);
			byte[] org = "Hello World!".getBytes();
			BigInteger[] enc = MCrypt.encodeBytes(pair, org);
			String b = MBigMath.toBase91(enc);
			b = MString.wrap(b,100);
			System.out.println(b);
			BigInteger[] benc = MBigMath.fromBase91Array(b);
			byte[] copy = MCrypt.decodeBytes(pair, benc);
			System.out.println(new String(copy));
			System.out.println("91 Size: " + b.length());
			assertEquals(new String(org), new String(copy));
		}
	}
	
	public void testBase91() {
		System.out.println(">>> testBase91");
		{
			int m = Integer.MAX_VALUE;
			String b62 = MBigMath.toBase91(BigInteger.valueOf(m));
			int n = MBigMath.fromBase91(b62).intValue();
			System.out.println(m + " " + b62 + " " + n);
			assertEquals(m, n);
		}
		{
			int m = Integer.MIN_VALUE;
			String b62 = MBigMath.toBase91(BigInteger.valueOf(m));
			int n = MBigMath.fromBase91(b62).intValue();
			System.out.println(m + " " + b62 + " " + n);
			assertEquals(m, n);
		}
		{
			int m = 0;
			String b62 = MBigMath.toBase91(BigInteger.valueOf(m));
			int n = MBigMath.fromBase91(b62).intValue();
			System.out.println(m + " " + b62 + " " + n);
			assertEquals(m, n);
		}
		{
			String m = "20362187185641719740744354144518944735575949959856858868198444658825667466984270784929654204614580514689002872397300330656664561831814098832695775153878115027562330791292701943761254258386134072241633320460815291031714014496633155881788866223406018609362093078216332925891836454970304283868408313426082512669633657354469746229309116875207499950872538992343485180039215301661855418632786237406531379449599695768733862836388927907549255503769238544123030194101611094996705530387720100026063425333350613562296206296883503866883085316253987741357241342223657485340474646359731670857982325029246463452542949298851914572037";
			String b62 = MBigMath.toBase91(new BigInteger(m));
			String n = MBigMath.fromBase91(b62).toString();
			System.out.println(m + "\n" + b62 + "\n" + n);
			assertEquals(m, n);
			System.out.println("91 Size: " + m.length() + " -> " + b62.length());
		}
	}
	
	public void testCipherBlockArithmetic() {
		System.out.println(">>> testCipherBlockArithmetic");
		CipherBlockAdd enc = new CipherBlockAdd(255);
		CipherBlockAdd dec = new CipherBlockAdd(255);
		for (byte b = Byte.MIN_VALUE; b < Byte.MAX_VALUE; b++) {
			enc.getBlock()[b-Byte.MIN_VALUE] = b;
			dec.getBlock()[b-Byte.MIN_VALUE] = b;
		}
		enc.reset();
		dec.reset();
		for (byte b = Byte.MIN_VALUE; b < Byte.MAX_VALUE; b++) {
			byte a = 127;
			byte x = enc.encode(a);
			byte in = dec.decode(x);
//			System.out.println(b + ": " + a + " -> " + x + " -> " + in);
			assertEquals(a, in);
		}
		enc.reset();
		dec.reset();
		for (byte b = Byte.MIN_VALUE; b < Byte.MAX_VALUE; b++) {
			byte a = -128;
			byte x = enc.encode(a);
			byte in = dec.decode(x);
//			System.out.println(b + ": " + a + " -> " + x + " -> " + in);
			assertEquals(a, in);
		}
		enc.reset();
		dec.reset();
		for (byte b = Byte.MIN_VALUE; b < Byte.MAX_VALUE; b++) {
			byte a = b;
			byte x = enc.encode(a);
			byte in = dec.decode(x);
//			System.out.println(b + ": " + a + " -> " + x + " -> " + in);
			assertEquals(a, in);
		}
	}

	public void testCipherBlockRotate() {
		System.out.println(">>> testCipherBlockRotate");
		CipherBlockRotate enc = new CipherBlockRotate(255);
		CipherBlockRotate dec = new CipherBlockRotate(255);
		for (byte b = Byte.MIN_VALUE; b < Byte.MAX_VALUE; b++) {
			enc.getBlock()[b-Byte.MIN_VALUE] = b;
			dec.getBlock()[b-Byte.MIN_VALUE] = b;
		}
		enc.reset();
		dec.reset();
		for (byte b = Byte.MIN_VALUE; b < Byte.MAX_VALUE; b++) {
			byte a = 127;
			byte x = enc.encode(a);
			byte in = dec.decode(x);
//			System.out.println(b + ": " + a + " -> " + x + " -> " + in);
			assertEquals(a, in);
		}
		enc.reset();
		dec.reset();
		for (byte b = Byte.MIN_VALUE; b < Byte.MAX_VALUE; b++) {
			byte a = -128;
			byte x = enc.encode(a);
			byte in = dec.decode(x);
//			System.out.println(b + ": " + a + " -> " + x + " -> " + in);
			assertEquals(a, in);
		}
		enc.reset();
		dec.reset();
		for (byte b = Byte.MIN_VALUE; b < Byte.MAX_VALUE; b++) {
			byte a = b;
			byte x = enc.encode(a);
			byte in = dec.decode(x);
//			System.out.println(b + ": " + a + " -> " + x + " -> " + in);
			assertEquals(a, in);
		}
	}

	public void testUtil() throws IOException {
		System.out.println(">>> testUtil");
		AsyncKey pair256 = MCrypt.loadPrivateRsaKey(key256);
		String org = "Fischers Fritze fischt frische Fische";
		String enc = MCrypt.encode(pair256, org);
		String copy = MCrypt.decode(pair256, enc);
		System.out.println(org);
		System.out.println(enc);
		System.out.println(copy);
		assertEquals(org, copy);
	}
	
	public void testCipherStream() {
		System.out.println(">>> testCipherStream");
		byte[] buf = "Fischers Fritze fischt frische Fische".getBytes();
		ByteArrayInputStream is = new ByteArrayInputStream(buf);
		CipherInputStream cis = new CipherInputStream(is);
		
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		CipherOutputStream cos = new CipherOutputStream(os);
		
		CipherBlockRotate cipher = MCrypt.createRandomCipherBlockRotate(20);
		
		cis.setCipher(cipher);
		cos.setCipher(cipher);
		
		MFile.copyFile(cis, cos);
		
		byte[] copy = os.toByteArray();
		
		assertEquals(new String(buf), new String(copy));
		
	}

	public void testMCipherStreamSymetry() throws IOException {
		System.out.println(">>> testMCipherStreamSymetry");
		String pass = "aaaaaaaaaa";
		byte[] buf = "------------------------------------------------------------------------------------------------------------------------------------------------------------------".getBytes();
		
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		OutputStream cos = MCrypt.createCipherOutputStream(os, pass);
		
		// write into output stream and crypt
		for (byte b : buf)
			cos.write(b);
		
		// get encrypted
		byte[] enc = os.toByteArray();
		
		// read and decrypt
		ByteArrayInputStream is = new ByteArrayInputStream(enc);
		InputStream cis = MCrypt.createCipherInputStream(is, pass);
		byte[] copy = MFile.readBinary(cis);
		
		// analyse
		int[] occur = new int[256];
		byte sameLast = 0;
		int sameCnt = 0;
		int sameMax = 0;
		for (byte p : enc) {
			if (sameLast == p)
				sameCnt++;
			else {
				sameMax = Math.max(sameMax, sameCnt);
				sameCnt = 0;
				sameLast = p;
			}
			occur[MMath.unsignetByteToInt(p)]++;
		}

		int aCnt = 0;
		int aMin = buf.length;
		int aMax = 0;
		for (int i = 0; i < occur.length; i++) {
			int c = occur[i];
			if (c > 0) {
				aCnt++;
				aMin = Math.min(aMin, c);
				aMax = Math.max(aMax, c);
			}
		}
		System.out.println("Org Length: " + buf.length);
		System.out.println("Enc Length: " + enc.length);
		System.out.println("Spread: " + aCnt);
		System.out.println("Min, Max, Diff: " + aMin + " - " + aMax + " => " + (aMax-aMin));
		System.out.println("SameMax: " + sameMax);
		
		//System.out.println(new String(buf));
		//System.out.println(new String(enc));
		//System.out.println(new String(copy));
		
		if (aCnt < 100) System.out.println("WARNING: Spread lower then 100");
		
		assertEquals(new String(buf), new String(copy));
		
	}

	public void testMCipherStreamReal() throws IOException {
		System.out.println(">>> testMCipherStreamReal");
		String pass = "passphrase123";
		byte[] buf = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.".getBytes();
		
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		OutputStream cos = MCrypt.createCipherOutputStream(os, pass);
		
		// write into output stream and crypt
		for (byte b : buf)
			cos.write(b);
		
		// get encrypted
		byte[] enc = os.toByteArray();
		
		// read and decrypt
		ByteArrayInputStream is = new ByteArrayInputStream(enc);
		InputStream cis = MCrypt.createCipherInputStream(is, pass);
		byte[] copy = MFile.readBinary(cis);
		
		// analyse
		int[] occur = new int[256];
		byte sameLast = 0;
		int sameCnt = 0;
		int sameMax = 0;
		for (byte p : enc) {
			if (sameLast == p)
				sameCnt++;
			else {
				sameMax = Math.max(sameMax, sameCnt);
				sameCnt = 0;
				sameLast = p;
			}
			occur[MMath.unsignetByteToInt(p)]++;
		}

		int aCnt = 0;
		int aMin = buf.length;
		int aMax = 0;
		for (int i = 0; i < occur.length; i++) {
			int c = occur[i];
			if (c > 0) {
				aCnt++;
				aMin = Math.min(aMin, c);
				aMax = Math.max(aMax, c);
			}
		}
		System.out.println("Org Length: " + buf.length);
		System.out.println("Enc Length: " + enc.length);
		System.out.println("Spread: " + aCnt);
		System.out.println("Min, Max, Diff: " + aMin + " - " + aMax + " => " + (aMax-aMin));
		System.out.println("SameMax: " + sameMax);
		
		//System.out.println(new String(buf));
		//System.out.println(new String(enc));
		//System.out.println(new String(copy));
		
		if (aCnt < 100) System.out.println("WARNING: Spread lower then 100");
		
		assertEquals(new String(buf), new String(copy));
		
	}

	public void testObfuscate() {
		System.out.println(">>> testObfuscate");
		byte[] org = new byte[256];
		for (int i = 0; i < 256; i++)
			org[i] = (byte)i;
		byte[] copy = MCrypt.unobfuscate( MCrypt.obfuscate(org));
		for (int i = 0; i < 256; i++)
			assertEquals(org[i],copy[i]);
	}
	
	public void testEnDeDirect() throws IOException {
		System.out.println(">>> testEnDeDirect");

		String passphrase = "Lorem ipsum";
		{
			byte[] org = MString.toBytes("Hello World!");
			byte[] enc = MCrypt.encode(passphrase, org);
			byte[] dec = MCrypt.decode(passphrase, enc);
			assertEquals(MString.toString(org), MString.toString(dec));
		}
		{
			byte[] org = new byte[256];
			for (int i=0; i < org.length; i++)
				org[i] = (byte)(i-128);
			byte[] enc = MCrypt.encode(passphrase, org);
			byte[] dec = MCrypt.decode(passphrase, enc);
			for (int i=0; i < org.length; i++)
				assertEquals(org[i], dec[i]);
		}
		
	}

	public void testMd5Salt() throws IOException {
		String real = "Hello World!";
		String md5 = MCrypt.md5WithSalt(real);
		assertTrue(MCrypt.validateMd5WithSalt(md5, real));
	}

}
