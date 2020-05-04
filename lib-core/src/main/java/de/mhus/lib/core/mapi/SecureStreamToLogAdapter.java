/**
 * Copyright 2018 Mike Hummel
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.core.mapi;

import java.io.PrintStream;

import de.mhus.lib.core.logging.Log.LEVEL;
import de.mhus.lib.core.logging.StreamToLogAdapter;

public class SecureStreamToLogAdapter extends StreamToLogAdapter {

    protected static ThreadLocal<Boolean> enter = new ThreadLocal<>();

    public SecureStreamToLogAdapter(LEVEL level, PrintStream forward) {
        super(level, forward);
    }

    @Override
    protected void writeLine() {
        if (enter.get() != null) return;
        enter.set(true);
        try {
            log.log(level, line);
            line.setLength(0);
        } finally {
            enter.remove();
        }
    }
}
