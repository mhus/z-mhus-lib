package de.mhus.lib.basics;

public class RC {

    public enum STATUS {
        
        WARNING_TEMPORARELY(199),
        OK(200),
        CREATED(201),
        ACCEPTED(202),
        WARNING(299),
        ERROR(400),
        ACCESS_DENIED(401),
        FORBIDDEN(403),
        NOT_FOUND(404),
        CONFLICT(409),
        GONE(410),
        TOO_LARGE(413),
        SYNTAX_ERROR(415),
        USAGE(422),
        TEAPOT(418),
        INTERNAL_ERROR(500),
        NOT_SUPPORTED(501),
        BUSY(503),
        TIMEOUT(504),
        TOO_DEEP(508)
        ;
        
        private final int rc;
        
        private STATUS(int rc) {
            this.rc = rc;
        }
        
        public int rc() {
            return rc;
        }
        
    }
    
    public static final int WARNING_TEMPORARELY = 199; // Miscellaneous warning
    
    public static final int OK = 200;
    public static final int CREATED = 201;
    public static final int ACCEPTED = 202;
    public static final int WARNING = 299; // Miscellaneous persistent warning, https://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.46

    // do not retry with this errors - professional errors
    public static final int ERROR = 400;
    public static final int ACCESS_DENIED = 401;
    public static final int FORBIDDEN = 403;
    public static final int NOT_FOUND = 404;
    public static final int CONFLICT = 409; // conflict state or wrong state
    public static final int GONE = 410;
    public static final int TOO_LARGE = 413;
    public static final int SYNTAX_ERROR = 415; // Unsupported Media Type - string instead of int
    public static final int USAGE = 422; // Unprocessable Entity - parameter not set

    public static final int TEAPOT = 418; // I’m a teapot - joke

    // retry later - technical errors
    public static final int INTERNAL_ERROR = 500; // Internal Server Error
    public static final int NOT_SUPPORTED = 501; // Not Implemented
    public static final int BUSY = 503; // Service Unavailable, do not use 403 because it could be repeated
    public static final int TIMEOUT = 504; // Gateway Timeout
    public static final int TOO_DEEP = 508; // Loop Detected

    public static final int RANGE_MIN_SUCCESSFUL = 200;
    public static final int RANGE_MAX_SUCCESSFUL = 299;
    public static final int RANGE_MIN_PROFESSIONAL = 400; // do not retry
    public static final int RANGE_MAX_PROFESSIONAL = 499; // do not retry
    public static final int RANGE_MIN_TECHNICAL = 500; // do retry later
    public static final int RANGE_MAX_TECHNICAL = 599; // do retry later

    public static final int RANGE_MIN_CUSTOM = 900;
    public static final int RANGE_MAX_CUSTOM = 999;

    public static final int RANGE_MAX = 999;

    public static String toResponseString(String msg, Object[] parameters) {
        // short cuts
        if (msg == null && parameters == null) return "?";
        if (parameters == null && msg.indexOf('|') == -1) return msg;
        // pipe to colon
        StringBuilder sb = new StringBuilder();
        addPipeEncoded(sb,msg);
        if (parameters != null && parameters.length > 0) {
            for (Object parameter : parameters) {
                if (parameter !=  null && parameter instanceof Throwable) continue;
                sb.append("|");
                if (parameter == null)
                    sb.append("null");
                else
                if (parameter instanceof Object[]) {
                    sb.append("<");
                    for (Object item : (Object[])parameter) {
                        sb.append("|");
                        addPipeEncoded(sb,String.valueOf(item));
                    }
                    sb.append("|>");
                } else
                    addPipeEncoded(sb,String.valueOf(parameter));
            }
        }
        return sb.toString();
    }

    private static void addPipeEncoded(StringBuilder sb, String msg) {
        int pos = 0;
        int nextPos;
        while ((nextPos = msg.indexOf('|', pos)) != -1) {
            sb.append(msg.substring(pos, nextPos));
            sb.append(":");
            pos = nextPos+1;
            if (pos >= msg.length()) break;
        }
        if (pos < msg.length())
            sb.append(msg.substring(pos));
    }

    /**
     * Allow all between 0 - 999, otherwise 400 (ERROR)
     * 
     * @param rc
     * @return normalize error code
     */
    public int normalize(int rc) {
        if (rc < 0) return ERROR;
        if (rc >= 1000) return ERROR;
        return rc;
    }
    /**
     * is successful but a warning
     * @param rc
     * @return true if this kind of error
     */
    public static boolean isWarning(int rc) {
        return rc == 0 || rc == WARNING || rc == WARNING_TEMPORARELY;
    }
    
    /**
     * not permanent, could be fixed
     * @param rc
     * @return true if this kind of error
     */
    public static boolean isTechnicalError(int rc) {
        return rc >= RANGE_MIN_TECHNICAL && rc <= RANGE_MAX_TECHNICAL;
    }
    
    /**
     * permanent error, retry will not fix it
     * @param rc
     * @return true if this kind of error
     */
    public static boolean isProfessionalError(int rc) {
        return rc < 0 || rc >= RANGE_MIN_PROFESSIONAL && rc <= RANGE_MAX_PROFESSIONAL || rc > RANGE_MIN_PROFESSIONAL;
    }
    
    /**
     * not an error - should not be used for exceptions
     * @param rc
     * @return true if this kind of error
     */
    public static boolean isSuccessful(int rc) {
        return rc >= 0 && rc <= RANGE_MAX_SUCCESSFUL;
    }

    public static String toString(int rc) {
        switch (rc) {
        case WARNING_TEMPORARELY: return "WARNING_TEMPORARELY";
        case OK: return "OK";
        case CREATED: return "CREATED";
        case ACCEPTED: return "ACCEPTED";
        case WARNING: return "WARNING";
        case ERROR: return "ERROR";
        case ACCESS_DENIED: return "ACCESS_DENIED";
        case FORBIDDEN: return "FORBIDDEN";
        case NOT_FOUND: return "NOT_FOUND";
        case GONE: return "GONE";
        case TOO_LARGE: return "TOO_LARGE";
        case SYNTAX_ERROR: return "SYNTAX_ERROR";
        case USAGE: return "USAGE";
        case TEAPOT: return "TEAPOT";
        case INTERNAL_ERROR: return "INTERNAL_ERROR";
        case NOT_SUPPORTED: return "NOT_SUPPORTED";
        case BUSY: return "BUSY";
        case TIMEOUT: return "TIMEOUT";
        case TOO_DEEP: return "TOO_DEEP";
        case CONFLICT: return "CONFLICT";
        }
        return String.valueOf(rc);
    }
    
}
