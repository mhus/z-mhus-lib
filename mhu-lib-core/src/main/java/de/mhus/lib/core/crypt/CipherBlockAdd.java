package de.mhus.lib.core.crypt;

import de.mhus.lib.core.MMath;

/**
 * add for encode and sub for decode current block value.
 * @author mikehummel
 *
 */
public class CipherBlockAdd implements CipherBlock {
	
	private byte[] block;
	private int pos;

	public CipherBlockAdd(byte[] block) {
		this.block = block;
	}
	
	public CipherBlockAdd(int size) {
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
		in = MMath.addRotate(in, block[pos]);
		next();
		return in;
	}

	@Override
	public byte decode(byte in) {
		in = MMath.subRotate(in, block[pos]);
		next();
		return in;
	}

	private void next() {
		pos = (pos + 1) % block.length;
	}
	
}
