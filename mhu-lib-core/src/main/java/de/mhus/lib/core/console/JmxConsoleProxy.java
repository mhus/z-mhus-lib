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
package de.mhus.lib.core.console;

import de.mhus.lib.annotations.jmx.JmxManaged;
import de.mhus.lib.core.jmx.MJmx;

@JmxManaged(descrition = "Virtual console management interface")
public class JmxConsoleProxy extends MJmx {

    private JmxConsole console;

    public JmxConsoleProxy(JmxConsole jmxConsole) {
        console = jmxConsole;
    }

    @JmxManaged(descrition = "Simulate typing")
    public void print(String in) {
        console.getInputWriter().print(in);
    }

    @JmxManaged(descrition = "Simulate typing with ENTER at the end")
    public void println(String in) {
        console.getInputWriter().println(in);
    }

    @JmxManaged(descrition = "Show the monochome display")
    public String getDisplay() {
        return console.getMonoDisplayAsString();
    }

    @JmxManaged(descrition = "Resize the display, Parameters: width, height")
    public void resize(int width, int height) {
        console.resize(width, height);
    }
}
