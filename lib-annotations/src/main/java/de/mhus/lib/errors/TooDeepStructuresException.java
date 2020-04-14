package de.mhus.lib.errors;

public class TooDeepStructuresException extends MRuntimeException {

    private static final long serialVersionUID = 1L;

    public TooDeepStructuresException(Object... in) {
        super(in);
    }

}
