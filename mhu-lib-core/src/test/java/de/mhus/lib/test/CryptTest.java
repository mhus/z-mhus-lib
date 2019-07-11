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
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import org.junit.jupiter.api.Test;

import de.mhus.lib.core.MBigMath;
import de.mhus.lib.core.MCollection;
import de.mhus.lib.core.MFile;
import de.mhus.lib.core.MMath;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.crypt.AsyncKey;
import de.mhus.lib.core.crypt.Blowfish;
import de.mhus.lib.core.crypt.CipherBlockAdd;
import de.mhus.lib.core.crypt.CipherBlockRotate;
import de.mhus.lib.core.crypt.CipherInputStream;
import de.mhus.lib.core.crypt.CipherOutputStream;
import de.mhus.lib.core.crypt.MBouncy;
import de.mhus.lib.core.crypt.MCrypt;
import de.mhus.lib.core.crypt.Twofish;
import de.mhus.lib.core.crypt.pem.PemUtil;
import de.mhus.lib.core.util.Base64;
import de.mhus.lib.core.util.Lorem;
import static org.junit.jupiter.api.Assertions.*;

public class CryptTest {

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
	
    String t1PublicKey = "-----BEGIN PUBLIC KEY-----\n" +
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCKp+9XIfeKAB\n" +
            "s4JTfsduqba+8U9MZUAWdakYEeI8AJ95TPcMnjku5E8xLOh5s8\n" +
            "soaS99H+1GffaI4nin3okBVuO4a0mYR8O7JpDbWCOc0G3UEkQQ\n" +
            "R+LEjS5bCqKwg41W+sLIeFDaLirZDkVKg5zsjy/s/BufjtVI4U\n" +
            "Bqv/PIl0kQIDAQAB" +
            "\n" +
            "-----END PUBLIC KEY-----";
    String t1PrivateKey = "-----BEGIN PRIVATE KEY-----\n" +
            "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAI\n" +
            "qn71ch94oAGzglN+x26ptr7xT0xlQBZ1qRgR4jwAn3lM9wyeOS\n" +
            "7kTzEs6HmzyyhpL30f7UZ99ojieKfeiQFW47hrSZhHw7smkNtY\n" +
            "I5zQbdQSRBBH4sSNLlsKorCDjVb6wsh4UNouKtkORUqDnOyPL+\n" +
            "z8G5+O1UjhQGq/88iXSRAgMBAAECgYBnFtD2QYTgD5AtQE7B+v\n" +
            "AXOjp5pDvIvXpwdfo/xGjFgFQdn0gbcWTB0s/Kyjv69ujjYGm7\n" +
            "Q4UvL3dxoqBWRroHKkKveqt8tmSZO37fOLQeHvaFZw5s5UN+wJ\n" +
            "7r3FpsG350gjyNVUXtukNxHSHZdc4n+WvYkavtxNtLux4hGqmd\n" +
            "oQJBAOqcSkiwuFyoYecywfWjkpnEHZq3aZriMjEEjLZ3gC2koz\n" +
            "MPuFo+96Tlz3VQe6KT9TLAw2ktko9BgAt7SEIbCp0CQQCXTBtq\n" +
            "oQ7a5xoojygrQxa1Iv5nPAwshkmYk4uh/SCRH/13AkMnxyMs+h\n" +
            "T8KRB7Sk1f2dBWFm9vb1BEZf+ScWWFAkEA45np6ukegi2MhTnV\n" +
            "txMQFwKOYdlLp0mHvcwXIrF99UnCVbgLdemeYCfegoYo20lE2A\n" +
            "7vxGrEwxudOAZKzG7ldQJBAJcLsENfz4jTN9ZONXgbXkwwR3Oh\n" +
            "CzZYSpk8lCaAo0a/fTiW1Zycvo1kjhbAmGe94klTFx8a/t1tb+\n" +
            "EZQ3FcLFECQQCpI9sVRmPGZqJlwiU8ozKyqnW5KyC9Z44w9aPK\n" +
            "W8WugXXci5tEbicnibZKp7lJc1KfA1M0naCwUdiroWW6QfFM" +
            "\n" +
            "-----END PRIVATE KEY-----";

