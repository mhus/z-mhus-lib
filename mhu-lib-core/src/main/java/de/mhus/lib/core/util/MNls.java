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
package de.mhus.lib.core.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import de.mhus.lib.core.AbstractProperties;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.parser.StringCompiler;
import de.mhus.lib.errors.MException;
import de.mhus.lib.errors.NotSupportedException;

public class MNls extends AbstractProperties {
	
	protected Properties properties = null;
	protected String prefix = "";

	public MNls() {
		this(new Properties(), "");
	}
	
	public MNls(Properties properties, String prefix) {
		this.properties = properties;
		this.prefix = prefix == null || "".equals(prefix) ? "" : prefix + ".";
	}
	
	public String find(String in, String ... strings ) {
		if (strings == null || strings.length == 0)
			return find(in,(Map<String, Object>)null);
		HashMap<String, Object> attr = new HashMap<String, Object>();
		for (int i = 0; i < strings.length; i++)
			attr.put(String.valueOf(i), strings[i]);
		return find(in,attr);
	}
	
	public String find(String in) {
		return find(in,(Map<String, Object>)null);
	}

	public String find(String in, Map<String, Object> attributes) {
		return find(this,in, attributes);
	}

	public static String find(MNlsProvider provider, String in) {
		return find(provider == null ? null : provider.getNls(), in, null);
	}

	public static String find(MNls nls, String in) {
		return find(nls, in, null);
	}
	
	public static String find(MNls nls, String in, Map<String, Object> attributes) {
		String def = null;
		if (in == null) return "";
		int pos = in.indexOf("=");
		if (pos == 0) {  // no key defined
			return in.substring(1);
		}
		if (pos > 0) { // default defined
			def = in.substring(pos+1);
			in  = in.substring(0,pos);
		}
		
		if (def == null) def = in;
		if (nls == null) return def;
		
		try {
			String ret = nls.getString(in,def);
			if (ret == null) return def;
			
			if (attributes != null && ret.indexOf('$') >=0) {
				ret = StringCompiler.compile(ret).execute(attributes);
			}
			
			return ret;
		} catch (MException e) {
		}

		return in;
	}

	@Override
	public Object getProperty(String name) {
		return properties.get( prefix + name);
	}

	@Override
	public boolean isProperty(String name) {
		return properties.containsKey(prefix + name);
	}

	@Override
	public void removeProperty(String key) {
		properties.remove(prefix + key);
	}

	@Override
	public void setProperty(String key, Object value) {
		properties.put(prefix + key, value );
	}

	@Override
	public boolean isEditable() {
		return true;
	}

	@Override
	public Set<String> keys() {
		return new SetCast<Object, String>(properties.keySet());
	}
	
	@Override
	public String toString() {
		return MSystem.toString(this, properties);
	}
	
	public MNls createSubstitute(String prefix) {
		if (prefix == null) return this;
		return new MNls(properties, this.prefix + prefix);
	}
	
	public static MNls lookup(Object owner) {
		MNlsFactory factory = MNlsFactory.lookup(owner);
		if (factory != null)
			return factory.load(owner.getClass());
		return null;
	}

	public static MNls lookup(Object owner, Locale locale) {
		MNlsFactory factory = MNlsFactory.lookup(owner);
		if (factory != null)
			return factory.load(owner.getClass(), locale);
		return null;
	}
	
	@Override
	public int size() {
		return properties.size();
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
		throw new NotSupportedException();
	}

	@Override
	public void clear() {
		properties.clear();
	}

}
