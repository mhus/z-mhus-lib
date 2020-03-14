package de.mhus.lib.errors;

public class RuntimeInterruptedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RuntimeInterruptedException() {
        super();
    }

    public RuntimeInterruptedException(Throwable cause) {
        super(cause);
    }

}
