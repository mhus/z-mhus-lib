package de.mhus.lib.cao.aaa;

import java.io.InputStream;
import java.net.URL;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import de.mhus.lib.cao.CaoAspect;
import de.mhus.lib.cao.CaoMetadata;
import de.mhus.lib.cao.CaoNode;
import de.mhus.lib.cao.CaoPolicy;
import de.mhus.lib.cao.CaoWritableElement;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.errors.MException;

public class AuthNode extends CaoNode {

	private CaoNode instance;

	public AuthNode(AuthConnection connection, AuthNode parent, CaoNode instance) {
		super(connection, parent);
	}

	public AuthNode(AuthConnection connection, CaoNode instance) {
		super(connection, null);
	}
	
	@Override
	public Log log() {
		return instance.log();
	}

	@Override
	public Object getProperty(String name) {
		return instance.getProperty(name);
	}

	@Override
	public String getString(String name, String def) {
		return instance.getString(name, def);
	}

	@Override
	public Collection<String> getPropertyKeys() {
		return instance.getPropertyKeys();
	}

	@Override
	public CaoWritableElement getWritableNode() throws MException {
		return new AuthWritableNode( this, instance.getWritableNode() );
	}

	@Override
	public CaoNode getNode(String key) {
		return new AuthNode( (AuthConnection)getConnection(), this, (CaoNode)instance.getNode(key) );
	}

	@Override
	public CaoMetadata getMetadata() {
		return instance.getMetadata();
	}

	@Override
	public String getString(String name) throws MException {
		return instance.getString(name);
	}

	@Override
	public String getId() throws MException {
		return instance.getId();
	}

	@Override
	public Collection<CaoNode> getNodes() {
		//out = new 
		return instance.getNodes();
	}

	@Override
	public boolean getBoolean(String name, boolean def) {
		return instance.getBoolean(name, def);
	}

	@Override
	public int hashCode() {
		return instance.hashCode();
	}

	@Override
	public String getName() throws MException {
		return instance.getName();
	}

	public Collection<CaoNode> getNodes(String key) {
		return instance.getNodes(key);
	}

	public boolean getBoolean(String name) throws MException {
		return instance.getBoolean(name);
	}

	public boolean isNode() {
		return instance.isNode();
	}

	public Spliterator<java.util.Map.Entry<String, Object>> spliterator() {
		return instance.spliterator();
	}

	public Collection<String> getNodeKeys() {
		return instance.getNodeKeys();
	}

	public int getInt(String name, int def) {
		return instance.getInt(name, def);
	}

	public void reload() throws MException {
		instance.reload();
	}

	public long getLong(String name, long def) {
		return instance.getLong(name, def);
	}

	public InputStream getInputStream() {
		return instance.getInputStream();
	}

	public float getFloat(String name, float def) {
		return instance.getFloat(name, def);
	}

	public String toString() {
		return instance.toString();
	}

	public InputStream getInputStream(String rendition) {
		return instance.getInputStream(rendition);
	}

	public double getDouble(String name, double def) {
		return instance.getDouble(name, def);
	}

	public boolean isValid() {
		return instance.isValid();
	}

	public String getExtracted(String key) throws MException {
		return instance.getExtracted(key);
	}

	public Calendar getCalendar(String name) throws MException {
		return instance.getCalendar(name);
	}

	public boolean equals(Object other) {
		return instance.equals(other);
	}

	public Date getDate(String name) {
		return instance.getDate(name);
	}

	public void setString(String name, String value) {
		instance.setString(name, value);
	}

	public String getExtracted(String key, String def) throws MException {
		return instance.getExtracted(key, def);
	}

	public void setInt(String name, int value) {
		instance.setInt(name, value);
	}

	public boolean isEditable() {
		return instance.isEditable();
	}

	public void setLong(String name, long value) {
		instance.setLong(name, value);
	}

	public void setDouble(String name, double value) {
		instance.setDouble(name, value);
	}

	public CaoPolicy getAccessPolicy() throws MException {
		return instance.getAccessPolicy();
	}

