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
package de.mhus.lib.core.operation.util;

import java.util.Map;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.operation.Operation;

public class SuccessfulForceMap extends SuccessfulMap {

    public SuccessfulForceMap(Operation operation, String msg) {
        super(operation, msg);
    }

    public SuccessfulForceMap(String path, int rc, String msg, String... keyValues) {
        super(path, rc, msg, keyValues);
    }

    public SuccessfulForceMap(String path, int rc, String msg) {
        super(path, rc, msg);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void setResult(Object result) {
        super.setResult(new MapValue((Map<?, ?>) result));
    }

    @SuppressWarnings("deprecation")
    @Override
    public void setResultNode(Map<String, Object> result) {
        super.setResult(new MapValue(result));
    }

    @Override
    @SuppressWarnings({"deprecation"})
    public IProperties getMap() {
        return (IProperties) ((MapValue) getResult()).getValue();
    }
}
