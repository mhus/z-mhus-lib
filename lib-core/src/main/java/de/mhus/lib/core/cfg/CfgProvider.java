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
package de.mhus.lib.core.cfg;

import de.mhus.lib.core.MLog;
import de.mhus.lib.core.node.INode;

public abstract class CfgProvider extends MLog {

    private String name;

    public abstract INode getConfig();

    public CfgProvider(String name) {
        this.name = name;
    }

    public abstract void doRestart();

    public abstract void doStart();

    public abstract void doStop();

    public final String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
