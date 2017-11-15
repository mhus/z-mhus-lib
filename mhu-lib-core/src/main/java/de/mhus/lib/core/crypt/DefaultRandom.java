package de.mhus.lib.core.crypt;

import java.util.Random;

public class DefaultRandom implements MRandom {

	private Random rand;

	@Override
	public byte getByte() {
		return (byte)(Math.random() * 255);
	}

	@Override
	public int getInt() {
		return (int)(Math.random() * Integer.MAX_VALUE); // no negative values!
	}

	@Override
	public double getDouble() {
		return Math.random();
	}

	@Override
	public long getLong() {
		return (long)(Math.random() * Long.MAX_VALUE); // no negative values!
	}
	
	public synchronized Random getRandom() {
		if (rand == null)
			rand = new Random(getLong());
		return rand;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T adaptTo(Class<? extends T> ifc) {
		if (Random.class.isAssignableFrom(ifc)) return (T)getRandom();
		return null;
	}

}
