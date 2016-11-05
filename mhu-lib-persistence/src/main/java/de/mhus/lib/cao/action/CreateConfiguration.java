package de.mhus.lib.cao.action;

import java.util.Locale;

import de.mhus.lib.cao.CaoList;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.definition.DefRoot;
import de.mhus.lib.form.definition.FmText;

public class CreateConfiguration extends CaoConfiguration {

	public CreateConfiguration(CaoConfiguration con, CaoList list, IConfig model) {
		super(con, list, model);
	}


	public static final String NAME = "_name";
	
	@Override
	protected IConfig createDefaultModel() {
		return new DefRoot(
				new FmText(NAME, "name.name=Name", "name.description=Technical name of the new node")
				);
	}


	public void setName(String name) {
		properties.setString(NAME, name);
	}
	
}
