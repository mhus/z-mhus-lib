/**
 * Copyright (C) 2020 Mike Hummel (mh@mhus.de)
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
package de.mhus.lib.core.console;

import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.logging.MLogUtil;

public class DefaultConsoleFactory implements ConsoleFactory {

    @Override
    public Console create() {
        try {
            if (MSystem.isWindows()) {
                return new CmdConsole();
            }
            String term = System.getenv("TERM");
            if (term != null) {
                term = term.toLowerCase();
                if (term.indexOf("xterm") >= 0) {
                    return new XTermConsole();
                }
                if (term.indexOf("ansi") >= 0) return new ANSIConsole();
            }
        } catch (Throwable t) {
            MLogUtil.log().d(t);
        }
        return new SimpleConsole();
    }
}
