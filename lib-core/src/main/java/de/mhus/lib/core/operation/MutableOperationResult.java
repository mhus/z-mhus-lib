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
package de.mhus.lib.core.operation;

import java.util.Map;

import de.mhus.lib.basics.RC;

public class MutableOperationResult extends OperationResult {

    public MutableOperationResult() {}

    public MutableOperationResult(OperationDescription description) {
        if (description != null) {
            setOperationPath(description.getPath());
        }
    }

    public MutableOperationResult(String path, int rc, String msg, Object... parameters) {
        setMsg(RC.toMessage(rc, RC.CAUSE.IGNORE, msg, parameters, 0));
        setOperationPath(path);
        setReturnCode(rc);
    }

    public MutableOperationResult(Operation operation, int rc, String msg, Object... parameters) {
        setMsg(RC.toMessage(rc, RC.CAUSE.IGNORE, msg, parameters, 0));
        setReturnCode(rc);
        if (operation != null && operation.getDescription() != null) {
            setOperationPath(operation.getDescription().getPath());
        }
    }

    public void setOperationPath(String operationPath) {
        this.path = operationPath;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setNextOperation(OperationDescription nextOperation) {
        this.nextOperation = nextOperation;
    }

    /**
     * Set the result as object. Don't use it directly. Should be stored as INode or Map.
     *
     * @param result
     */
    @Deprecated
    public void setResult(Object result) {
        this.result = result;
    }

    public void setResultNode(Map<String, Object> result) {
        this.result = result;
    }

    public void setResultString(String result) {
        this.result = result;
    }

    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }
}
