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
package de.mhus.lib.core.operation;

import java.util.Map;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.MProperties;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.config.MConfig;
import de.mhus.lib.core.util.MUri;
import de.mhus.lib.errors.MRuntimeException;
import de.mhus.lib.errors.UsageException;

public class OperationResult {

    public static final int OK = 0;
    public static final int EMPTY = -10;
    public static final int BUSY = -11;
    public static final int NOT_EXECUTABLE = -12;
    public static final int SYNTAX_ERROR = -13;
    public static final int USAGE = -14;

    public static final int INTERNAL_ERROR = -500;
    public static final int ACCESS_DENIED = -401;
    public static final int NOT_FOUND = -404;
    public static final int NOT_SUPPORTED = -505;
    public static final int WRONG_STATUS = -506;

    private String operationPath;
    private String caption;
    private String msg;
    private Object result; // technical result
    private boolean successful;
    private long returnCode = 0;

    private OperationDescription nextOperation;

    public OperationResult() {}

    public OperationResult(OperationDescription description) {
        if (description != null) {
            setOperationPath(description.getGroup() + "/" + description.getId());
            setCaption(description.getCaption());
        }
    }

    public String getOperationPath() {
        return operationPath;
    }

    public void setOperationPath(String operationPath) {
        this.operationPath = operationPath;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String title) {
        this.caption = title;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public OperationDescription getNextOperation() {
        return nextOperation;
    }

    public void setNextOperation(OperationDescription nextOperation) {
        this.nextOperation = nextOperation;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public boolean isResult(Class<?> clazz) {
        return result != null && clazz.isInstance(result);
    }

    public IProperties getResultAsMap() {
        if (result == null) return new MProperties();
        if (result instanceof IProperties) return (IProperties) result;
        if (result instanceof Map) return new MProperties((Map<?, ?>) result);
        throw new UsageException("Can't cast result to map", result.getClass());
    }

    public String getResultAsString() {
        if (result == null) return "";
        if (result instanceof String) return (String) result;
        return String.valueOf(result);
    }

    @Override
    public String toString() {
        return MSystem.toString(this, operationPath, successful, msg, nextOperation); // result ?
    }

    public long getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(long returnCode) {
        this.returnCode = returnCode;
    }

    public String getMsgCaption() {
        if (MString.isSet(msg) && msg.startsWith("m=")) {
            Map<String, String> msgParts = MUri.explode(msg);
            return msgParts.get("c");
        }
        return null;
    }

    public String getMsgMessage() {
        if (MString.isSet(msg) && msg.startsWith("m=")) {
            Map<String, String> msgParts = MUri.explode(msg);
            return msgParts.get("m");
        }
        return msg;
    }

    public boolean isEmpty() {
        return result == null;
    }

    @SuppressWarnings("unchecked")
    public IConfig getResultAsConfig() {
        if (result == null) return new MConfig();
        try {
            if (result instanceof IConfig) return (IConfig) result;
            if (result instanceof String) return IConfig.readConfigFromString((String) result);
            if (result instanceof Map)
                return IConfig.readFromProperties((Map<String, Object>) result);

            // fallback
            return IConfig.readConfigFromString(String.valueOf(result));

        } catch (Exception e) {
            throw new MRuntimeException(this, e); // or empty config?
        }
    }
}
