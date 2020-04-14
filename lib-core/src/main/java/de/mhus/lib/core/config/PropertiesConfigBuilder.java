package de.mhus.lib.core.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Map.Entry;

import de.mhus.lib.core.MProperties;
import de.mhus.lib.errors.MException;

public class PropertiesConfigBuilder extends IConfigBuilder {

    @Override
    public IConfig read(InputStream is) throws MException {
        try {
            MProperties p = MProperties.load(is);
            return readFromMap(p);
        } catch (IOException e) {
            throw new MException(e);
        }
    }

    @Override
    public void write(IConfig config, OutputStream os) throws MException {
        MProperties p =new MProperties(config);
        try {
            p.save(os);
        } catch (IOException e) {
            throw new MException(e);
        }
    }

    public IConfig readFromMap(Map<String, Object> map) {
        IConfig config = new MConfig();
        for (Entry<String, Object> entry : map.entrySet())
            config.put(entry.getKey(),entry.getValue());
        return config;
    }

}
