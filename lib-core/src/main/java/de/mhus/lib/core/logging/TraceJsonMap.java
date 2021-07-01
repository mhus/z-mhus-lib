package de.mhus.lib.core.logging;

import java.util.Iterator;
import java.util.Map.Entry;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.opentracing.propagation.TextMap;

public class TraceJsonMap implements TextMap {

    private ObjectNode node;
    private String prefix;

    public TraceJsonMap(ObjectNode node, String prefix) {
        this.node = node;
        this.prefix = prefix;
    }
    
    @Override
    public Iterator<Entry<String, String>> iterator() {
        final Iterator<Entry<String, JsonNode>> iter = node.fields();
        return new Iterator<Entry<String, String>>() {

            @Override
            public boolean hasNext() {
                return iter.hasNext();
            }

            @Override
            public Entry<String, String> next() {
                final Entry<String, JsonNode> entry = iter.next();
                return new Entry<String, String>() {

                    @Override
                    public String getKey() {
                        return entry.getKey();
                    }

                    @Override
                    public String getValue() {
                        return entry.getValue().asText();
                    }

                    @Override
                    public String setValue(String value) {
                        return null;
                    }
                };
            }
        };
    }

    @Override
    public void put(String key, String value) {
        node.put(prefix + key, value);
    }

}
