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
package de.mhus.lib.core.pojo;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Set;

public class FunctionAction implements PojoAction {

    private Class<?> clazz;
    private Method action;
    private String name;
    private FunctionAttribute<Object> parent;

    public FunctionAction(
            Class<?> clazz, Method action, String name, FunctionAttribute<Object> parent) {
        this.clazz = clazz;
        this.action = action;
        this.name = name;
        this.parent = parent;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object doExecute(Object pojo, Object... args) throws IOException {

        pojo = PojoParser.checkParent(parent, pojo);

        try {
            return action.invoke(pojo, args);
        } catch (Exception e) {
            throw new IOException("Execution of action [" + name + "] failed", e);
        }
    }

    @Override
    public Annotation getAnnotation(Class<? extends Annotation> annotationClass) {

        Annotation out = action.getAnnotation(annotationClass);
        if (out != null) return out;

        Set<Method> res = MethodAnalyser.getMethodsForMethod(clazz, action.getName());
        for (Method m2 : res) {
            out = m2.getAnnotation(annotationClass);
            if (out != null) return out;
        }

        return null;
    }

    @Override
    public Class<?> getManagedClass() {
        return clazz;
    }

    @Override
    public Class<?> getReturnType() {
        return action.getReturnType();
    }

    @Override
    public Class<?>[] getParameterType() {
        return action.getParameterTypes();
    }
}
