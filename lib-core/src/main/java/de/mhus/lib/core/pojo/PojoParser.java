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
import java.lang.reflect.InvocationTargetException;

/**
 * Usage new Parser().parse(object).filter(new DefautFilter()).getModel()
 *
 * @author mikehummel
 */
public class PojoParser {

    private PojoModelImpl model;

    public PojoParser parse(Object pojo) {
        return parse(pojo, (PojoStrategy) null);
    }

    public PojoParser parse(
            Object pojo,
            String embedGlue,
            boolean allowPublic,
            Class<? extends Annotation>[] annotationMarker) {
        return parse(
                pojo,
                new DefaultStrategy(true, embedGlue, annotationMarker).setAllowPublic(allowPublic));
    }

    public PojoParser parse(
            Object pojo, String embedGlue, Class<? extends Annotation>[] annotationMarker) {
        return parse(pojo, new DefaultStrategy(true, embedGlue, annotationMarker));
    }

    public PojoParser parse(Object pojo, PojoStrategy strategy) {
        if (model == null) model = new PojoModelImpl(pojo.getClass());
        if (strategy == null) strategy = new DefaultStrategy();
        if (pojo instanceof Class) {
            strategy.parse(this, (Class<?>) pojo, model);
        } else {
            strategy.parseObject(this, pojo, model);
        }
        return this;
    }

    public PojoParser filter(
            boolean removeHidden,
            boolean removeEmbedded,
            boolean removeWriteOnly,
            boolean removeReadOnly,
            boolean removeNoActions) {
        return filter(
                new DefaultFilter(
                        removeHidden,
                        removeEmbedded,
                        removeWriteOnly,
                        removeReadOnly,
                        removeNoActions));
    }

    public PojoParser filter(PojoFilter filter) {
        filter.filter(model);
        return this;
    }

    public PojoModel getModel() {
        return model;
    }

    public static Object checkParent(PojoAttribute<Object> parent, Object pojo) throws IOException {
        Object out = pojo;
        if (parent != null) {
            out = parent.get(pojo);
            if (out == null) {
                try {
                    out = parent.getType().getDeclaredConstructor().newInstance();
                    parent.set(pojo, out, true);
                } catch (InstantiationException
                        | IllegalAccessException
                        | IllegalArgumentException
                        | InvocationTargetException
                        | NoSuchMethodException
                        | SecurityException e) {
                    throw new IOException("can't create parent: " + parent.getName(), e);
                }
            }
        }
        return out;
    }
}
