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

import de.mhus.lib.basics.IResult;
import de.mhus.lib.basics.RC;

public class NotSuccessful extends MutableOperationResult {

    public NotSuccessful() {
        super();
    }

    public NotSuccessful(Operation operation, int rc, String msg, Object... parameters) {
        super(operation, rc, msg, parameters);
    }

    public NotSuccessful(OperationDescription description) {
        super(description);
    }

    public NotSuccessful(String path, int rc, String msg, Object... parameters) {
        super(path, rc, msg, parameters);
    }

    public NotSuccessful(Operation operation, IResult cause) {
        super(operation, cause.getReturnCode(), null);
        setMsg(cause.getMessage());
    }

    public NotSuccessful(OperationDescription description, IResult cause) {
        super(description);
        setReturnCode(cause.getReturnCode());
        setMsg(cause.getMessage());
    }

    public NotSuccessful(String path, IResult cause) {
        super(path, cause.getReturnCode(), null);
        setMsg(cause.getMessage());
    }

    public NotSuccessful(Operation operation, IResult cause, String msg, Object... parameters) {
        super(operation, cause.getReturnCode(), null);
        setMsg(RC.toMessage(cause, msg, parameters, 0));
    }

    public NotSuccessful(OperationDescription description, IResult cause, String msg, Object... parameters) {
        super(description);
        setReturnCode(cause.getReturnCode());
        setMsg(RC.toMessage(cause, msg, parameters, 0));
    }

    public NotSuccessful(String path, IResult cause, String msg, Object... parameters) {
        super(path, cause.getReturnCode(), null);
        setMsg(RC.toMessage(cause, msg, parameters, 0));
    }
    
    @Override
    public void setReturnCode(int returnCode) {
        /*
        if (returnCode <= RC.RANGE_MAX_SUCCESSFUL) {
            MLogUtil.log().d("de.mhus.lib.core.operation.NotSuccessful: wrong return code",returnCode);
            this.returnCode = RC.INTERNAL_ERROR;
        } else
        if (returnCode > RC.RANGE_MAX) {
            MLogUtil.log().d("de.mhus.lib.core.operation.NotSuccessful: return code out of range",returnCode);
            this.returnCode = RC.INTERNAL_ERROR;
        } else
        */
        this.returnCode = returnCode;
    }

}
