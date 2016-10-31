package de.mhus.lib.cao.util;

import de.mhus.lib.cao.CaoDriver;
import de.mhus.lib.cao.CaoMetaDefinition;
import de.mhus.lib.cao.CaoMetaDefinition.TYPE;
import de.mhus.lib.cao.CaoMetadata;

public class MutableMetadata extends CaoMetadata {

	public MutableMetadata(CaoDriver driver) {
		super(driver);
	}

	
	public MutableMetadata addDefinition(CaoMetaDefinition def) {
		CaoMetaDefinition last = index.put(def.getName(), def);
		if (last != null) definition.remove(last);
		definition.add(def);
		return this;
	}

	public MutableMetadata addDefinition(String name, TYPE type, String nls, long size, String ... categories ) {
		addDefinition(new CaoMetaDefinition(this, name, type, nls, size, categories));
		return this;
	}
	
	public MutableMetadata addDefinition(String name, TYPE type, long size, String ... categories ) {
		addDefinition(new CaoMetaDefinition(this, name, type, name, size, categories));
		return this;
	}
	
}
