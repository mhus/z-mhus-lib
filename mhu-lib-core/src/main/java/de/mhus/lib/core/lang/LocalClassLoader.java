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
package de.mhus.lib.core.lang;

import java.util.Arrays;
import java.util.HashMap;

/**
 * This class loader contains local classes with byte code and will provide them preferred.
 *
 * @author mikehummel
 */
public class LocalClassLoader extends ClassLoader {

    private HashMap<String, byte[]> localCode = new HashMap<>();
    private HashMap<String, Class<?>> localClasses = new HashMap<>();

    public LocalClassLoader() {
        super();
    }

    public LocalClassLoader(ClassLoader parent) {
        super(parent);
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class<?> clazz = localClasses.get(name);
        if (clazz != null) return clazz;

        byte[] code = localCode.get(name);
        if (code != null) {
            clazz = defineClass(name, code, 0, code.length);
            if (resolve) resolveClass(clazz);
            localClasses.put(name, clazz);
        }
        if (clazz != null) return clazz;

        return super.loadClass(name, resolve);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        Class<?> clazz = localClasses.get(name);
        if (clazz != null) return clazz;

        byte[] code = localCode.get(name);
        if (code != null) {
            clazz = defineClass(name, code, 0, code.length);
            resolveClass(clazz);
            localClasses.put(name, clazz);
        }
        if (clazz != null) return clazz;

        return super.findClass(name);
    }

    /**
     * Add new class code. The method is not synchronized.
     *
     * @param name
     * @param code
     * @throws AlreadyBoundException
     */
    public void addClassCode(String name, byte[] code) throws AlreadyBoundException {
        if (localClasses.containsKey(name)) throw new AlreadyBoundException(name);
        localCode.put(name, Arrays.copyOf(code, code.length));
    }

    /**
     * Remove class code. The method is not synchronized.
     *
     * @param name
     */
    public void removeClassCode(String name) {
        localCode.remove(name);
        localClasses.remove(name);
    }
}
