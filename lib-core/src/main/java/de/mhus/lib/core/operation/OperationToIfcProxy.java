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

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.MCast;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.definition.DefRoot;
import de.mhus.lib.core.util.Version;
import de.mhus.lib.errors.MException;
import de.mhus.lib.errors.NotFoundException;
import de.mhus.lib.form.definition.FmText;

public abstract class OperationToIfcProxy extends AbstractOperation {

    public static final String METHOD = "method";
    public static final String PARAMETER = "parameter";
    public static final String TYPE = "type";
    public static final String SERIALISED = ":s";
    public static final String PARAMETERTYPE = "ptype";
    public static final String NULL = ":null";
    public static final String PARAMETERORGTYPE = "otype";
    public static final String JSON = "json";
    public static final String VALUE = "value";

    protected abstract Class<?> getInterfaceClass();

    protected abstract Object getInterfaceObject();

    protected abstract Version getInterfaceVersion();

    protected abstract void initOperationDescription(HashMap<String, String> parameters);

    @Override
    protected OperationResult doExecute2(TaskContext context) throws Exception {

        Class<?> clazz = getInterfaceClass();
        ClassLoader cl = getClass().getClassLoader();

        IProperties p = context.getParameters();
        String methodName = p.getString(METHOD);
        Method method = null;

        m:
        for (Method m : clazz.getMethods()) {
            //			if (m.isAccessible()) {
            if (m.getName().equals(methodName)) {
                // check parameters
                Parameter[] mp = m.getParameters();
                for (int i = 0; i < mp.length; i++) {
                    String mpType = mp[i].getType().getCanonicalName();
                    String reqType = p.getString(PARAMETERTYPE + i, null);
                    if (reqType != null && reqType.equals("null")) {
                        reqType = p.getString(TYPE + i, null);
                    }
                    if (reqType == null || !mpType.equals(reqType)) continue m;
                }
                // check for more parameters
                if (p.containsKey(PARAMETERTYPE + mp.length)) continue;
                // found the right method with correct parameter types
                method = m;
                break;
            }
            //			}
        }

        if (method == null) throw new NotFoundException("Method not found", methodName, p);

        int pcount = method.getParameterCount();
        Object[] params = new Object[pcount];
        for (int i = 0; i < pcount; i++) {
            params[i] = toObject(p.get(PARAMETER + i), p.getString(TYPE + i), cl);
        }

        Object obj = getInterfaceObject();
        try {
            Object ret = method.invoke(obj, params);

            return new Successful(this, "", ret);
        } catch (InvocationTargetException e) {
            log().d(clazz, obj.getClass(), e);
            Throwable t = e;
            if (e.getCause() != null) t = e.getCause();
            if (t instanceof Exception) throw (Exception) t;
            if (t instanceof RuntimeException) throw (RuntimeException) t;
            throw new MException(e.toString());
        }
    }

    private Object toObject(Object value, String type, ClassLoader cl)
            throws ClassNotFoundException, IOException {
        if (type != null && type.equals(NULL)) {
            return null;
        }
        if (value == null) return MCast.getDefaultPrimitive(type);
        if (type != null && type.equals(SERIALISED)) {
            return MCast.unserializeFromString(String.valueOf(value), cl);
        }
        Class<?> t = MSystem.getClass(cl, type);
        return MCast.toType(value, t, null);
    }

    @Override
    protected OperationDescription createDescription() {
        Class<?> clazz = getInterfaceClass();
        DefRoot form = new DefRoot();
        for (Method m : clazz.getMethods()) {
            //			if (m.isAccessible()) {
            StringBuilder desc = new StringBuilder();
            desc.append(m.getReturnType().getCanonicalName());
            for (Parameter p : m.getParameters()) {
                desc.append(",");
                desc.append(p.getName()).append(":").append(p.getType());
            }
            FmText def = new FmText(m.getName(), m.getName(), desc.toString());
            form.addDefinition(def);
            //			}
        }

        OperationDescription out =
                new OperationDescription(
                        getUuid(),
                        clazz.getPackage().getName(),
                        clazz.getSimpleName(),
                        getInterfaceVersion(),
                        this,
                        this.getClass().getCanonicalName(),
                        form);
        out.putLabel(OperationDescription.TAG_TECH, OperationDescription.TECH_JAVA);
        initOperationDescription(out.getLabels());
        return out;
    }
}
