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
package de.mhus.lib.core.directory;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import de.mhus.lib.core.MCollection;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.node.NodeList;
import de.mhus.lib.core.node.INode;
import de.mhus.lib.core.node.MNode;

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
    public INode getResourceByPath(String name) {
        return new CLResourceNode(name);
    }

    public ClassLoader getClassLoader() {
        return loader;
    }

    public void setClassLoader(ClassLoader loader) {
        this.loader = loader;
    }

    private static class CLResourceNode extends MNode {

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
        public INode getObject(String key) {
            return null;
        }

        @Override
        public List<INode> getObjects() {
            return MCollection.getEmptyList();
        }

        @Override
        public NodeList getArray(String key) {
            return new NodeList(key, this);
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
        public INode getParent() {
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
    public INode getResourceById(String id) {
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
