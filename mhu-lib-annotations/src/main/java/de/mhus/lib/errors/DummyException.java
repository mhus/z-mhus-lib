package de.mhus.lib.errors;

public class DummyException extends MException {

    private static final long serialVersionUID = 1L;
    private StackTraceElement[] stackTrace;

    public DummyException(String msg) {
        super(msg);
    }
    
    public DummyException(String msg, StackTraceElement[] stackTrace) {
        super(msg);
        this.stackTrace = stackTrace;
    }
    
    @Override
    public StackTraceElement[] getStackTrace() {
        if (stackTrace != null) return stackTrace;
        return super.getStackTrace();
    }
    
    
}
