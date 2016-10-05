package de.mhus.lib.core.config;

import java.io.InputStream;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.core.directory.WritableResourceNode;
import de.mhus.lib.errors.MException;

public class DirectoryToConfigWrapper extends IConfig {

	private WritableResourceNode node;

	public DirectoryToConfigWrapper(WritableResourceNode node) {
		this.node = node;
	}
	
	@Override
	public Object getProperty(String name) {
		return node.getProperty(name);
	}

	@Override
	public WritableResourceNode createConfig(String key) throws MException {
		return node.createConfig(key);
	}

	@Override
	public int moveConfig(ResourceNode config, int newPos) throws MException {
		return node.moveConfig(config, newPos);
	}

	@Override
	public String getString(String name, String def) {
		return node.getString(name, def);
	}

	@Override
	public String[] getPropertyKeys() {
		return node.getPropertyKeys();
	}

	@Override
	public ResourceNode getNode(String key) {
		return node.getNode(key);
	}

	@Override
	public boolean getBoolean(String name, boolean def) {
		return node.getBoolean(name, def);
	}

	@Override
	public void removeConfig(ResourceNode config) throws MException {
		node.removeConfig(config);
	}

	@Override
	public ResourceNode[] getNodes() {
		return node.getNodes();
	}

	@Override
	public int getInt(String name, int def) {
		return node.getInt(name, def);
	}

	@Override
	public ResourceNode[] getNodes(String key) {
		return node.getNodes(key);
	}

	@Override
	public void setString(String name, String value) {
		node.setString(name, value);
	}

	@Override
	public int hashCode() {
		return node.hashCode();
	}

	@Override
	public long getLong(String name, long def) {
		return node.getLong(name, def);
	}

	@Override
	public String[] getNodeKeys() {
		return node.getNodeKeys();
	}

	@Override
	public String getName() throws MException {
		return node.getName();
	}

	@Override
	public String toString() {
		return node.toString();
	}

	@Override
	public float getFloat(String name, float def) {
		return node.getFloat(name, def);
	}

	@Override
	public InputStream getInputStream(String key) {
		return node.getInputStream(key);
	}

	@Override
	public String getExtracted(String key) throws MException {
		return node.getExtracted(key);
	}

	@Override
	public double getDouble(String name, double def) {
		return node.getDouble(name, def);
	}

	@Override
	public Calendar getCalendar(String name) throws MException {
		return node.getCalendar(name);
	}

	@Override
	public String getExtracted(String key, String def) throws MException {
		return node.getExtracted(key, def);
	}

	@Override
	public Date getDate(String name) {
		return node.getDate(name);
	}

	@Override
	public ResourceNode getParent() {
		return node.getParent();
	}

	@Override
	public Set<String> keys() {
		return node.keys();
	}

	@Override
	public void setInt(String name, int value) {
		node.setInt(name, value);
	}

	@Override
	public void setLong(String name, long value) {
		node.setLong(name, value);
	}

	@Override
	public void setDouble(String name, double value) {
		node.setDouble(name, value);
	}

	@Override
	public void setFloat(String name, float value) {
		node.setFloat(name, value);
	}

	@Override
	public void setBoolean(String name, boolean value) {
		node.setBoolean(name, value);
	}

	@Override
	public void setCalendar(String name, Calendar value) {
		node.setCalendar(name, value);
	}

	@Override
	public boolean equals(Object obj) {
		return node.equals(obj);
	}

	@Override
	public void setDate(String name, Date value) {
		node.setDate(name, value);
	}

	@Override
	public void setNumber(String name, Number value) {
		node.setNumber(name, value);
	}

	@Override
	public Number getNumber(String name, Number def) {
		return node.getNumber(name, def);
	}

	@Override
	public boolean isProperty(String name) {
		return node.isProperty(name);
	}

	@Override
	public void removeProperty(String key) {
		node.removeProperty(key);
	}

	@Override
	public void setProperty(String key, Object value) {
		node.setProperty(key, value);
	}

	@Override
	public boolean isEditable() {
		return node.isEditable();
	}

	@Override
	public Iterator<Entry<String, Object>> iterator() {
		return node.iterator();
	}

	@Override
	public URL getUrl() {
		return node.getUrl();
	}
	
}
