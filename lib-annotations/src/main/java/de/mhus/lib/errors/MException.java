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

import java.util.UUID;

public class MException extends Exception {

    private static final long serialVersionUID = 1L;

    private UUID errorId = UUID.randomUUID();

    public MException(Object... in) {
        super(argToString(4, in), argToCause(4, in));
    }

    @Override
    public String toString() {
        return errorId.toString() + " " + super.toString();
    }

    public static String argToString(int level, Object... in) {
        StringBuilder sb = new StringBuilder();
        for (Object o : in) {
            if (o instanceof Object[]) {
                sb.append("[");
                if (level < 0) sb.append(o);
                else sb.append(argToString(level - 1, o));
                sb.append("]");
            } else sb.append("[").append(o).append("]");
        }
        return sb.toString();
    }

    public static Throwable argToCause(int level, Object... in) {
        if (level < 0) return null;
        Throwable cause = null;
        for (Object o : in) {
            if ((o instanceof Throwable) && cause == null) {
                cause = (Throwable) o;
            } else if (o instanceof Object[]) {
                cause = argToCause(level - 1, o);
                if (cause != null) return cause;
            }
        }
        return cause;
    }

    public UUID getId() {
        return errorId;
    }
}
