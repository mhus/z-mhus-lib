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

import java.lang.annotation.Annotation;

public class AnnotationFilter implements PojoFilter {

    private Class<? extends Annotation>[] allowed;

    @SafeVarargs
    public AnnotationFilter(Class<? extends Annotation>... allowed) {
        this.allowed = allowed;
    }

    @Override
    public void filter(PojoModelImpl model) {
        for (String name : model.getAttributeNames()) {
            PojoAttribute<?> attr = model.getAttribute(name);
            boolean done = false;
            for (Class<? extends Annotation> a : allowed)
                if (attr.getAnnotation(a) != null) {
                    done = true;
                    break;
                }
            if (!done) {
                model.removeAttribute(name);
            }
        }

        for (String name : model.getActionNames()) {
            PojoAction attr = model.getAction(name);
            boolean done = false;
            for (Class<? extends Annotation> a : allowed)
                if (attr.getAnnotation(a) != null) {
                    done = true;
                    break;
                }
            if (!done) {
                model.removeAction(name);
            }
        }
    }
}
