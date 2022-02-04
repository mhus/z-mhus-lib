/**
 * Copyright (C) 2002 Mike Hummel (mh@mhus.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.errors;

import de.mhus.lib.basics.RC;

public class MException extends Exception {

    private static final long serialVersionUID = 1L;

    private int rc;

    public MException(RC.STATUS rc, Object... in) {
        super(argToString(rc.name(), in),argToCause(in));
        setReturnCode(rc.rc());
    }

    public MException(int rc, Throwable cause) {
        super(argToString(cause.getMessage()),cause);
        setReturnCode(rc);
    }

    public MException(MException cause) {
        super(cause.getMessage(), cause);
        setReturnCode(cause.getReturnCode());
    }

    public MException(int rc, String msg, Object... in) {
        super(argToString(msg, in), argToCause(in));
        setReturnCode(rc);
    }

    public MException(int rc) {
        super(RC.toString(rc));
        setReturnCode(rc);
    }
    
    @Override
    public String toString() {
        return getReturnCode() + "|" + super.toString();
    }

    public static String argToString(String msg, Object... in) {
        return RC.toResponseString(msg, in);
    }

    public static Throwable argToCause(Object... in) {
        if (in == null) return null;
        for (Object o : in) {
            if (o instanceof Throwable) {
                return (Throwable) o;
            }
        }
        return null;
    }

    public int getReturnCode() {
        return rc;
    }

    protected void setReturnCode(int rc) {
        this.rc = rc;
    }
}
