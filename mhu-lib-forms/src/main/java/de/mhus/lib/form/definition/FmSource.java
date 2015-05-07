package de.mhus.lib.form.definition;

import de.mhus.lib.core.config.HashConfig;
import de.mhus.lib.core.definition.DefComponent;
import de.mhus.lib.core.definition.IDefAttribute;
import de.mhus.lib.errors.MException;

public class FmSource extends HashConfig implements IDefAttribute {

	private String tag;

	public FmSource(String tag, String name) {
		super(tag, null);
		this.tag = tag;
		setString("name", name);
	}
	
	@Override
	public void inject(DefComponent root) throws MException {
		HashConfig sources = (HashConfig) root.getNode("sources");
		if (sources == null) {
			sources = (HashConfig) root.createConfig("sources");
		}
		sources.setConfig(tag, this);
	}

}