    String t1Secret = "Vom Eise befreit sind Strom und Bäche\n" + 
            "Durch des Frühlings holden, belebenden Blick,\n" + 
            "Im Tale grünet Hoffnungsglück;\n" + 
            "Der alte Winter, in seiner Schwäche,\n" + 
            "Zog sich in rauhe Berge zurück.\n" + 
            "Von dort her sendet er, fliehend, nur\n" + 
            "Ohnmächtige Schauer körnigen Eises\n" + 
            "In Streifen über die grünende Flur.\n" + 
            "Aber die Sonne duldet kein Weißes,\n" + 
            "Überall regt sich Bildung und Streben,\n" + 
            "Alles will sie mit Farben beleben;\n" + 
            "Doch an Blumen fehlts im Revier,\n" + 
            "Sie nimmt geputzte Menschen dafür.\n" + 
            "Kehre dich um, von diesen Höhen\n" + 
            "Nach der Stadt zurück zu sehen!\n" + 
            "Aus dem hohlen finstern Tor\n" + 
            "Dringt ein buntes Gewimmel hervor.\n" + 
            "Jeder sonnt sich heute so gern.\n" + 
            "Sie feiern die Auferstehung des Herrn,\n" + 
            "Denn sie sind selber auferstanden:\n" + 
            "Aus niedriger Häuser dumpfen Gemächern,\n" + 
            "Aus Handwerks- und Gewerbesbanden,\n" + 
            "Aus dem Druck von Giebeln und Dächern,\n" + 
            "Aus der Straßen quetschender Enge,\n" + 
            "Aus der Kirchen ehrwürdiger Nacht\n" + 
            "Sind sie alle ans Licht gebracht.\n" + 
            "Sieh nur, sieh! wie behend sich die Menge\n" + 
            "Durch die Gärten und Felder zerschlägt,\n" + 
            "Wie der Fluß in Breit und Länge\n" + 
            "So manchen lustigen Nachen bewegt,\n" + 
            "Und, bis zum Sinken überladen,\n" + 
            "Entfernt sich dieser letzte Kahn.\n" + 
            "Selbst von des Berges fernen Pfaden\n" + 
            "Blinken uns farbige Kleider an.\n" + 
            "Ich höre schon des Dorfs Getümmel,\n" + 
            "Hier ist des Volkes wahrer Himmel,\n" + 
            "Zufrieden jauchzet groß und klein:\n" + 
            "Hier bin ich Mensch, hier darf ichs sein!";
    
    @Test
    public void testTest1BC() throws Exception {
        // This is called test 1 because it's the first test with csharp
        
        PrivateKey privKey = MBouncy.getPrivateKey(PemUtil.getBlockAsString(t1PrivateKey));
        PublicKey publKey = MBouncy.getPublicKey(PemUtil.getBlockAsString(t1PublicKey));
        
        byte[] secure = MBouncy.encryptRsa(MString.toBytes(t1Secret), publKey);
        System.out.println(Base64.encode(secure));
        
        String secret = MString.byteToString(MBouncy.decryptRsa(secure, privKey));
        
        assertEquals(t1Secret, secret);
        
    }
    
    @Test
	public void testPrivateKeyLoad() throws IOException {
		AsyncKey pair256 = MCrypt.loadPrivateRsaKey(key256);
		System.out.println(pair256);
		AsyncKey pair2048 = MCrypt.loadPrivateRsaKey(key2048);
		System.out.println(pair2048);
		
	}
	
	@Test
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
	
	@Test
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
	
	@Test
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
	
	@Test
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
	
	@Test
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

	@Test
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
	
	@Test
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
	
	@Test
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

	@Test
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

	@Test
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
	
	@Test
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

	@Test
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

	@Test
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

	@Test
	public void testObfuscate() {
		System.out.println(">>> testObfuscate");
		byte[] org = new byte[256];
		for (int i = 0; i < 256; i++)
			org[i] = (byte)i;
		byte[] copy = MCrypt.unobfuscate( MCrypt.obfuscate(org));
		for (int i = 0; i < 256; i++)
			assertEquals(org[i],copy[i]);
	}
	
	@Test
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

	@Test
	public void testMd5Salt() throws IOException {
		String real = "Hello World!";
		String md5 = MCrypt.md5WithSalt(real);
		assertTrue(MCrypt.validateMd5WithSalt(md5, real));
	}

	@Test
	public void testRsaBigBlocks() throws Exception {
		String text = Lorem.create();
		byte[] block = text.getBytes();
		KeyPair key = MBouncy.generateRsaKey(MBouncy.RSA_KEY_SIZE_DEFAULT);
		byte[] enc = MBouncy.encryptRsa(block, key.getPublic());
		byte[] dec = MBouncy.decryptRsa(enc, key.getPrivate());
		String text2 = new String(dec);
		assertEquals(text, text2);
	}
	
