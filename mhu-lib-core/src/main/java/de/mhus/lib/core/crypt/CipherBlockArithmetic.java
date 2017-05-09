package de.mhus.lib.core.crypt;

/**
 * add for encode and sub for decode current block value.
 * @author mikehummel
 *
 */
public class CipherBlockArithmetic implements CipherBlock {
	
	private byte[] block;
	private int pos;

	public CipherBlockArithmetic(int size) {
		block = new byte[size]; 
	}
	
	public byte[] getBlock() {
		return block;
	}
	
	public int getSize() {
		return block.length;
	}

	@Override
	public void reset() {
		pos = 0;
	}

	@Override
	public byte encode(byte in) {
		// add block value, rotate if overlaps max boundary
		// use int to extend boundaries, move first number to 0 use modulo to cut, transform back
		in = (byte)( ((int)in + 128 + block[pos]) % 256 - 128);
		next();
		return in;
	}

	@Override
	public byte decode(byte in) {
		// sub block value, rotate if overlaps min boundary
		in = (byte)(0-( ((int)(0-in) + 128 + block[pos]) % 256 - 128));
//		int i = ((int)in + 128 - block[pos]);
//		if ( i < 0) i = 256 + i;
//		in = (byte)(i - 128);
		next();
		return in;
	}

	private void next() {
		pos = (pos + 1) % block.length;
	}
	
}
