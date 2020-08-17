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
package de.mhus.lib.core.jmx;

import de.mhus.lib.annotations.jmx.JmxManaged;
import de.mhus.lib.core.MStopWatch;

@JmxManaged(descrition = "Simple Stop Watch")
public class JmxStopWatch extends MStopWatch {

    @Override
    @JmxManaged(descrition = "Current status of the watch")
    public String getStatusAsString() {
        return super.getStatusAsString();
    }

    @Override
    @JmxManaged(descrition = "Currently elapsed time")
    public String getCurrentTimeAsString() {
        return super.getCurrentTimeAsString();
    }

    @Override
    @JmxManaged(descrition = "Name of the watch")
    public String getName() {
        return super.getName();
    }
}
