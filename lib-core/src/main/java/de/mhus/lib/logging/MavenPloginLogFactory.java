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

import org.apache.maven.plugin.AbstractMojo;

import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.logging.LogEngine;
import de.mhus.lib.core.logging.LogFactory;

public class MavenPloginLogFactory extends LogFactory {

    private AbstractMojo owner;

    public MavenPloginLogFactory(AbstractMojo owner) {
        this.owner = owner;
    }

    @Override
    public void init(IConfig config) throws Exception {}

    @Override
    public LogEngine createInstance(String name) {
        return new MavenPluginLogEngine(owner, name);
    }
}
