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

import de.mhus.lib.core.operation.Operation;
import de.mhus.lib.core.operation.OperationResult;

@Deprecated
public class SuccessfulObject extends OperationResult {

    public SuccessfulObject(Operation operation, String msg, Object result) {
        this(operation.getDescription().getPath(), msg, 0, result);
        setCaption(operation.getDescription().getCaption());
    }

    public SuccessfulObject(Operation operation, String msg, int rc, Object result) {
        this(operation.getDescription().getPath(), msg, rc, result);
        setCaption(operation.getDescription().getCaption());
    }

    public SuccessfulObject(String path, String msg, Object result) {
        this(path, msg, 0, result);
    }

    public SuccessfulObject(String path, String msg, int rc, Object result) {
        setOperationPath(path);
        setResult(result);
        setMsg(msg);
        setReturnCode(rc);
        setSuccessful(true);
    }
}
