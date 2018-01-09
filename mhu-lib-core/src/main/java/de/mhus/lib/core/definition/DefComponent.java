package de.mhus.lib.core.definition;

import java.util.LinkedList;
import java.util.Properties;

import de.mhus.lib.core.config.HashConfig;
import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.errors.MException;

public class DefComponent extends HashConfig implements IDefDefinition {

	private static final long serialVersionUID = 1L;
	private String tag;
	private LinkedList<IDefDefinition> definitions = new LinkedList<IDefDefinition>();

	public DefComponent(String tag, IDefDefinition ... definitions) {
		super(tag,null);
		this.tag = tag;
		addDefinition(definitions);
	}
	
	public DefComponent addAttribute(String name, Object value) {
		return addDefinition(new DefAttribute(name, value));
	}
	
	public DefComponent addDefinition(IDefDefinition ... def) {
		if (def == null) return this;
		for (IDefDefinition d : def)
			if (d != null) definitions.add(d);
		return this;
	}
	
	public LinkedList<IDefDefinition> definitions() {
		return definitions;
	}
	
	@Override
	public void inject(DefComponent parent) throws MException {
		if (parent != null) {
			parent.setConfig(tag, this);
		}
		for (IDefDefinition d : definitions) {
			d.inject(this);
		}

	}

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
		for ( ResourceNode<?> c : config.getNodes()) {
			if (c instanceof DefComponent)
				((DefComponent)c).fillNls(p);
			else
				fill((HashConfig) c,p);
		}
	}
	
}
