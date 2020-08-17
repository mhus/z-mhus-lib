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
package de.mhus.lib.core.pojo;

import java.io.IOException;
import java.lang.annotation.Annotation;

public interface PojoAttribute<T> {

    T get(Object pojo) throws IOException;

    void set(Object pojo, T value, boolean force) throws IOException;

    Class<?> getType();

    boolean canRead();

    boolean canWrite();

    Class<T> getManagedClass();

    String getName();

    <A extends Annotation> A getAnnotation(Class<? extends A> annotationClass);
}
