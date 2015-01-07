package de.mhus.lib.core.config;

import java.io.File;
import java.net.URI;

import de.mhus.lib.core.MFile;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.MXml;
import de.mhus.lib.core.directory.WritableResourceNode;
import de.mhus.lib.core.lang.IBase;
import de.mhus.lib.core.util.Rfc1738;

public class MConfigFactory implements IBase {

	public WritableResourceNode createConfigFor(File file) throws Exception {
		
		if (file.isDirectory())
			return new DirConfig(file);
		
		String key = file.getName();
		
		if (key.endsWith(".xml")) {
			XmlConfigFile out = new XmlConfigFile(file);
			out.setName(MFile.getFileNameOnly(key)); // set the filename as name
			out.setString("__path", file.getAbsolutePath());
			return out;
		}
		if (key.endsWith(".properties")) {
			PropertiesConfigFile out = new PropertiesConfigFile(file);
			out.setName(MFile.getFileNameOnly(key)); // set the filename as name
			out.setString("__path", file.getAbsolutePath());
			return out;
		}
		if (key.endsWith(".json")) {
			JsonConfigFile out = new JsonConfigFile(file);
			out.setName(MFile.getFileNameOnly(key)); // set the filename as name
			out.setString("__path", file.getAbsolutePath());
			return out;
		}
		return null;
	}
	public WritableResourceNode createConfigFor(URI uri) throws Exception {
		if (uri == null) return null;
		
		String key = uri.getPath();
		if (key == null) key = uri.toString();
		
		if (key.endsWith(".xml")) {
			XmlConfigFile out = new XmlConfigFile(uri.toURL().openStream());
			out.setName(MFile.getFileNameOnly(key)); // set the filename as name
			out.setString("__path", uri.getPath());
			out.setString("__host", uri.getHost());
			out.setInt("__port", uri.getPort());
			out.setString("__user", uri.getUserInfo());
			return out;
		}
		if (key.endsWith(".properties")) {
			PropertiesConfigFile out = new PropertiesConfigFile(uri.toURL().openStream());
			out.setName(MFile.getFileNameOnly(key)); // set the filename as name
			out.setString("__path", uri.getPath());
			out.setString("__host", uri.getHost());
			out.setInt("__port", uri.getPort());
			out.setString("__user", uri.getUserInfo());
			return out;
		}
		if (key.endsWith(".json")) {
			JsonConfigFile out = new JsonConfigFile(uri.toURL().openStream());
			out.setName(MFile.getFileNameOnly(key)); // set the filename as name
			out.setString("__path", uri.getPath());
			out.setString("__host", uri.getHost());
			out.setInt("__port", uri.getPort());
			out.setString("__user", uri.getUserInfo());
			return out;
		}
		return null;
	}
	
	public WritableResourceNode createConfigForFile(String key) throws Exception {
		if (key == null) return null;

		//TODO dynamic please - and what about the MActivator .... !!
		if (key.endsWith(".xml"))
			return new XmlConfig();
		if (key.endsWith(".properties"))
			return new PropertiesConfig();
		if (key.endsWith(".json"))
			return new JsonConfig();
		
		return null;
	}
	
	/**
	 * Return a config or null if the string is not understud.
	 * 
	 * @param configString
	 * @return A config object if the config is found or null. If no config is recognized it returns null
	 * @throws Exception
	 */
	public WritableResourceNode toConfig(String configString) throws Exception {
		if (MString.isEmptyTrim(configString)) return new HashConfig();
		if (configString.startsWith("[") || configString.startsWith("{") ) {
			return new JsonConfig(configString);
		}
		if (configString.startsWith("<?")) {
			return new XmlConfig(MXml.loadXml(configString).getDocumentElement());
		}
		
		if (configString.contains("=")) {
			if (configString.contains("&"))
				return new HashConfig(Rfc1738.explode(configString));
			else
				return new PropertiesConfig(configString);
		}
		
		return null;
	}

}
