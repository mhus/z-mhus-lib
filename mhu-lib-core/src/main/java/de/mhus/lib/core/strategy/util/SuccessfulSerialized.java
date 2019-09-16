/**
 * Copyright 2018 Mike Hummel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.core.strategy.util;

import java.io.Serializable;

import de.mhus.lib.core.lang.SerializedValue;
import de.mhus.lib.core.strategy.Operation;
import de.mhus.lib.core.strategy.Successful;

public class SuccessfulSerialized extends Successful {

    public SuccessfulSerialized(Operation operation, String msg, long rc, Object result) {
        super(operation, msg, rc, result);
    }

    public SuccessfulSerialized(Operation operation, String msg, Object result) {
        super(operation, msg, result);
    }

    public SuccessfulSerialized(Operation operation, String msg, String... keyValues) {
        super(operation, msg, keyValues);
    }

    public SuccessfulSerialized(Operation operation) {
        super(operation);
    }

    public SuccessfulSerialized(String path, String msg, long rc, Object result) {
        super(path, msg, rc, result);
    }

    public SuccessfulSerialized(String path, String msg, long rc, String... keyValues) {
        super(path, msg, rc, keyValues);
    }

    public SuccessfulSerialized(String path) {
        super(path);
    }

    @Override
    public void setResult(Object result) {
        if (result != null && result instanceof Serializable && ! (result instanceof SerializedValue))
            result = new SerializedValue((Serializable) result);
        super.setResult(result);
    }

}
