package de.mhus.lib.core.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Element;

import de.mhus.lib.core.MFile;
import de.mhus.lib.core.MProperties;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.MXml;
import de.mhus.lib.core.util.MUri;
import de.mhus.lib.errors.MException;
import de.mhus.lib.errors.NotFoundException;

public class DefaultConfigFactory implements IConfigFactory {
    
    private HashMap<String, IConfigBuilder> registry = new HashMap<>();
    
    public DefaultConfigFactory() {
        registry.put("xml", new XmlConfigBuilder());
        registry.put("json", new JsonConfigBuilder());
        registry.put("yml", new YamlConfigBuilder());
        registry.put("yaml", new YamlConfigBuilder());
        registry.put("properties", new PropertiesConfigBuilder());
    }

    @Override
    public IConfig read(Class<?> owner, String fileName) throws MException {
        try {
            URL url = MSystem.locateResource(owner, fileName);
            return read(url);
        } catch (IOException e) {
            throw new MException(fileName, e);
        }
    }

    @Override
    public IConfig read(File file) throws MException {
        String ext = MFile.getFileExtension(file);
        IConfigBuilder builder = getBuilder(ext);
        if (builder == null) throw new NotFoundException("builder for resource not found",file.getName());
        return builder.readFromFile(file);
    }

    @Override
    public IConfig read(URL url) throws MException {
        String ext = MFile.getFileExtension(url.getPath());
        IConfigBuilder builder = getBuilder(ext);
        if (builder == null) throw new NotFoundException("builder for resource not found",url);
        try (InputStream is =url.openStream()) {
            return builder.read(is);
        } catch (IOException e) {
            throw new MException(url, e);
        }
    }
    
    @Override
    public IConfigBuilder getBuilder(String ext) {
        ext = ext.toLowerCase().trim(); // paranoia
        return registry.get(ext);
    }

    @Override
    public IConfig create() {
        return new MConfig();
    }

    public static IConfig readFromJsonString(String json) throws MException {
        return new JsonConfigBuilder().readFromString(json);
    }

    public static IConfig readFromXmlString(Element documentElement) throws MException {
        return new XmlConfigBuilder().readFromElement(documentElement);
    }

    public static IConfig readFromYamlString(String yaml) throws MException {
        return new YamlConfigBuilder().readFromString(yaml);
    }

    @Override
    public void write(IConfig config, File file) throws MException {
        String ext = MFile.getFileExtension(file);
        IConfigBuilder builder = getBuilder(ext);
        if (builder == null) throw new NotFoundException("builder for resource not found",file.getName());
        builder.writeToFile(config, file);
    }

    public static IConfig readFromProperties(Map<String, Object> lines) {
        return new PropertiesConfigBuilder().readFromMap(lines);
    }

    /**
     * Return a config or null if the string is not understud.
     *
     * @param configString
     * @return A config object if the config is found or null. If no config is recognized it returns
     *     null
     * @throws MException
     */
    public static IConfig readConfigFromString(String configString) throws MException {
        if (MString.isEmptyTrim(configString)) return new MConfig();
        if (configString.startsWith("[") || configString.startsWith("{")) {
            try {
                return DefaultConfigFactory.readFromJsonString(configString);
            } catch (Exception e) {
                throw new MException(configString, e);
            }
        }
        if (configString.startsWith("<?")) {
            try {
                return DefaultConfigFactory.readFromXmlString(MXml.loadXml(configString).getDocumentElement());
            } catch (Exception e) {
                throw new MException(configString, e);
            }
        }

        if (configString.contains("=")) {
            if (configString.contains("&")) 
                return DefaultConfigFactory.readFromProperties(new HashMap<>(MUri.explode(configString)));
            else return DefaultConfigFactory.readFromProperties(MProperties.explodeToMProperties(configString));
        }

        return null;
    }

}
