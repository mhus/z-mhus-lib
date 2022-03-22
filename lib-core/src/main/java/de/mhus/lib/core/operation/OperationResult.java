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

import de.mhus.lib.basics.IResult;
import de.mhus.lib.basics.RC;
import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.MProperties;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.node.INode;
import de.mhus.lib.core.node.MNode;
import de.mhus.lib.core.node.NodeSerializable;
import de.mhus.lib.errors.MRuntimeException;
import de.mhus.lib.errors.UsageException;

public class OperationResult implements IResult {

    protected String path;
    protected String msg;
    protected Object result; // technical result
    protected int returnCode = 0;

    protected OperationDescription nextOperation;

    public OperationResult() {}

    public OperationResult(OperationDescription description) {
        if (description != null) {
            path = description.getPath();
        }
    }

    public String getOperationPath() {
        return path;
    }

    /**
     * Use getMessage() instead
     *
     * @return Message
     */
    @Deprecated
    public String getMsg() {
        return msg;
    }

    public boolean isSuccessful() {
        return returnCode >= 0 && returnCode <= RC.RANGE_MAX_SUCCESSFUL;
    }

    public OperationDescription getNextOperation() {
        return nextOperation;
    }

    /**
     * Return the raw result. Don't use this method directly.
     *
     * @return The resulting object
     */
    @Deprecated
    public Object getResult() {
        return result;
    }

    @Deprecated
    public boolean isResult(Class<?> clazz) {
        return result != null && clazz.isInstance(result);
    }

    public IProperties getResultAsMap() {
        if (result == null) return new MProperties();
        if (result instanceof IProperties) return (IProperties) result;
        if (result instanceof Map) return new MProperties((Map<?, ?>) result);

        if (result instanceof String)
            try {
                INode ret = INode.readNodeFromString(String.valueOf(result));
                if (ret != null) return ret;
            } catch (Throwable t) {
                throw new MRuntimeException(RC.STATUS.CONFLICT, this, t);
            }
        throw new UsageException("Can't cast result to map", result.getClass());
    }

    public boolean isResultNull() {
        return result == null;
    }

    public String getResultAsString() {
        if (result == null) return "";
        if (result instanceof String) return (String) result;
        if (result instanceof byte[])
            return new String((byte[]) result, MString.CHARSET_CHARSET_UTF_8);
        return String.valueOf(result);
    }

    @Override
    public String toString() {
        return MSystem.toString(this, path, returnCode, msg, nextOperation, result);
    }

    @Override
    public int getReturnCode() {
        return returnCode;
    }

    public boolean isEmpty() {
        return result == null;
    }

    /**
     * Try to convert the result into a INode object. Therefore a string is analyzed to be a json or
     * xml and will be readed as INode. Also INode or IProperty will be transformed.
     *
     * @return A INode object or a RuntimeException
     */
    @SuppressWarnings("unchecked")
    public INode getResultAsNode() {
        if (result == null) return new MNode();
        try {
            if (result instanceof INode) return (INode) result;
            if (result instanceof IProperties) {
                MNode ret = new MNode();
                ret.putAll((IProperties) result);
                return ret;
            }
            if (result instanceof Map)
                return INode.readFromProperties((Map<String, Object>) result);

            // fallback
            INode ret = INode.readNodeFromString(String.valueOf(result));
            if (ret != null) return ret;

            throw new UsageException("Can't cast result to node", result.getClass());

        } catch (Exception e) {
            throw new MRuntimeException(RC.STATUS.CONFLICT, this, e);
        }
    }

    /**
     * Get a INode result and load it via NodeSynchronize mechanism into the given object.
     *
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
            throw new MRuntimeException(RC.STATUS.CONFLICT, this, e);
        }
        return fillIn;
    }

    @Override
    public String getMessage() {
        return getMsg();
    }
}
