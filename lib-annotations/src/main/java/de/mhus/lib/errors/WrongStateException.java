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

import de.mhus.lib.basics.IResult;
import de.mhus.lib.basics.RC;
import de.mhus.lib.basics.RC.STATUS;

public class WrongStateException extends MRuntimeException {

    private static final long serialVersionUID = 1L;

    public static STATUS getDefaultStatus() {
        return RC.STATUS.CONFLICT;
    }

    public WrongStateException(Object... in) {
        super(getDefaultStatus(),in);
    }

    public WrongStateException(RC.CAUSE causeHandling, Object... in) {
        super(causeHandling, getDefaultStatus(), in);
    }

    public WrongStateException(Throwable cause) {
        super(getDefaultStatus().rc(), cause);
    }

    public WrongStateException(IResult cause) {
        super(cause);
    }

    public WrongStateException(String msg, Object... in) {
        super(getDefaultStatus().rc(), msg, in);
    }

    public WrongStateException(RC.CAUSE causeHandling, String msg, Object... parameters) {
        super(causeHandling, getDefaultStatus().rc(), msg, parameters);
    }

    public WrongStateException(int rc) {
        super(getDefaultStatus().rc());
    }
}
