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
package de.mhus.lib.core.cfg;

import de.mhus.lib.core.MApi;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.util.SecureString;

public class CfgSecure extends CfgValue<SecureString> {

    public CfgSecure(Object owner, String path, String def) {
        this(owner, path, def == null ? null : new SecureString(def));
    }

    public CfgSecure(Object owner, String path, SecureString def) {
        super(owner, path, def);
    }

    @Override
    protected SecureString loadValue() {
        int p = getPath().indexOf('@');
        if (p < 0)
            return new SecureString(
                    MApi.getCfg(getOwner()).getString(getPath(), strValueOf(getDefault())));
        IConfig node = MApi.getCfg(getOwner()).getNodeByPath(getPath().substring(0, p));
        if (node == null) return getDefault();
        return new SecureString(
                node.getString(getPath().substring(p + 1), strValueOf(getDefault())));
    }

    private String strValueOf(SecureString def) {
        if (def == null) return null;
        return def.value();
    }

    @Override
    protected SecureString loadValue(String value) {
        return new SecureString(value);
    }

    public String valueAsString() {
        SecureString v = value();
        return v == null ? null : v.value();
    }
}
