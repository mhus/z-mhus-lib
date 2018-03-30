/**
 * Copyright 2018 Mike Hummel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.core.config;

import java.io.File;
import java.net.URI;
import java.util.HashSet;

import de.mhus.lib.core.MFile;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.MXml;
import de.mhus.lib.core.lang.IBase;
import de.mhus.lib.core.util.MUri;

public class MConfigFactory implements IBase {

	public IConfig createConfigFor(File file) throws Exception {
		return createConfigFor(file, false);
	}
	
	public IConfig createConfigFor(File file, boolean include) throws Exception {
		if (include) {
			HashSet<String> included = new HashSet<>();
			return create(file, included);
		} else {
			return create(file);
		}
	}
	
	protected IConfig create(File file, HashSet<String> included) throws Exception {
		IConfig out = create(file);
		included.add(file.getAbsolutePath());
		for (String path : out.getString("__include", "").split(",")) {
			if (MString.isSetTrim(path)) {
				File absolut = MFile.getFile(file, path);
				if (absolut == null) continue;
				String abs = absolut.getAbsolutePath();
				if (!included.contains(abs)) {
					
					IConfig nextOne = create(absolut, included);
					for (String key : nextOne.getPropertyKeys())
						if (!out.containsKey(key))
							out.setProperty(key, nextOne.get(key));
					
					included.add(abs);
				}
			}
		}
		return out;
	}
	
	protected IConfig create(File file) throws Exception {
		
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
	public IConfig createConfigFor(URI uri) throws Exception {
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
	
	public IConfig createConfigForFile(String key) throws Exception {
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
	public IConfig toConfig(String configString) throws Exception {
		if (MString.isEmptyTrim(configString)) return new HashConfig();
		if (configString.startsWith("[") || configString.startsWith("{") ) {
			return new JsonConfig(configString);
		}
		if (configString.startsWith("<?")) {
			return new XmlConfig(MXml.loadXml(configString).getDocumentElement());
		}
		
		if (configString.contains("=")) {
			if (configString.contains("&"))
				return new HashConfig(MUri.explode(configString));
			else
				return new PropertiesConfig(configString);
		}
		
		return null;
	}

}
