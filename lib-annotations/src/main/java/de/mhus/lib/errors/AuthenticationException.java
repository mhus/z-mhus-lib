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

public class AuthenticationException extends MRuntimeException {

    private static final long serialVersionUID = 1L;

    public AuthenticationException() {
        super(RC.STATUS.ACCESS_DENIED);
    }

    public AuthenticationException(
            String message,
            Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace) {
        super(RC.ACCESS_DENIED, message, cause, enableSuppression, writableStackTrace);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(RC.ACCESS_DENIED, message, cause);
    }

    public AuthenticationException(String message) {
        super(RC.ACCESS_DENIED, message);
    }

    public AuthenticationException(Throwable cause) {
        super(RC.ACCESS_DENIED, cause);
    }
}
