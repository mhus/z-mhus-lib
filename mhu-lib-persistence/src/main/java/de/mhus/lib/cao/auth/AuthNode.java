package de.mhus.lib.cao.auth;

import java.io.InputStream;
import java.net.URL;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import de.mhus.lib.cao.CaoAspect;
import de.mhus.lib.cao.CaoAspectFactory;
import de.mhus.lib.cao.CaoMetadata;
import de.mhus.lib.cao.CaoNode;
import de.mhus.lib.cao.CaoPolicy;
import de.mhus.lib.cao.CaoWritableElement;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.errors.AccessDeniedException;
import de.mhus.lib.errors.MException;
import de.mhus.lib.errors.NotSupportedException;

public class AuthNode extends CaoNode {

	private static final long serialVersionUID = 1L;
	protected CaoNode instance;
	AuthAccess access;

	public AuthNode(AuthConnection connection, AuthNode parent, CaoNode instance) {
		super(connection, parent);
		this.instance = instance;
	}

	public AuthNode(AuthConnection connection, CaoNode instance) {
		super(connection, null);
		this.instance = instance;
	}
	
	@Override
	public Log log() {
		return instance.log();
	}

	@Override
	public Object getProperty(String name) {
		if (!((AuthConnection)con).hasReadAccess(instance, name)) return null;
		return instance.getProperty(name);
	}

	@Override
	public String getString(String name, String def) {
		if (!((AuthConnection)con).hasReadAccess(instance, name)) return def;
		return instance.getString(name, def);
	}

	@Override
	public Collection<String> getPropertyKeys() {
		return instance.getPropertyKeys();
	}

	@Override
	public CaoWritableElement getWritableNode() throws MException {
		if (!((AuthConnection)con).hasWriteAccess(instance)) return null;
		return new AuthWritableNode( this, instance, instance.getWritableNode() );
	}

	@Override
	public CaoNode getNode(String key) {
		CaoNode n = instance.getNode(key);
		if (!((AuthConnection)con).hasReadAccess(n)) return null;
		return new AuthNode( (AuthConnection)getConnection(), this, n );
	}

	@Override
	public CaoMetadata getMetadata() {
		return instance.getMetadata();
	}

	@Override
	public String getString(String name) throws MException {
		if (!((AuthConnection)con).hasReadAccess(instance, name)) return null;
		return instance.getString(name);
	}

	@Override
	public String getId() throws MException {
		return instance.getId();
	}

	@Override
	public Collection<CaoNode> getNodes() {
		Collection<CaoNode> in = instance.getNodes();
		LinkedList<CaoNode> out = new LinkedList<>();
		for (CaoNode n : in) {
			if (((AuthConnection)con).hasReadAccess(n))
			out.add(new AuthNode((AuthConnection) getConnection(), this,  n));
		}
		return out;
	}

	@Override
	public boolean getBoolean(String name, boolean def) {
		if (!((AuthConnection)con).hasReadAccess(instance, name)) return def;
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
		Collection<CaoNode> in = instance.getNodes(key);
		LinkedList<CaoNode> out = new LinkedList<>();
		for (CaoNode n : in) {
			if (((AuthConnection)con).hasReadAccess(n))
			out.add(new AuthNode((AuthConnection) getConnection(), this,  n));
		}
		return out;
	}

	@Override
	public boolean getBoolean(String name) throws MException {
		if (!((AuthConnection)con).hasReadAccess(instance, name)) throw new AccessDeniedException(name);
		return instance.getBoolean(name);
	}

	@Override
	public boolean isNode() {
		return instance.isNode();
	}

	@Override
	public Spliterator<java.util.Map.Entry<String, Object>> spliterator() {
//		return instance.spliterator();
		throw new NotSupportedException();
	}

	@Override
	public Collection<String> getNodeKeys() {
		HashSet<String> out = new HashSet<>();
		for (CaoNode n : getNodes())
			try {
				out.add(n.getName());
			} catch (MException e) {
			}
		return out;
	}

	@Override
	public int getInt(String name, int def) {
		if (!((AuthConnection)con).hasReadAccess(instance, name)) return def;
		return instance.getInt(name, def);
	}

	@Override
	public void reload() throws MException {
		instance.reload();
	}

	@Override
	public long getLong(String name, long def) {
		if (!((AuthConnection)con).hasReadAccess(instance, name)) return def;
		return instance.getLong(name, def);
	}

	@Override
	public InputStream getInputStream() {
		if (!((AuthConnection)con).hasContentAccess(instance, null)) return null;
		return instance.getInputStream();
	}

	@Override
	public float getFloat(String name, float def) {
		if (!((AuthConnection)con).hasReadAccess(instance, name)) return def;
		return instance.getFloat(name, def);
	}

	@Override
	public String toString() {
		return instance.toString();
	}

	@Override
	public InputStream getInputStream(String rendition) {
		if (!((AuthConnection)con).hasContentAccess(instance, rendition)) return null;
		return instance.getInputStream(rendition);
	}

	@Override
	public double getDouble(String name, double def) {
		if (!((AuthConnection)con).hasReadAccess(instance, name)) return def;
		return instance.getDouble(name, def);
	}

	@Override
	public boolean isValid() {
		return instance.isValid();
	}

	@Override
	public String getExtracted(String key) throws MException {
		if (!((AuthConnection)con).hasReadAccess(instance, key)) throw new AccessDeniedException();
		return instance.getExtracted(key);
	}

	@Override
	public Calendar getCalendar(String name) throws MException {
		if (!((AuthConnection)con).hasReadAccess(instance, name)) throw new AccessDeniedException();
		return instance.getCalendar(name);
	}

