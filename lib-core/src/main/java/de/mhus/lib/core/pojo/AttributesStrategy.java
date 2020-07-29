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
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import de.mhus.lib.annotations.base.IgnoreBind;
import de.mhus.lib.annotations.generic.Public;
import de.mhus.lib.annotations.pojo.Embedded;
import de.mhus.lib.core.MCast;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.util.MObject;

@IgnoreBind
public class AttributesStrategy extends MObject implements PojoStrategy {

    private boolean embedded;
    private String embedGlue;
    private boolean toLower;
    private Class<? extends Annotation> annotationMarker[];
    private boolean allowPublic = true;

    public AttributesStrategy() {
        this(true, true, ".", null);
    }

    public AttributesStrategy(
            boolean embedded,
            boolean toLower,
            String embedGlue,
            Class<? extends Annotation>[] annotationMarker) {
        this.embedded = embedded;
        this.toLower = toLower;
        this.embedGlue = embedGlue;
        this.annotationMarker = annotationMarker;
    }

    @Override
    public void parseObject(PojoParser parser, Object pojo, PojoModelImpl model) {
        if (pojo instanceof Class) parse(parser, (Class<?>) pojo, model);
        else parseObject(parser, pojo.getClass(), model);
    }

    @Override
    public void parse(PojoParser parser, Class<?> clazz, PojoModelImpl model) {
        parse("", null, parser, clazz, model);
    }

    @SuppressWarnings("deprecation")
    protected void parse(
            String prefix,
            Attribute<Object> parent,
            PojoParser parser,
            Class<?> clazz,
            PojoModelImpl model) {

        for (Field field : MSystem.getAttributes(clazz)) {

            // ignore static fields
            if (Modifier.isStatic(field.getModifiers())) continue;

            // in this case we need to try accessibility without an object, we only have a class
            // structure
            if (!field.isAccessible()) {
                try {
                    field.setAccessible(true);
                } catch (Throwable t) {
                    log().d(
                                    "setAccessible",
                                    field.getDeclaringClass().getCanonicalName(),
                                    field.getName(),
                                    t);
                }
            }

            if (field.isAccessible() && (isMarker(field) || isEmbedded(field))) {
                Public desc = field.getAnnotation(Public.class);
                if (!allowPublic) desc = null;
                String name = field.getName();
                boolean readable = true;
                boolean writable = true;
                if (desc != null) {
                    if (desc.name().length() > 0) name = desc.name();
                    readable = desc.readable();
                    writable = desc.writable();
                }
                // final is in every case not writable
                if (Modifier.isFinal(field.getModifiers())) writable = false;

                String s = (toLower ? name.toLowerCase() : name);
                name = prefix + s;
                Attribute<Object> attr =
                        new Attribute<Object>(name, parent, field, readable, writable);
                if (isEmbedded(field)) {
                    parse(name + embedGlue, attr, parser, attr.getType(), model);
                } else {
                    if (!model.hasAttribute(name)) model.addAttribute(attr);
                }
            }
        }
    }

    protected boolean isEmbedded(Field field) {
        if (!embedded) return false;
        return field.isAnnotationPresent(Embedded.class);
    }

    protected boolean isMarker(Field field) {
        if (annotationMarker == null) return true;
        for (Class<? extends Annotation> a : annotationMarker)
            if (field.isAnnotationPresent(a)) return true;
        return false;
    }

    public boolean isAllowPublic() {
        return allowPublic;
    }

    public AttributesStrategy setAllowPublic(boolean allowPublic) {
        this.allowPublic = allowPublic;
        return this;
    }

    class Attribute<T> implements PojoAttribute<T> {

        private Field field;
        private String name;
        private Attribute<Object> parent;
        private boolean readable;
        private boolean writable;

        public Attribute(
                String name,
                Attribute<Object> parent,
                Field field,
                boolean readable,
                boolean writable) {
            this.name = name;
            this.field = field;
            this.parent = parent;
            this.readable = readable;
            this.writable = writable;
        }

        @SuppressWarnings("unchecked")
        @Override
        public T get(Object pojo) throws IOException {
            if (!readable) throw new IOException("field is write only: " + name);
            try {
                pojo = PojoParser.checkParent(parent, pojo);
                if (!field.canAccess(pojo)) field.setAccessible(true);
                return (T) field.get(pojo);
            } catch (Exception e) {
                throw new IOException(field.getName(), e);
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        public void set(Object pojo, T value, boolean force) throws IOException {
            if (!force && !writable) throw new IOException("field is read only: " + name);
            try {
                pojo = PojoParser.checkParent(parent, pojo);
                if (!field.canAccess(pojo)) field.setAccessible(true);

                value = (T) MCast.toType(value, getType(), null);
                if (getType().isPrimitive() && value == null) {
                    // that's not possible
                    value = (T) MCast.getDefaultPrimitive(getType());
                }

                field.set(pojo, value);
            } catch (Exception e) {
                throw new IOException(field.getName(), e);
            }
        }

        @Override
        public Class<?> getType() {
            return field.getType();
        }

        @Override
        public boolean canRead() {
            return readable;
        }

        @Override
        public boolean canWrite() {
            return writable;
        }

        @SuppressWarnings("unchecked")
        @Override
        public Class<T> getManagedClass() {
            return (Class<T>) field.getDeclaringClass();
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public <A extends Annotation> A getAnnotation(Class<? extends A> annotationClass) {
            return field.getAnnotation(annotationClass);
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
