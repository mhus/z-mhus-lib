package de.mhus.lib.errors;

import de.mhus.lib.basics.RC;
import de.mhus.lib.basics.RC.STATUS;

public class ErrorException extends MException {

    private static final long serialVersionUID = 1L;

    public static STATUS getDefaultStatus() {
        return RC.STATUS.ERROR;
    }

    public ErrorException(Object... in) {
        super(getDefaultStatus(),in);
    }

    public ErrorException(RC.CAUSE causeHandling, Object... in) {
        super(causeHandling, getDefaultStatus(), in);
    }

    public ErrorException(Throwable cause) {
        super(getDefaultStatus().rc(), cause);
    }

    public ErrorException(IException cause) {
        super(cause);
    }

    public ErrorException(String msg, Object... in) {
        super(getDefaultStatus().rc(), msg, in);
    }

    public ErrorException(RC.CAUSE causeHandling, String msg, Object... parameters) {
        super(causeHandling, getDefaultStatus().rc(), msg, parameters);
    }

    public ErrorException(int rc) {
        super(getDefaultStatus().rc());
    }
}
