package de.mhus.lib.core.definition;

import java.util.LinkedList;
import java.util.Properties;

import de.mhus.lib.core.config.HashConfig;
import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.errors.MException;

/**
 * <p>DefComponent class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class DefComponent extends HashConfig implements IDefDefinition {

	private String tag;
	private LinkedList<IDefDefinition> definitions = new LinkedList<IDefDefinition>();

	/**
	 * <p>Constructor for DefComponent.</p>
	 *
	 * @param tag a {@link java.lang.String} object.
	 * @param definitions a {@link de.mhus.lib.core.definition.IDefDefinition} object.
	 */
	public DefComponent(String tag, IDefDefinition ... definitions) {
		super(tag,null);
		this.tag = tag;
		addDefinition(definitions);
	}
	
	/**
	 * <p>addAttribute.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param value a {@link java.lang.Object} object.
	 * @return a {@link de.mhus.lib.core.definition.DefComponent} object.
	 */
	public DefComponent addAttribute(String name, Object value) {
		return addDefinition(new DefAttribute(name, value));
	}
	
	/**
	 * <p>addDefinition.</p>
	 *
	 * @param def a {@link de.mhus.lib.core.definition.IDefDefinition} object.
	 * @return a {@link de.mhus.lib.core.definition.DefComponent} object.
	 */
	public DefComponent addDefinition(IDefDefinition ... def) {
		if (def == null) return this;
		for (IDefDefinition d : def)
			if (d != null) definitions.add(d);
		return this;
	}
	
	/**
	 * <p>definitions.</p>
	 *
	 * @return a {@link java.util.LinkedList} object.
	 */
	public LinkedList<IDefDefinition> definitions() {
		return definitions;
	}
	
	/** {@inheritDoc} */
	@Override
	public void inject(DefComponent parent) throws MException {
		if (parent != null) {
			parent.setConfig(tag, this);
		}
		for (IDefDefinition d : definitions) {
			d.inject(this);
		}

	}

	/**
	 * <p>fillNls.</p>
	 *
	 * @param p a {@link java.util.Properties} object.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	public void fillNls(Properties p) throws MException {
		
		String nls = getString("nls",null);
		if (nls ==  null) nls = getString("name", null);
		if (nls != null && isProperty("title")) {
			p.setProperty( nls + "_title", getString("title", null));
		}
		if (nls != null && isProperty("description")) {
			p.setProperty( nls + "_description", getString("description", null));
		}
		
		fill(this,p);
	}
		
	private void fill(HashConfig config, Properties p) throws MException {
		for ( ResourceNode c : config.getNodes()) {
			if (c instanceof DefComponent)
				((DefComponent)c).fillNls(p);
			else
				fill((HashConfig) c,p);
		}
	}

}
