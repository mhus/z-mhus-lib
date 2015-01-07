package de.mhus.lib.core.directory;

import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.parser.CompiledString;
import de.mhus.lib.core.parser.DefaultScriptPart;
import de.mhus.lib.core.parser.StringCompiler;
import de.mhus.lib.core.parser.StringPart;
import de.mhus.lib.core.util.ArraySet;
import de.mhus.lib.errors.MException;

/**
 * This interface represent a generic Directory Node. Nodes are
 * sets of definitions. The definitions can be bound together to a inner Nodes.
 * 
 */

public abstract class ResourceNode extends IProperties {

	protected ConfigStringCompiler compiler;
	protected HashMap<String, CompiledString> compiledCache;

	/**
	 * Return all existing keys. A property key is unique.
	 *  
	 * @return
	 */
	public abstract String[] getPropertyKeys();

	/**
	 * Returns a inner configuration by the name. Inner configurations
	 * are not unique. If more then one configurations exists it will
	 * return the first one. if no configuration exists it returns null.
	 * 
	 * @param key
	 * @return
	 */
	public abstract ResourceNode getNode(String key);

	/**
	 * Return all inner configurations ignoring the name. The order
	 * is like in the configuration file. This never returns null.
	 * 
	 * @param key
	 * @return
	 */
	public abstract ResourceNode[] getNodes();

	/**
	 * Return all inner configurations by the given name. The order
	 * is like in the configuration file. This never returns null.
	 * 
	 * @param key
	 * @return
	 */
	public abstract ResourceNode[] getNodes(String key);

	/**
	 * Return all possible, existing inner configuration names.
	 * @return
	 */
	public abstract String[] getNodeKeys();

	/**
	 * Return a name of this config element could also be null.
	 * The name most time is the name of a sub config.
	 * @return
	 * @throws MException 
	 */
	public abstract String getName() throws MException;

	/**
	 * Return the default content input stream.
	 * 
	 * @return
	 */
	public InputStream getInputStream() {
		return getInputStream(null);
	}
	
	/**
	 * Return the input stream of a content resource.
	 * @param key Name of a rendition or null for the default content
	 * @return
	 */
	public abstract InputStream getInputStream(String key);
	
	/**
	 * Returns a compiled and executed string. The string is compiled with StringCompiler and is cached. Example
	 * is "this is the value of another $anothername,default$" or with the prefix "root.": "This is a root attribute $root.name$
	 * 
	 * User the "../" prefix to go one back ...
	 * 
	 * @see StringCompiler
	 * @param key
	 * @return
	 * @throws MException 
	 */
	public String getExtracted(String key) throws MException {
		return getExtracted(key, null);
	}
	
	public String getExtracted(String key, String def) throws MException {
		return getExtracted(key,def,0);
	}
	
	/**
	 * return the parent config if possible.
	 * 
	 * @return
	 */
	public abstract ResourceNode getParent();

	@Override
	public Set<String> keys() {
		return new ArraySet<String>(getPropertyKeys());
	}

	protected String getExtracted(String key, String def,int level) throws MException {
		
		if (level > 10) return def;
		
		String value = getString(key,null);
		
		if (value == null) return def;
		if (value.indexOf('$') < 0) return value;
		
		synchronized (this) {
			if (compiler == null) {
				compiler = new ConfigStringCompiler();
				compiledCache = new HashMap<String, CompiledString>();
			}
			CompiledString cached = compiledCache.get(key);
			if (cached == null) {
				cached = compiler.compileString(value);
				compiledCache.put(key, cached);
			}
			return cached.execute(level == 0 ?null : new ConfigMap(level));
		}
	}
	
	private class ConfigStringCompiler extends StringCompiler {
		
		@Override
		protected StringPart createDefaultAttributePart(String part) {
			if (part.startsWith("root."))				
				return new RootAttributePart(part);
			if (part.startsWith(">>>"))
				return new DefaultScriptPart(part);
			return new ConfigAttributePart(part);
		}
	}

	private class RootAttributePart implements StringPart {
		private String name;
		private String def;
		private ResourceNode root;

		public RootAttributePart(String part) {
			name = MString.afterIndex(part,'.');
			root = ResourceNode.this;
			while (root.getParent() != null && root.getParent() != root) root = root.getParent();
			int pos = name.indexOf(',');
			if (pos > 0) {
				def = name.substring(pos+1);
				name = name.substring(0,pos);
			}
		}

		@Override
		public void execute(StringBuffer out, Map<String, Object> attributes) throws MException {
			out.append(root.getString(name,def));
		}		
		
		@Override
		public void dump(int level, StringBuffer out) {
			MString.appendRepeating(level, ' ', out);
			out.append(getClass().getCanonicalName()).append(name).append("(").append(def).append(")");
		}

	}
	
	private class ConfigAttributePart implements StringPart {

		private String name;
		private String def;
		private ResourceNode config;

		public ConfigAttributePart(String part) {
			name = part;
			int pos = name.indexOf(',');
			if (pos > 0) {
				def = name.substring(pos+1);
				name = name.substring(0,pos);
			}
			config = ResourceNode.this;
			while (name.startsWith("../")) {
				config = config.getParent();
				name = name.substring(3);
				if (config == null ) break;
			}
		}

		@Override
		public void execute(StringBuffer out, Map<String, Object> attributes) throws MException {
			int level = 0;
			if (attributes != null && attributes instanceof ConfigMap) {
				level = ((ConfigMap)attributes).getLevel();
			}
			if (config== null)
				out.append(def);
			else
				out.append(config.getExtracted(name,def,level));
		}
		
		@Override
		public void dump(int level, StringBuffer out) {
			MString.appendRepeating(level, ' ', out);
			out.append(getClass().getCanonicalName()).append(name).append("(").append(def).append(")");
		}

	}
	
	private class ConfigMap implements Map<String,Object> {

		private int level;

		private ConfigMap(int level) {
			this.level = level;
		}
		
		@Override
		public int size() {
			return 0;
		}

		public int getLevel() {
			return level;
		}

		@Override
		public boolean isEmpty() {
			return false;
		}

		@Override
		public boolean containsKey(Object key) {
			return false;
		}

		@Override
		public boolean containsValue(Object value) {
			return false;
		}

		@Override
		public Object get(Object key) {
			return null;
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
		public void putAll(Map<? extends String, ? extends Object> m) {
		}

		@Override
		public void clear() {
		}

		@Override
		public Set<String> keySet() {
			return null;
		}

		@Override
		public Collection<Object> values() {
			return null;
		}

		@Override
		public Set<java.util.Map.Entry<String, Object>> entrySet() {
			return null;
		}
		
	}

	public abstract URL getUrl();

	public abstract boolean isValide();
	
	public abstract boolean hasContent();
	
}
