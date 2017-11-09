package de.mhus.lib.core.crypt;

public class DefaultRandom implements MRandom {

	@Override
	public byte getByte() {
		return (byte)(Math.random() * 255);
	}

	@Override
	public int getInt() {
		return (byte)(Math.random() * Integer.MAX_VALUE); // no negative values!
	}

	@Override
	public double getDouble() {
		return Math.random();
	}

}
