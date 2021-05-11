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

import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.MProperties;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.node.NodeSerializable;
import de.mhus.lib.core.node.INode;
import de.mhus.lib.core.node.MNode;
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
    private int returnCode = 0;

    private OperationDescription nextOperation;

    public OperationResult() {}

    public OperationResult(OperationDescription description) {
        if (description != null) {
            setOperationPath(description.getPath());
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

    /**
     * Return the raw result. Don't use this method directly.
     * @return The resulting object
     */
    @Deprecated
    public Object getResult() {
        return result;
    }

    /**
     * Set the result as object. Don't use it directly. Should be stored as INode or Map.
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

    @Deprecated
    public boolean isResult(Class<?> clazz) {
        return result != null && clazz.isInstance(result);
    }

    public IProperties getResultAsMap() {
        if (result == null) return new MProperties();
        if (result instanceof IProperties) return (IProperties) result;
        if (result instanceof Map) return new MProperties((Map<?, ?>) result);
        throw new UsageException("Can't cast result to map", result.getClass());
    }

    public boolean isResultNull() {
        return result == null;
    }
    
    public String getResultAsString() {
        if (result == null) return "";
        if (result instanceof String) return (String) result;
        if (result instanceof byte[]) return new String( (byte[])result, MString.CHARSET_CHARSET_UTF_8 );
        return String.valueOf(result);
    }

    @Override
    public String toString() {
        return MSystem.toString(this, operationPath, successful, msg, nextOperation, result);
    }

    public int getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(int returnCode) {
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

    /**
     * Try to convert the result into a INode object. Therefore a string is analyzed to be a
     * json or xml and will be readed as INode. Also INode or IProperty will be transformed.
     * 
     * @return A INode object or a RuntimeException
     */
    @SuppressWarnings("unchecked")
    public INode getResultAsNode() {
        if (result == null) return new MNode();
        try {
            if (result instanceof String) return INode.readNodeFromString((String) result);
            if (result instanceof INode) return (INode) result;
            if (result instanceof IProperties) {
                MNode ret = new MNode();
                ret.putAll( (IProperties)result );
                return ret;
            }
            if (result instanceof Map)
                return INode.readFromProperties((Map<String, Object>) result);

            // fallback
            return INode.readNodeFromString(String.valueOf(result));

        } catch (Exception e) {
            throw new MRuntimeException(this, e); // or empty node?
        }
    }

    /**
     * Get a INode result and load it via NodeSynchronize mechanism into the given object.
     * @param <T> Type of the given object, must be NodeSerializable
     * @param fillIn The object to fill
     * @return The filled Object given in fillIn
     */
    public <T extends NodeSerializable> T loadResult(T fillIn) {
        if (result == null) return fillIn;
        INode cfg = getResultAsNode();
        try {
            fillIn.readSerializabledNode(cfg);
        } catch (Exception e) {
            throw new MRuntimeException(this, e);
        }
        return fillIn;
    }

}
