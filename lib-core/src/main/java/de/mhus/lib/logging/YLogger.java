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
package de.mhus.lib.logging;

import de.mhus.lib.core.logging.Log;

public class YLogger extends Log {

    private Log[] targets;

    public YLogger(Log... targets) {
        super(targets[0].getName());
        this.targets = targets;
    }

    @Override
    public void log(LEVEL level, Object... msg) {
        for (Log target : targets) target.log(level, msg);
    }

    @Override
    public void update() {}

    //	@Override
    //	public void register() {
    //	}
    //
    //	@Override
    //	public void unregister() {
    //	}

    @Override
    public boolean isLevelEnabled(LEVEL level) {
        return targets[0].isLevelEnabled(level);
    }

    @Override
    public void close() {
        for (Log target : targets) target.close();
    }
}
