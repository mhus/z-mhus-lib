package de.mhus.lib.cao.auth;

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

	private static final long serialVersionUID = 1L;
	protected CaoNode instance;

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

	@Override
	public Collection<CaoNode> getNodes(String key) {
		return instance.getNodes(key);
	}

	@Override
	public boolean getBoolean(String name) throws MException {
		return instance.getBoolean(name);
	}

	@Override
	public boolean isNode() {
		return instance.isNode();
	}

	@Override
	public Spliterator<java.util.Map.Entry<String, Object>> spliterator() {
		return instance.spliterator();
	}

	@Override
	public Collection<String> getNodeKeys() {
		return instance.getNodeKeys();
	}

	@Override
	public int getInt(String name, int def) {
		return instance.getInt(name, def);
	}

	@Override
	public void reload() throws MException {
		instance.reload();
	}

	@Override
	public long getLong(String name, long def) {
		return instance.getLong(name, def);
	}

	@Override
	public InputStream getInputStream() {
		return instance.getInputStream();
	}

	@Override
	public float getFloat(String name, float def) {
		return instance.getFloat(name, def);
	}

	@Override
	public String toString() {
		return instance.toString();
	}

	@Override
	public InputStream getInputStream(String rendition) {
		return instance.getInputStream(rendition);
	}

	@Override
	public double getDouble(String name, double def) {
		return instance.getDouble(name, def);
	}

	@Override
	public boolean isValid() {
		return instance.isValid();
	}

	@Override
	public String getExtracted(String key) throws MException {
		return instance.getExtracted(key);
	}

	@Override
	public Calendar getCalendar(String name) throws MException {
		return instance.getCalendar(name);
	}

	@Override
	public boolean equals(Object other) {
		return instance.equals(other);
	}

	@Override
	public Date getDate(String name) {
		return instance.getDate(name);
	}

	@Override
	public void setString(String name, String value) {
		instance.setString(name, value);
	}

	@Override
	public String getExtracted(String key, String def) throws MException {
		return instance.getExtracted(key, def);
	}

	@Override
	public void setInt(String name, int value) {
		instance.setInt(name, value);
	}

	@Override
	public boolean isEditable() {
		return instance.isEditable();
	}

	@Override
	public void setLong(String name, long value) {
		instance.setLong(name, value);
	}

	@Override
	public void setDouble(String name, double value) {
		instance.setDouble(name, value);
	}

	@Override
	public CaoPolicy getAccessPolicy() throws MException {
		return instance.getAccessPolicy();
	}

	@Override
	public Set<String> keys() {
		return instance.keys();
	}

	@Override
	public void setFloat(String name, float value) {
		instance.setFloat(name, value);
	}

	@Override
	public void setBoolean(String name, boolean value) {
		instance.setBoolean(name, value);
	}

	@Override
	public <T extends CaoAspect> T adaptTo(Class<? extends CaoAspect> ifc) {
		return instance.adaptTo(ifc);
	}

	@Override
	public void setCalendar(String name, Calendar value) {
		instance.setCalendar(name, value);
	}

	@Override
	public void setDate(String name, Date value) {
		instance.setDate(name, value);
	}

	@Override
	public void setNumber(String name, Number value) {
		instance.setNumber(name, value);
	}

	@Override
	public Number getNumber(String name, Number def) {
		return instance.getNumber(name, def);
	}

	@Override
	public boolean isProperty(String name) {
		return instance.isProperty(name);
	}

	@Override
	public void removeProperty(String key) {
		instance.removeProperty(key);
	}

	@Override
	public void setProperty(String key, Object value) {
		instance.setProperty(key, value);
	}

	@Override
	public Iterator<java.util.Map.Entry<String, Object>> iterator() {
		return instance.iterator();
	}

	@Override
	public Map<String, Object> toMap() {
		return instance.toMap();
	}

	@Override
	public boolean isEmpty() {
		return instance.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return instance.containsKey(key);
	}

	@Override
	public Object get(Object key) {
		return instance.get(key);
	}

	@Override
	public Object put(String key, Object value) {
		return instance.put(key, value);
	}

	@Override
	public Object remove(Object key) {
		return instance.remove(key);
	}

	@Override
	public URL getUrl() {
		return instance.getUrl();
	}

	@Override
	public void putAll(Map<? extends String, ? extends Object> m) {
		instance.putAll(m);
	}

	@Override
	public boolean hasContent() {
		return instance.hasContent();
	}

	@Override
	public CaoNode getNodeByPath(String path) {
		return instance.getNodeByPath(path);
	}

	@Override
	public void clear() {
		instance.clear();
	}

	@Override
	public Set<String> keySet() {
		return instance.keySet();
	}

	@Override
	public String dump() throws MException {
		return instance.dump();
	}

	@Override
	public int size() {
		return instance.size();
	}

	@Override
	public boolean containsValue(Object value) {
		return instance.containsValue(value);
	}

	@Override
	public Collection<Object> values() {
		return instance.values();
	}

	@Override
	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		return instance.entrySet();
	}

	@Override
	public  Object getOrDefault(Object key, Object defaultValue) {
		return instance.getOrDefault(key, defaultValue);
	}

	@Override
	public  void forEach(BiConsumer<? super String, ? super Object> action) {
		instance.forEach(action);
	}

	@Override
	public  void replaceAll(BiFunction<? super String, ? super Object, ? extends Object> function) {
		instance.replaceAll(function);
	}

	@Override
	public  Object putIfAbsent(String key, Object value) {
		return instance.putIfAbsent(key, value);
	}

	@Override
	public  boolean remove(Object key, Object value) {
		return instance.remove(key, value);
	}

	@Override
	public  boolean replace(String key, Object oldValue, Object newValue) {
		return instance.replace(key, oldValue, newValue);
	}

	@Override
	public  Object replace(String key, Object value) {
		return instance.replace(key, value);
	}

	@Override
	public  Object computeIfAbsent(String key, Function<? super String, ? extends Object> mappingFunction) {
		return instance.computeIfAbsent(key, mappingFunction);
	}

	@Override
	public  Object computeIfPresent(String key,
			BiFunction<? super String, ? super Object, ? extends Object> remappingFunction) {
		return instance.computeIfPresent(key, remappingFunction);
	}

	@Override
	public  Object compute(String key,
			BiFunction<? super String, ? super Object, ? extends Object> remappingFunction) {
		return instance.compute(key, remappingFunction);
	}

	@Override
	public  Object merge(String key, Object value,
			BiFunction<? super Object, ? super Object, ? extends Object> remappingFunction) {
		return instance.merge(key, value, remappingFunction);
	}

	@Override
	public Collection<String> getRenditions() {
		return instance.getRenditions();
	}
	

}
