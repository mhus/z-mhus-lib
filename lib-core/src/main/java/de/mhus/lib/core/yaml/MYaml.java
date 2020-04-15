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
import java.util.HashMap;
import java.util.LinkedList;

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

    public synchronized static Yaml getYaml() {
        if (yaml == null)
            yaml = new Yaml();
        return yaml;
    }

    public static YMap loadFromString(String content) {
        getYaml();
        YMap docE = new YMap(yaml.load(content));
        return docE;
    }

    public static YMap createMap() {
        return new YMap(new HashMap<>());
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
