package de.mhus.lib.core.crypt;

import java.io.IOException;
import java.io.OutputStream;

import de.mhus.lib.core.MMath;

public class SaltOutputStream extends OutputStream {

	private OutputStream next;
	private MRandom random;
	private int cnt;
	private boolean addRandomBlocks;
	private byte salt;
	private int maxBlockSize;

	/**
	 * 
	 * @param next
	 * @param random
	 * @param maxBlockSize 
	 * @param addRandomBlocks 
	 */
	public SaltOutputStream(OutputStream next, MRandom random, int maxBlockSize, boolean addRandomBlocks) {
		this.next = next;
		this.random = random;
		this.addRandomBlocks = addRandomBlocks;
		this.maxBlockSize = maxBlockSize;
		cnt = 0;
	}
	
	@Override
	public void write(int b) throws IOException {
		cnt--;
		if (cnt <= 0) {
			
			if (addRandomBlocks) {
				cnt = MMath.unsignetByteToInt(random.getByte()) % maxBlockSize;
				next.write(cnt);
				for (int i = 0; i < cnt; i++)
					next.write(random.getByte());
			}
			
			salt = random.getByte();
			cnt = MMath.unsignetByteToInt(random.getByte()) % maxBlockSize;
			next.write(salt);
			next.write(cnt);
		}
		
		b = MMath.addRotate((byte)b,salt);
		next.write(b);
	}

    @Override
	public void close() throws IOException {
    	next.close();
    }

    @Override
	public void flush() throws IOException {
    	next.flush();
    }
    
}