	public Set<String> keys() {
		return instance.keys();
	}

	public void setFloat(String name, float value) {
		instance.setFloat(name, value);
	}

	public void setBoolean(String name, boolean value) {
		instance.setBoolean(name, value);
	}

	public <T extends CaoAspect> T adaptTo(Class<? extends CaoAspect> ifc) {
		return instance.adaptTo(ifc);
	}

	public void setCalendar(String name, Calendar value) {
		instance.setCalendar(name, value);
	}

	public void setDate(String name, Date value) {
		instance.setDate(name, value);
	}

	public void setNumber(String name, Number value) {
		instance.setNumber(name, value);
	}

	public String getVersionLabel() throws MException {
		return instance.getVersionLabel();
	}

	public Set<String> getVersions() throws MException {
		return instance.getVersions();
	}

	public CaoNode getVersion(String version) throws MException {
		return instance.getVersion(version);
	}

	public Number getNumber(String name, Number def) {
		return instance.getNumber(name, def);
	}

	public boolean isProperty(String name) {
		return instance.isProperty(name);
	}

	public void removeProperty(String key) {
		instance.removeProperty(key);
	}

	public void setProperty(String key, Object value) {
		instance.setProperty(key, value);
	}

	public Iterator<java.util.Map.Entry<String, Object>> iterator() {
		return instance.iterator();
	}

	public Map<String, Object> toMap() {
		return instance.toMap();
	}

	public boolean isEmpty() {
		return instance.isEmpty();
	}

	public boolean containsKey(Object key) {
		return instance.containsKey(key);
	}

	public Object get(Object key) {
		return instance.get(key);
	}

	public Object put(String key, Object value) {
		return instance.put(key, value);
	}

	public Object remove(Object key) {
		return instance.remove(key);
	}

	public URL getUrl() {
		return instance.getUrl();
	}

	public void putAll(Map<? extends String, ? extends Object> m) {
		instance.putAll(m);
	}

	public boolean hasContent() {
		return instance.hasContent();
	}

	public CaoNode getNodeByPath(String path) {
		return instance.getNodeByPath(path);
	}

	public void clear() {
		instance.clear();
	}

	public Set<String> keySet() {
		return instance.keySet();
	}

	public String dump() throws MException {
		return instance.dump();
	}

	public int size() {
		return instance.size();
	}

	public boolean containsValue(Object value) {
		return instance.containsValue(value);
	}

	public Collection<Object> values() {
		return instance.values();
	}

	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		return instance.entrySet();
	}

	public  Object getOrDefault(Object key, Object defaultValue) {
		return instance.getOrDefault(key, defaultValue);
	}

	public  void forEach(BiConsumer<? super String, ? super Object> action) {
		instance.forEach(action);
	}

	public  void replaceAll(BiFunction<? super String, ? super Object, ? extends Object> function) {
		instance.replaceAll(function);
	}

	public  Object putIfAbsent(String key, Object value) {
		return instance.putIfAbsent(key, value);
	}

	public  boolean remove(Object key, Object value) {
		return instance.remove(key, value);
	}

	public  boolean replace(String key, Object oldValue, Object newValue) {
		return instance.replace(key, oldValue, newValue);
	}

	public  Object replace(String key, Object value) {
		return instance.replace(key, value);
	}

	public  Object computeIfAbsent(String key, Function<? super String, ? extends Object> mappingFunction) {
		return instance.computeIfAbsent(key, mappingFunction);
	}

	public  Object computeIfPresent(String key,
			BiFunction<? super String, ? super Object, ? extends Object> remappingFunction) {
		return instance.computeIfPresent(key, remappingFunction);
	}

	public  Object compute(String key,
			BiFunction<? super String, ? super Object, ? extends Object> remappingFunction) {
		return instance.compute(key, remappingFunction);
	}

	public  Object merge(String key, Object value,
			BiFunction<? super Object, ? super Object, ? extends Object> remappingFunction) {
		return instance.merge(key, value, remappingFunction);
	}
	

}
