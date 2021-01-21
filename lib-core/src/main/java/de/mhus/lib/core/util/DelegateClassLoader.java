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
package de.mhus.lib.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedList;

public class DelegateClassLoader extends ClassLoader {

    private LinkedList<Package> list = new LinkedList<Package>();

    public DelegateClassLoader() {}

    public DelegateClassLoader(ClassLoader parent) {
        super(parent);
    }

    public void register(ClassLoader loader) {
        synchronized (list) {
            list.add(new Package(loader));
        }
    }

    public void clear() {
        synchronized (list) {
            list.clear();
        }
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        synchronized (list) {
            for (Package p : list) {
                Class<?> clazz = p.loadClass(name, resolve);
                if (clazz != null) return clazz;
            }
        }
        return super.loadClass(name, resolve);
    }

    private class Package {

        private ClassLoader loader;

        public Package(ClassLoader loader) {
            this.loader = loader;
        }

        public Class<?> loadClass(String name, boolean resolve) {
            try {
                return loader.loadClass(name);
            } catch (ClassNotFoundException e) {
            }
            return null;
        }

        public URL getResource(String name) {
            return loader.getResource(name);
        }

        public InputStream getResourceAsStream(String name) {
            return loader.getResourceAsStream(name);
        }

        public Enumeration<URL> getResources(String name) {
            try {
                return loader.getResources(name);
            } catch (IOException e) {
            }
            return null;
        }
    }

    @Override
    public URL getResource(String name) {
        synchronized (list) {
            for (Package p : list) {
                URL out = p.getResource(name);
                if (out != null) return out;
            }
        }
        return super.getResource(name);
    }

    @Override
    public InputStream getResourceAsStream(String name) {
        synchronized (list) {
            for (Package p : list) {
                InputStream out = p.getResourceAsStream(name);
                if (out != null) return out;
            }
        }
        return super.getResourceAsStream(name);
    }

    @Override
    public Enumeration<URL> getResources(String name) throws IOException {
        synchronized (list) {
            for (Package p : list) {
                Enumeration<URL> out = p.getResources(name);
                if (out != null) return out;
            }
        }
        return super.getResources(name);
    }
}
