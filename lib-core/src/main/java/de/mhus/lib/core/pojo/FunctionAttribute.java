/**
 * Copyright 2018 Mike Hummel
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.core.pojo;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import de.mhus.lib.core.MCast;

public class FunctionAttribute<T> implements PojoAttribute<T> {

    private Class<T> clazz;
    private Method getter;
    private Method setter;
    private String name;
    private FunctionAttribute<Object> parent;

    public FunctionAttribute(
            Class<T> clazz,
            Method getter,
            Method setter,
            String name,
            FunctionAttribute<Object> parent) {
        this.clazz = clazz;
        this.getter = getter;
        this.setter = setter;
        this.name = name;
        this.parent = parent;
    }

    @Override
    public Class<T> getManagedClass() {
        return clazz;
    }

    @Override
    public boolean canRead() {
        return getter != null;
    }

    @Override
    public boolean canWrite() {
        return setter != null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<T> getType() {
        if (getter != null) return (Class<T>) getter.getReturnType();
        else return (Class<T>) setter.getParameterTypes()[0];
    }

    @SuppressWarnings("unchecked")
    @Override
    public void set(Object pojo, T value) throws IOException {

        pojo = PojoParser.checkParent(parent, pojo);

        if (setter == null) throw new IOException("Method is read only: " + getter.getName());
        //		if (!getType().isInstance(pojo))
        //			throw new IOException("Object is not instance of " + getType().getCanonicalName());
        try {
            value = (T) MCast.toType(value, getType(), null);
            if (getType().isPrimitive() && value == null) {
                // that's not possible
                value = (T) MCast.getDefaultPrimitive(getType());
            }
            setter.invoke(pojo, value);
        } catch (Exception e) {
            throw new IOException("Error set: " + name + "(" + getType() + ")=" + value, e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public T get(Object pojo) throws IOException {

        pojo = PojoParser.checkParent(parent, pojo);

        //		if (!getType().isInstance(pojo))
        //			throw new IOException("Object is not instance of " + getType().getCanonicalName());
        try {
            return (T) getter.invoke(pojo);
        } catch (Exception e) {
            throw new IOException("Error get: " + name + "(" + clazz + ")", e);
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public <A extends Annotation> A getAnnotation(Class<? extends A> annotationClass) {
        A out = getter == null ? null : getter.getAnnotation(annotationClass);
        if (out == null && setter != null) out = setter.getAnnotation(annotationClass);
        return out;
    }

    @Override
    public String toString() {
        return "[" + name + "@FunctionAttribute]";
    }
}
