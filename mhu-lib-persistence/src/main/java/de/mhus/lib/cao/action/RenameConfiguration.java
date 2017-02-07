package de.mhus.lib.cao.action;

import de.mhus.lib.cao.CaoList;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.definition.DefRoot;
import de.mhus.lib.form.definition.FmText;

public class RenameConfiguration extends CaoConfiguration {

	public RenameConfiguration(CaoConfiguration con, CaoList list, IConfig model) {
		super(con, list, model);
	}

	public static final String NAME = "name";
	

	public void setName(String name) {
		properties.setString(NAME, name);
	}

	@Override
	protected IConfig createDefaultModel() {
		return new DefRoot(
				new FmText(NAME, "name.name=Name", "name.description=New technical name of the node.")
				);
	}
	
}
