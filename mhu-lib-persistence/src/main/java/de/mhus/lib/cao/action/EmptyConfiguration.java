package de.mhus.lib.cao.action;

import de.mhus.lib.cao.CaoList;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.definition.DefRoot;

public class EmptyConfiguration extends CaoConfiguration {

	public EmptyConfiguration(CaoConfiguration con, CaoList list, IConfig model) {
		super(con, list, model);
	}


	@Override
	protected IConfig createDefaultModel() {
		return new DefRoot(
				);
	}


	public void setName(String name) {
	}
	
}
