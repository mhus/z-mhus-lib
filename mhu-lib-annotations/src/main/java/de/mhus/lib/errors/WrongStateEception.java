package de.mhus.lib.errors;

public class WrongStateEception extends MRuntimeException {

	private static final long serialVersionUID = 1L;

	public WrongStateEception(Object... in) {
		super(in);
	}

}
