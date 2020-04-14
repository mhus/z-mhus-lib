package de.mhus.lib.core.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;

import de.mhus.lib.core.MJson;
import de.mhus.lib.core.lang.NullValue;
import de.mhus.lib.core.util.MIterable;
import de.mhus.lib.errors.MException;
import de.mhus.lib.errors.NotSupportedException;
import de.mhus.lib.errors.TooDeepStructuresException;

public class JsonConfigBuilder extends IConfigBuilder {

    @Override
    public IConfig read(InputStream is) throws MException {
        try {
            JsonNode docJ = MJson.load(is);
            MConfig config = new MConfig();
            if (docJ.isArray()) {
                ConfigList array = config.createArray(IConfig.NAMELESS_VALUE);
                for (JsonNode itemJ : docJ) {
                    IConfig obj = array.createObject();
                    fill(obj, "", itemJ, 0);
                }
            } else
            if (docJ.isObject()) {
                fill(config, "", docJ, 0);
            } else 
            if (docJ.isValueNode())
            {
                // TODO separate for each type
                config.setString(IConfig.NAMELESS_VALUE, docJ.asText());
            } else {
                throw new MException("Unknown basic json object type");
            }
            
            return config;
        } catch (IOException e) {
            throw new MException(e);
        }
    }

    private void fill(IConfig config, String name, JsonNode json, int level) {
        
        if (level > 100)
            throw new TooDeepStructuresException();
        
        if (json.isValueNode()) {
            config.put(IConfig.NAMELESS_VALUE, json.asText());
            return;
        }
        
        for (Map.Entry<String,JsonNode> itemJ : new MIterable<>(json.getFields())) {
            if (itemJ.getValue().isArray()) {
                ConfigList array = config.createArray(itemJ.getKey());
                for (JsonNode item2J : itemJ.getValue()) {
                    IConfig obj = array.createObject();
                    fill(obj, itemJ.getKey() , item2J, level+1);
                }
            } else
            if (itemJ.getValue().isObject()) {
                IConfig obj = config.createObject(itemJ.getKey());
                fill(obj,itemJ.getKey(), itemJ.getValue(), level+1);
            } else
                config.put(itemJ.getKey(), itemJ.getValue().asText());
        }
    }

    @Override
    public void write(IConfig config, OutputStream os) throws MException {
        try {
            if (config.isArray(IConfig.NAMELESS_VALUE)) {
//                ArrayNode arrayJ = MJson.createArrayNode();
//                for (IConfig itemC : config.getArrayOrNull(IConfig.NAMELESS_VALUE)) {
//                    
//                }
//                MJson.save(arrayJ, os);
                throw new NotSupportedException("first config node as array is not supported");
            } else {
                ObjectNode objectJ = MJson.createObjectNode();
                fill(objectJ, config, 0);
                MJson.save(objectJ, os);
            }
        } catch (IOException e) {
            throw new MException(e);
        }
    }

    private void fill(ObjectNode objectJ, IConfig itemC, int level) {
        if (level > 100)
            throw new TooDeepStructuresException();

        for (String key : itemC.keys()) {
            if (itemC.isArray(key)) {
                ArrayNode arrJ = objectJ.putArray(key);
                for (IConfig arrC : itemC.getArrayOrNull(key)) {
                    ObjectNode newJ = arrJ.addObject();
                    fill(newJ, arrC, level+1);
                }
            } else
            if (itemC.isObject(key)) {
                
            } else
            if (itemC.get(key) instanceof NullValue)
                objectJ.putNull(key);
            else
                objectJ.put(key, itemC.getString(key,null));
        }
    }

}
