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
package de.mhus.lib.form.definition;

import de.mhus.lib.core.config.HashConfig;
import de.mhus.lib.core.definition.DefComponent;
import de.mhus.lib.core.definition.IDefAttribute;
import de.mhus.lib.errors.MException;

public class FaSource extends HashConfig implements IDefAttribute {

    private static final long serialVersionUID = 1L;
    private String tag;

    public FaSource(String tag, String name) {
        super(tag, null);
        this.tag = tag;
        setString("name", name);
    }

    @Override
    public void inject(DefComponent root) throws MException {
        HashConfig sources = (HashConfig) root.getNode("sources");
        if (sources == null) {
            sources = (HashConfig) root.createConfig("sources");
        }
        sources.setConfig(tag, this);
    }
}
