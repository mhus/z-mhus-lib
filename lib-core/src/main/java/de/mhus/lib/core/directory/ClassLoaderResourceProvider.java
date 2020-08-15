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
package de.mhus.lib.core.directory;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import de.mhus.lib.core.MCollection;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.config.ConfigList;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.config.MConfig;

public class ClassLoaderResourceProvider extends MResourceProvider {

    private ClassLoader loader;

    public ClassLoaderResourceProvider() {
        // this(Thread.currentThread().getContextClassLoader());
        this(ClassLoaderResourceProvider.class.getClassLoader());
    }

    public ClassLoaderResourceProvider(ClassLoader loader) {
        this.loader = loader;
    }

    @Override
    public IConfig getResourceByPath(String name) {
        return new CLResourceNode(name);
    }

    public ClassLoader getClassLoader() {
        return loader;
    }

    public void setClassLoader(ClassLoader loader) {
        this.loader = loader;
    }

    private static class CLResourceNode extends MConfig {

        private static final long serialVersionUID = 1L;
        private String name;

        public CLResourceNode(String name) {
            this.name = name;
        }

        @Override
        public List<String> getPropertyKeys() {
            return MCollection.getEmptyList();
        }

        @Override
        public IConfig getObject(String key) {
            return null;
        }

        @Override
        public List<IConfig> getObjects() {
            return MCollection.getEmptyList();
        }

        @Override
        public ConfigList getArray(String key) {
            return new ConfigList(key, this);
        }

        @Override
        public List<String> getObjectKeys() {
            return MCollection.getEmptyList();
        }

        @Override
        public String getName() {
            return MString.afterLastIndex(name, '/');
        }

        @Override
        public IConfig getParent() {
            return null;
        }

        @Override
        public Object getProperty(String name) {
            return null;
        }

        @Override
        public boolean isProperty(String name) {
            return false;
        }

        @Override
        public void removeProperty(String key) {}

        @Override
        public void setProperty(String key, Object value) {}

        @Override
        public boolean isEditable() {
            return false;
        }
    }

    @Override
    public IConfig getResourceById(String id) {
        return getResourceByPath(id);
    }

    @Override
    public String getName() {
        return MSystem.getObjectId(this);
    }

    @Override
    public InputStream getInputStream(String key) {
        if (key == null) return loader.getResourceAsStream(key);
        return null;
    }

    @Override
    public URL getUrl(String key) {
        return loader.getResource(key);
    }

    @Override
    public boolean isValid() {
        return true;
    }
}
