package de.mhus.lib.test;

import java.io.IOException;

import de.mhus.lib.core.io.StreamBuffer;
import de.mhus.lib.core.util.ByteBuffer;
import junit.framework.TestCase;

public class MUtilsTest extends TestCase {

	public void testByteBuffer() {
		ByteBuffer buffer = new ByteBuffer();
		for (int i = 0; i < 1000; i++) { // 1024 * 10 buffer size, 1000 * 10 
			buffer.append("Hello".getBytes());
			buffer.append("World".getBytes());
			assertEquals((i+1)*10, buffer.size());
		}
		byte[] res = buffer.toByte();
		for (int i = 0; i < 1000; i++) {
			assertEquals('H', res[i*10+0]);
			assertEquals('e', res[i*10+1]);
			assertEquals('l', res[i*10+2]);
			assertEquals('l', res[i*10+3]);
			assertEquals('o', res[i*10+4]);
			assertEquals('W', res[i*10+5]);
			assertEquals('o', res[i*10+6]);
			assertEquals('r', res[i*10+7]);
			assertEquals('l', res[i*10+8]);
			assertEquals('d', res[i*10+9]);
		}
	}
	
	public void testByteStream() throws IOException {
		StreamBuffer stream = new StreamBuffer();
		for (int i = 0; i < 10000; i++)
			stream.getOutputStream().write( (byte)(i % 100) );
		assertEquals(10000, stream.size());
		
		for (int i = 0; i < 10000; i++) {
			byte x = (byte)(i % 100);
			int y = stream.getInputStream().read();
			assertEquals(x, y);
		}
		assertEquals(0, stream.size());
		
		// again
		for (int i = 0; i < 10000; i++)
			stream.getOutputStream().write( (byte)(i % 100) );
		assertEquals(10000, stream.size());
		
		for (int i = 0; i < 10000; i++) {
			byte x = (byte)(i % 100);
			int y = stream.getInputStream().read();
			assertEquals(x, y);
		}
		assertEquals(0, stream.size());
		
	}
}