	@Test
	public void testBlowfish() throws Exception {
		String text = Lorem.create();
		{ 
			String key = "";
			String enc = Blowfish.encrypt(text, key);
			String dec = Blowfish.decrypt(enc, key);
			assertEquals(text, dec);
		}
		{
			String key = "A";
			String enc = Blowfish.encrypt(text, key);
			String dec = Blowfish.decrypt(enc, key);
			assertEquals(text, dec);
		}
		{
			String key = "Secret!";
			String enc = Blowfish.encrypt(text, key);
			String dec = Blowfish.decrypt(enc, key);
			assertEquals(text, dec);
		}
		
		// https://docs.oracle.com/javase/7/docs/technotes/guides/security/SunProviders.html#importlimits
		try {
			String key = "create an instance of cipher";
			String enc = Blowfish.encrypt(text, key);
			String dec = Blowfish.decrypt(enc, key);
			assertEquals(text, dec);
		} catch (InvalidKeyException e) {
			System.out.println("!!!! JCE not installed !!!");
			System.out.println(e);
		}
		
		{
			String pass = "Secret!";
			String text1 = "Acdfgtrertgfdsertfdrtytghioplkhgfdser54spoikjhgfty78kjuhX";
			byte[] enc1 = Blowfish.encrypt(text1.getBytes(), pass);
			byte[] enc2 = Blowfish.encrypt(text1.getBytes(), pass);
			assertEquals(enc1.length, enc2.length);
			for (int i = 0; i < enc1.length; i++)
				assertEquals(enc1[i], enc2[i]);
		}
		
	}

	@Test
	public void testTwofish() throws Exception {
		String text = Lorem.create();
		{ 
			String key = "";
			String enc = Twofish.encrypt(text, key);
			String dec = Twofish.decrypt(enc, key);
			assertEquals(text, dec);
		}
		{
			String key = "A";
			String enc = Twofish.encrypt(text, key);
			String dec = Twofish.decrypt(enc, key);
			assertEquals(text, dec);
		}
		try {
			String key = "create an instance of cipher";
			String enc = Twofish.encrypt(text, key);
			String dec = Twofish.decrypt(enc, key);
			assertEquals(text, dec);
		} catch (InvalidKeyException e) {
			System.out.println("!!!! JCE not installed !!!");
			System.out.println(e);
		}
	}
	
	@Test
	public void testPepper() {
		{
			String text = "";
			String peppered = MCrypt.addPepper(text);
			String content = MCrypt.removePepper(peppered);
			assertFalse(text.equals(peppered));
			assertFalse(content.equals(peppered));
			assertEquals(text, content);
		}
		{
			String text = Lorem.create();
			String peppered = MCrypt.addPepper(text);
			String content = MCrypt.removePepper(peppered);
			assertFalse(text.equals(peppered));
			assertFalse(content.equals(peppered));
			assertEquals(text, content);
		}
		{
			byte[] text = Lorem.create().getBytes();
			byte[] peppered = MCrypt.addPepper(text);
			byte[] content = MCrypt.removePepper(peppered);
			assertTrue(MCollection.equals(text, content));
		}
	}

	   @Test
	    public void testBountySigner() {
	       System.out.println(">>> testBountySigner");
	       // positive test
	       KeyPair keys = MBouncy.generateEccKey(MBouncy.ECC_SPEC.PRIME192V1);
	       String signature = MBouncy.createSignature(keys.getPrivate(), t1Secret);
	       System.out.println("Private Key");
	       //System.out.println(MBouncy.getPrivateKey(keys));
           System.out.println(MBouncy.getPrivatePem(keys));
           System.out.println("Public Key");
           //System.out.println(MBouncy.getPublicKey(keys));
           System.out.println(MBouncy.getPublicPem(keys));
           System.out.println("Signature");
           System.out.println(signature);
           
           boolean valid = MBouncy.validateSignature(keys.getPublic(), t1Secret, signature);
           assertTrue(valid);
           
           // negative test
           KeyPair keys2 = MBouncy.generateEccKey(MBouncy.ECC_SPEC.PRIME192V1);
           String signature2 = MBouncy.createSignature(keys2.getPrivate(), t1Secret);
           boolean valid2 = MBouncy.validateSignature(keys.getPublic(), t1Secret, signature2);
           
           assertFalse(valid2);
           
	   }

}