	@Override
	public boolean equals(Object other) {
		return instance.equals(other);
	}

	@Override
	public Date getDate(String name) {
		if (!((AuthConnection)con).hasReadAccess(instance, name)) return null;
		return instance.getDate(name);
	}

	@Override
	public void setString(String name, String value) {
	}

	@Override
	public String getExtracted(String key, String def) throws MException {
		if (!((AuthConnection)con).hasReadAccess(instance, key)) return def;
		return instance.getExtracted(key, def);
	}

	@Override
	public void setInt(String name, int value) {
	}

	@Override
	public boolean isEditable() {
		if (!((AuthConnection)con).hasWriteAccess(instance)) return false;
		return instance.isEditable();
	}

	@Override
	public void setLong(String name, long value) {
	}

	@Override
	public void setDouble(String name, double value) {
	}

	@Override
	public Set<String> keys() {
		return instance.keys();
	}

	@Override
	public void setFloat(String name, float value) {
	}

	@Override
	public void setBoolean(String name, boolean value) {
	}

	@Override
	public <T extends CaoAspect> T adaptTo(Class<? extends CaoAspect> ifc) {
		if ( !((AuthConnection)con).hasAspectAccess(instance, ifc)) return null;
		return super.adaptTo(ifc);
	}

	@Override
	public void setCalendar(String name, Calendar value) {
	}

	@Override
	public void setDate(String name, Date value) {
	}

	@Override
	public void setNumber(String name, Number value) {
	}

	@Override
	public Number getNumber(String name, Number def) {
		if (!((AuthConnection)con).hasReadAccess(instance, name)) return def;
		return instance.getNumber(name, def);
	}

	@Override
	public boolean isProperty(String name) {
		if (!((AuthConnection)con).hasReadAccess(instance, name)) return false;
		return instance.isProperty(name);
	}

	@Override
	public void removeProperty(String key) {
	}

	@Override
	public void setProperty(String key, Object value) {
	}

	@Override
	public Iterator<java.util.Map.Entry<String, Object>> iterator() {
		return instance.iterator();
	}

	@Override
	public Map<String, Object> toMap() {
		throw new NotSupportedException();
	}

	@Override
	public boolean isEmpty() {
		return instance.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		if (!((AuthConnection)con).hasReadAccess(instance, String.valueOf(key))) return false;
		return instance.containsKey(key);
	}

	@Override
	public Object get(Object key) {
		if (!((AuthConnection)con).hasReadAccess(instance, String.valueOf(key))) return null;
		return instance.get(key);
	}

	@Override
	public Object put(String key, Object value) {
		return null;
	}

	@Override
	public Object remove(Object key) {
		return null;
	}

	@Override
	public URL getUrl() {
		return instance.getUrl();
	}

	@Override
	public void putAll(Map<? extends String, ? extends Object> m) {
	}

	@Override
	public boolean hasContent() {
		return instance.hasContent();
	}

	@Override
	public CaoNode getNodeByPath(String path) {
		CaoNode n = instance.getNodeByPath(path);
		if (!((AuthConnection)con).hasReadAccess(n)) return null;
		return new AuthNode((AuthConnection) con, n);
	}

	@Override
	public void clear() {
	}

	@Override
	public Set<String> keySet() {
		return instance.keySet();
	}

	@Override
	public String dump() throws MException {
//		return instance.dump();
		return "";
	}

	@Override
	public int size() {
		return instance.size();
	}

	@Override
	public boolean containsValue(Object value) {
		throw new NotSupportedException();
	}

	@Override
	public Collection<Object> values() {
		throw new NotSupportedException();
	}

	@Override
	public Set<java.util.Map.Entry<String, Object>> entrySet() {
//		return instance.entrySet();
		throw new NotSupportedException();
	}

	@Override
	public  Object getOrDefault(Object key, Object defaultValue) {
		throw new NotSupportedException();
	}

	@Override
	public  void forEach(BiConsumer<? super String, ? super Object> action) {
		throw new NotSupportedException();
	}

	@Override
	public  void replaceAll(BiFunction<? super String, ? super Object, ? extends Object> function) {
		throw new NotSupportedException();
	}

	@Override
	public  Object putIfAbsent(String key, Object value) {
		throw new NotSupportedException();
	}

	@Override
	public  boolean remove(Object key, Object value) {
		throw new NotSupportedException();
	}

	@Override
	public  boolean replace(String key, Object oldValue, Object newValue) {
		throw new NotSupportedException();
	}

	@Override
	public  Object replace(String key, Object value) {
		throw new NotSupportedException();
	}

	@Override
	public  Object computeIfAbsent(String key, Function<? super String, ? extends Object> mappingFunction) {
		throw new NotSupportedException();
	}

	@Override
	public  Object computeIfPresent(String key,
			BiFunction<? super String, ? super Object, ? extends Object> remappingFunction) {
		throw new NotSupportedException();
	}

	@Override
	public  Object compute(String key,
			BiFunction<? super String, ? super Object, ? extends Object> remappingFunction) {
		throw new NotSupportedException();
	}

	@Override
	public  Object merge(String key, Object value,
			BiFunction<? super Object, ? super Object, ? extends Object> remappingFunction) {
		throw new NotSupportedException();
	}

	@Override
	public Collection<String> getRenditions() {
		return instance.getRenditions();
	}
	
	@Override
	public CaoNode getParent() {
		CaoNode p = super.getParent();
		if (p == null) {
			CaoNode ip = instance.getParent();
			if (ip == null) return null;
			parent = new AuthNode((AuthConnection)getConnection(), ip);
			p = parent;
		}
		return p;
	}

}
