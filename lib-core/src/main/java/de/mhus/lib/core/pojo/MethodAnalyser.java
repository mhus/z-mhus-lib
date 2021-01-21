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

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public class MethodAnalyser {

    private static void traverseInterfacesForMethod(
            Class<?> cls, Set<Class<?>> result, String methodName) {
        for (Class<?> c : cls.getInterfaces()) {
            for (Method m : c.getDeclaredMethods()) {
                if (methodName.equals(m.getName())) {
                    result.add(c);
                }
            }

            traverseInterfacesForMethod(c, result, methodName);
        }
    }

    public static Set<Class<?>> getInterfacesForMethod(Class<?> cls, String methodName) {
        Set<Class<?>> result = new HashSet<>();
        traverseInterfacesForMethod(cls, result, methodName);
        return result;
    }

    private static void traverseMethodsForMethod(
            Class<?> cls, Set<Method> result, String methodName) {
        for (Class<?> c : cls.getInterfaces()) {
            for (Method m : c.getDeclaredMethods()) {
                if (methodName.equals(m.getName())) {
                    result.add(m);
                }
            }

            traverseMethodsForMethod(c, result, methodName);
        }
    }

    public static Set<Method> getMethodsForMethod(Class<?> cls, String methodName) {
        Set<Method> result = new HashSet<>();
        traverseMethodsForMethod(cls, result, methodName);
        return result;
    }
}
