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
package de.mhus.lib.core.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Map.Entry;

import de.mhus.lib.core.MProperties;
import de.mhus.lib.errors.MException;

public class PropertiesConfigBuilder extends IConfigBuilder {

    @Override
    public IConfig read(InputStream is) throws MException {
        try {
            MProperties p = MProperties.load(is);
            return readFromMap(p);
        } catch (IOException e) {
            throw new MException(e);
        }
    }

    @Override
    public void write(IConfig config, OutputStream os) throws MException {
        MProperties p = new MProperties(config);
        try {
            p.save(os);
        } catch (IOException e) {
            throw new MException(e);
        }
    }

    public IConfig readFromMap(Map<String, Object> map) {
        IConfig config = new MConfig();
        for (Entry<String, Object> entry : map.entrySet())
            config.put(entry.getKey(), entry.getValue());
        return config;
    }
}
