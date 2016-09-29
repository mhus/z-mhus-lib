/*
 *  Copyright (C) 2002-2004 Mike Hummel
 *
 *  This library is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published
 *  by the Free Software Foundation; either version 2.1 of the License, or
 *  (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

package de.mhus.lib.core.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.net.URL;
import java.util.Properties;

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.core.directory.WritableResourceNode;
import de.mhus.lib.errors.MException;

/**
 * Load the configuration from a properties file. The implementation do not
 * support inner configurations.
 * 
 * @author mhu
 *
 */
public class PropertiesConfig extends IConfig implements IFlatConfig {

	protected Properties properties = null;
	protected boolean changed = false;
	protected String name;

	public PropertiesConfig() {
		this(new Properties());
	}
	
	public PropertiesConfig(Properties config) {
		properties = config;
	}
	
	public PropertiesConfig(String fill) {
		properties = new Properties();
		if (fill != null) {
			try {
				readConfig(new StringReader(fill));
			} catch (IOException e) {
			}
		}
	}

	public void writeConfig(Writer os) throws IOException {
		properties.store(os, "");
		changed = false;
	}

	public void readConfig(Reader file) throws IOException {
		properties.load(file);
	}

	public void configRemoved() {
	}

	@Override
	public ResourceNode getNode(String key) {
		return null;
	}

	@Override
	public ResourceNode[] getNodes(String key) {
		return new WritableResourceNode[0];
	}

	@Override
	public ResourceNode[] getNodes() {
		return new WritableResourceNode[0];
	}
	
	@Override
	public String[] getNodeKeys() {
		//return (String[]) properties.keySet().toArray(new String[properties.size()]);
		return new String[0];
	}

	@Override
	public String[] getPropertyKeys() {
		return (String[]) properties.keySet().toArray(new String[properties.size()]);
	}

	@Override
	public String getProperty(String name) {
		return properties.getProperty(name);
	}

	@Override
	public boolean isProperty(String name) {
		return properties.containsKey(name);
	}

	@Override
	public void removeProperty(String key) {
		properties.remove(key);
		changed = true;
	}

	@Override
	public void setProperty(String key, Object value) {
		properties.setProperty(key,MCast.objectToString(value));
		changed = true;
	}

	public boolean isConfigChanged() {
		return changed;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public WritableResourceNode createConfig(String key) throws MException {
		throw new MException("not supported");
	}

	@Override
	public int moveConfig(ResourceNode config, int newPos) throws MException {
		throw new MException("not supported");
	}

	@Override
	public void removeConfig(ResourceNode config) throws MException {
		throw new MException("not supported");
	}

	@Override
	public boolean isEditable() {
		return false;
	}

	@Override
	public WritableResourceNode getParent() {
		return null;
	}

	@Override
	public InputStream getInputStream(String key) {
		return null;
	}

	@Override
	public URL getUrl() {
		return null;
	}

}
