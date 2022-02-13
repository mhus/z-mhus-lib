package de.mhus.lib.errors;

import de.mhus.lib.basics.IResult;
import de.mhus.lib.basics.RC;
import de.mhus.lib.basics.RC.STATUS;

public class ConflictException extends MException {

    private static final long serialVersionUID = 1L;

    public static STATUS getDefaultStatus() {
        return RC.STATUS.CONFLICT;
    }

    public ConflictException(Object... in) {
        super(getDefaultStatus(),in);
    }

    public ConflictException(RC.CAUSE causeHandling, Object... in) {
        super(causeHandling, getDefaultStatus(), in);
    }

    public ConflictException(Throwable cause) {
        super(getDefaultStatus().rc(), cause);
    }

    public ConflictException(IResult cause) {
        super(cause);
    }

    public ConflictException(String msg, Object... in) {
        super(getDefaultStatus().rc(), msg, in);
    }

    public ConflictException(RC.CAUSE causeHandling, String msg, Object... parameters) {
        super(causeHandling, getDefaultStatus().rc(), msg, parameters);
    }

    public ConflictException(int rc) {
        super(getDefaultStatus().rc());
    }

}
