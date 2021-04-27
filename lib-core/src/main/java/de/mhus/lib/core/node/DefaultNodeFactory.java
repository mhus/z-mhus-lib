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
package de.mhus.lib.core.node;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

import de.mhus.lib.core.MFile;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.errors.MException;
import de.mhus.lib.errors.NotFoundException;

public class DefaultNodeFactory implements INodeFactory {

    private HashMap<String, INodeBuilder> registry = new HashMap<>();

    public DefaultNodeFactory() {
        registry.put("xml", new XmlNodeBuilder());
        registry.put("json", new JsonNodeBuilder());
        registry.put("yml", new YamlNodeBuilder());
        registry.put("yaml", new YamlNodeBuilder());
        registry.put("properties", new PropertiesNodeBuilder());
    }

    @Override
    public INode read(Class<?> owner, String fileName) throws MException {
        try {
            URL url = MSystem.locateResource(owner, fileName);
            return read(url);
        } catch (IOException e) {
            throw new MException(fileName, e);
        }
    }

    @Override
    public INode read(File file) throws MException {
        String ext = MFile.getFileExtension(file);
        INodeBuilder builder = getBuilder(ext);
        if (builder == null)
            throw new NotFoundException("builder for resource not found", file.getName());
        return builder.readFromFile(file);
    }

    @Override
    public INode read(URL url) throws MException {
        String ext = MFile.getFileExtension(url.getPath());
        INodeBuilder builder = getBuilder(ext);
        if (builder == null) throw new NotFoundException("builder for resource not found", url);
        try (InputStream is = url.openStream()) {
            return builder.read(is);
        } catch (IOException e) {
            throw new MException(url, e);
        }
    }

    @Override
    public INodeBuilder getBuilder(String ext) {
        ext = ext.toLowerCase().trim(); // paranoia
        return registry.get(ext);
    }

    @Override
    public INode create() {
        return new MNode();
    }

    @Override
    public void write(INode node, File file) throws MException {
        String ext = MFile.getFileExtension(file);
        INodeBuilder builder = getBuilder(ext);
        if (builder == null)
            throw new NotFoundException("builder for resource not found", file.getName());
        builder.writeToFile(node, file);
    }
}
