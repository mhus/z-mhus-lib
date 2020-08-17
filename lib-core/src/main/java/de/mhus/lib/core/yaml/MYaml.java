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
package de.mhus.lib.core.yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

public class MYaml {

    private static Yaml yaml;

    public static YMap load(File file) throws FileNotFoundException, IOException {
        try (InputStream is = new FileInputStream(file)) {
            return load(is);
        }
    }

    public static YMap load(InputStream is) {
        getYaml();
        Object obj = yaml.load(is);
        return new YMap(obj);
    }

    public static synchronized Yaml getYaml() {
        if (yaml == null) {
            DumperOptions options = new DumperOptions();
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            options.setPrettyFlow(true);
            yaml = new Yaml(options);
        }
        return yaml;
    }

    @SuppressWarnings("rawtypes")
    public static YElement loadFromString(String content) {
        getYaml();
        Object obj = yaml.load(content);
        if (obj instanceof Map) return new YMap((Map) obj);
        if (obj instanceof List) return new YList((List) obj);
        return new YElement(obj);
    }

    public static YMap loadMapFromString(String content) {
        getYaml();
        YMap docE = new YMap(yaml.load(content));
        return docE;
    }

    public static YList loadListFromString(String content) {
        getYaml();
        YList docE = new YList(yaml.load(content));
        return docE;
    }

    public static YMap createMap() {
        return new YMap(new LinkedHashMap<String, Object>());
    }

    public static YList createList() {
        return new YList(new LinkedList<>());
    }

    public static void write(YElement elemY, OutputStream os) throws IOException {
        try (Writer writer = new OutputStreamWriter(os)) {
            write(elemY, writer);
        }
    }

    public static void write(YElement elemY, File file) throws IOException {
        try (Writer writer = new FileWriter(file)) {
            write(elemY, writer);
        }
    }

    public static void write(YElement elemY, Writer writer) {
        getYaml();
        yaml.dump(elemY.getObject(), writer);
    }
}
