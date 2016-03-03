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

/**
 * <p>DirectoryToConfigWrapper class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class DirectoryToConfigWrapper extends IConfig {

	private WritableResourceNode node;

	/**
	 * <p>Constructor for DirectoryToConfigWrapper.</p>
	 *
	 * @param node a {@link de.mhus.lib.core.directory.WritableResourceNode} object.
	 */
	public DirectoryToConfigWrapper(WritableResourceNode node) {
		this.node = node;
	}
	
	/** {@inheritDoc} */
	@Override
	public Object getProperty(String name) {
		return node.getProperty(name);
	}

	/** {@inheritDoc} */
	@Override
	public WritableResourceNode createConfig(String key) throws MException {
		return node.createConfig(key);
	}

	/** {@inheritDoc} */
	@SuppressWarnings("deprecation")
	@Override
	public int getProperty(String name, int def) throws MException {
		return node.getProperty(name, def);
	}

	/** {@inheritDoc} */
	@Override
	public int moveConfig(ResourceNode config, int newPos) throws MException {
		return node.moveConfig(config, newPos);
	}

	/** {@inheritDoc} */
	@Override
	public String getString(String name, String def) {
		return node.getString(name, def);
	}

	/** {@inheritDoc} */
	@Override
	public String[] getPropertyKeys() {
		return node.getPropertyKeys();
	}

	/** {@inheritDoc} */
	@Override
	public ResourceNode getNode(String key) {
		return node.getNode(key);
	}

	/** {@inheritDoc} */
	@Override
	public boolean getBoolean(String name, boolean def) {
		return node.getBoolean(name, def);
	}

	/** {@inheritDoc} */
	@Override
	public void removeConfig(ResourceNode config) throws MException {
		node.removeConfig(config);
	}

	/** {@inheritDoc} */
	@Override
	public ResourceNode[] getNodes() {
		return node.getNodes();
	}

	/** {@inheritDoc} */
	@Override
	public int getInt(String name, int def) {
		return node.getInt(name, def);
	}

	/** {@inheritDoc} */
	@Override
	public ResourceNode[] getNodes(String key) {
		return node.getNodes(key);
	}

	/** {@inheritDoc} */
	@Override
	public void setString(String name, String value) {
		node.setString(name, value);
	}

	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		return node.hashCode();
	}

	/** {@inheritDoc} */
	@Override
	public long getLong(String name, long def) {
		return node.getLong(name, def);
	}

	/** {@inheritDoc} */
	@Override
	public String[] getNodeKeys() {
		return node.getNodeKeys();
	}

	/** {@inheritDoc} */
	@Override
	public String getName() throws MException {
		return node.getName();
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return node.toString();
	}

	/** {@inheritDoc} */
	@Override
	public float getFloat(String name, float def) {
		return node.getFloat(name, def);
	}

	/** {@inheritDoc} */
	@Override
	public InputStream getInputStream(String key) {
		return node.getInputStream(key);
	}

	/** {@inheritDoc} */
	@Override
	public String getExtracted(String key) throws MException {
		return node.getExtracted(key);
	}

	/** {@inheritDoc} */
	@Override
	public double getDouble(String name, double def) {
		return node.getDouble(name, def);
	}

	/** {@inheritDoc} */
	@Override
	public Calendar getCalendar(String name) throws MException {
		return node.getCalendar(name);
	}

	/** {@inheritDoc} */
	@Override
	public String getExtracted(String key, String def) throws MException {
		return node.getExtracted(key, def);
	}

	/** {@inheritDoc} */
	@Override
	public Date getDate(String name) {
		return node.getDate(name);
	}

	/** {@inheritDoc} */
	@Override
	public ResourceNode getParent() {
		return node.getParent();
	}

	/** {@inheritDoc} */
	@Override
	public Set<String> keys() {
		return node.keys();
	}

	/** {@inheritDoc} */
	@Override
	public void setInt(String name, int value) {
		node.setInt(name, value);
	}

	/** {@inheritDoc} */
	@Override
	public void setLong(String name, long value) {
		node.setLong(name, value);
	}

	/** {@inheritDoc} */
	@Override
	public void setDouble(String name, double value) {
		node.setDouble(name, value);
	}

	/** {@inheritDoc} */
	@Override
	public void setFloat(String name, float value) {
		node.setFloat(name, value);
	}

	/** {@inheritDoc} */
	@Override
	public void setBoolean(String name, boolean value) {
		node.setBoolean(name, value);
	}

	/** {@inheritDoc} */
	@Override
	public void setCalendar(String name, Calendar value) {
		node.setCalendar(name, value);
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(Object obj) {
		return node.equals(obj);
	}

	/** {@inheritDoc} */
	@Override
	public void setDate(String name, Date value) {
		node.setDate(name, value);
	}

	/** {@inheritDoc} */
	@Override
	public void setNumber(String name, Number value) {
		node.setNumber(name, value);
	}

	/** {@inheritDoc} */
	@Override
	public Number getNumber(String name, Number def) {
		return node.getNumber(name, def);
	}

	/** {@inheritDoc} */
	@Override
	public boolean isProperty(String name) {
		return node.isProperty(name);
	}

	/** {@inheritDoc} */
	@Override
	public void removeProperty(String key) {
		node.removeProperty(key);
	}

	/** {@inheritDoc} */
	@Override
	public void setProperty(String key, Object value) {
		node.setProperty(key, value);
	}

	/** {@inheritDoc} */
	@Override
	public boolean isEditable() {
		return node.isEditable();
	}

	/** {@inheritDoc} */
	@Override
	public Iterator<Entry<String, Object>> iterator() {
		return node.iterator();
	}

	/** {@inheritDoc} */
	@Override
	public URL getUrl() {
		return node.getUrl();
	}
	
}
