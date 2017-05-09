package de.mhus.lib.core.crypt;

import java.util.LinkedList;

public class CipherBlockBundle extends LinkedList<CipherBlock> implements CipherBlock {

	private static final long serialVersionUID = 1L;
	protected CipherBlock current;

	public void select(int index) {
		current = get(index);
	}
	
	public CipherBlock getCurrent() {
		return current;
	}

	@Override
	public void reset() {
		current.reset();
	}

	@Override
	public byte encode(byte in) {
		return current.encode(in);
	}

	@Override
	public byte decode(byte in) {
		return current.decode(in);
	}
	
	
	
}
